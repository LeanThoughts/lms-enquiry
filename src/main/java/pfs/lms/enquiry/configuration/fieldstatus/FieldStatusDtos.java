package pfs.lms.enquiry.configuration.fieldstatus;

import java.util.ArrayList;
import java.util.List;

/** Request and response bodies of the field status Configuration app. */
public final class FieldStatusDtos {

    private FieldStatusDtos() {
    }

    /** A business partner role with the number of field status rows it has. */
    public static class RoleSummary {
        private String code;
        private String description;
        private long entityFieldCount;
        private long entitySetFieldCount;

        public RoleSummary() {
        }

        public RoleSummary(String code, String description, long entityFieldCount, long entitySetFieldCount) {
            this.code = code;
            this.description = description;
            this.entityFieldCount = entityFieldCount;
            this.entitySetFieldCount = entitySetFieldCount;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getEntityFieldCount() { return entityFieldCount; }
        public void setEntityFieldCount(long entityFieldCount) { this.entityFieldCount = entityFieldCount; }
        public long getEntitySetFieldCount() { return entitySetFieldCount; }
        public void setEntitySetFieldCount(long entitySetFieldCount) { this.entitySetFieldCount = entitySetFieldCount; }
    }

    /** Roles and the user's access (start of the app). */
    public static class Overview {
        private List<RoleSummary> roles = new ArrayList<>();
        private boolean canChange;
        private String userRole;
        /** Entities and entity sets used by any role, for the Add dialogs. */
        private List<String> entities = new ArrayList<>();
        private List<String> entitySets = new ArrayList<>();

        public List<RoleSummary> getRoles() { return roles; }
        public void setRoles(List<RoleSummary> roles) { this.roles = roles; }
        public boolean isCanChange() { return canChange; }
        public void setCanChange(boolean canChange) { this.canChange = canChange; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
        public List<String> getEntities() { return entities; }
        public void setEntities(List<String> entities) { this.entities = entities; }
        public List<String> getEntitySets() { return entitySets; }
        public void setEntitySets(List<String> entitySets) { this.entitySets = entitySets; }
    }

    /** One row of BupaRoleEntityFieldStatus. */
    public static class EntityField {
        private Integer id;
        private String entity;
        private String fieldName;
        private Integer fieldStatus;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getEntity() { return entity; }
        public void setEntity(String entity) { this.entity = entity; }
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public Integer getFieldStatus() { return fieldStatus; }
        public void setFieldStatus(Integer fieldStatus) { this.fieldStatus = fieldStatus; }
    }

    /** One row of BupaRoleEntitySetFieldStatus. */
    public static class EntitySetField {
        private Integer id;
        private String entitySet;
        private String keyFieldValue;
        private String fieldName;
        private Boolean keyField;
        private Integer minimumEntries;
        private Integer fieldStatus;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getEntitySet() { return entitySet; }
        public void setEntitySet(String entitySet) { this.entitySet = entitySet; }
        public String getKeyFieldValue() { return keyFieldValue; }
        public void setKeyFieldValue(String keyFieldValue) { this.keyFieldValue = keyFieldValue; }
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public Boolean getKeyField() { return keyField; }
        public void setKeyField(Boolean keyField) { this.keyField = keyField; }
        public Integer getMinimumEntries() { return minimumEntries; }
        public void setMinimumEntries(Integer minimumEntries) { this.minimumEntries = minimumEntries; }
        public Integer getFieldStatus() { return fieldStatus; }
        public void setFieldStatus(Integer fieldStatus) { this.fieldStatus = fieldStatus; }
    }

    /** All field status rows of one role. */
    public static class RoleFieldStatus {
        private String roleCode;
        private String roleDescription;
        private List<EntityField> entityFields = new ArrayList<>();
        private List<EntitySetField> entitySetFields = new ArrayList<>();

        public String getRoleCode() { return roleCode; }
        public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
        public String getRoleDescription() { return roleDescription; }
        public void setRoleDescription(String roleDescription) { this.roleDescription = roleDescription; }
        public List<EntityField> getEntityFields() { return entityFields; }
        public void setEntityFields(List<EntityField> entityFields) { this.entityFields = entityFields; }
        public List<EntitySetField> getEntitySetFields() { return entitySetFields; }
        public void setEntitySetFields(List<EntitySetField> entitySetFields) { this.entitySetFields = entitySetFields; }
    }

