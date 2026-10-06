package pfs.lms.enquiry.configuration.bptables;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Request and response bodies of the business partner configuration tables API. */
public final class BpTableDtos {

    private BpTableDtos() {
    }

    /** A value of a dropdown: the stored value and what is shown */
    public static class Option {
        private String value;
        private String label;

        public Option() {
        }

        public Option(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
    }

    /** A table in the overview */
    public static class TableSummary {
        private final String key;
        private final String title;
        private final String description;
        private final String configClass;
        private final long count;

        public TableSummary(BpTableDefinition definition, long count) {
            this.key = definition.getKey();
            this.title = definition.getTitle();
            this.description = definition.getDescription();
            this.configClass = definition.getConfigClass();
            this.count = count;
        }

        public String getKey() { return key; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getConfigClass() { return configClass; }
        public long getCount() { return count; }
    }

    public static class Overview {
        private List<TableSummary> tables = new ArrayList<>();
        private boolean canChange;
        private String userRole;

        public List<TableSummary> getTables() { return tables; }
        public void setTables(List<TableSummary> tables) { this.tables = tables; }
        public boolean isCanChange() { return canChange; }
        public void setCanChange(boolean canChange) { this.canChange = canChange; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
    }

    /** A row: its id (as text) and the values of the visible columns */
    public static class Row {
        private String id;
        private Map<String, Object> values = new LinkedHashMap<>();
        /** Texts of reference values, e.g. partnerGroup -> "0001 · PFS-Main Loan Partners" */
        private Map<String, String> labels = new LinkedHashMap<>();

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Map<String, Object> getValues() { return values; }
        public void setValues(Map<String, Object> values) { this.values = values; }
        public Map<String, String> getLabels() { return labels; }
        public void setLabels(Map<String, String> labels) { this.labels = labels; }
    }

    /** One page of a table with everything the app needs to show and change it */
    public static class TablePage {
        private BpTableDefinition table;
        private List<Row> rows = new ArrayList<>();
        private int page;
        private int size;
        private long total;
        private String search;
        private boolean canChange;
        private String userRole;
        /** Dropdown values per referenced table key */
        private Map<String, List<Option>> references = new LinkedHashMap<>();

        public BpTableDefinition getTable() { return table; }
        public void setTable(BpTableDefinition table) { this.table = table; }
        public List<Row> getRows() { return rows; }
        public void setRows(List<Row> rows) { this.rows = rows; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public String getSearch() { return search; }
        public void setSearch(String search) { this.search = search; }
        public boolean isCanChange() { return canChange; }
        public void setCanChange(boolean canChange) { this.canChange = canChange; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
        public Map<String, List<Option>> getReferences() { return references; }
        public void setReferences(Map<String, List<Option>> references) { this.references = references; }
    }

    /** Values of a new or changed row, by column name */
    public static class RowRequest {
        private Map<String, Object> values = new LinkedHashMap<>();

        public Map<String, Object> getValues() { return values; }
        public void setValues(Map<String, Object> values) { this.values = values; }
    }
}
