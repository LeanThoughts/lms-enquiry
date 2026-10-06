package pfs.lms.enquiry.configuration.fieldstatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntityFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntitySetFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.Changes;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntityField;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetField;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetFieldStatus;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetOverview;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.Overview;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.RoleFieldStatus;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.RoleSummary;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Configuration app "Business Partner Field Status": maintains BupaRoleEntityFieldStatus and
 * BupaRoleEntitySetFieldStatus per business partner role. Field status: 0 Display only, 1 Optional, 2 Mandatory,
 * 3 Hide. Every signed-in user may display; the roles in configuration.roles (default ZLM023) may change.
 */
@Service
@Transactional
public class FieldStatusConfigurationService {

    public static final int DISPLAY_ONLY = 0;
    public static final int HIDE = 3;

    private static final Logger log = LoggerFactory.getLogger(FieldStatusConfigurationService.class);
    private static final int MAX_TEXT = 255;

    /** The existing repositories are used as they are (their id type is declared as String, so rows are found by role). */
    private final BupaRoleEntityFieldStatusRepository entityRepository;
    private final BupaRoleEntitySetFieldStatusRepository entitySetRepository;
    private final BusinessPartnerRoleTypeRepository roleTypeRepository;
    private final IdentificationCategoryRepository identificationCategoryRepository;
    private final UserRepository userRepository;
    private final boolean authorizationEnabled;
    private final List<String> configurationRoles;

