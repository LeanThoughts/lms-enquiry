package pfs.lms.enquiry.reports.workflowstatus;

import pfs.lms.enquiry.reports.SelectOption;

import java.util.ArrayList;
import java.util.List;

/** JSON shapes of the Workflow Status Report (Reports › Miscellaneous). */
public final class WorkflowStatusDtos {

    private WorkflowStatusDtos() {
    }

    /** The selection screen of the SAP report "Loans: Workflow Status Report". */
    public static class Selection {
        public String companyCode;      // Bukrs, e.g. PFS
        public String releaseObject;    // Relobj, e.g. VDARL
        public List<SelectOption> contract = new ArrayList<>();   // Ranl select-option
        public List<SelectOption> partner = new ArrayList<>();    // Partner select-option
        public List<SelectOption> date = new ArrayList<>();       // Datum select-option, ISO dates
    }

    /** One row of the SAP entity WorkflowStatus (ZPFS_LMS_REPORTS_SRV), property names as in the gateway. */
    public static class Row {
        private String rowId;
        private String bukrs;
        private String relobj;
        private String partner;
        private String datum;
        private String relObject;
        private String ranl;
        private String parnter;
        private String boId;
        private String initiatorId;
        private String initiatorPosition;
        private String initiatorName;
        private String initiationDate;
        private String initiationTime;
        private String wfReferenceNo;
        private String taskText;
        private String approverId;
        private String approverPosition;
        private String approverName;
        private String approvalDate;
        private String approvalTime;
        private String releaseStatus;
        private String releaseStatusDesc;
        private String releaseIndicator;
        private String releaseIndicatorDesc;

        public String getRowId() { return rowId; }
        public void setRowId(String rowId) { this.rowId = rowId; }
        public String getBukrs() { return bukrs; }
        public void setBukrs(String bukrs) { this.bukrs = bukrs; }
        public String getRelobj() { return relobj; }
        public void setRelobj(String relobj) { this.relobj = relobj; }
        public String getPartner() { return partner; }
        public void setPartner(String partner) { this.partner = partner; }
        public String getDatum() { return datum; }
        public void setDatum(String datum) { this.datum = datum; }
        public String getRelObject() { return relObject; }
        public void setRelObject(String relObject) { this.relObject = relObject; }
        public String getRanl() { return ranl; }
        public void setRanl(String ranl) { this.ranl = ranl; }
        public String getParnter() { return parnter; }
        public void setParnter(String parnter) { this.parnter = parnter; }
        public String getBoId() { return boId; }
        public void setBoId(String boId) { this.boId = boId; }
        public String getInitiatorId() { return initiatorId; }
        public void setInitiatorId(String initiatorId) { this.initiatorId = initiatorId; }
        public String getInitiatorPosition() { return initiatorPosition; }
        public void setInitiatorPosition(String initiatorPosition) { this.initiatorPosition = initiatorPosition; }
        public String getInitiatorName() { return initiatorName; }
        public void setInitiatorName(String initiatorName) { this.initiatorName = initiatorName; }
        public String getInitiationDate() { return initiationDate; }
        public void setInitiationDate(String initiationDate) { this.initiationDate = initiationDate; }
        public String getInitiationTime() { return initiationTime; }
        public void setInitiationTime(String initiationTime) { this.initiationTime = initiationTime; }
        public String getWfReferenceNo() { return wfReferenceNo; }
        public void setWfReferenceNo(String wfReferenceNo) { this.wfReferenceNo = wfReferenceNo; }
        public String getTaskText() { return taskText; }
        public void setTaskText(String taskText) { this.taskText = taskText; }
        public String getApproverId() { return approverId; }
        public void setApproverId(String approverId) { this.approverId = approverId; }
        public String getApproverPosition() { return approverPosition; }
        public void setApproverPosition(String approverPosition) { this.approverPosition = approverPosition; }
        public String getApproverName() { return approverName; }
        public void setApproverName(String approverName) { this.approverName = approverName; }
        public String getApprovalDate() { return approvalDate; }
        public void setApprovalDate(String approvalDate) { this.approvalDate = approvalDate; }
        public String getApprovalTime() { return approvalTime; }
        public void setApprovalTime(String approvalTime) { this.approvalTime = approvalTime; }
        public String getReleaseStatus() { return releaseStatus; }
        public void setReleaseStatus(String releaseStatus) { this.releaseStatus = releaseStatus; }
        public String getReleaseStatusDesc() { return releaseStatusDesc; }
        public void setReleaseStatusDesc(String releaseStatusDesc) { this.releaseStatusDesc = releaseStatusDesc; }
        public String getReleaseIndicator() { return releaseIndicator; }
        public void setReleaseIndicator(String releaseIndicator) { this.releaseIndicator = releaseIndicator; }
        public String getReleaseIndicatorDesc() { return releaseIndicatorDesc; }
        public void setReleaseIndicatorDesc(String releaseIndicatorDesc) { this.releaseIndicatorDesc = releaseIndicatorDesc; }
    }

    /** What the browser sends to export: the selection and the rows as shown (sorted / filtered). */
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

    /** The report: the rows SAP returned for the selection. */
    public static class Report {
        private List<Row> rows = new ArrayList<>();
        private int total;
        private String generatedOn;
        /** The OData query sent to SAP (for the user's reference / support). */
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
}