    /** Changed rows of one role, saved together (only id and the changeable values are used). */
    public static class Changes {
        private List<EntityField> entityFields = new ArrayList<>();
        private List<EntitySetField> entitySetFields = new ArrayList<>();

        public List<EntityField> getEntityFields() { return entityFields; }
        public void setEntityFields(List<EntityField> entityFields) { this.entityFields = entityFields; }
        public List<EntitySetField> getEntitySetFields() { return entitySetFields; }
        public void setEntitySetFields(List<EntitySetField> entitySetFields) { this.entitySetFields = entitySetFields; }
    }

    /** Copies the rows of another role that this role does not have yet. */
    public static class CopyRequest {
        private String sourceRoleCode;
        /** True: copy the entity fields only (page BP Field Status); entity sets are copied per entity set. */
        private Boolean entityFieldsOnly;

        public Boolean getEntityFieldsOnly() { return entityFieldsOnly; }
        public void setEntityFieldsOnly(Boolean entityFieldsOnly) { this.entityFieldsOnly = entityFieldsOnly; }

        public String getSourceRoleCode() { return sourceRoleCode; }
        public void setSourceRoleCode(String sourceRoleCode) { this.sourceRoleCode = sourceRoleCode; }
    }

    /** Start of the entity set app: roles, entity sets, rows per role and entity set, and the user's access. */
    public static class EntitySetOverview {
        private List<RoleSummary> roles = new ArrayList<>();
        private List<String> entitySets = new ArrayList<>();
        /** Number of rows by role code and entity set. */
        private java.util.Map<String, java.util.Map<String, Long>> counts = new java.util.TreeMap<>();
        private boolean canChange;
        private String userRole;

        public List<RoleSummary> getRoles() { return roles; }
        public void setRoles(List<RoleSummary> roles) { this.roles = roles; }
        public List<String> getEntitySets() { return entitySets; }
        public void setEntitySets(List<String> entitySets) { this.entitySets = entitySets; }
        public java.util.Map<String, java.util.Map<String, Long>> getCounts() { return counts; }
        public void setCounts(java.util.Map<String, java.util.Map<String, Long>> counts) { this.counts = counts; }
        public boolean isCanChange() { return canChange; }
        public void setCanChange(boolean canChange) { this.canChange = canChange; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
    }

    /** The fields of one entity set of one role. */
    public static class EntitySetFieldStatus {
        private String roleCode;
        private String roleDescription;
        private String entitySet;
        private List<EntitySetField> fields = new ArrayList<>();
        /** Key field values of this entity set used by any role (suggestions for Add). */
        private List<String> keyFieldValues = new ArrayList<>();
        /**
         * Description of each key field value, e.g. Z00001 -> "PAN Card" for BusinessPartnerIdentification (from the
         * identification categories). Values without a description are missing from the map.
         */
        private java.util.Map<String, String> keyFieldDescriptions = new java.util.TreeMap<>();

        public java.util.Map<String, String> getKeyFieldDescriptions() { return keyFieldDescriptions; }
        public void setKeyFieldDescriptions(java.util.Map<String, String> keyFieldDescriptions) { this.keyFieldDescriptions = keyFieldDescriptions; }

        public String getRoleCode() { return roleCode; }
        public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
        public String getRoleDescription() { return roleDescription; }
        public void setRoleDescription(String roleDescription) { this.roleDescription = roleDescription; }
        public String getEntitySet() { return entitySet; }
        public void setEntitySet(String entitySet) { this.entitySet = entitySet; }
        public List<EntitySetField> getFields() { return fields; }
        public void setFields(List<EntitySetField> fields) { this.fields = fields; }
        public List<String> getKeyFieldValues() { return keyFieldValues; }
        public void setKeyFieldValues(List<String> keyFieldValues) { this.keyFieldValues = keyFieldValues; }
    }
}
