package pfs.lms.enquiry.collateral.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralDocument;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralSecuritiesPosition;
import pfs.lms.enquiry.collateral.domain.CollateralValue;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;
import pfs.lms.enquiry.collateral.domain.SapField;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;
import pfs.lms.enquiry.collateral.repository.CollateralCoverageRepository;
import pfs.lms.enquiry.collateral.repository.CollateralDocumentRepository;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionCersaiRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionNeslRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionRocRepository;
import pfs.lms.enquiry.collateral.repository.CollateralSecuritiesPositionRepository;
import pfs.lms.enquiry.collateral.repository.CollateralValueRepository;
import pfs.lms.enquiry.collateral.service.ICollateralChildService;
import pfs.lms.enquiry.exception.LmsException;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollateralChildService implements ICollateralChildService {

    private static final String SOURCE_OF_ENTRY_PORTAL = "PORTAL";
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String DEFAULT_CURRENCY = "INR";
    /** References of the portal file storage (POST /api/upload) are UUIDs. */
    private static final Pattern FILE_REFERENCE = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    /** Fields the dialogs cannot change: numbered or derived by the portal. */
    private static final Set<String> SYSTEM_FIELDS = new HashSet<>(Arrays.asList(
            "serialNumber", "eventDescription", "coverageBasisDescription", "documentTypeDescription",
            "documentStageDescription", "bdsDocumentId", "securitiesShortName"));

    /** Value list of each coded field. */
    private static final Map<String, CollateralValueList> CODED_FIELDS = new HashMap<>();

    static {
        CODED_FIELDS.put("eventType", CollateralValueList.PERFECTION_EVENT_TYPE);
        CODED_FIELDS.put("coverageBasis", CollateralValueList.COVERAGE_BASIS);
        CODED_FIELDS.put("actionDaysPrefix", CollateralValueList.ACTION_DAYS_PREFIX);
        CODED_FIELDS.put("actionDaysSuffix", CollateralValueList.ACTION_DAYS_SUFFIX);
        CODED_FIELDS.put("timelineEvent", CollateralValueList.TIMELINE_EVENT);
        CODED_FIELDS.put("documentStage", CollateralValueList.DOCUMENT_STAGE);
        CODED_FIELDS.put("securitiesType", CollateralValueList.SECURITIES_TYPE);
        CODED_FIELDS.put("nominalValueCurrency", CollateralValueList.CURRENCY);
    }

    private static final Map<String, String> LABELS = new HashMap<>();

    static {
        LABELS.put("effectiveFromDate", "Effective from date");
        LABELS.put("expectedCoverageAmount", "Exp. Cov. Val (Amt)");
        LABELS.put("expectedCoveragePercentage", "Exp. Cov. Val (%)");
        LABELS.put("coverageBasis", "Coverage Basis");
        LABELS.put("basisAmount", "Basis Amount");
        LABELS.put("coverageAmount", "Coverage Amount");
        LABELS.put("remarks", "Remarks");
        LABELS.put("eventType", "Event");
        LABELS.put("eventDate", "Event Date");
        LABELS.put("rocCertificateChargeIdNumber", "RoC Certificate Charge ID Number");
        LABELS.put("rocSecuritySatisfactionDate", "RoC Sec. Satisfy Date");
        LABELS.put("cersaiAcknowledgmentNumber", "CERSAI Acknowledgment Number");
        LABELS.put("neslReference", "NeSL Reference");
        LABELS.put("timelinesText", "Timelines");
        LABELS.put("actionDaysPrefix", "Action Period");
        LABELS.put("actionDaysSuffix", "Action Period Unit");
        LABELS.put("actionPeriod", "Action Period (number)");
        LABELS.put("timelineEvent", "Timeline Event");
        LABELS.put("timelineEventDate", "Timeline Event Date");
        LABELS.put("timelineDate", "Timeline Date");
        LABELS.put("documentStage", "Document Stage");
        LABELS.put("documentType", "Document Type");
        LABELS.put("documentTitle", "Document Title");
        LABELS.put("fileReference", "Uploaded document");
        LABELS.put("fileName", "File name");
        LABELS.put("securitiesType", "Securities Type");
        LABELS.put("securitiesChangeDate", "Date");
        LABELS.put("numberOfUnits", "No of Units");
        LABELS.put("nominalValue", "Nominal Value");
        LABELS.put("nominalValueCurrency", "Currency");
        LABELS.put("holdingPercentage", "Holding %");
    }

    private final Map<CollateralChildType, JpaRepository<? extends CollateralChildRecord<?>, UUID>> repositories =
            new EnumMap<>(CollateralChildType.class);
    private final Map<CollateralChildType, Function<UUID, List<? extends CollateralChildRecord<?>>>> finders =
            new EnumMap<>(CollateralChildType.class);
    private final Map<Class<?>, List<Field>> fields = new HashMap<>();

    private final CollateralItemRepository itemRepository;
    private final CollateralValueRepository valueRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CollateralChangeDocumentService changeDocumentService;
    private final ObjectMapper objectMapper;

    public CollateralChildService(CollateralCoverageRepository coverageRepository,
                                  CollateralPerfectionRocRepository rocRepository,
                                  CollateralPerfectionCersaiRepository cersaiRepository,
                                  CollateralPerfectionNeslRepository neslRepository,
                                  CollateralDocumentRepository documentRepository,
                                  CollateralSecuritiesPositionRepository securitiesPositionRepository,
                                  CollateralItemRepository itemRepository,
                                  CollateralValueRepository valueRepository,
                                  DocumentTypeRepository documentTypeRepository,
                                  CollateralChangeDocumentService changeDocumentService,
                                  ObjectMapper objectMapper) {
        this.itemRepository = itemRepository;
        this.valueRepository = valueRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.changeDocumentService = changeDocumentService;
        this.objectMapper = objectMapper;

        repositories.put(CollateralChildType.COVERAGE, coverageRepository);
        repositories.put(CollateralChildType.ROC, rocRepository);
        repositories.put(CollateralChildType.CERSAI, cersaiRepository);
        repositories.put(CollateralChildType.NESL, neslRepository);
        repositories.put(CollateralChildType.DOCUMENT, documentRepository);
        repositories.put(CollateralChildType.SECURITIES_POSITION, securitiesPositionRepository);
        finders.put(CollateralChildType.COVERAGE, coverageRepository::findByItem_Id);
        finders.put(CollateralChildType.ROC, rocRepository::findByItem_Id);
        finders.put(CollateralChildType.CERSAI, cersaiRepository::findByItem_Id);
        finders.put(CollateralChildType.NESL, neslRepository::findByItem_Id);
        finders.put(CollateralChildType.DOCUMENT, documentRepository::findByItem_Id);
        finders.put(CollateralChildType.SECURITIES_POSITION, securitiesPositionRepository::findByItem_Id);

        for (CollateralChildType type : CollateralChildType.values()) {
            fields.put(type.getEntityClass(), Collections.unmodifiableList(
                    Arrays.stream(type.getEntityClass().getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(SapField.class))
                            .filter(field -> !Modifier.isStatic(field.getModifiers()))
                            .peek(field -> field.setAccessible(true))
                            .collect(Collectors.toList())));
        }
    }

    // ------------------------------------------------------------------------------------------------ reading

    @Override
    @Transactional(readOnly = true)
    public List<? extends CollateralChildRecord<?>> list(CollateralChildType type, UUID itemId) {
        List<CollateralChildRecord<?>> rows = new ArrayList<>(finders.get(type).apply(itemId));
        rows.sort(order(type));
        return rows;
    }

    // ------------------------------------------------------------------------------------------------ writing

    @Override
    public CollateralChildRecord<?> create(CollateralChildType type, UUID itemId, JsonNode request, String userName) {
        CollateralItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new LmsException("Collateral not found. It may have been deleted.", HttpStatus.NOT_FOUND));
        CollateralService.checkNotInApproval(item.getChecklist());
        CollateralChildRecord<?> input = read(type, request);

        CollateralChildRecord<?> row = newInstance(type);
        copyBusinessFields(type, input, row);
        normalizeAndValidate(type, row);
        row.setItem(item);
        row.setChecklistIdNo(item.getChecklistIdNo());
        CollateralChecklist checklist = item.getChecklist();
        row.setContractNumber(checklist.getLoanContractId());
        row.setSourceOfEntry(SOURCE_OF_ENTRY_PORTAL);
        if (hasSerialNumber(type)) {
            setFieldValue(row, "serialNumber", nextSerialNumber(type, itemId));
        }
        linkPortalDocument(row, null);
        row.setCreatedOn(LocalDate.now());
        row.setCreatedAt(LocalTime.now());
        row.setCreatedByUserName(userName);
        row = save(type, row);

        changeDocumentService.record(checklist.getLoanApplication(), checklist.getId(), row.getId(),
                describe(type, item, row), null, snapshot(type, row), CollateralChangeDocumentService.CREATED, userName,
                type.getSubProcessName());
        return row;
    }

    @Override
    public CollateralChildRecord<?> update(CollateralChildType type, UUID id, JsonNode request, String userName) {
        CollateralChildRecord<?> row = find(type, id);
        CollateralService.checkNotInApproval(row.getItem().getChecklist());
        CollateralChildRecord<?> before = snapshot(type, row);

        CollateralChildRecord<?> changed = snapshot(type, row);
        copyBusinessFields(type, read(type, request), changed);
        normalizeAndValidate(type, changed);
        copyAllFields(type, changed, row);
        linkPortalDocument(row, before);
        row.setChangedOn(LocalDate.now());
        row.setChangedAt(LocalTime.now());
        row.setChangedByUserName(userName);
        row = save(type, row);

        CollateralItem item = row.getItem();
        CollateralChecklist checklist = item.getChecklist();
        changeDocumentService.record(checklist.getLoanApplication(), checklist.getId(), row.getId(),
                describe(type, item, row), before, snapshot(type, row), CollateralChangeDocumentService.UPDATED, userName,
                type.getSubProcessName());
        return row;
    }

    @Override
    public void delete(CollateralChildType type, UUID id, String userName) {
        CollateralChildRecord<?> row = find(type, id);
        CollateralService.checkNotInApproval(row.getItem().getChecklist());
        deleteRow(type, row, userName);
    }

    @Override
    public void deleteAllOfItem(CollateralItem item, String userName) {
        for (CollateralChildType type : CollateralChildType.values()) {
            for (CollateralChildRecord<?> row : new ArrayList<>(finders.get(type).apply(item.getId()))) {
                deleteRow(type, row, userName);
            }
        }
    }

    @Override
    public void updateChecklistIdNo(CollateralItem item) {
        for (CollateralChildType type : CollateralChildType.values()) {
            for (CollateralChildRecord<?> row : finders.get(type).apply(item.getId())) {
                row.setChecklistIdNo(item.getChecklistIdNo());
                save(type, row);
            }
        }
    }

    private void deleteRow(CollateralChildType type, CollateralChildRecord<?> row, String userName) {
        CollateralItem item = row.getItem();
        CollateralChecklist checklist = item.getChecklist();
        CollateralChildRecord<?> deleted = snapshot(type, row);
        String description = describe(type, item, row);
        deleteEntity(type, row);
        changeDocumentService.record(checklist.getLoanApplication(), checklist.getId(), deleted.getId(), description,
                null, deleted, CollateralChangeDocumentService.DELETED, userName, type.getSubProcessName());
    }

    // ------------------------------------------------------------------------------------------------ validation

    void normalizeAndValidate(CollateralChildType type, CollateralChildRecord<?> row) {
        List<Field> rowFields = fields.get(type.getEntityClass());
        for (Field field : rowFields) {
            if (field.getType() == String.class) {
                String value = (String) get(field, row);
                if (value != null) {
                    String trimmed = value.trim();
                    set(field, row, trimmed.isEmpty() ? null : trimmed);
                }
            }
        }

        if (row instanceof CollateralDocument) {
            validateDocument((CollateralDocument) row);
        } else if (row instanceof CollateralSecuritiesPosition) {
            validateSecuritiesPosition((CollateralSecuritiesPosition) row);
        } else if (row instanceof CollateralCoverage) {
            CollateralCoverage coverage = (CollateralCoverage) row;
            if (coverage.getEffectiveFromDate() == null) {
                throw invalid(label("effectiveFromDate") + " is required.");
            }
            checkPercentage(coverage.getExpectedCoveragePercentage(), "expectedCoveragePercentage");
            checkNotNegative(coverage.getExpectedCoverageAmount(), "expectedCoverageAmount");
            checkNotNegative(coverage.getBasisAmount(), "basisAmount");
            checkNotNegative(coverage.getCoverageAmount(), "coverageAmount");
        } else if (fieldValue(row, "eventType") == null) {
            throw invalid(label("eventType") + " is required.");
        }

        Map<String, Map<String, String>> lists = valueListsByCode();
        for (Field field : rowFields) {
            CollateralValueList list = CODED_FIELDS.get(field.getName());
            Object code = get(field, row);
            if (list != null && code != null
                    && !lists.getOrDefault(list.name(), Collections.emptyMap()).containsKey(code.toString())) {
                throw invalid(label(field.getName()) + ": unknown value " + code + ".");
            }
            if (field.getType() == Integer.class && code != null && ((Integer) code < 0 || (Integer) code > 999)) {
                throw invalid(label(field.getName()) + " must be between 0 and 999.");
            }
            checkLength(field, row);
            checkDigits(field, row);
        }

        // Descriptions kept beside the codes, as in SAP
        if (row instanceof CollateralDocument) {
            CollateralDocument document = (CollateralDocument) row;
            document.setDocumentStageDescription(
                    description(lists, CollateralValueList.DOCUMENT_STAGE, document.getDocumentStage()));
            DocumentType documentType = documentTypeRepository.findByCode(document.getDocumentType());
            if (documentType == null) {
                throw invalid(label("documentType") + ": unknown document type " + document.getDocumentType() + ".");
            }
            document.setDocumentTypeDescription(truncate(documentType.getDescription(), 40));
        } else if (row instanceof CollateralSecuritiesPosition) {
            CollateralSecuritiesPosition position = (CollateralSecuritiesPosition) row;
            position.setSecuritiesShortName(truncate(
                    description(lists, CollateralValueList.SECURITIES_TYPE, position.getSecuritiesType()), 40));
        } else if (row instanceof CollateralCoverage) {
            CollateralCoverage coverage = (CollateralCoverage) row;
            coverage.setCoverageBasisDescription(
                    description(lists, CollateralValueList.COVERAGE_BASIS, coverage.getCoverageBasis()));
        } else {
            setFieldValue(row, "eventDescription",
                    description(lists, CollateralValueList.PERFECTION_EVENT_TYPE, (String) fieldValue(row, "eventType")));
        }
    }

    private void validateDocument(CollateralDocument document) {
        if (document.getDocumentStage() == null) {
            throw invalid(label("documentStage") + " is required.");
        }
        if (document.getDocumentType() == null) {
            throw invalid(label("documentType") + " is required.");
        }
        if (document.getFileReference() == null) {
            document.setFileName(null);
        } else if (!FILE_REFERENCE.matcher(document.getFileReference()).matches()) {
            throw invalid(label("fileReference") + ": the file reference is not valid. Upload the document again.");
        } else if (document.getFileName() == null) {
            document.setFileName("document");
        }
    }

    private void validateSecuritiesPosition(CollateralSecuritiesPosition position) {
        if (position.getSecuritiesType() == null) {
            throw invalid(label("securitiesType") + " is required.");
        }
        if (position.getNominalValueCurrency() == null) {
            position.setNominalValueCurrency(DEFAULT_CURRENCY);
        }
        checkPercentage(position.getHoldingPercentage(), "holdingPercentage");
        checkNotNegative(position.getNumberOfUnits(), "numberOfUnits");
        checkNotNegative(position.getNominalValue(), "nominalValue");
    }

    /**
     * Keeps SAP's portal document id (PTL_DOCU_ID) in step with the uploaded file of a document, so the later SAP
     * integration can find it. A value that came from SAP is only replaced when a file is uploaded in the portal.
     */
    private static void linkPortalDocument(CollateralChildRecord<?> row, CollateralChildRecord<?> before) {
        if (!(row instanceof CollateralDocument)) {
            return;
        }
        String reference = ((CollateralDocument) row).getFileReference();
        String previous = before instanceof CollateralDocument ? ((CollateralDocument) before).getFileReference() : null;
        if (reference != null) {
            row.setPortalDocumentId(reference);
        } else if (previous != null && previous.equals(row.getPortalDocumentId())) {
            row.setPortalDocumentId(null);
        }
    }

    private void checkLength(Field field, Object row) {
        Column column = field.getAnnotation(Column.class);
        if (field.getType() != String.class || column == null) {
            return;
        }
        String value = (String) get(field, row);
        if (value != null && value.length() > column.length()) {
            throw invalid(label(field.getName()) + " can have at most " + column.length() + " characters.");
        }
    }

    private void checkDigits(Field field, Object row) {
        Column column = field.getAnnotation(Column.class);
        if (field.getType() != BigDecimal.class || column == null || column.precision() == 0) {
            return;
        }
        BigDecimal value = (BigDecimal) get(field, row);
        if (value == null) {
            return;
        }
        if (value.scale() > column.scale()) {
            throw invalid(label(field.getName()) + " can have at most " + column.scale() + " decimal places.");
        }
        if (value.precision() - value.scale() > column.precision() - column.scale()) {
            throw invalid(label(field.getName()) + " is too large.");
        }
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private Comparator<CollateralChildRecord<?>> order(CollateralChildType type) {
        if (hasSerialNumber(type)) {
            return Comparator.comparing(row -> {
                Integer serial = (Integer) fieldValue(row, "serialNumber");
                return serial == null ? Integer.MAX_VALUE : serial;
            });
        }
        return Comparator.comparing((CollateralChildRecord<?> row) -> {
            LocalDate date = (LocalDate) fieldValue(row, "eventDate");
            return date == null ? LocalDate.MAX : date;
        }).thenComparing(row -> row.getCreatedOn() == null ? LocalDate.MIN : row.getCreatedOn())
                .thenComparing(row -> row.getCreatedAt() == null ? LocalTime.MIN : row.getCreatedAt());
    }

    /** Coverage, documents and securities positions are numbered per collateral (SERIAL_NO). */
    private boolean hasSerialNumber(CollateralChildType type) {
        return fields.get(type.getEntityClass()).stream().anyMatch(field -> field.getName().equals("serialNumber"));
    }

    private int nextSerialNumber(CollateralChildType type, UUID itemId) {
        return finders.get(type).apply(itemId).stream()
                .map(row -> (Integer) fieldValue(row, "serialNumber"))
                .filter(serial -> serial != null)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    private CollateralChildRecord<?> read(CollateralChildType type, JsonNode request) {
        if (request == null || !request.isObject()) {
            throw invalid("The " + type.getLabel() + " data is missing.");
        }
        try {
            return objectMapper.treeToValue(request, type.getEntityClass());
        } catch (Exception ex) {
            throw invalid("The " + type.getLabel() + " data could not be read: check dates and numbers.");
        }
    }

    private CollateralChildRecord<?> find(CollateralChildType type, UUID id) {
        return repositories.get(type).findById(id)
                .orElseThrow(() -> new LmsException(type.getLabel() + " not found. It may have been deleted.",
                        HttpStatus.NOT_FOUND));
    }

    @SuppressWarnings("unchecked")
    private <T extends CollateralChildRecord<?>> T save(CollateralChildType type, CollateralChildRecord<?> row) {
        return ((JpaRepository<T, UUID>) repositories.get(type)).save((T) row);
    }

    @SuppressWarnings("unchecked")
    private <T extends CollateralChildRecord<?>> void deleteEntity(CollateralChildType type, CollateralChildRecord<?> row) {
        ((JpaRepository<T, UUID>) repositories.get(type)).delete((T) row);
    }

    private static CollateralChildRecord<?> newInstance(CollateralChildType type) {
        try {
            return type.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void copyBusinessFields(CollateralChildType type, Object source, Object target) {
        for (Field field : fields.get(type.getEntityClass())) {
            if (!SYSTEM_FIELDS.contains(field.getName())) {
                set(field, target, get(field, source));
            }
        }
    }

    private void copyAllFields(CollateralChildType type, Object source, Object target) {
        for (Field field : fields.get(type.getEntityClass())) {
            set(field, target, get(field, source));
        }
    }

    /** Detached copy of the table fields and the id, for change documents (no link to the collateral). */
    private CollateralChildRecord<?> snapshot(CollateralChildType type, CollateralChildRecord<?> row) {
        CollateralChildRecord<?> copy = newInstance(type);
        copyAllFields(type, row, copy);
        copy.setId(row.getId());
        return copy;
    }

    private String describe(CollateralChildType type, CollateralItem item, CollateralChildRecord<?> row) {
        String number = item.getChecklistIdNo() != null ? String.valueOf(item.getChecklistIdNo()) : "New";
        StringBuilder text = new StringBuilder(number).append(' ').append(type.getLabel());
        if (row instanceof CollateralDocument) {
            CollateralDocument document = (CollateralDocument) row;
            text.append(' ').append(document.getSerialNumber() != null ? document.getSerialNumber() : "");
            Object title = document.getDocumentTitle() != null ? document.getDocumentTitle() : document.getDocumentTypeDescription();
            if (title != null) {
                text.append(' ').append(title);
            }
        } else if (row instanceof CollateralSecuritiesPosition) {
            CollateralSecuritiesPosition position = (CollateralSecuritiesPosition) row;
            text.append(' ').append(position.getSerialNumber() != null ? position.getSerialNumber() : "");
            if (position.getSecuritiesType() != null) {
                text.append(' ').append(position.getSecuritiesType());
            }
            if (position.getSecuritiesChangeDate() != null) {
                text.append(' ').append(DATE.format(position.getSecuritiesChangeDate()));
            }
        } else if (row instanceof CollateralCoverage) {
            CollateralCoverage coverage = (CollateralCoverage) row;
            text.append(' ').append(coverage.getSerialNumber() != null ? coverage.getSerialNumber() : "");
            if (coverage.getEffectiveFromDate() != null) {
                text.append(" from ").append(DATE.format(coverage.getEffectiveFromDate()));
            }
        } else {
            Object event = fieldValue(row, "eventDescription");
            Object date = fieldValue(row, "eventDate");
            if (event != null) {
                text.append(' ').append(event);
            }
            if (date != null) {
                text.append(' ').append(DATE.format((LocalDate) date));
            }
        }
        return text.toString().trim();
    }

    private Map<String, Map<String, String>> valueListsByCode() {
        Map<String, Map<String, String>> lists = new LinkedHashMap<>();
        for (CollateralValue value : valueRepository.findAll()) {
            lists.computeIfAbsent(value.getListName(), name -> new LinkedHashMap<>())
                    .put(value.getCode(), value.getDescription());
        }
        return lists;
    }

    private static String description(Map<String, Map<String, String>> lists, CollateralValueList list, String code) {
        if (code == null) {
            return null;
        }
        String text = lists.getOrDefault(list.name(), Collections.emptyMap()).get(code);
        return text == null || text.length() <= 60 ? text : text.substring(0, 60);
    }

    private Object fieldValue(CollateralChildRecord<?> row, String name) {
        for (Field field : fields.get(Hibernate.getClass(row))) {
            if (field.getName().equals(name)) {
                return get(field, row);
            }
        }
        return null;
    }

    private void setFieldValue(CollateralChildRecord<?> row, String name, Object value) {
        for (Field field : fields.get(Hibernate.getClass(row))) {
            if (field.getName().equals(name)) {
                set(field, row, value);
            }
        }
    }

    private static String truncate(String text, int length) {
        return text == null || text.length() <= length ? text : text.substring(0, length);
    }

    private static void checkPercentage(BigDecimal value, String fieldName) {
        if (value != null && (value.signum() < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0)) {
            throw invalid(label(fieldName) + " must be between 0 and 100.");
        }
    }

    private static void checkNotNegative(BigDecimal value, String fieldName) {
        if (value != null && value.signum() < 0) {
            throw invalid(label(fieldName) + " cannot be negative.");
        }
    }

    private static LmsException invalid(String message) {
        return new LmsException(message, HttpStatus.PRECONDITION_FAILED);
    }

    private static String label(String fieldName) {
        return LABELS.getOrDefault(fieldName, fieldName);
    }

    private static Object get(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void set(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
