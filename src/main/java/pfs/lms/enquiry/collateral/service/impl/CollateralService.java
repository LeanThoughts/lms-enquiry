package pfs.lms.enquiry.collateral.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralValue;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;
import pfs.lms.enquiry.collateral.domain.SapField;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.dto.CollateralItemDetailDto;
import pfs.lms.enquiry.collateral.dto.LoanSummaryDto;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralValueRepository;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.service.ICollateralChildService;
import pfs.lms.enquiry.collateral.service.ICollateralPartnerService;
import pfs.lms.enquiry.collateral.service.ICollateralService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.UnitOfMeasure;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UnitOfMeasureRepository;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollateralService implements ICollateralService {

    public static final String DOCUMENT_TYPE_LIST = "DOCUMENT_TYPE";
    public static final String UNIT_OF_MEASURE_LIST = "UNIT_OF_MEASURE";

    /** Prefix of the loan document types (business object BUS2049) offered for collateral documents. */
    private static final String LOAN_DOCUMENT_TYPE_PREFIX = "ZPFSLM";

    private static final String SOURCE_OF_ENTRY_PORTAL = "PORTAL";
    private static final String DEFAULT_CURRENCY = "INR";

    /** Fields that the UI cannot change: set by SAP / the migration or by the portal itself. */
    private static final Set<String> SYSTEM_FIELDS = new HashSet<>(Arrays.asList(
            "checklistIdNo", "sourceOfEntry", "portalId", "monitoringId",
            "complianceStatusText", "collateralAgreementTypeDescription"));

    /** Business fields of a collateral: every field with its SAP column (or SAP long text). */
    private static final List<Field> ITEM_FIELDS = Collections.unmodifiableList(
            Arrays.stream(CollateralItem.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(SapField.class))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList()));

    private static final Map<String, String> LABELS = labels();

    private final CollateralChecklistRepository checklistRepository;
    private final CollateralItemRepository itemRepository;
    private final CollateralValueRepository valueRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final PartnerRepository partnerRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CollateralAgreementTypeMapping agreementTypeMapping;
    private final CollateralChangeDocumentService changeDocumentService;
    private final ICollateralChildService childService;
    private final ICollateralPartnerService partnerService;
    private final CollateralNumberRangeService numberRangeService;

    public CollateralService(CollateralChecklistRepository checklistRepository,
                             CollateralItemRepository itemRepository,
                             CollateralValueRepository valueRepository,
                             LoanApplicationRepository loanApplicationRepository,
                             PartnerRepository partnerRepository,
                             DocumentTypeRepository documentTypeRepository,
                             UnitOfMeasureRepository unitOfMeasureRepository,
                             CollateralAgreementTypeMapping agreementTypeMapping,
                             CollateralChangeDocumentService changeDocumentService,
                             ICollateralChildService childService,
                             ICollateralPartnerService partnerService,
                             CollateralNumberRangeService numberRangeService) {
        this.checklistRepository = checklistRepository;
        this.itemRepository = itemRepository;
        this.valueRepository = valueRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.partnerRepository = partnerRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.agreementTypeMapping = agreementTypeMapping;
        this.changeDocumentService = changeDocumentService;
        this.childService = childService;
        this.partnerService = partnerService;
        this.numberRangeService = numberRangeService;
    }

    // ------------------------------------------------------------------------------------------------ value lists

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<ValueEntryDto>> getValueLists() {
        Map<String, List<ValueEntryDto>> lists = new LinkedHashMap<>();
        for (CollateralValueList list : CollateralValueList.values()) {
            lists.put(list.name(), new ArrayList<>());
        }
        valueRepository.findAll().stream()
                .sorted(Comparator.comparing(CollateralValue::getListName)
                        .thenComparing(value -> value.getSortOrder() == null ? Integer.MAX_VALUE : value.getSortOrder())
                        .thenComparing(CollateralValue::getCode))
                .forEach(value -> lists.computeIfAbsent(value.getListName(), name -> new ArrayList<>())
                        .add(new ValueEntryDto(value.getCode(), value.getDescription())));

        lists.put(DOCUMENT_TYPE_LIST, documentTypeRepository.findAll().stream()
                .filter(type -> type.getCode() != null && type.getCode().startsWith(LOAN_DOCUMENT_TYPE_PREFIX))
                .sorted(Comparator.comparing(DocumentType::getCode))
                .map(type -> new ValueEntryDto(type.getCode(), type.getDescription()))
                .collect(Collectors.toList()));
        lists.put(UNIT_OF_MEASURE_LIST, unitOfMeasureRepository.findAll().stream()
                .filter(unit -> unit.getCode() != null)
                .sorted(Comparator.comparing(UnitOfMeasure::getCode))
                .map(unit -> new ValueEntryDto(unit.getCode(), unit.getValue()))
                .collect(Collectors.toList()));
        return lists;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ValueEntryDto> getAgreementTypes(String collateralObjectType) {
        List<String> allowed = allowedAgreementTypes(collateralObjectType);
        return valueRepository.findByListName(CollateralValueList.AGREEMENT_TYPE.name()).stream()
                .filter(value -> allowed.isEmpty() || allowed.contains(value.getCode()))
                .sorted(Comparator.comparing(value -> value.getSortOrder() == null ? Integer.MAX_VALUE : value.getSortOrder()))
                .map(value -> new ValueEntryDto(value.getCode(), value.getDescription()))
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------ reading

    @Override
    @Transactional(readOnly = true)
    public CollateralChecklistDto getChecklist(UUID loanApplicationId) {
        LoanApplication loanApplication = findLoanApplication(loanApplicationId);
        CollateralChecklistDto dto = new CollateralChecklistDto();
        dto.setLoan(loanSummary(loanApplication));
        checklistRepository.findByLoanApplication_Id(loanApplicationId).ifPresent(checklist -> {
            dto.setId(checklist.getId());
            dto.setWorkFlowStatusCode(checklist.getWorkFlowStatusCode());
            dto.setWorkFlowStatusDescription(checklist.getWorkFlowStatusDescription());
            dto.setRejectionReason(checklist.getRejectionReason());
            List<CollateralItem> items = itemRepository.findByChecklist_Id(checklist.getId());
            items.sort(ITEM_ORDER);
            dto.setItems(items);
        });
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public CollateralChecklistDto getChecklistById(UUID checklistId) {
        CollateralChecklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new LmsException("Collateral checklist not found.", HttpStatus.NOT_FOUND));
        return getChecklist(checklist.getLoanApplication().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public CollateralItemDetailDto getItem(UUID itemId) {
        CollateralItem item = findItem(itemId);
        CollateralChecklist checklist = item.getChecklist();
        CollateralItemDetailDto detail = new CollateralItemDetailDto(item, loanSummary(checklist.getLoanApplication()),
                checklist.getWorkFlowStatusCode(), checklist.getWorkFlowStatusDescription());
        detail.setCoverages(childService.list(CollateralChildType.COVERAGE, itemId));
        detail.setRocEvents(childService.list(CollateralChildType.ROC, itemId));
        detail.setCersaiEvents(childService.list(CollateralChildType.CERSAI, itemId));
        detail.setNeslEvents(childService.list(CollateralChildType.NESL, itemId));
        detail.setDocuments(childService.list(CollateralChildType.DOCUMENT, itemId));
        detail.setSecuritiesPositions(childService.list(CollateralChildType.SECURITIES_POSITION, itemId));
        detail.setPartnerNames(partnerService.getNames(Arrays.asList(item.getSecurityTrustee(), item.getSecurityAgent(),
                item.getCustodian()).stream().filter(Objects::nonNull).distinct().collect(Collectors.toList())));
        return detail;
    }

    // ------------------------------------------------------------------------------------------------ writing

    @Override
    public CollateralItem createItem(UUID loanApplicationId, CollateralItem request, String userName) {
        LoanApplication loanApplication = findLoanApplication(loanApplicationId);

        CollateralChecklist checklist = checklistRepository.findByLoanApplication_Id(loanApplicationId).orElse(null);
        if (checklist == null) {
            checklist = new CollateralChecklist();
            checklist.setLoanApplication(loanApplication);
            checklist.setLoanContractId(loanApplication.getLoanContractId());
            checklist.setWorkFlowStatusCode(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL);
            checklist.setWorkFlowStatusDescription(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL_TEXT);
            stampCreated(checklist, userName);
            checklist = checklistRepository.save(checklist);
            changeDocumentService.record(loanApplication, checklist.getId(), checklist.getId(),
                    "Collateral checklist of loan " + loanApplication.getLoanContractId(), null, checklist,
                    CollateralChangeDocumentService.CREATED, userName, CollateralChangeDocumentService.SUB_PROCESS_CHECKLIST);
        }

        checkNotInApproval(checklist);
        CollateralItem item = new CollateralItem();
        copyBusinessFields(request, item);
        normalizeAndValidate(item);
        checkPartners(null, item);
        item.setChecklist(checklist);
        item.setChecklistIdNo(numberRangeService.nextChecklistIdNo());
        item.setSourceOfEntry(SOURCE_OF_ENTRY_PORTAL);
        stampCreated(item, userName);
        item = itemRepository.save(item);

        changeDocumentService.record(loanApplication, checklist.getId(), item.getId(), describe(item), null,
                snapshot(item), CollateralChangeDocumentService.CREATED, userName,
                CollateralChangeDocumentService.SUB_PROCESS_ITEM);
        return item;
    }

    @Override
    public CollateralItem updateItem(UUID itemId, CollateralItem request, String userName) {
        CollateralItem item = findItem(itemId);
        checkNotInApproval(item.getChecklist());
        CollateralItem before = snapshot(item);

        CollateralItem changed = snapshot(item);
        copyBusinessFields(request, changed);
        normalizeAndValidate(changed);
        checkPartners(before, changed);
        copyBusinessFields(changed, item);
        copyField("complianceStatusText", changed, item);
        copyField("collateralAgreementTypeDescription", changed, item);
        item.setChangedOn(LocalDate.now());
        item.setChangedAt(LocalTime.now());
        item.setChangedByUserName(userName);
        item = itemRepository.save(item);

        CollateralChecklist checklist = item.getChecklist();
        changeDocumentService.record(checklist.getLoanApplication(), checklist.getId(), item.getId(), describe(item),
                before, snapshot(item), CollateralChangeDocumentService.UPDATED, userName,
                CollateralChangeDocumentService.SUB_PROCESS_ITEM);
        return item;
    }

    @Override
    public void deleteItem(UUID itemId, String userName) {
        CollateralItem item = findItem(itemId);
        CollateralChecklist checklist = item.getChecklist();
        checkNotInApproval(checklist);
        CollateralItem deleted = snapshot(item);
        childService.deleteAllOfItem(item, userName);
        itemRepository.delete(item);
        changeDocumentService.record(checklist.getLoanApplication(), checklist.getId(), deleted.getId(),
                describe(deleted), null, deleted, CollateralChangeDocumentService.DELETED, userName,
                CollateralChangeDocumentService.SUB_PROCESS_ITEM);
    }

    /** Collaterals of a checklist that waits for approval cannot be created, changed or deleted. */
    static void checkNotInApproval(CollateralChecklist checklist) {
        if (checklist != null && checklist.isInApproval()) {
            throw new LmsException("The collateral checklist of this loan is waiting for approval and cannot be changed.",
                    HttpStatus.CONFLICT);
        }
    }

    // ------------------------------------------------------------------------------------------------ validation

    /** Trims texts, derives descriptions and checks required fields, value lists, lengths, ranges and dates. */
    void normalizeAndValidate(CollateralItem item) {
        for (Field field : ITEM_FIELDS) {
            if (field.getType() == String.class) {
                String value = (String) get(field, item);
                if (value != null) {
                    String trimmed = isLongText(field) ? value : value.trim();
                    set(field, item, trimmed.trim().isEmpty() ? null : trimmed);
                }
            }
        }

        require(item.getConditionGroup(), "conditionGroup");
        require(item.getConditionCategory(), "conditionCategory");
        require(item.getCollateralObjectType(), "collateralObjectType");

        Map<String, Map<String, String>> lists = valueListsByCode();
        checkCode(lists, CollateralValueList.CONDITION_GROUP, item.getConditionGroup(), "conditionGroup");
        checkCode(lists, CollateralValueList.CONDITION_CATEGORY, item.getConditionCategory(), "conditionCategory");
        checkCode(lists, CollateralValueList.COLLATERAL_OBJECT_TYPE, item.getCollateralObjectType(), "collateralObjectType");
        checkCode(lists, CollateralValueList.AGREEMENT_TYPE, item.getCollateralAgreementType(), "collateralAgreementType");
        checkCode(lists, CollateralValueList.COMPLIANCE_STATUS, item.getComplianceStatus(), "complianceStatus");
        checkCode(lists, CollateralValueList.RESPONSIBLE_PARTY, item.getResponsibleParty(), "responsibleParty");
        checkCode(lists, CollateralValueList.ACTION_DAYS_PREFIX, item.getActionDaysPrefix(), "actionDaysPrefix");
        checkCode(lists, CollateralValueList.ACTION_DAYS_SUFFIX, item.getActionDaysSuffix(), "actionDaysSuffix");
        checkCode(lists, CollateralValueList.TIMELINE_EVENT, item.getTimelineEvent(), "timelineEvent");
        checkCode(lists, CollateralValueList.COMPETENT_AUTHORITY, item.getCompetentAuthority(), "competentAuthority");
        checkCode(lists, CollateralValueList.CURRENCY, item.getCollateralValueCurrency(), "collateralValueCurrency");
        checkCode(lists, CollateralValueList.FREQUENCY_UNIT, item.getFrequencyOfRecurrenceUnit(), "frequencyOfRecurrenceUnit");

        if (item.getCollateralAgreementType() != null) {
            List<String> allowed = allowedAgreementTypes(item.getCollateralObjectType());
            if (!allowed.isEmpty() && !allowed.contains(item.getCollateralAgreementType())) {
                throw invalid("Collateral Agreement Type " + item.getCollateralAgreementType()
                        + " is not allowed for collateral object " + item.getCollateralObjectType() + ".");
            }
        }
        if (item.getPostExecutionDocumentType() != null && documentTypeRepository.findAll().stream()
                .noneMatch(type -> item.getPostExecutionDocumentType().equals(type.getCode()))) {
            throw invalid(label("postExecutionDocumentType") + ": unknown document type " + item.getPostExecutionDocumentType() + ".");
        }
        if (item.getLandAreaUnit() != null && unitOfMeasureRepository.findAll().stream()
                .noneMatch(unit -> item.getLandAreaUnit().equals(unit.getCode()))) {
            throw invalid(label("landAreaUnit") + ": unknown unit of measure " + item.getLandAreaUnit() + ".");
        }

        // Descriptions kept beside the codes, as in SAP
        item.setComplianceStatusText(description(lists, CollateralValueList.COMPLIANCE_STATUS, item.getComplianceStatus(), 60));
        item.setCollateralAgreementTypeDescription(
                description(lists, CollateralValueList.AGREEMENT_TYPE, item.getCollateralAgreementType(), 30));
        if (item.getCollateralValue() != null && item.getCollateralValueCurrency() == null) {
            item.setCollateralValueCurrency(DEFAULT_CURRENCY);
        }

        checkPercentage(item.getPenalChargesPercentage(), "penalChargesPercentage");
        checkPercentage(item.getPenalChargesWaiverPercentage(), "penalChargesWaiverPercentage");
        checkPercentage(item.getExpectedValuePercentageHolding(), "expectedValuePercentageHolding");
        checkPercentage(item.getSecuritiesPercentageHolding(), "securitiesPercentageHolding");
        if (item.getPercentageOfDisbursement() != null
                && (item.getPercentageOfDisbursement() < 0 || item.getPercentageOfDisbursement() > 100)) {
            throw invalid(label("percentageOfDisbursement") + " must be between 0 and 100.");
        }
        checkNotNegative(item.getFrequencyOfRecurrence(), "frequencyOfRecurrence", 99);
        checkNotNegative(item.getActionPeriod(), "actionPeriod", 999);
        checkNotNegative(item.getCollateralValue(), "collateralValue");
        checkNotNegative(item.getExpectedValue(), "expectedValue");
        checkNotNegative(item.getLandArea(), "landArea");

        checkDateOrder(item.getValidFromDate(), item.getValidToDate(), "validFromDate", "validToDate");
        checkDateOrder(item.getStartDate(), item.getEndDate(), "startDate", "endDate");

        for (Field field : ITEM_FIELDS) {
            checkLength(field, item);
            checkDigits(field, item);
        }
    }

    /**
     * Security Trustee, Security Agent and Custodian must be party numbers of business partners. Values that did not
     * change are not checked again (migrated SAP values may name partners that are not in the portal).
     */
    private void checkPartners(CollateralItem before, CollateralItem item) {
        checkPartner(before == null ? null : before.getSecurityTrustee(), item.getSecurityTrustee(), "securityTrustee");
        checkPartner(before == null ? null : before.getSecurityAgent(), item.getSecurityAgent(), "securityAgent");
        checkPartner(before == null ? null : before.getCustodian(), item.getCustodian(), "custodian");
    }

    private void checkPartner(String previous, String partyNumber, String fieldName) {
        if (partyNumber != null && !partyNumber.equals(previous) && !partnerService.exists(partyNumber)) {
            throw invalid(label(fieldName) + ": no business partner with party number " + partyNumber + ".");
        }
    }

    private void checkLength(Field field, CollateralItem item) {
        Column column = field.getAnnotation(Column.class);
        if (field.getType() != String.class || column == null || isLongText(field)) {
            return;
        }
        String value = (String) get(field, item);
        if (value != null && value.length() > column.length()) {
            throw invalid(label(field.getName()) + " can have at most " + column.length() + " characters.");
        }
    }

    private void checkDigits(Field field, CollateralItem item) {
        Column column = field.getAnnotation(Column.class);
        if (field.getType() != BigDecimal.class || column == null || column.precision() == 0) {
            return;
        }
        BigDecimal value = (BigDecimal) get(field, item);
        if (value == null) {
            return;
        }
        if (value.scale() > column.scale()) {
            throw invalid(label(field.getName()) + " can have at most " + column.scale() + " decimal places.");
        }
        int integerDigits = value.precision() - value.scale();
        if (integerDigits > column.precision() - column.scale()) {
            throw invalid(label(field.getName()) + " is too large.");
        }
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private static final Comparator<CollateralItem> ITEM_ORDER = Comparator
            .comparing((CollateralItem item) -> item.getChecklistIdNo() == null ? 1 : 0)
            .thenComparing(item -> item.getChecklistIdNo() == null ? 0L : item.getChecklistIdNo())
            .thenComparing(item -> item.getCreatedOn() == null ? LocalDate.MIN : item.getCreatedOn())
            .thenComparing(item -> item.getCreatedAt() == null ? LocalTime.MIN : item.getCreatedAt());


    private LoanApplication findLoanApplication(UUID loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId)
                .orElseThrow(() -> new LmsException("Loan application not found.", HttpStatus.NOT_FOUND));
    }

    private CollateralItem findItem(UUID itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new LmsException("Collateral not found. It may have been deleted.", HttpStatus.NOT_FOUND));
    }

    private LoanSummaryDto loanSummary(LoanApplication loanApplication) {
        LoanSummaryDto dto = new LoanSummaryDto();
        dto.setLoanApplicationId(loanApplication.getId());
        dto.setLoanContractId(loanApplication.getLoanContractId());
        dto.setEnquiryNo(loanApplication.getEnquiryNo() != null ? loanApplication.getEnquiryNo().getId() : null);
        dto.setProjectName(loanApplication.getProjectName());
        dto.setBorrowerNumber(loanApplication.getbusPartnerNumber());
        dto.setFunctionalStatusDescription(loanApplication.getFunctionalStatusDescription());
        if (loanApplication.getLoanApplicant() != null) {
            Optional<Partner> partner = partnerRepository.findById(loanApplication.getLoanApplicant());
            partner.ifPresent(value -> dto.setBorrowerName(value.getPartyName1()));
        }
        return dto;
    }

    private List<String> allowedAgreementTypes(String collateralObjectType) {
        List<String> allowed = collateralObjectType == null ? null
                : agreementTypeMapping.getAllowedAgreementTypes(collateralObjectType);
        return allowed == null ? Collections.emptyList() : allowed;
    }

    private Map<String, Map<String, String>> valueListsByCode() {
        Map<String, Map<String, String>> lists = new LinkedHashMap<>();
        for (CollateralValue value : valueRepository.findAll()) {
            lists.computeIfAbsent(value.getListName(), name -> new LinkedHashMap<>())
                    .put(value.getCode(), value.getDescription());
        }
        return lists;
    }

    private static void checkCode(Map<String, Map<String, String>> lists, CollateralValueList list, String code,
                                  String fieldName) {
        if (code != null && !lists.getOrDefault(list.name(), Collections.emptyMap()).containsKey(code)) {
            throw invalid(label(fieldName) + ": unknown value " + code + ".");
        }
    }

    private static String description(Map<String, Map<String, String>> lists, CollateralValueList list, String code,
                                      int maxLength) {
        if (code == null) {
            return null;
        }
        String text = lists.getOrDefault(list.name(), Collections.emptyMap()).get(code);
        return text == null || text.length() <= maxLength ? text : text.substring(0, maxLength);
    }

    private static void require(String value, String fieldName) {
        if (value == null) {
            throw invalid(label(fieldName) + " is required.");
        }
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

    private static void checkNotNegative(Integer value, String fieldName, int max) {
        if (value != null && (value < 0 || value > max)) {
            throw invalid(label(fieldName) + " must be between 0 and " + max + ".");
        }
    }

    private static void checkDateOrder(LocalDate from, LocalDate to, String fromField, String toField) {
        if (from != null && to != null && to.isBefore(from)) {
            throw invalid(label(toField) + " cannot be before " + label(fromField) + ".");
        }
    }

    private static LmsException invalid(String message) {
        return new LmsException(message, HttpStatus.PRECONDITION_FAILED);
    }

    private static boolean isLongText(Field field) {
        return field.getAnnotation(SapField.class).longText();
    }

    /** Copies the fields the UI may change (all business fields except the system fields). */
    private static void copyBusinessFields(CollateralItem source, CollateralItem target) {
        for (Field field : ITEM_FIELDS) {
            if (!SYSTEM_FIELDS.contains(field.getName())) {
                set(field, target, get(field, source));
            }
        }
    }

    private static void copyField(String name, CollateralItem source, CollateralItem target) {
        for (Field field : ITEM_FIELDS) {
            if (field.getName().equals(name)) {
                set(field, target, get(field, source));
            }
        }
    }

    /** Detached copy of the business fields and the id, for change documents (no link to the checklist). */
    static CollateralItem snapshot(CollateralItem item) {
        CollateralItem copy = new CollateralItem();
        for (Field field : ITEM_FIELDS) {
            set(field, copy, get(field, item));
        }
        copy.setId(item.getId());
        return copy;
    }

    private static String describe(CollateralItem item) {
        String number = item.getChecklistIdNo() != null ? String.valueOf(item.getChecklistIdNo()) : "New";
        String text = item.getConditionDescription() != null ? item.getConditionDescription()
                : Objects.toString(item.getCollateralObjectType(), "");
        return (number + " " + text).trim();
    }

    private static void stampCreated(pfs.lms.enquiry.domain.AggregateRoot<?> entity, String userName) {
        entity.setCreatedOn(LocalDate.now());
        entity.setCreatedAt(LocalTime.now());
        entity.setCreatedByUserName(userName);
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

    static String label(String fieldName) {
        return LABELS.getOrDefault(fieldName, fieldName);
    }

    private static Map<String, String> labels() {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("conditionGroup", "Condition Group");
        labels.put("conditionCategory", "Condition Category");
        labels.put("collateralObjectType", "Collateral Object");
        labels.put("collateralObjectDescription", "Coll. Obj. Descr.");
        labels.put("conditionDescription", "Condition Description");
        labels.put("collateralAgreementType", "Collateral Agreement Type");
        labels.put("collateralAgreementId", "Collateral Agreement ID");
        labels.put("collateralValue", "Collateral Value");
        labels.put("collateralValueCurrency", "Currency");
        labels.put("validFromDate", "Valid from");
        labels.put("validToDate", "Valid to");
        labels.put("remarks", "Remarks");
        labels.put("additionalText", "Additional Text");
        labels.put("complianceStatus", "Compliance Status");
        labels.put("responsibleParty", "Responsible Party");
        labels.put("responsiblePartyDescription", "Responsible Party Description");
        labels.put("actionDaysPrefix", "Action Period");
        labels.put("actionDaysSuffix", "Action Period Unit");
        labels.put("actionPeriod", "Action Period (number)");
        labels.put("timelineEvent", "Event");
        labels.put("timelinesText", "Timelines");
        labels.put("competentAuthority", "Competent Authority");
        labels.put("boardCommitteeName", "Board Committee Name");
        labels.put("competentAuthorityRemarks", "Comp. Authority Remarks");
        labels.put("penalChargesPercentage", "Penal Charges %");
        labels.put("penalChargesWaiverPercentage", "Penal Charges Waiver %");
        labels.put("expectedValuePercentageHolding", "Expected Value % Holding");
        labels.put("securitiesPercentageHolding", "Percentage Holding");
        labels.put("percentageOfDisbursement", "Percentage of the Disbursement");
        labels.put("frequencyOfRecurrence", "Frequency of Recurrence");
        labels.put("frequencyOfRecurrenceUnit", "Frequency Unit");
        labels.put("expectedValue", "Expected Value");
        labels.put("landArea", "Total Land Area");
        labels.put("landAreaUnit", "Unit of Measure");
        labels.put("startDate", "Start Date");
        labels.put("endDate", "End Date");
        labels.put("postExecutionOpinion", "Post Execution Opinion from LLC");
        labels.put("postExecutionDocumentType", "Document Type");
        labels.put("postExecutionDocumentName", "Document Name");
        labels.put("remarksForKeyApprovals", "Remarks for Key Approvals");
        labels.put("waiverReason", "Remarks Waiver Reason");
        labels.put("securityTrustee", "Security Trustee");
        labels.put("securityAgent", "Security Agent");
        labels.put("custodian", "Custodian");
        labels.put("securityTrusteeRemarks", "Sec. Trustee Remarks");
        labels.put("securityAgentRemarks", "Sec. Agent Remarks");
        labels.put("custodianRemarks", "Custodian Remarks");
        return labels;
    }
}
