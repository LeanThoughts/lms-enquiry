package pfs.lms.enquiry.reports.iccapproval;

import pfs.lms.enquiry.reports.SelectOption;

import java.util.ArrayList;
import java.util.List;

/** JSON shapes of the ICC Approval Status Report (Reports › Loans Management). */
public final class IccApprovalDtos {

    private IccApprovalDtos() {
    }

    /** The selection screen of the SAP report "Loans : ICC Approval Status Report". */
    public static class Selection {
        public String companyCode;   // shown on the screen; MISIccApprovalStatRpt has no company code property
        public String fiscalYear;    // Fyear
        public String quarter;       // Quarter
        public List<SelectOption> contract = new ArrayList<>();   // Ranl select-option
    }

    /** One row of the SAP entity MISIccApprovalStatRpt (ZPFS_LMS_REPORTS_SRV), property names as in the gateway. */
    public static class Row {
        private String fyear;
        private String serialNo;
        private String quarter;
        private String ranl;
        private String status;
        private String bupaId;
        private String borrowerName;
        private String projectName;
        private String zzsanctionedAmt;
        private String commitCapital;
        private String loanType;
        private String sector;
        private String projectType;
        private String grpCompName;
        private String feePct;
        private String roi;
        private String iccStatusDesc;
        private String iccApprDate;
        private String ziccRemarks;
        private String bodDate;
        private String bodStatusDesc;
        private String zboardAppRemarks;

        public String getFyear() { return fyear; }
        public void setFyear(String v) { this.fyear = v; }
        public String getSerialNo() { return serialNo; }
        public void setSerialNo(String v) { this.serialNo = v; }
        public String getQuarter() { return quarter; }
        public void setQuarter(String v) { this.quarter = v; }
        public String getRanl() { return ranl; }
        public void setRanl(String v) { this.ranl = v; }
        public String getStatus() { return status; }
        public void setStatus(String v) { this.status = v; }
        public String getBupaId() { return bupaId; }
        public void setBupaId(String v) { this.bupaId = v; }
        public String getBorrowerName() { return borrowerName; }
        public void setBorrowerName(String v) { this.borrowerName = v; }
        public String getProjectName() { return projectName; }
        public void setProjectName(String v) { this.projectName = v; }
        public String getZzsanctionedAmt() { return zzsanctionedAmt; }
        public void setZzsanctionedAmt(String v) { this.zzsanctionedAmt = v; }
        public String getCommitCapital() { return commitCapital; }
        public void setCommitCapital(String v) { this.commitCapital = v; }
        public String getLoanType() { return loanType; }
        public void setLoanType(String v) { this.loanType = v; }
        public String getSector() { return sector; }
        public void setSector(String v) { this.sector = v; }
        public String getProjectType() { return projectType; }
        public void setProjectType(String v) { this.projectType = v; }
        public String getGrpCompName() { return grpCompName; }
        public void setGrpCompName(String v) { this.grpCompName = v; }
        public String getFeePct() { return feePct; }
        public void setFeePct(String v) { this.feePct = v; }
        public String getRoi() { return roi; }
        public void setRoi(String v) { this.roi = v; }
        public String getIccStatusDesc() { return iccStatusDesc; }
        public void setIccStatusDesc(String v) { this.iccStatusDesc = v; }
        public String getIccApprDate() { return iccApprDate; }
        public void setIccApprDate(String v) { this.iccApprDate = v; }
        public String getZiccRemarks() { return ziccRemarks; }
        public void setZiccRemarks(String v) { this.ziccRemarks = v; }
        public String getBodDate() { return bodDate; }
        public void setBodDate(String v) { this.bodDate = v; }
        public String getBodStatusDesc() { return bodStatusDesc; }
        public void setBodStatusDesc(String v) { this.bodStatusDesc = v; }
        public String getZboardAppRemarks() { return zboardAppRemarks; }
        public void setZboardAppRemarks(String v) { this.zboardAppRemarks = v; }
    }

    /** The result of a run. */
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

    /** Excel export request: the selection and the rows as shown. */
    public static class ExportRequest {
        private Selection selection;
        private List<Row> rows = new ArrayList<>();
        /** Titles of the columns to write (the visible ones); empty = all columns. */
        private List<String> columns = new ArrayList<>();

        public Selection getSelection() { return selection; }
        public void setSelection(Selection selection) { this.selection = selection; }
        public List<Row> getRows() { return rows; }
        public void setRows(List<Row> rows) { this.rows = rows; }
        public List<String> getColumns() { return columns; }
        public void setColumns(List<String> columns) { this.columns = columns == null ? new ArrayList<>() : columns; }
    }
}
