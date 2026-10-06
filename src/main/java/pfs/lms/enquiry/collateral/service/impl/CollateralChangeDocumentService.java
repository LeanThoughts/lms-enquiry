package pfs.lms.enquiry.collateral.service.impl;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.ChangeDocument;
import pfs.lms.enquiry.domain.ChangeDocumentItem;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.service.ISAPIntegrationPointerService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import pfs.lms.enquiry.collateral.domain.SapField;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Writes change documents for Collateral Management (NFR 1) in the existing change document tables and creates the
 * SAP integration pointer, as the portal's ChangeDocumentService does. That service only knows the entities listed in
 * it, so the documents are built here instead of changing the existing class.
 * <p>
 * Changed values are found by comparing the business fields (those with a {@link SapField}) of the copies before and
 * after the change; amounts are compared by value, so 2.5 and 2.50 are equal.
 * <p>
 * A failure is logged and does not undo the user's change, as in the existing service.
 */
@Service
public class CollateralChangeDocumentService {

    public static final String BUSINESS_PROCESS = "Collateral Management";
    public static final String SUB_PROCESS_CHECKLIST = "Collateral Checklist";
    public static final String SUB_PROCESS_ITEM = "Collateral Item";

    public static final String CREATED = "Created";
    public static final String UPDATED = "Updated";
    public static final String DELETED = "Deleted";
    /** Workflow actions on the checklist header (no field changes). */
    public static final String SENT_FOR_APPROVAL = "Sent for Approval";
    public static final String APPROVED = "Approved";
    public static final String REJECTED = "Rejected";

    private static final int MAX_VALUE_LENGTH = 255;

    private static final Logger log = LoggerFactory.getLogger(CollateralChangeDocumentService.class);

    private final IChangeDocumentService changeDocumentService;
    private final ISAPIntegrationPointerService sapIntegrationPointerService;

    public CollateralChangeDocumentService(IChangeDocumentService changeDocumentService,
                                           ISAPIntegrationPointerService sapIntegrationPointerService) {
        this.changeDocumentService = changeDocumentService;
        this.sapIntegrationPointerService = sapIntegrationPointerService;
    }

    /**
     * @param loanApplication loan the object belongs to
     * @param checklistId     id of the checklist header (main entity)
     * @param entityId        id of the changed object
     * @param description     readable key of the object, e.g. "912 Personal Guarantee"
     * @param oldObject       detached copy before the change (updates only)
     * @param newObject       detached copy after the change (for deletions: the deleted object)
     * @param action          {@link #CREATED}, {@link #UPDATED} or {@link #DELETED}
     */
    public void record(LoanApplication loanApplication, UUID checklistId, UUID entityId, String description,
                       Object oldObject, Object newObject, String action, String userName, String subProcess) {
        try {
            ChangeDocument changeDocument = new ChangeDocument();
            changeDocument.setLoanBusinessProcessObjectId(checklistId);
            changeDocument.setEnitityId(entityId.toString());
            changeDocument.setMainEntityId(checklistId.toString());
            changeDocument.setBusinessProcessName(BUSINESS_PROCESS);
            changeDocument.setSubProcessName(subProcess);
            changeDocument.setUserName(userName);
            changeDocument.setDate(new Date());
            // A lazy proxy (reached through the checklist) is replaced by the loaded loan application
            changeDocument.setLoanApplication(loanApplication != null
                    ? (LoanApplication) Hibernate.unproxy(loanApplication) : null);
            changeDocument.setLoanContractId(loanApplication != null ? loanApplication.getLoanContractId() : null);
            changeDocument.setAction(action);
            changeDocument.setTableKey(truncate(description));

            if (UPDATED.equals(action) && oldObject != null) {
                List<ChangeDocumentItem> items = changedValues(oldObject, newObject, entityId, description);
                if (items.isEmpty()) {
                    log.info("Collateral change document skipped: {} {} has no changed values", subProcess, entityId);
                    return; // nothing changed: no change document and no SAP pointer
                }
                changeDocument.setChangeDocumentItems(items);
            } else {
                changeDocument.setChangeDocumentItems(new ArrayList<>());
            }

            changeDocumentService.saveChangeDocument(changeDocument);
            log.info("Collateral change document written: {} {} {} ({} changed values)", action, subProcess, entityId,
                    changeDocument.getChangeDocumentItems().size());
            // The pointer service keeps one unposted pointer per object: an update of an object whose earlier
            // pointer is not posted yet adds no second pointer.
            sapIntegrationPointerService.saveForObject(BUSINESS_PROCESS, subProcess, entityId.toString(),
                    checklistId.toString(), mode(action));
        } catch (Exception ex) {
            log.error("Error during change document create: {}-{}: {}", BUSINESS_PROCESS, subProcess, ex.getMessage(), ex);
        }
    }

    private List<ChangeDocumentItem> changedValues(Object oldObject, Object newObject, UUID entityId, String description) {
        List<ChangeDocumentItem> items = new ArrayList<>();
        int itemNo = 1;
        for (Field field : businessFields(newObject.getClass())) {
            Object oldValue = read(field, oldObject);
            Object newValue = read(field, newObject);
            if (sameValue(oldValue, newValue)) {
                continue;
            }
            ChangeDocumentItem item = new ChangeDocumentItem();
            item.setItemNo(itemNo++);
            item.setEntityName(newObject.getClass().getSimpleName());
            item.setEntityDescription(description);
            item.setAttributeName(field.getName());
            item.setOldValue(oldValue != null ? truncate(text(oldValue)) : null);
            item.setNewValue(newValue != null ? truncate(text(newValue)) : null);
            item.setTableKey(entityId.toString());
            item.setCreatedAt(new Date());
            item.setUpdatedAt(new Date());
            items.add(item);
        }
        return items;
    }

    /** Fields with a SAP column or SAP long text, of the class and its superclasses. */
    private static List<Field> businessFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(SapField.class)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private static Object read(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean sameValue(Object oldValue, Object newValue) {
        if (oldValue instanceof BigDecimal && newValue instanceof BigDecimal) {
            return ((BigDecimal) oldValue).compareTo((BigDecimal) newValue) == 0;
        }
        if (oldValue instanceof String && ((String) oldValue).isEmpty()) {
            oldValue = null;
        }
        if (newValue instanceof String && ((String) newValue).isEmpty()) {
            newValue = null;
        }
        return Objects.equals(oldValue, newValue);
    }

    private static String text(Object value) {
        return value instanceof BigDecimal ? ((BigDecimal) value).toPlainString() : value.toString();
    }

    private static String mode(String action) {
        switch (action) {
            case CREATED:
                return "C";
            case DELETED:
                return "D";
            default:
                return "U";
        }
    }

    private static String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() <= MAX_VALUE_LENGTH ? value : value.substring(0, MAX_VALUE_LENGTH - 1) + "…";
    }
}
