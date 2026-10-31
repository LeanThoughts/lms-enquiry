package pfs.lms.enquiry.configuration.bptables;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** A configuration table: an entity that one of the CommandLineRunner configs fills at startup (business partner or loans). */
public class BpTableDefinition {

    private final String key;
    private final String title;
    private final String description;
    /** The CommandLineRunner in pfs.lms.enquiry.businesspartner.config that delivers the initial rows */
    private final String configClass;
    @JsonIgnore
    private final Class<?> entityClass;
    private final List<BpTableField> fields;
    /** Column that names a row (codes in other tables point to it), e.g. "code" */
    private final String valueField;
    /** Column with the description of a row, e.g. "value" */
    private final String labelField;
    /** Configuration module (tab of the Configuration workspace) the table belongs to: business-partner or loans */
    private String module = BpTableDefinitions.BUSINESS_PARTNER;
    /** Sort of the rows (columns), default: the business keys */
    @JsonIgnore
    private List<String> sortFields = new java.util.ArrayList<>();
    /** Extra check of a row before it is saved; returns an error text or null */
    @JsonIgnore
    private java.util.function.Function<Object, String> validator;

    public BpTableDefinition(String key, String title, String description, String configClass, Class<?> entityClass,
                             String valueField, String labelField, BpTableField... fields) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.configClass = configClass;
        this.entityClass = entityClass;
        this.valueField = valueField;
        this.labelField = labelField;
        this.fields = Arrays.asList(fields);
    }

    @JsonIgnore
    public BpTableField getIdField() {
        return fields.stream().filter(BpTableField::isIdField).findFirst()
                .orElseThrow(() -> new IllegalStateException("No id field in " + key));
    }

    @JsonIgnore
    public List<BpTableField> getBusinessKeyFields() {
        return fields.stream().filter(BpTableField::isBusinessKey).collect(Collectors.toList());
    }

    public Optional<BpTableField> field(String name) {
        return fields.stream().filter(field -> field.getName().equals(name)).findFirst();
    }

    public String getKey() { return key; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getConfigClass() { return configClass; }
    public Class<?> getEntityClass() { return entityClass; }
    public List<BpTableField> getFields() { return fields; }
    public String getValueField() { return valueField; }
    public String getLabelField() { return labelField; }
    public String getModule() { return module; }

    public List<String> getSortFields() { return sortFields; }
    public java.util.function.Function<Object, String> getValidator() { return validator; }

    /** Sorts the rows by these columns (then by the key) */
    public BpTableDefinition sortBy(String... fields) {
        this.sortFields = Arrays.asList(fields);
        return this;
    }

    /** Checks a row before it is saved: the function returns an error text, or null when the row is fine */
    public BpTableDefinition validate(java.util.function.Function<Object, String> validator) {
        this.validator = validator;
        return this;
    }

    /** Puts the table into another configuration module (default: business-partner) */
    public BpTableDefinition module(String module) {
        this.module = module;
        return this;
    }
}
