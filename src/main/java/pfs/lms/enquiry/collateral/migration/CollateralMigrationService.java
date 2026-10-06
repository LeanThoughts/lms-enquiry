package pfs.lms.enquiry.collateral.migration;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralValue;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.repository.CollateralCoverageRepository;
import pfs.lms.enquiry.collateral.repository.CollateralDocumentRepository;
import pfs.lms.enquiry.collateral.repository.CollateralSecuritiesPositionRepository;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionCersaiRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionNeslRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionRocRepository;
import pfs.lms.enquiry.collateral.repository.CollateralValueRepository;
import pfs.lms.enquiry.collateral.service.impl.CollateralNumberRangeService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * Loads collateral checklists from SAP (sent by the ABAP migration program) into the portal.
 * <p>
 * Each loan is saved in its own transaction. The load is idempotent: a checklist is found by loan contract,
 * a collateral by its Checklist ID (ZID_NO), a child row by its SAP row GUID (ITEM_ID); reruns update instead of
 * duplicating. A child table that is sent replaces the SAP rows of that table (rows created in the portal, without
 * ITEM_ID, are kept); a child table that is not sent is left as it is. Collaterals not sent are left as they are.
 * <p>
 * The data comes from SAP, the system of record, so no change documents and no SAP integration pointers are
 * written, and the portal's business validation is not applied: only values that do not fit a column are skipped.
 */
@Service
public class CollateralMigrationService {

    public static final String MIGRATION_USER = "SAP_MIGRATION";

    private static final Logger log = LoggerFactory.getLogger(CollateralMigrationService.class);
    private static final DateTimeFormatter SAP_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /** Keys of the item record that hold child tables; values are the accepted spellings. */
    private static final Map<CollateralChildType, String[]> CHILD_KEYS = new EnumMap<>(CollateralChildType.class);

    static {
        CHILD_KEYS.put(CollateralChildType.COVERAGE, new String[]{"coverages", "ZCOL_COV"});
        CHILD_KEYS.put(CollateralChildType.ROC, new String[]{"roc", "ZCOL_SPFCT_ROC"});
        CHILD_KEYS.put(CollateralChildType.CERSAI, new String[]{"cersai", "ZCOL_SPFCT_CERSAI"});
        CHILD_KEYS.put(CollateralChildType.NESL, new String[]{"nesl", "ZCOL_SPFCT_NESL"});
        CHILD_KEYS.put(CollateralChildType.DOCUMENT, new String[]{"documents", "ZCOL_DOC"});
        CHILD_KEYS.put(CollateralChildType.SECURITIES_POSITION, new String[]{"securities", "ZCOL_SEC_POS"});
    }

    /** Coded fields and their value lists; values sent as description or "code description" are converted. */
    private static final Map<String, CollateralValueList> CODED_FIELDS = new LinkedHashMap<>();

    static {
        CODED_FIELDS.put("conditionGroup", CollateralValueList.CONDITION_GROUP);
        CODED_FIELDS.put("conditionCategory", CollateralValueList.CONDITION_CATEGORY);
        CODED_FIELDS.put("collateralObjectType", CollateralValueList.COLLATERAL_OBJECT_TYPE);
        CODED_FIELDS.put("collateralAgreementType", CollateralValueList.AGREEMENT_TYPE);
        CODED_FIELDS.put("complianceStatus", CollateralValueList.COMPLIANCE_STATUS);
        CODED_FIELDS.put("responsibleParty", CollateralValueList.RESPONSIBLE_PARTY);
        CODED_FIELDS.put("actionDaysPrefix", CollateralValueList.ACTION_DAYS_PREFIX);
        CODED_FIELDS.put("actionDaysSuffix", CollateralValueList.ACTION_DAYS_SUFFIX);
        CODED_FIELDS.put("timelineEvent", CollateralValueList.TIMELINE_EVENT);
        CODED_FIELDS.put("competentAuthority", CollateralValueList.COMPETENT_AUTHORITY);
        CODED_FIELDS.put("frequencyOfRecurrenceUnit", CollateralValueList.FREQUENCY_UNIT);
        CODED_FIELDS.put("eventType", CollateralValueList.PERFECTION_EVENT_TYPE);
        CODED_FIELDS.put("coverageBasis", CollateralValueList.COVERAGE_BASIS);
        CODED_FIELDS.put("documentStage", CollateralValueList.DOCUMENT_STAGE);
        CODED_FIELDS.put("securitiesType", CollateralValueList.SECURITIES_TYPE);
    }

