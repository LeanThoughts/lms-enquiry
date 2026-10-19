package pfs.lms.enquiry.reports.disbursement;

import pfs.lms.enquiry.reports.SelectOption;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** JSON shapes of the Loans Disbursement - Periodwise and Comparison Report (Reports › Loans Management). */
public final class DisbursementDtos {

    private DisbursementDtos() {
    }

    /** The selection screen of the SAP report. */
    public static class Selection {
        public String companyCode;                                   // Bukrs         Company Code
        public String mode = "R";                                    // Mode          Type: R = Report, C = Comparative
        public String fiscalYearFrom;                                // GjahrFrom     Fiscal Year
        public String fiscalYearTo;                                  // GjahrTo       Fiscal Year to
        public String periodType = "M";                              // PeriodType    Abstract Report
        public String periodFilter;                                  // PeriodFilter
        public List<SelectOption> contract = new ArrayList<>();      // Ranl          Loan Contract
        public List<SelectOption> partner = new ArrayList<>();       // Partner       Business Partner Id
        public String projectType;                                   // ProjectType   Project Type
        public boolean allLoans = true;                              // AllLoans      Fetch all loans
        public boolean suppressZero = true;                          // SuppressZero  Filter nil disbursements
    }

    /** A column of the result: its key in the rows and the SAP Fieldlabel as heading. */
    public static class Column {
        private String key;
        private String label;

        public Column() {
        }

        public Column(String key, String label) {
            this.key = key;
            this.label = label;
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
    }

    /** One row: a RowNo of the SAP result; its cells by column key (c0, c1, …). */
    public static class Row extends LinkedHashMap<String, String> {
        private static final long serialVersionUID = 1L;
    }

    public static class Report {
        private List<Column> columns = new ArrayList<>();
        private List<Row> rows = new ArrayList<>();
        private int total;
        private String generatedOn;
        private String query;

        public List<Column> getColumns() { return columns; }
        public void setColumns(List<Column> columns) { this.columns = columns; }
        public List<Row> getRows() { return rows; }
        public void setRows(List<Row> rows) { this.rows = rows; }
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
