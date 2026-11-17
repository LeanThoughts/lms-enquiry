package pfs.lms.enquiry.reports.balancelist;

import pfs.lms.enquiry.reports.SelectOption;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** JSON shapes of the Loans Balance List (Reports › Loans Management). */
public final class BalanceListDtos {

    private BalanceListDtos() {
    }

    /** The selection screen of the SAP report "Balance List" (FNR7). */
    public static class Selection {
        public String companyCode;                                   // fixed PFS (Bukrs)
        public List<SelectOption> contract = new ArrayList<>();      // Ranl      Contract Number
        public List<SelectOption> productType = new ArrayList<>();   // Gsart     Product Type (business type)
        public List<SelectOption> loanType = new ArrayList<>();      // Stitel    Loan Type
        public List<SelectOption> currency = new ArrayList<>();      // Santwhr   Contract Currency
        public List<SelectOption> acctRef = new ArrayList<>();       // Rrefkont  Acct Assignment Ref.
        public List<SelectOption> changedBy = new ArrayList<>();     // Rbear     Last Changed by
        public boolean evaluateCompleted = true;                     // PaInteb   Evaluate completed contracts?
        public String keyDate;                                       // PaStich   Key date, ISO date
        public String displayCurrency;                               // PaAnzw    Display currency
        public boolean nonZeroOnly;                                  // FlgSald   Only display balances <> 0?
        public boolean totalsForZero;                                // PaAll     Also totals for balances = 0?
        public boolean totalsOnly;                                   // PaSumme   Only display totals?
    }

    /** One row: the output properties of the SAP entity ZFNR7ReportSet by their lower-camel-case names. */
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
