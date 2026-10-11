package pfs.lms.enquiry.administration.workflowreport;

import java.util.ArrayList;
import java.util.List;

/** Request and response bodies of the Outstanding Workflow Approvals report (Administration). */
public final class OutstandingApprovalDtos {

    private OutstandingApprovalDtos() {
    }

    /** One outstanding (active, not completed) approval task from ACT_RU_TASK with its process variables. */
    public static class Row {
        private String taskId;
        private String taskName;
        private String processName;
        private String processLabel;
        private String businessProcessId;
        private String loanContractId;
        private String projectName;
        private String requestorName;
        private String requestorEmail;
        private String approverName;
        private String approverEmail;
        private String assignee;
        /** ISO date the task was created (ACT_RU_TASK.CREATE_TIME_) */
        private String createdOn;
        /** Whole days the task has been waiting */
        private long ageDays;
        private int priority;
        private String processInstanceId;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public String getProcessName() { return processName; }
        public void setProcessName(String processName) { this.processName = processName; }
        public String getProcessLabel() { return processLabel; }
        public void setProcessLabel(String processLabel) { this.processLabel = processLabel; }
        public String getBusinessProcessId() { return businessProcessId; }
        public void setBusinessProcessId(String businessProcessId) { this.businessProcessId = businessProcessId; }
        public String getLoanContractId() { return loanContractId; }
        public void setLoanContractId(String loanContractId) { this.loanContractId = loanContractId; }
        public String getProjectName() { return projectName; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
        public String getRequestorName() { return requestorName; }
        public void setRequestorName(String requestorName) { this.requestorName = requestorName; }
        public String getRequestorEmail() { return requestorEmail; }
        public void setRequestorEmail(String requestorEmail) { this.requestorEmail = requestorEmail; }
        public String getApproverName() { return approverName; }
        public void setApproverName(String approverName) { this.approverName = approverName; }
        public String getApproverEmail() { return approverEmail; }
        public void setApproverEmail(String approverEmail) { this.approverEmail = approverEmail; }
        public String getAssignee() { return assignee; }
        public void setAssignee(String assignee) { this.assignee = assignee; }
        public String getCreatedOn() { return createdOn; }
        public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
        public long getAgeDays() { return ageDays; }
        public void setAgeDays(long ageDays) { this.ageDays = ageDays; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public String getProcessInstanceId() { return processInstanceId; }
        public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }
    }

    /** A value with a count, used for the "by process" and "by approver" breakdowns. */
    public static class Count {
        private final String key;
        private final String label;
        private final long count;

        public Count(String key, String label, long count) {
            this.key = key;
            this.label = label;
            this.count = count;
        }

        public String getKey() { return key; }
        public String getLabel() { return label; }
        public long getCount() { return count; }
    }

    /** Totals computed over the whole filtered result (not only the current page). */
    public static class Summary {
        private long total;
        private int distinctApprovers;
        private long oldestAgeDays;
        private double averageAgeDays;
        /** Tasks older than the overdue threshold */
        private long overdue;
        private int overdueThresholdDays;
        private List<Count> byProcess = new ArrayList<>();
        private List<Count> byApprover = new ArrayList<>();

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public int getDistinctApprovers() { return distinctApprovers; }
        public void setDistinctApprovers(int distinctApprovers) { this.distinctApprovers = distinctApprovers; }
        public long getOldestAgeDays() { return oldestAgeDays; }
        public void setOldestAgeDays(long oldestAgeDays) { this.oldestAgeDays = oldestAgeDays; }
        public double getAverageAgeDays() { return averageAgeDays; }
        public void setAverageAgeDays(double averageAgeDays) { this.averageAgeDays = averageAgeDays; }
        public long getOverdue() { return overdue; }
        public void setOverdue(long overdue) { this.overdue = overdue; }
        public int getOverdueThresholdDays() { return overdueThresholdDays; }
        public void setOverdueThresholdDays(int overdueThresholdDays) { this.overdueThresholdDays = overdueThresholdDays; }
        public List<Count> getByProcess() { return byProcess; }
        public void setByProcess(List<Count> byProcess) { this.byProcess = byProcess; }
        public List<Count> getByApprover() { return byApprover; }
        public void setByApprover(List<Count> byApprover) { this.byApprover = byApprover; }
    }

    /** A page of the report plus the summary, the filter values in effect and the available filter options. */
    public static class ReportPage {
        private List<Row> rows = new ArrayList<>();
        private Summary summary = new Summary();
        private int page;
        private int size;
        private long total;
        private List<Count> processes = new ArrayList<>();
        private List<String> approvers = new ArrayList<>();
        private String generatedOn;
        private boolean canView;
        private String userRole;

        public List<Row> getRows() { return rows; }
        public void setRows(List<Row> rows) { this.rows = rows; }
        public Summary getSummary() { return summary; }
        public void setSummary(Summary summary) { this.summary = summary; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public List<Count> getProcesses() { return processes; }
        public void setProcesses(List<Count> processes) { this.processes = processes; }
        public List<String> getApprovers() { return approvers; }
        public void setApprovers(List<String> approvers) { this.approvers = approvers; }
        public String getGeneratedOn() { return generatedOn; }
        public void setGeneratedOn(String generatedOn) { this.generatedOn = generatedOn; }
        public boolean isCanView() { return canView; }
        public void setCanView(boolean canView) { this.canView = canView; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
    }
}
