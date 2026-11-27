package pfs.lms.enquiry.reports.collateralsecurity;

import pfs.lms.enquiry.reports.SelectOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** JSON shapes of the Collateral Securities Report (Reports › Collaterals). */
public final class CollateralSecurityDtos {

    private CollateralSecurityDtos() {
    }

    /** The selection screen of the SAP report (Collateral Securities Report). */
    public static class Selection {
        public String companyCode = "PFS";                           // Ccode   Company Code
        public List<SelectOption> contract = new ArrayList<>();      // RRanl   Loan Number
        public List<SelectOption> partner = new ArrayList<>();       // RGpart  Loan Partner
        /** Filter block: the compliance states to include (all set by default, as on the SAP screen) */
        public boolean complied = true;                              // Co      Complied
        public boolean notComplied = true;                           // Nc      Not Complied
        public boolean inProcess = true;                             // Ip      In Process
        public boolean partiallyComplied = true;                     // Pc      Partially Complied
        public boolean timelineAvailable = true;                     // Ta      Timeline Available
    }

    /** A column of the result: the entity property, its heading and its kind (text, amount, number or date). */
    public static class Column {
        private String key;
        private String label;
        private String type;

        public Column() {
        }

        public Column(String key, String label, String type) {
            this.key = key;
            this.label = label;
            this.type = type;
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class Report {
        private List<Column> columns = new ArrayList<>();
        /** One row per collateral condition: values by property (dates as yyyy-MM-dd, decimals as plain numbers) */
        private List<Map<String, String>> rows = new ArrayList<>();
        private int total;
        private String generatedOn;
        private String query;

        public List<Column> getColumns() { return columns; }
        public void setColumns(List<Column> columns) { this.columns = columns; }
        public List<Map<String, String>> getRows() { return rows; }
        public void setRows(List<Map<String, String>> rows) { this.rows = rows; }
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        public String getGeneratedOn() { return generatedOn; }
        public void setGeneratedOn(String generatedOn) { this.generatedOn = generatedOn; }
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }

    /** Excel export request: the selection, the rows as shown and the visible columns in order. */
    public static class ExportRequest {
        private Selection selection;
        private List<Map<String, String>> rows = new ArrayList<>();
        private List<Column> columns = new ArrayList<>();

        public Selection getSelection() { return selection; }
        public void setSelection(Selection selection) { this.selection = selection; }
        public List<Map<String, String>> getRows() { return rows; }
        public void setRows(List<Map<String, String>> rows) { this.rows = rows == null ? new ArrayList<>() : rows; }
        public List<Column> getColumns() { return columns; }
        public void setColumns(List<Column> columns) { this.columns = columns == null ? new ArrayList<>() : columns; }
    }
}
