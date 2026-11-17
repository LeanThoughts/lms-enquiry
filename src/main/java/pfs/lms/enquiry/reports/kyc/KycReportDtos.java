package pfs.lms.enquiry.reports.kyc;

import pfs.lms.enquiry.reports.SelectOption;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** JSON shapes of the Loans KYC Report (Reports › Loans Management). */
public final class KycReportDtos {

    private KycReportDtos() {
    }

    /** The selection screen of the SAP report "Loans KYC Report". */
    public static class Selection {
        public String companyCode;                 // fixed PFS; the entity has no company code property
        public List<SelectOption> contract = new ArrayList<>();   // ContractNo select-option
        public List<SelectOption> partner = new ArrayList<>();    // PartnerId select-option
        public List<SelectOption> status = new ArrayList<>();     // Status select-option (loan contract status)
        public boolean includeAuthSignatories;     // IncludeAuthSign
        public String runDate;                     // RunDate, ISO date
        public boolean effCapOnly;                 // NonZeroEffCapOnly (Select Loans having Eff Cap)
        public List<SelectOption> kycDate = new ArrayList<>();    // DateKycCompl select-option, ISO dates
    }

    /** One row: the output properties of the SAP entity KycReport by their lower-camel-case names. */
    public static class Row extends LinkedHashMap<String, String> {
        private static final long serialVersionUID = 1L;
    }

    public static class Report {
        private List<Row> rows = new ArrayList<>();
        private int total;
        private String generatedOn;
        private String query;

        public List<Row> getRows() { return rows; }
        public void setRows(List<Row> rows) { this.rows = rows; }
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        public String getGeneratedOn() { return generatedOn; }
        public void setGeneratedOn(String generatedOn) { this.generatedOn = generatedOn; }
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }

    /** Excel export request: the selection, the rows as shown and the titles of the visible columns. */
    public static class ExportRequest {
        private Selection selection;
        private List<Map<String, String>> rows = new ArrayList<>();
        private List<String> columns = new ArrayList<>();

        public Selection getSelection() { return selection; }
        public void setSelection(Selection selection) { this.selection = selection; }
        public List<Map<String, String>> getRows() { return rows; }
        public void setRows(List<Map<String, String>> rows) { this.rows = rows == null ? new ArrayList<>() : rows; }
        public List<String> getColumns() { return columns; }
        public void setColumns(List<String> columns) { this.columns = columns == null ? new ArrayList<>() : columns; }
    }
}