    private final CollateralChecklistRepository checklistRepository;
    private final CollateralItemRepository itemRepository;
    private final CollateralValueRepository valueRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final TransactionTemplate transactionTemplate;

    private final SapRecordMapper itemMapper = new SapRecordMapper(CollateralItem.class, CODED_FIELDS.keySet());
    private final Map<CollateralChildType, SapRecordMapper> childMappers = new EnumMap<>(CollateralChildType.class);
    private final Map<CollateralChildType, JpaRepository<? extends CollateralChildRecord<?>, UUID>> childRepositories =
            new EnumMap<>(CollateralChildType.class);
    private final Map<CollateralChildType, Function<UUID, List<? extends CollateralChildRecord<?>>>> childFinders =
            new EnumMap<>(CollateralChildType.class);

    public CollateralMigrationService(CollateralChecklistRepository checklistRepository,
                                      CollateralItemRepository itemRepository,
                                      CollateralValueRepository valueRepository,
                                      LoanApplicationRepository loanApplicationRepository,
                                      CollateralCoverageRepository coverageRepository,
                                      CollateralPerfectionRocRepository rocRepository,
                                      CollateralPerfectionCersaiRepository cersaiRepository,
                                      CollateralPerfectionNeslRepository neslRepository,
                                      CollateralDocumentRepository documentRepository,
                                      CollateralSecuritiesPositionRepository securitiesPositionRepository,
                                      PlatformTransactionManager transactionManager) {
        this.checklistRepository = checklistRepository;
        this.itemRepository = itemRepository;
        this.valueRepository = valueRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);

