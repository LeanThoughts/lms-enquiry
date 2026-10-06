package pfs.lms.enquiry.configuration.bptables;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** A business partner configuration table: an entity that one of the CommandLineRunner configs fills at startup. */
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
}