    public FieldStatusConfigurationService(BupaRoleEntityFieldStatusRepository entityRepository,
                                           BupaRoleEntitySetFieldStatusRepository entitySetRepository,
                                           BusinessPartnerRoleTypeRepository roleTypeRepository,
                                           IdentificationCategoryRepository identificationCategoryRepository,
                                           UserRepository userRepository,
                                           @Value("${collateral.authorization.enabled:true}") boolean authorizationEnabled,
                                           @Value("${configuration.roles:ZLM023}") String configurationRoles) {
        this.entityRepository = entityRepository;
        this.entitySetRepository = entitySetRepository;
        this.roleTypeRepository = roleTypeRepository;
        this.identificationCategoryRepository = identificationCategoryRepository;
        this.userRepository = userRepository;
        this.authorizationEnabled = authorizationEnabled;
        this.configurationRoles = Arrays.stream(configurationRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------ reading

    /** Business partner roles (with the number of rows each has), the user's access and the known entities. */
    @Transactional(readOnly = true)
    public Overview getOverview(String principalName) {
        List<BupaRoleEntityFieldStatus> entityRows = entityRepository.findAll();
        List<BupaRoleEntitySetFieldStatus> entitySetRows = entitySetRepository.findAll();
        Map<String, Long> entityCounts = entityRows.stream()
                .collect(Collectors.groupingBy(row -> trim(row.getBupaRoleCode()), Collectors.counting()));
        Map<String, Long> entitySetCounts = entitySetRows.stream()
                .collect(Collectors.groupingBy(row -> trim(row.getBupaRoleCode()), Collectors.counting()));

        Map<String, String> descriptions = roleDescriptions();
        Set<String> codes = new TreeSet<>(descriptions.keySet());
        codes.addAll(entityCounts.keySet());
        codes.addAll(entitySetCounts.keySet());
        codes.remove("");

        Overview overview = new Overview();
        overview.setRoles(codes.stream()
                .map(code -> new RoleSummary(code, descriptions.get(code), entityCounts.getOrDefault(code, 0L),
                        entitySetCounts.getOrDefault(code, 0L)))
                .collect(Collectors.toList()));
        overview.setEntities(entityRows.stream().map(row -> trim(row.getEntity())).filter(text -> !text.isEmpty())
                .distinct().sorted().collect(Collectors.toList()));
        overview.setEntitySets(entitySetRows.stream().map(row -> trim(row.getEntitySet())).filter(text -> !text.isEmpty())
                .distinct().sorted().collect(Collectors.toList()));
        User user = user(principalName);
        overview.setUserRole(user != null ? user.getRole() : null);
        overview.setCanChange(canChange(user));
        return overview;
    }

    /** All field status rows of a role, sorted by entity (set), key field value and field name. */
    @Transactional(readOnly = true)
    public RoleFieldStatus getRole(String roleCode) {
        String code = requireRoleCode(roleCode);
        RoleFieldStatus result = new RoleFieldStatus();
        result.setRoleCode(code);
        result.setRoleDescription(roleDescriptions().get(code));
        result.setEntityFields(entityRepository.findByBupaRoleCode(code).stream()
                .sorted(Comparator.comparing((BupaRoleEntityFieldStatus row) -> trim(row.getEntity()))
                        .thenComparing(row -> trim(row.getFieldName()).toLowerCase()))
                .map(FieldStatusConfigurationService::toDto)
                .collect(Collectors.toList()));
        result.setEntitySetFields(entitySetRepository.findByBupaRoleCode(code).stream()
                .sorted(Comparator.comparing((BupaRoleEntitySetFieldStatus row) -> trim(row.getEntitySet()))
                        .thenComparing(row -> trim(row.getKeyFieldValue()))
                        .thenComparing(row -> row.isKeyField() ? 0 : 1)
                        .thenComparing(row -> trim(row.getFieldName()).toLowerCase()))
                .map(FieldStatusConfigurationService::toDto)
                .collect(Collectors.toList()));
        return result;
    }

    // ------------------------------------------------------------------------------------------------ changing

    /** Saves the changed field status (and, for entity sets, key field flag and minimum entries) of a role. */
    public RoleFieldStatus saveChanges(String roleCode, Changes changes, String principalName) {
        checkChangeAccess(principalName);
        String code = requireRoleCode(roleCode);
        if (changes == null) {
            throw invalid("No changes were sent.");
        }
        Map<Integer, BupaRoleEntityFieldStatus> entityRows = entityRepository.findByBupaRoleCode(code).stream()
                .collect(Collectors.toMap(BupaRoleEntityFieldStatus::getId, Function.identity()));
        Map<Integer, BupaRoleEntitySetFieldStatus> entitySetRows = entitySetRepository.findByBupaRoleCode(code).stream()
                .collect(Collectors.toMap(BupaRoleEntitySetFieldStatus::getId, Function.identity()));

        int changed = 0;
        for (EntityField change : changes.getEntityFields()) {
            BupaRoleEntityFieldStatus row = change.getId() == null ? null : entityRows.get(change.getId());
            if (row == null) {
                throw new LmsException("A changed field no longer exists for role " + code
                        + ". Refresh and try again.", HttpStatus.CONFLICT);
            }
            int status = checkStatus(change.getFieldStatus(), trim(row.getEntity()) + "." + trim(row.getFieldName()));
            if (!Objects.equals(row.getFieldStatus(), status)) {
                log.info("Field status {} {}.{}: {} -> {} by {}", code, trim(row.getEntity()), trim(row.getFieldName()),
                        row.getFieldStatus(), status, principalName);
                row.setFieldStatus(status);
                entityRepository.save(row);
                changed++;
            }
        }
        for (EntitySetField change : changes.getEntitySetFields()) {
            BupaRoleEntitySetFieldStatus row = change.getId() == null ? null : entitySetRows.get(change.getId());
            if (row == null) {
                throw new LmsException("A changed field no longer exists for role " + code
                        + ". Refresh and try again.", HttpStatus.CONFLICT);
            }
            String label = trim(row.getEntitySet()) + " " + trim(row.getKeyFieldValue()) + "." + trim(row.getFieldName());
            int status = checkStatus(change.getFieldStatus(), label);
            int minimum = checkMinimum(change.getMinimumEntries(), label);
            boolean keyField = change.getKeyField() != null ? change.getKeyField() : row.isKeyField();
            if (!Objects.equals(row.getFieldStatus(), status) || !Objects.equals(row.getMinimumEntries(), minimum)
                    || row.isKeyField() != keyField) {
                log.info("Field status {} {}: status {} -> {}, minimum {} -> {}, key field {} -> {} by {}", code, label,
                        row.getFieldStatus(), status, row.getMinimumEntries(), minimum, row.isKeyField(), keyField,
                        principalName);
                row.setFieldStatus(status);
                row.setMinimumEntries(minimum);
                row.setKeyField(keyField);
                entitySetRepository.save(row);
                changed++;
            }
        }
        log.info("Field status of role {}: {} rows changed by {}", code, changed, principalName);
        return getRole(code);
    }

    /** Adds a field to an entity of a role. */
    public RoleFieldStatus addEntityField(String roleCode, EntityField field, String principalName) {
        checkChangeAccess(principalName);
        String code = requireRoleCode(roleCode);
        if (field == null) {
            throw invalid("The field is missing.");
        }
        String entity = required(field.getEntity(), "Entity");
        String fieldName = required(field.getFieldName(), "Field Name");
        int status = checkStatus(field.getFieldStatus(), entity + "." + fieldName);
        boolean exists = entityRepository.findByBupaRoleCode(code).stream()
                .anyMatch(row -> trim(row.getEntity()).equals(entity) && trim(row.getFieldName()).equals(fieldName));
        if (exists) {
            throw new LmsException("Field " + entity + "." + fieldName + " already exists for role " + code + ".",
                    HttpStatus.CONFLICT);
        }
        entityRepository.save(new BupaRoleEntityFieldStatus(null, code, entity, fieldName, status));
        log.info("Field status {} {}.{} = {} added by {}", code, entity, fieldName, status, principalName);
        return getRole(code);
    }

    /** Adds a field to an entity set (and key field value) of a role. */
    public RoleFieldStatus addEntitySetField(String roleCode, EntitySetField field, String principalName) {
        checkChangeAccess(principalName);
        String code = requireRoleCode(roleCode);
        if (field == null) {
            throw invalid("The field is missing.");
        }
        String entitySet = required(field.getEntitySet(), "Entity Set");
        String keyFieldValue = required(field.getKeyFieldValue(), "Key Field Value");
        String fieldName = required(field.getFieldName(), "Field Name");
        String label = entitySet + " " + keyFieldValue + "." + fieldName;
        int status = checkStatus(field.getFieldStatus(), label);
        int minimum = checkMinimum(field.getMinimumEntries(), label);
        boolean exists = entitySetRepository.findByBupaRoleCode(code).stream()
                .anyMatch(row -> trim(row.getEntitySet()).equals(entitySet) && trim(row.getKeyFieldValue()).equals(keyFieldValue)
                        && trim(row.getFieldName()).equals(fieldName));
        if (exists) {
            throw new LmsException("Field " + label + " already exists for role " + code + ".", HttpStatus.CONFLICT);
        }
        entitySetRepository.save(new BupaRoleEntitySetFieldStatus(null, code, entitySet, fieldName, keyFieldValue,
                Boolean.TRUE.equals(field.getKeyField()), minimum, status));
        log.info("Field status {} {} = {} added by {}", code, label, status, principalName);
        return getRole(code);
    }

    public RoleFieldStatus deleteEntityField(Integer id, String principalName) {
        checkChangeAccess(principalName);
        BupaRoleEntityFieldStatus row = entityRepository.findAll().stream()
                .filter(candidate -> Objects.equals(candidate.getId(), id))
                .findFirst()
                .orElseThrow(() -> new LmsException("The field no longer exists.", HttpStatus.NOT_FOUND));
        entityRepository.delete(row);
        log.info("Field status {} {}.{} deleted by {}", row.getBupaRoleCode(), trim(row.getEntity()),
                trim(row.getFieldName()), principalName);
        return getRole(row.getBupaRoleCode());
    }

    public RoleFieldStatus deleteEntitySetField(Integer id, String principalName) {
        checkChangeAccess(principalName);
        BupaRoleEntitySetFieldStatus row = entitySetRepository.findAll().stream()
                .filter(candidate -> Objects.equals(candidate.getId(), id))
                .findFirst()
                .orElseThrow(() -> new LmsException("The field no longer exists.", HttpStatus.NOT_FOUND));
        entitySetRepository.delete(row);
        log.info("Field status {} {} {}.{} deleted by {}", row.getBupaRoleCode(), trim(row.getEntitySet()),
                trim(row.getKeyFieldValue()), trim(row.getFieldName()), principalName);
        return getRole(row.getBupaRoleCode());
    }

    /** Copies the rows of the source role that the target role does not have yet; existing rows are kept. */
    public RoleFieldStatus copyFromRole(String roleCode, String sourceRoleCode, String principalName) {
        return copyFromRole(roleCode, sourceRoleCode, false, principalName);
    }

    /**
     * Copies the rows of the source role that the target role does not have yet; existing rows are kept.
     *
     * @param entityFieldsOnly true: only BupaRoleEntityFieldStatus rows (entity sets are copied per entity set)
     */
    public RoleFieldStatus copyFromRole(String roleCode, String sourceRoleCode, boolean entityFieldsOnly,
                                        String principalName) {
        checkChangeAccess(principalName);
        String code = requireRoleCode(roleCode);
        String source = requireRoleCode(sourceRoleCode);
        if (code.equals(source)) {
            throw invalid("Choose another role to copy from.");
        }
        Set<String> entityKeys = new HashSet<>();
        entityRepository.findByBupaRoleCode(code).forEach(row -> entityKeys.add(key(row)));
        Set<String> entitySetKeys = new HashSet<>();
        entitySetRepository.findByBupaRoleCode(code).forEach(row -> entitySetKeys.add(key(row)));

        int copied = 0;
        for (BupaRoleEntityFieldStatus row : entityRepository.findByBupaRoleCode(source)) {
            if (entityKeys.add(key(row))) {
                entityRepository.save(new BupaRoleEntityFieldStatus(null, code, row.getEntity(), row.getFieldName(),
                        row.getFieldStatus()));
                copied++;
            }
        }
        for (BupaRoleEntitySetFieldStatus row : entityFieldsOnly ? List.<BupaRoleEntitySetFieldStatus>of()
                : entitySetRepository.findByBupaRoleCode(source)) {
            if (entitySetKeys.add(key(row))) {
                entitySetRepository.save(new BupaRoleEntitySetFieldStatus(null, code, row.getEntitySet(),
                        row.getFieldName(), row.getKeyFieldValue(), row.isKeyField(), row.getMinimumEntries(),
                        row.getFieldStatus()));
                copied++;
            }
        }
        log.info("Field status: {} rows copied from role {} to role {} by {}", copied, source, code, principalName);
        return getRole(code);
    }

    // ------------------------------------------------------------------------------------------------ by role and entity set

    /** Roles, entity sets and the number of rows of each role and entity set (app "BP Entity Set Fields"). */
    @Transactional(readOnly = true)
    public EntitySetOverview getEntitySetOverview(String principalName) {
        List<BupaRoleEntitySetFieldStatus> rows = entitySetRepository.findAll();
        Map<String, Map<String, Long>> counts = new TreeMap<>();
        for (BupaRoleEntitySetFieldStatus row : rows) {
            String role = trim(row.getBupaRoleCode());
            String set = trim(row.getEntitySet());
            if (!role.isEmpty() && !set.isEmpty()) {
                counts.computeIfAbsent(role, key -> new TreeMap<>()).merge(set, 1L, Long::sum);
            }
        }
        Map<String, String> descriptions = roleDescriptions();
        Set<String> codes = new TreeSet<>(descriptions.keySet());
        codes.addAll(counts.keySet());

        EntitySetOverview overview = new EntitySetOverview();
        overview.setRoles(codes.stream()
                .map(code -> new RoleSummary(code, descriptions.get(code), 0,
                        counts.getOrDefault(code, new TreeMap<>()).values().stream().mapToLong(Long::longValue).sum()))
                .collect(Collectors.toList()));
        overview.setEntitySets(rows.stream().map(row -> trim(row.getEntitySet())).filter(text -> !text.isEmpty())
                .distinct().sorted().collect(Collectors.toList()));
        overview.setCounts(counts);
        User user = user(principalName);
        overview.setUserRole(user != null ? user.getRole() : null);
        overview.setCanChange(canChange(user));
        return overview;
    }

    /** The fields of one entity set of a role, sorted by key field value, key field first, then field name. */
    @Transactional(readOnly = true)
    public EntitySetFieldStatus getEntitySet(String roleCode, String entitySet) {
        String code = requireRoleCode(roleCode);
        String set = required(entitySet, "Entity Set");
        EntitySetFieldStatus result = new EntitySetFieldStatus();
        result.setRoleCode(code);
        result.setRoleDescription(roleDescriptions().get(code));
        result.setEntitySet(set);
        result.setFields(rowsOf(code, set).stream()
                .sorted(Comparator.comparing((BupaRoleEntitySetFieldStatus row) -> trim(row.getKeyFieldValue()))
                        .thenComparing(row -> row.isKeyField() ? 0 : 1)
                        .thenComparing(row -> trim(row.getFieldName()).toLowerCase()))
                .map(FieldStatusConfigurationService::toDto)
                .collect(Collectors.toList()));
        result.setKeyFieldValues(entitySetRepository.findAll().stream()
                .filter(row -> trim(row.getEntitySet()).equals(set))
                .map(row -> trim(row.getKeyFieldValue()))
                .filter(text -> !text.isEmpty())
                .distinct().sorted().collect(Collectors.toList()));
        result.setKeyFieldDescriptions(keyFieldDescriptions(set));
        return result;
    }

    /** Entity set of the business partner identifications: its key field value is the identification category. */
    public static final String IDENTIFICATION_ENTITY_SET = "BusinessPartnerIdentification";

    /**
     * Descriptions of the key field values of an entity set: for identifications the identification categories
     * (IdentificationCategory code -> value, e.g. Z00001 -> PAN Card); other entity sets have none.
     */
    private Map<String, String> keyFieldDescriptions(String entitySet) {
        Map<String, String> descriptions = new TreeMap<>();
        if (IDENTIFICATION_ENTITY_SET.equals(entitySet)) {
            identificationCategoryRepository.findAll().forEach(category -> {
                if (category.getCode() != null && category.getValue() != null) {
                    descriptions.put(category.getCode().trim(), category.getValue().trim());
                }
            });
        }
        return descriptions;
    }

    /** Saves the changed rows of one entity set of a role (field status, key field, minimum entries). */
    public EntitySetFieldStatus saveEntitySetChanges(String roleCode, String entitySet, List<EntitySetField> changes,
                                                     String principalName) {
        checkChangeAccess(principalName);
        String code = requireRoleCode(roleCode);
        String set = required(entitySet, "Entity Set");
        Set<Integer> idsOfSet = rowsOf(code, set).stream().map(BupaRoleEntitySetFieldStatus::getId).collect(Collectors.toSet());
        for (EntitySetField change : changes == null ? List.<EntitySetField>of() : changes) {
            if (change.getId() == null || !idsOfSet.contains(change.getId())) {
                throw new LmsException("A changed field no longer belongs to " + set + " of role " + code
                        + ". Refresh and try again.", HttpStatus.CONFLICT);
            }
        }
        Changes all = new Changes();
        all.setEntitySetFields(changes == null ? List.of() : changes);
        saveChanges(code, all, principalName);
        return getEntitySet(code, set);
    }

    /** Adds a field to one entity set of a role. */
    public EntitySetFieldStatus addEntitySetField(String roleCode, String entitySet, EntitySetField field, String principalName) {
        if (field == null) {
            throw invalid("The field is missing.");
        }
        field.setEntitySet(required(entitySet, "Entity Set"));
        addEntitySetField(roleCode, field, principalName);
        return getEntitySet(roleCode, entitySet);
    }

    /** Copies the rows of one entity set from another role that this role does not have yet. */
    public EntitySetFieldStatus copyEntitySet(String roleCode, String entitySet, String sourceRoleCode, String principalName) {
        checkChangeAccess(principalName);
        String code = requireRoleCode(roleCode);
        String source = requireRoleCode(sourceRoleCode);
        String set = required(entitySet, "Entity Set");
        if (code.equals(source)) {
            throw invalid("Choose another role to copy from.");
        }
        Set<String> keys = rowsOf(code, set).stream().map(FieldStatusConfigurationService::key).collect(Collectors.toCollection(HashSet::new));
        int copied = 0;
        for (BupaRoleEntitySetFieldStatus row : rowsOf(source, set)) {
            if (keys.add(key(row))) {
                entitySetRepository.save(new BupaRoleEntitySetFieldStatus(null, code, row.getEntitySet(),
                        row.getFieldName(), row.getKeyFieldValue(), row.isKeyField(), row.getMinimumEntries(),
                        row.getFieldStatus()));
                copied++;
            }
        }
        log.info("Field status: {} rows of {} copied from role {} to role {} by {}", copied, set, source, code, principalName);
        return getEntitySet(code, set);
    }

    /** Deletes a row and returns the remaining rows of its entity set. */
    public EntitySetFieldStatus deleteEntitySetFieldOfSet(Integer id, String principalName) {
        BupaRoleEntitySetFieldStatus row = entitySetRepository.findAll().stream()
                .filter(candidate -> Objects.equals(candidate.getId(), id))
                .findFirst()
                .orElseThrow(() -> new LmsException("The field no longer exists.", HttpStatus.NOT_FOUND));
        deleteEntitySetField(id, principalName);
        return getEntitySet(row.getBupaRoleCode(), trim(row.getEntitySet()));
    }

    private List<BupaRoleEntitySetFieldStatus> rowsOf(String roleCode, String entitySet) {
        return entitySetRepository.findByBupaRoleCode(roleCode).stream()
                .filter(row -> trim(row.getEntitySet()).equals(entitySet))
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private void checkChangeAccess(String principalName) {
        User user = user(principalName);
        if (!canChange(user)) {
            String role = user != null && user.getRole() != null ? user.getRole().trim() : "(none)";
            throw new LmsException("No edit access for the user with the role " + role
                    + ". The field status can only be displayed.", HttpStatus.FORBIDDEN);
        }
    }

    private boolean canChange(User user) {
        if (!authorizationEnabled) {
            return true;
        }
        return user != null && user.isStatus() && user.getRole() != null
                && configurationRoles.contains(user.getRole().trim());
    }

    private User user(String principalName) {
        return principalName == null ? null : userRepository.findByEmail(principalName);
    }

    private Map<String, String> roleDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        for (BusinessPartnerRoleType type : roleTypeRepository.findAll()) {
            if (type.getCode() != null && !type.getCode().trim().isEmpty()) {
                descriptions.put(type.getCode().trim(), type.getValue());
            }
        }
        return descriptions;
    }

    private static String requireRoleCode(String roleCode) {
        String code = trim(roleCode);
        if (code.isEmpty()) {
            throw invalid("The business partner role is missing.");
        }
        return code;
    }

    private static int checkStatus(Integer status, String label) {
        if (status == null || status < DISPLAY_ONLY || status > HIDE) {
            throw invalid("Field status of " + label + " must be 0 (Display only), 1 (Optional), 2 (Mandatory) or 3 (Hide).");
        }
        return status;
    }

    private static int checkMinimum(Integer minimum, String label) {
        int value = minimum == null ? 0 : minimum;
        if (value < 0 || value > 999) {
            throw invalid("Minimum entries of " + label + " must be between 0 and 999.");
        }
        return value;
    }

    private static String required(String value, String label) {
        String text = trim(value);
        if (text.isEmpty()) {
            throw invalid(label + " is required.");
        }
        if (text.length() > MAX_TEXT) {
            throw invalid(label + " can have at most " + MAX_TEXT + " characters.");
        }
        return text;
    }

    private static String key(BupaRoleEntityFieldStatus row) {
        return trim(row.getEntity()) + "|" + trim(row.getFieldName());
    }

    private static String key(BupaRoleEntitySetFieldStatus row) {
        return trim(row.getEntitySet()) + "|" + trim(row.getKeyFieldValue()) + "|" + trim(row.getFieldName());
    }

    /** Some seeded field names end with blanks; they are compared and shown without them but stored unchanged. */
    static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static EntityField toDto(BupaRoleEntityFieldStatus row) {
        EntityField dto = new EntityField();
        dto.setId(row.getId());
        dto.setEntity(trim(row.getEntity()));
        dto.setFieldName(trim(row.getFieldName()));
        dto.setFieldStatus(row.getFieldStatus());
        return dto;
    }

    private static EntitySetField toDto(BupaRoleEntitySetFieldStatus row) {
        EntitySetField dto = new EntitySetField();
        dto.setId(row.getId());
        dto.setEntitySet(trim(row.getEntitySet()));
        dto.setKeyFieldValue(trim(row.getKeyFieldValue()));
        dto.setFieldName(trim(row.getFieldName()));
        dto.setKeyField(row.isKeyField());
        dto.setMinimumEntries(row.getMinimumEntries());
        dto.setFieldStatus(row.getFieldStatus());
        return dto;
    }

    private static LmsException invalid(String message) {
        return new LmsException(message, HttpStatus.PRECONDITION_FAILED);
    }
}
