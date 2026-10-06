package pfs.lms.enquiry.collateral.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Outcome of migrating the checklist of one loan. */
public class MigrationResultDto {

    /** OK: everything saved; PARTIAL: saved with skipped collaterals or values; FAILED: nothing saved. */
    public enum Status { OK, PARTIAL, FAILED }

    private String loanContractId;
    private Status status = Status.OK;
    private boolean dryRun;
    private UUID checklistId;
    private int itemsCreated;
    private int itemsUpdated;
    private int itemsSkipped;
    private int childRowsCreated;
    private int childRowsUpdated;
    private int childRowsDeleted;
    private List<Message> messages = new ArrayList<>();

    /** A problem found while migrating; ERROR skips the collateral, WARNING only the value. */
    public static class Message {
        private String severity;
        private String checklistIdNo;
        private String text;

        public Message() {
        }

        public Message(String severity, String checklistIdNo, String text) {
            this.severity = severity;
            this.checklistIdNo = checklistIdNo;
            this.text = text;
        }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }

        public String getChecklistIdNo() { return checklistIdNo; }
        public void setChecklistIdNo(String checklistIdNo) { this.checklistIdNo = checklistIdNo; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    void warning(String checklistIdNo, String text) {
        messages.add(new Message("WARNING", checklistIdNo, text));
        if (status == Status.OK) {
            status = Status.PARTIAL;
        }
    }

    void error(String checklistIdNo, String text) {
        messages.add(new Message("ERROR", checklistIdNo, text));
        if (status == Status.OK) {
            status = Status.PARTIAL;
        }
    }

    void failed(String text) {
        messages.add(new Message("ERROR", null, text));
        status = Status.FAILED;
    }

    public String getLoanContractId() { return loanContractId; }
    public void setLoanContractId(String loanContractId) { this.loanContractId = loanContractId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isDryRun() { return dryRun; }
    public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }

    public UUID getChecklistId() { return checklistId; }
    public void setChecklistId(UUID checklistId) { this.checklistId = checklistId; }

    public int getItemsCreated() { return itemsCreated; }
    public void setItemsCreated(int itemsCreated) { this.itemsCreated = itemsCreated; }

    public int getItemsUpdated() { return itemsUpdated; }
    public void setItemsUpdated(int itemsUpdated) { this.itemsUpdated = itemsUpdated; }

    public int getItemsSkipped() { return itemsSkipped; }
    public void setItemsSkipped(int itemsSkipped) { this.itemsSkipped = itemsSkipped; }

    public int getChildRowsCreated() { return childRowsCreated; }
    public void setChildRowsCreated(int childRowsCreated) { this.childRowsCreated = childRowsCreated; }

    public int getChildRowsUpdated() { return childRowsUpdated; }
    public void setChildRowsUpdated(int childRowsUpdated) { this.childRowsUpdated = childRowsUpdated; }

    public int getChildRowsDeleted() { return childRowsDeleted; }
    public void setChildRowsDeleted(int childRowsDeleted) { this.childRowsDeleted = childRowsDeleted; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}
