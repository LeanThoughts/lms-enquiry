package pfs.lms.enquiry.configuration.bptables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Option;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Overview;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Row;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.TablePage;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.TableSummary;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Configuration apps of the business partner configuration tables ({@link BpTableDefinitions}): paged and searchable
 * display, create, change and delete of rows. Works on the entities through JPA directly (some of the existing
 * repositories declare a different id type than their entity). Every signed-in user may display; the roles in
 * configuration.roles (default ZLM023) may change. Primary and business keys cannot be changed on existing rows,
 * because the startup configs find their rows by these keys.
 */
@Service
@Transactional
public class BpTableService {

    public static final int DEFAULT_PAGE_SIZE = 25;
    public static final int MAX_PAGE_SIZE = 200;

    private static final Logger log = LoggerFactory.getLogger(BpTableService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final boolean authorizationEnabled;
    private final List<String> configurationRoles;

    public BpTableService(UserRepository userRepository,
                          @Value("${collateral.authorization.enabled:true}") boolean authorizationEnabled,
                          @Value("${configuration.roles:ZLM023}") String configurationRoles) {
        this.userRepository = userRepository;
        this.authorizationEnabled = authorizationEnabled;
        this.configurationRoles = Arrays.stream(configurationRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .collect(Collectors.toList());
    }

    /** For tests */
    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // ------------------------------------------------------------------------------------------------ reading

    @Transactional(readOnly = true)
    public Overview getOverview(String principalName) {
        Overview overview = new Overview();
        overview.setTables(BpTableDefinitions.all().stream()
                .map(table -> new TableSummary(table, count(table, null)))
                .collect(Collectors.toList()));
        User user = user(principalName);
        overview.setUserRole(user != null ? user.getRole() : null);
        overview.setCanChange(canChange(user));
        return overview;
    }

    /** A page (0 based) of the rows matching the search text (in any text column), sorted by key. */
    @Transactional(readOnly = true)
    public TablePage getPage(String key, Integer page, Integer size, String search, String principalName) {
        BpTableDefinition table = table(key);
        int pageSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        String text = search == null ? "" : search.trim();
        long total = count(table, text);
        int lastPage = total == 0 ? 0 : (int) ((total - 1) / pageSize);
        int pageIndex = page == null || page < 0 ? 0 : Math.min(page, lastPage);

        TypedQuery<?> query = entityManager.createQuery("select e from " + from(table)
                + where(table, text) + orderBy(table), table.getEntityClass());
        if (!text.isEmpty()) {
            query.setParameter("search", "%" + text.toLowerCase() + "%");
        }
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);

        Map<String, List<Option>> references = references(table);
        TablePage result = new TablePage();
        result.setTable(table);
        result.setRows(query.getResultList().stream().map(entity -> toRow(table, entity, references))
                .collect(Collectors.toList()));
        result.setPage(pageIndex);
        result.setSize(pageSize);
        result.setTotal(total);
        result.setSearch(text);
        result.setReferences(references);
        User user = user(principalName);
        result.setUserRole(user != null ? user.getRole() : null);
        result.setCanChange(canChange(user));
        return result;
    }

    // ------------------------------------------------------------------------------------------------ changing

    public Row create(String key, Map<String, Object> values, String principalName) {
        checkChangeAccess(principalName);
        BpTableDefinition table = table(key);
        Map<String, Object> input = values == null ? new LinkedHashMap<>() : values;
        Object entity = newInstance(table);
        Map<String, List<Option>> references = references(table);
        for (BpTableField field : table.getFields()) {
            if (field.getIdKind() == BpTableField.IdKind.GENERATED_ID) {
                continue;
            }
            if (field.getIdKind() == BpTableField.IdKind.NEXT_NUMBER_ID) {
                set(entity, field, nextNumber(table, field));
                continue;
            }
            set(entity, field, convert(table, field, input.get(field.getName()), references));
        }
        BpTableField idField = table.getIdField();
        if (idField.getIdKind() == BpTableField.IdKind.ID
                && entityManager.find(table.getEntityClass(), get(entity, idField)) != null) {
            throw new LmsException(idField.getLabel() + " " + get(entity, idField) + " already exists in "
                    + table.getTitle() + ".", HttpStatus.CONFLICT);
        }
        checkBusinessKeyUnique(table, entity);
        entityManager.persist(entity);
        entityManager.flush();
        log.info("{}: row {} created by {}", table.getTitle(), describe(table, entity), principalName);
        return toRow(table, entity, references);
    }

    /** Changes the changeable columns of a row (not its keys). */
    public Row update(String key, String id, Map<String, Object> values, String principalName) {
        checkChangeAccess(principalName);
        BpTableDefinition table = table(key);
        Object entity = find(table, id);
        Map<String, Object> input = values == null ? new LinkedHashMap<>() : values;
        Map<String, List<Option>> references = references(table);
        int changed = 0;
        for (BpTableField field : table.getFields()) {
            if (!field.isChangeable() || !input.containsKey(field.getName())) {
                continue;
            }
            Object value = convert(table, field, input.get(field.getName()), references);
            if (!Objects.equals(normalize(get(entity, field)), normalize(value))) {
                set(entity, field, value);
                changed++;
            }
        }
        if (changed > 0) {
            entityManager.flush();
            log.info("{}: row {} changed by {} ({} columns)", table.getTitle(), describe(table, entity),
                    principalName, changed);
        }
        return toRow(table, entity, references);
    }

    public void delete(String key, String id, String principalName) {
        checkChangeAccess(principalName);
        BpTableDefinition table = table(key);
        Object entity = find(table, id);
        String name = describe(table, entity);
        try {
            entityManager.remove(entity);
            entityManager.flush();
        } catch (PersistenceException | DataIntegrityViolationException e) {
            throw new LmsException(name + " is used by other entries and cannot be deleted.", HttpStatus.CONFLICT);
        }
        log.info("{}: row {} deleted by {}", table.getTitle(), name, principalName);
    }

    // ------------------------------------------------------------------------------------------------ queries

    private long count(BpTableDefinition table, String search) {
        String text = search == null ? "" : search.trim();
        TypedQuery<Long> query = entityManager.createQuery("select count(e) from " + from(table)
                + where(table, text), Long.class);
        if (!text.isEmpty()) {
            query.setParameter("search", "%" + text.toLowerCase() + "%");
        }
        return query.getSingleResult();
    }

    /** "IndustryType e left join e.industrySystem r_industrySystem" */
    private static String from(BpTableDefinition table) {
        StringBuilder from = new StringBuilder(entityName(table)).append(" e");
        for (BpTableField field : table.getFields()) {
            if (field.isReferenceEntity()) {
                from.append(" left join e.").append(field.getName()).append(' ').append(alias(field));
            }
        }
        return from.toString();
    }

    private static String alias(BpTableField field) {
        return "r_" + field.getName();
    }

    private static String where(BpTableDefinition table, String search) {
        if (search == null || search.isEmpty()) {
            return "";
        }
        List<String> conditions = new ArrayList<>();
        for (BpTableField field : table.getFields()) {
            if (field.getType() != BpTableField.Type.TEXT || !field.isVisible()) {
                continue;
            }
            if (field.isReferenceEntity()) {
                BpTableDefinition referenced = table(field.getReferenceTable());
                conditions.add("lower(" + alias(field) + "." + referenced.getValueField() + ") like :search");
                conditions.add("lower(" + alias(field) + "." + referenced.getLabelField() + ") like :search");
            } else {
                conditions.add("lower(e." + field.getName() + ") like :search");
            }
        }
        return conditions.isEmpty() ? "" : " where " + String.join(" or ", conditions);
    }

    private static String orderBy(BpTableDefinition table) {
        List<String> sort = new ArrayList<>();
        List<BpTableField> keys = new ArrayList<>(table.getBusinessKeyFields());
        // Industry types: by industry system first
        keys.sort((a, b) -> Boolean.compare(!a.isReferenceEntity(), !b.isReferenceEntity()));
        for (BpTableField field : keys) {
            sort.add(field.isReferenceEntity()
                    ? alias(field) + "." + table(field.getReferenceTable()).getValueField()
                    : "e." + field.getName());
        }
        sort.add("e." + table.getIdField().getName());
        return " order by " + String.join(", ", sort);
    }

    private Object nextNumber(BpTableDefinition table, BpTableField field) {
        Object max = entityManager.createQuery("select max(e." + field.getName() + ") from " + entityName(table) + " e")
                .getSingleResult();
        long next = max == null ? 1 : ((Number) max).longValue() + 1;
        Class<?> type = javaField(table.getEntityClass(), field.getName()).getType();
        return type == Integer.class || type == int.class ? (Object) (int) next : (Object) next;
    }

    private void checkBusinessKeyUnique(BpTableDefinition table, Object entity) {
        List<BpTableField> keys = table.getBusinessKeyFields();
        if (keys.isEmpty()) {
            return;
        }
        StringBuilder jpql = new StringBuilder("select count(e) from " + entityName(table) + " e where ");
        for (int i = 0; i < keys.size(); i++) {
            jpql.append(i > 0 ? " and " : "").append("e.").append(keys.get(i).getName()).append(" = :k").append(i);
        }
        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        for (int i = 0; i < keys.size(); i++) {
            query.setParameter("k" + i, get(entity, keys.get(i)));
        }
        if (query.getSingleResult() > 0) {
            throw new LmsException(describe(table, entity) + " already exists in " + table.getTitle() + ".",
                    HttpStatus.CONFLICT);
        }
    }

    /** Dropdown values of the referenced tables and of fixed value lists */
    private Map<String, List<Option>> references(BpTableDefinition table) {
        Map<String, List<Option>> references = new LinkedHashMap<>();
        for (BpTableField field : table.getFields()) {
            String key = field.getReferenceTable();
            if (key == null || references.containsKey(key)) {
                continue;
            }
            BpTableDefinition referenced = table(key);
            List<?> rows = entityManager.createQuery("select e from " + from(referenced)
                    + orderBy(referenced), referenced.getEntityClass()).getResultList();
            BpTableField valueField = referenced.field(referenced.getValueField()).orElseThrow(IllegalStateException::new);
            BpTableField labelField = referenced.field(referenced.getLabelField()).orElseThrow(IllegalStateException::new);
            references.put(key, rows.stream().map(row -> {
                String code = text(get(row, valueField));
                String value = field.isReferenceEntity() ? text(get(row, referenced.getIdField())) : code;
                String label = text(get(row, labelField));
                return new Option(value, label.isEmpty() ? code : code + " · " + label);
            }).collect(Collectors.toList()));
        }
        return references;
    }

    // ------------------------------------------------------------------------------------------------ rows

    private Row toRow(BpTableDefinition table, Object entity, Map<String, List<Option>> references) {
        Row row = new Row();
        row.setId(text(get(entity, table.getIdField())));
        for (BpTableField field : table.getFields()) {
            if (!field.isVisible()) {
                continue;
            }
            Object value = get(entity, field);
            if (field.isReferenceEntity()) {
                BpTableDefinition referenced = table(field.getReferenceTable());
                String id = value == null ? null : text(get(value, referenced.getIdField()));
                row.getValues().put(field.getName(), id);
                if (value != null) {
                    String code = text(get(value, referenced.field(referenced.getValueField()).orElseThrow(IllegalStateException::new)));
                    String label = text(get(value, referenced.field(referenced.getLabelField()).orElseThrow(IllegalStateException::new)));
                    row.getLabels().put(field.getName(), label.isEmpty() ? code : code + " · " + label);
                }
                continue;
            }
            row.getValues().put(field.getName(), field.getType() == BpTableField.Type.TEXT && value != null ? text(value) : value);
            String code = value == null ? "" : text(value);
            if (!code.isEmpty()) {
                List<Option> options = field.getReferenceTable() != null ? references.get(field.getReferenceTable())
                        : field.getOptions();
                if (options != null) {
                    options.stream().filter(option -> code.equals(option.getValue())).findFirst()
                            .ifPresent(option -> row.getLabels().put(field.getName(), option.getLabel()));
                }
            }
        }
        return row;
    }

    /** Checks and converts an entered value to the type of the entity column. */
    private Object convert(BpTableDefinition table, BpTableField field, Object raw, Map<String, List<Option>> references) {
        Class<?> type = javaField(table.getEntityClass(), field.getName()).getType();
        if (field.getType() == BpTableField.Type.BOOLEAN) {
            boolean value = raw instanceof Boolean ? (Boolean) raw : "true".equalsIgnoreCase(text(raw));
            return value;
        }
        String value = raw == null ? "" : text(raw);
        if (value.isEmpty()) {
            if (field.isRequired()) {
                throw invalid(field.getLabel() + " is required.");
            }
            return null;
        }
        if (value.length() > field.getMaxLength()) {
            throw invalid(field.getLabel() + " can have at most " + field.getMaxLength() + " characters.");
        }
        if (field.isReferenceEntity()) {
            BpTableDefinition referenced = table(field.getReferenceTable());
            Object target;
            try {
                target = entityManager.find(referenced.getEntityClass(), toIdType(referenced, value));
            } catch (IllegalArgumentException e) {
                target = null;
            }
            if (target == null) {
                throw invalid(field.getLabel() + " " + value + " does not exist.");
            }
            return target;
        }
        if (field.getReferenceTable() != null) {
            boolean known = references.getOrDefault(field.getReferenceTable(), new ArrayList<>()).stream()
                    .anyMatch(option -> value.equals(option.getValue()));
            if (!known) {
                throw invalid(field.getLabel() + " " + value + " does not exist in "
                        + table(field.getReferenceTable()).getTitle() + ".");
            }
        } else if (!field.getOptions().isEmpty()
                && field.getOptions().stream().noneMatch(option -> value.equals(option.getValue()))) {
            throw invalid(field.getLabel() + " " + value + " is not allowed.");
        }
        if (type == Integer.class || type == int.class) {
            return Integer.valueOf(value);
        }
        if (type == Long.class || type == long.class) {
            return Long.valueOf(value);
        }
        return value;
    }

    private Object find(BpTableDefinition table, String id) {
        Object entity = null;
        if (id != null && !id.trim().isEmpty()) {
            try {
                entity = entityManager.find(table.getEntityClass(), toIdType(table, id.trim()));
            } catch (IllegalArgumentException e) {
                entity = null;
            }
        }
        if (entity == null) {
            throw new LmsException("The entry no longer exists in " + table.getTitle() + ". Refresh and try again.",
                    HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    private static Object toIdType(BpTableDefinition table, String id) {
        Class<?> type = javaField(table.getEntityClass(), table.getIdField().getName()).getType();
        if (type == Long.class || type == long.class) {
            return Long.valueOf(id);
        }
        if (type == Integer.class || type == int.class) {
            return Integer.valueOf(id);
        }
        return id;
    }

    /** "Role TR0100 / Partner Group 0001" */
    private static String describe(BpTableDefinition table, Object entity) {
        List<BpTableField> keys = new ArrayList<>(table.getBusinessKeyFields());
        if (keys.isEmpty()) {
            keys.add(table.getIdField());
        }
        return keys.stream().map(field -> {
            Object value = get(entity, field);
            if (field.isReferenceEntity() && value != null) {
                BpTableDefinition referenced = table(field.getReferenceTable());
                value = get(value, referenced.field(referenced.getValueField()).orElseThrow(IllegalStateException::new));
            }
            return field.getLabel() + " " + text(value);
        }).collect(Collectors.joining(" / "));
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private static BpTableDefinition table(String key) {
        return BpTableDefinitions.find(key)
                .orElseThrow(() -> new LmsException("Unknown configuration table " + key + ".", HttpStatus.NOT_FOUND));
    }

    private static String entityName(BpTableDefinition table) {
        return table.getEntityClass().getSimpleName();
    }

    private static Object newInstance(BpTableDefinition table) {
        try {
            return table.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create " + table.getEntityClass().getName(), e);
        }
    }

    private static Field javaField(Class<?> type, String name) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                // look in the super class
            }
        }
        throw new IllegalStateException("No field " + name + " in " + type.getName());
    }

    private static Object get(Object entity, BpTableField field) {
        try {
            return javaField(entity.getClass(), field.getName()).get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void set(Object entity, BpTableField field, Object value) {
        try {
            Field javaField = javaField(entity.getClass(), field.getName());
            if (value == null && javaField.getType().isPrimitive()) {
                value = javaField.getType() == boolean.class ? Boolean.FALSE : 0;
            }
            javaField.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object normalize(Object value) {
        if (value instanceof String) {
            String text = ((String) value).trim();
            return text.isEmpty() ? null : text;
        }
        return value;
    }

    private static String text(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private void checkChangeAccess(String principalName) {
        User user = user(principalName);
        if (!canChange(user)) {
            String role = user != null && user.getRole() != null ? user.getRole().trim() : "(none)";
            throw new LmsException("No edit access for the user with the role " + role
                    + ". The configuration can only be displayed.", HttpStatus.FORBIDDEN);
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

    private static LmsException invalid(String message) {
        return new LmsException(message, HttpStatus.PRECONDITION_FAILED);
    }
}