        for (CollateralChildType type : CollateralChildType.values()) {
            childMappers.put(type, new SapRecordMapper(type.getEntityClass(), CODED_FIELDS.keySet()));
        }
        childRepositories.put(CollateralChildType.COVERAGE, coverageRepository);
        childRepositories.put(CollateralChildType.ROC, rocRepository);
        childRepositories.put(CollateralChildType.CERSAI, cersaiRepository);
        childRepositories.put(CollateralChildType.NESL, neslRepository);
        childRepositories.put(CollateralChildType.DOCUMENT, documentRepository);
        childRepositories.put(CollateralChildType.SECURITIES_POSITION, securitiesPositionRepository);
        childFinders.put(CollateralChildType.COVERAGE, coverageRepository::findByItem_Id);
        childFinders.put(CollateralChildType.ROC, rocRepository::findByItem_Id);
        childFinders.put(CollateralChildType.CERSAI, cersaiRepository::findByItem_Id);
        childFinders.put(CollateralChildType.NESL, neslRepository::findByItem_Id);
        childFinders.put(CollateralChildType.DOCUMENT, documentRepository::findByItem_Id);
        childFinders.put(CollateralChildType.SECURITIES_POSITION, securitiesPositionRepository::findByItem_Id);
    }

    /** Migrates the checklists; each loan in its own transaction. A dry run checks everything and saves nothing. */
    public List<MigrationResultDto> migrate(List<JsonNode> checklists, boolean dryRun) {
        List<MigrationResultDto> results = new ArrayList<>();
        for (JsonNode checklist : checklists) {
            results.add(migrateChecklist(checklist, dryRun));
        }
        return results;
    }

    private MigrationResultDto migrateChecklist(JsonNode node, boolean dryRun) {
        MigrationResultDto result = new MigrationResultDto();
        result.setDryRun(dryRun);
        String loanContractId = SapRecordMapper.text(node, "loanContractId", "RANL");
        result.setLoanContractId(loanContractId);
        if (!node.isObject()) {
            result.failed("Each checklist must be a JSON object.");
            return result;
        }
        if (loanContractId == null) {
            result.failed("loanContractId (RANL) is missing.");
            return result;
        }
        try {
            transactionTemplate.execute(status -> {
                migrateInTransaction(node, loanContractId, result);
                if (dryRun || result.getStatus() == MigrationResultDto.Status.FAILED) {
                    status.setRollbackOnly();
                }
                return null;
            });
        } catch (RuntimeException ex) {
            log.error("Collateral migration of loan {} failed", loanContractId, ex);
            result.setItemsCreated(0);
            result.setItemsUpdated(0);
            result.setChildRowsCreated(0);
            result.setChildRowsUpdated(0);
            result.setChildRowsDeleted(0);
            result.setChecklistId(null);
            result.failed("Nothing saved for this loan: " + rootMessage(ex));
        }
        log.info("Collateral migration of loan {}: {}{} – items created {}, updated {}, skipped {}; child rows created {}, "
                        + "updated {}, deleted {}", loanContractId, result.getStatus(), dryRun ? " (dry run)" : "",
                result.getItemsCreated(), result.getItemsUpdated(), result.getItemsSkipped(), result.getChildRowsCreated(),
                result.getChildRowsUpdated(), result.getChildRowsDeleted());
        return result;
    }

    private void migrateInTransaction(JsonNode node, String loanContractId, MigrationResultDto result) {
        LoanApplication loan = findLoan(loanContractId);
        if (loan == null) {
            result.failed("No loan application with loan contract " + loanContractId + ".");
            return;
        }

        CollateralChecklist checklist = checklistRepository.findByLoanApplication_Id(loan.getId()).orElse(null);
        boolean newChecklist = checklist == null;
        if (newChecklist) {
            checklist = new CollateralChecklist();
            checklist.setLoanApplication(loan);
            checklist.setLoanContractId(loan.getLoanContractId());
            checklist.setWorkFlowStatusCode(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL);
            checklist.setWorkFlowStatusDescription(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL_TEXT);
            checklist.setCreatedOn(LocalDate.now());
            checklist.setCreatedAt(LocalTime.now());
            checklist.setCreatedByUserName(MIGRATION_USER);
        }
        String statusCode = SapRecordMapper.text(node, "workFlowStatusCode");
        if (statusCode != null) {
            try {
                checklist.setWorkFlowStatusCode(Integer.valueOf(statusCode));
                checklist.setWorkFlowStatusDescription(SapRecordMapper.text(node, "workFlowStatusDescription"));
            } catch (NumberFormatException ex) {
                result.warning(null, "workFlowStatusCode '" + statusCode + "' is not a number; status not changed.");
            }
        }
        checklist = checklistRepository.save(checklist);
        // A dry run saves nothing: a checklist created by it does not exist afterwards
        result.setChecklistId(result.isDryRun() && newChecklist ? null : checklist.getId());

        Map<String, Map<String, String>> lists = valueLists();
        Map<Long, CollateralItem> existing = new HashMap<>();
        for (CollateralItem item : itemRepository.findByChecklist_Id(checklist.getId())) {
            if (item.getChecklistIdNo() != null) {
                existing.put(item.getChecklistIdNo(), item);
            }
        }

        Set<String> childKeys = new HashSet<>();
        CHILD_KEYS.values().forEach(keys -> Arrays.stream(keys).forEach(key -> childKeys.add(key.toUpperCase(Locale.ROOT))));

        Set<Long> seen = new HashSet<>();
        for (JsonNode itemNode : SapRecordMapper.array(node, "items", "ZPFS_T_LN_CHKLST")) {
            String sentIdNo = alphaOut(SapRecordMapper.text(itemNode, "ZID_NO", "checklistIdNo"));
            if (sentIdNo == null) {
                result.setItemsSkipped(result.getItemsSkipped() + 1);
                result.error(null, "A collateral without ZID_NO was skipped.");
                continue;
            }
            Long checklistIdNo = checklistNumber(sentIdNo);
            String idNo = sentIdNo;
            if (checklistIdNo == null) {
                result.setItemsSkipped(result.getItemsSkipped() + 1);
                result.error(idNo, "ZID_NO '" + sentIdNo + "' is not a number between 1 and "
                        + CollateralNumberRangeService.MAX_CHECKLIST_ID + "; the collateral was skipped.");
                continue;
            }
            if (!seen.add(checklistIdNo)) {
                result.setItemsSkipped(result.getItemsSkipped() + 1);
                result.error(idNo, "ZID_NO sent twice for this loan; the second one was skipped.");
                continue;
            }

            CollateralItem item = existing.get(checklistIdNo);
            boolean isNew = item == null;
            if (isNew) {
                CollateralItem other = itemRepository.findByChecklistIdNo(checklistIdNo).orElse(null);
                if (other != null) {
                    result.setItemsSkipped(result.getItemsSkipped() + 1);
                    result.error(idNo, "Checklist ID No. " + checklistIdNo + " already belongs to a collateral of loan "
                            + other.getChecklist().getLoanContractId() + "; the collateral was skipped. If it was "
                            + "created in the portal, raise collateral.checklist-id.start-number above the SAP numbers.");
                    continue;
                }
            }
            if (isNew) {
                item = new CollateralItem();
                item.setChecklist(checklist);
            }
            List<String> problems = new ArrayList<>();
            itemMapper.apply(itemNode, item, childKeys, problems);
            item.setChecklistIdNo(checklistIdNo);
            normalizeCodes(itemMapper, item, lists, problems);
            if (item.getComplianceStatus() != null && item.getComplianceStatusText() == null) {
                item.setComplianceStatusText(truncate(description(lists, CollateralValueList.COMPLIANCE_STATUS,
                        item.getComplianceStatus()), 60));
            }
            if (item.getCollateralAgreementType() != null && item.getCollateralAgreementTypeDescription() == null) {
                item.setCollateralAgreementTypeDescription(truncate(description(lists, CollateralValueList.AGREEMENT_TYPE,
                        item.getCollateralAgreementType()), 30));
            }
            if (item.getSourceOfEntry() == null) {
                item.setSourceOfEntry(MIGRATION_USER);
            }
            stamp(item, itemNode, isNew);
            item = itemRepository.save(item);
            if (isNew) {
                result.setItemsCreated(result.getItemsCreated() + 1);
            } else {
                result.setItemsUpdated(result.getItemsUpdated() + 1);
            }
            problems.forEach(problem -> result.warning(idNo, problem));

            for (CollateralChildType type : CollateralChildType.values()) {
                if (hasKey(itemNode, CHILD_KEYS.get(type))) {
                    migrateChildren(type, item, loan, SapRecordMapper.array(itemNode, CHILD_KEYS.get(type)), lists, result);
                }
            }
        }
    }

    private void migrateChildren(CollateralChildType type, CollateralItem item, LoanApplication loan,
                                 List<JsonNode> rows, Map<String, Map<String, String>> lists, MigrationResultDto result) {
        SapRecordMapper mapper = childMappers.get(type);
        String idNo = String.valueOf(item.getChecklistIdNo());
        Map<String, CollateralChildRecord<?>> existing = new HashMap<>();
        List<CollateralChildRecord<?>> current = item.getId() == null ? new ArrayList<>()
                : new ArrayList<>(childFinders.get(type).apply(item.getId()));
        for (CollateralChildRecord<?> row : current) {
            if (row.getSapItemId() != null) {
                existing.put(normalizeGuid(row.getSapItemId()), row);
            }
        }

        Set<String> sent = new HashSet<>();
        for (JsonNode rowNode : rows) {
            String guid = normalizeGuid(SapRecordMapper.text(rowNode, "ITEM_ID", "sapItemId"));
            if (guid == null) {
                result.error(idNo, type.getLabel() + " row without ITEM_ID skipped.");
                continue;
            }
            if (!sent.add(guid)) {
                result.error(idNo, type.getLabel() + " row " + guid + " sent twice; the second one was skipped.");
                continue;
            }
            CollateralChildRecord<?> row = existing.get(guid);
            boolean isNew = row == null;
            if (isNew) {
                row = newChild(type);
                row.setItem(item);
            }
            List<String> problems = new ArrayList<>();
            mapper.apply(rowNode, row, new HashSet<>(), problems);
            row.setSapItemId(guid);
            row.setChecklistIdNo(item.getChecklistIdNo());
            if (row.getContractNumber() == null) {
                row.setContractNumber(loan.getLoanContractId());
            }
            if (row.getSourceOfEntry() == null) {
                row.setSourceOfEntry(MIGRATION_USER);
            }
            normalizeCodes(mapper, row, lists, problems);
            if (row instanceof CollateralCoverage) {
                CollateralCoverage coverage = (CollateralCoverage) row;
                if (coverage.getCoverageBasis() != null && coverage.getCoverageBasisDescription() == null) {
                    coverage.setCoverageBasisDescription(truncate(description(lists, CollateralValueList.COVERAGE_BASIS,
                            coverage.getCoverageBasis()), 60));
                }
            } else if (type == CollateralChildType.DOCUMENT) {
                if (mapper.get(row, "documentStage") != null && mapper.get(row, "documentStageDescription") == null) {
                    mapper.set(row, "documentStageDescription", truncate(description(lists, CollateralValueList.DOCUMENT_STAGE,
                            (String) mapper.get(row, "documentStage")), 60));
                }
            } else if (type == CollateralChildType.SECURITIES_POSITION) {
                if (mapper.get(row, "securitiesType") != null && mapper.get(row, "securitiesShortName") == null) {
                    mapper.set(row, "securitiesShortName", truncate(description(lists, CollateralValueList.SECURITIES_TYPE,
                            (String) mapper.get(row, "securitiesType")), 40));
                }
            } else if (mapper.get(row, "eventType") != null && mapper.get(row, "eventDescription") == null) {
                mapper.set(row, "eventDescription", truncate(description(lists, CollateralValueList.PERFECTION_EVENT_TYPE,
                        (String) mapper.get(row, "eventType")), 60));
            }
            stamp(row, rowNode, isNew);
            save(type, row);
            if (isNew) {
                result.setChildRowsCreated(result.getChildRowsCreated() + 1);
            } else {
                result.setChildRowsUpdated(result.getChildRowsUpdated() + 1);
            }
            problems.forEach(problem -> result.warning(idNo, type.getLabel() + " " + guid + ": " + problem));
        }

        // SAP rows that SAP no longer has are removed; rows created in the portal (no ITEM_ID) are kept
        for (Map.Entry<String, CollateralChildRecord<?>> entry : existing.entrySet()) {
            if (!sent.contains(entry.getKey())) {
                delete(type, entry.getValue());
                result.setChildRowsDeleted(result.getChildRowsDeleted() + 1);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private LoanApplication findLoan(String loanContractId) {
        String trimmed = loanContractId.trim();
        LoanApplication loan = loanApplicationRepository.findByLoanContractId(trimmed);
        if (loan == null && trimmed.matches("\\d+") && trimmed.length() < 13) {
            loan = loanApplicationRepository.findByLoanContractId(String.format("%13s", trimmed).replace(' ', '0'));
        }
        if (loan == null && trimmed.matches("0+\\d+")) {
            loan = loanApplicationRepository.findByLoanContractId(alphaOut(trimmed));
        }
        return loan;
    }

    /** Sets coded fields to their code when SAP sent a description, "code description" or a zero-padded code. */
    private static void normalizeCodes(SapRecordMapper mapper, Object target, Map<String, Map<String, String>> lists,
                                       List<String> problems) {
        for (Map.Entry<String, CollateralValueList> coded : CODED_FIELDS.entrySet()) {
            Object value = mapper.get(target, coded.getKey());
            if (!(value instanceof String)) {
                continue;
            }
            Map<String, String> codes = lists.getOrDefault(coded.getValue().name(), new HashMap<>());
            String code = toCode((String) value, codes);
            int maxLength = mapper.maxLength(coded.getKey());
            if (code == null && maxLength > 0 && ((String) value).length() > maxLength) {
                problems.add(coded.getKey() + ": value '" + value + "' is not in list " + coded.getValue().name()
                        + " and longer than " + maxLength + " characters; not loaded.");
                mapper.set(target, coded.getKey(), null);
            } else if (code == null) {
                problems.add(coded.getKey() + ": value '" + value + "' is not in list " + coded.getValue().name()
                        + "; kept as sent.");
            } else if (!code.equals(value)) {
                mapper.set(target, coded.getKey(), code);
            }
        }
    }

    static String toCode(String value, Map<String, String> codes) {
        String trimmed = value.trim();
        if (codes.containsKey(trimmed)) {
            return trimmed;
        }
        for (Map.Entry<String, String> entry : codes.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equalsIgnoreCase(trimmed)) {
                return entry.getKey();
            }
        }
        int space = trimmed.indexOf(' ');
        if (space > 0 && codes.containsKey(trimmed.substring(0, space))) {
            return trimmed.substring(0, space);
        }
        if (trimmed.matches("\\d+")) {
            for (String code : codes.keySet()) {
                if (code.matches("\\d+") && Integer.parseInt(code) == Integer.parseInt(trimmed)) {
                    return code;
                }
            }
        }
        return null;
    }

    /** Audit fields: from the SAP CREATED_* / CHANGED_* values when sent, otherwise the migration user and now. */
    private static void stamp(pfs.lms.enquiry.domain.AggregateRoot<?> entity, JsonNode node, boolean isNew) {
        if (isNew) {
            LocalDateTime created = timestamp(SapRecordMapper.text(node, "CREATED_AT"));
            String createdBy = SapRecordMapper.text(node, "CREATED_BY");
            entity.setCreatedOn(created != null ? created.toLocalDate() : LocalDate.now());
            entity.setCreatedAt(created != null ? created.toLocalTime() : LocalTime.now());
            entity.setCreatedByUserName(createdBy != null ? createdBy : MIGRATION_USER);
        }
        LocalDateTime changed = timestamp(SapRecordMapper.text(node, "CHANGED_AT"));
        String changedBy = SapRecordMapper.text(node, "CHANGED_BY");
        if (changed != null || changedBy != null || !isNew) {
            entity.setChangedOn(changed != null ? changed.toLocalDate() : LocalDate.now());
            entity.setChangedAt(changed != null ? changed.toLocalTime() : LocalTime.now());
            entity.setChangedByUserName(changedBy != null ? changedBy : MIGRATION_USER);
        }
    }

    /** SAP DEC 15 timestamp YYYYMMDDhhmmss; null when empty, zero or not readable. */
    private static LocalDateTime timestamp(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replace(".", "").replace(",", "").trim();
        if (digits.length() > 14) {
            digits = digits.substring(0, 14);
        }
        if (!digits.matches("\\d{14}") || digits.matches("0+")) {
            return null;
        }
        try {
            return LocalDateTime.parse(digits, SAP_TIMESTAMP);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private static boolean hasKey(JsonNode node, String[] keys) {
        java.util.Iterator<String> names = node.fieldNames();
        while (names.hasNext()) {
            String name = names.next();
            for (String key : keys) {
                if (name.equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<String, Map<String, String>> valueLists() {
        Map<String, Map<String, String>> lists = new LinkedHashMap<>();
        for (CollateralValue value : valueRepository.findAll()) {
            lists.computeIfAbsent(value.getListName(), name -> new LinkedHashMap<>())
                    .put(value.getCode(), value.getDescription());
        }
        return lists;
    }

    private static String description(Map<String, Map<String, String>> lists, CollateralValueList list, String code) {
        return lists.getOrDefault(list.name(), new HashMap<>()).get(code);
    }

    /** SAP ALPHA conversion out: leading zeros removed from numbers ("0000000912" becomes "912"). */
    /** The Checklist ID No. as a number, or null when it is not a number in the range of ZID_NO. */
    static Long checklistNumber(String value) {
        if (value == null || !value.matches("\\d{1,10}")) {
            return null;
        }
        long number = Long.parseLong(value);
        return number >= 1 && number <= CollateralNumberRangeService.MAX_CHECKLIST_ID ? number : null;
    }

    static String alphaOut(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.matches("\\d+")) {
            String stripped = trimmed.replaceFirst("^0+", "");
            return stripped.isEmpty() ? "0" : stripped;
        }
        return trimmed;
    }

    /** RAW 16 GUID as 32 upper-case hex characters (dashes removed). */
    static String normalizeGuid(String value) {
        if (value == null) {
            return null;
        }
        String guid = value.replace("-", "").trim().toUpperCase(Locale.ROOT);
        return guid.isEmpty() ? null : guid;
    }

    private static String truncate(String value, int length) {
        return value == null || value.length() <= length ? value : value.substring(0, length);
    }

    private static CollateralChildRecord<?> newChild(CollateralChildType type) {
        try {
            return type.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CollateralChildRecord<?>> void save(CollateralChildType type, CollateralChildRecord<?> row) {
        ((JpaRepository<T, UUID>) childRepositories.get(type)).save((T) row);
    }

    @SuppressWarnings("unchecked")
    private <T extends CollateralChildRecord<?>> void delete(CollateralChildType type, CollateralChildRecord<?> row) {
        ((JpaRepository<T, UUID>) childRepositories.get(type)).delete((T) row);
    }

    private static String rootMessage(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage() != null ? root.getMessage() : root.getClass().getSimpleName();
    }
}
