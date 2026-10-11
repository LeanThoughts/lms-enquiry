package pfs.lms.enquiry.administration.workflowreport;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.Count;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.ReportPage;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.Row;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.Summary;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Outstanding Workflow Approvals report (Administration). Reads the active (not completed, not suspended) approval
 * tasks from Activiti's runtime tables — ACT_RU_TASK joined with ACT_RU_VARIABLE (process variables) and
 * ACT_RU_EXECUTION (process instance) — through the engine's {@link org.activiti.engine.TaskService} query, and
 * presents them with filters, totals and CSV export. Read-only.
 * <p>
 * Every signed-in user may open the report. It can be restricted to specific roles with the property
 * {@code administration.approval-report.roles} (comma separated); when that is empty, all signed-in users may view it.
 */
@Service
public class OutstandingApprovalService {

    public static final int DEFAULT_PAGE_SIZE = 25;
    public static final int MAX_PAGE_SIZE = 500;

    private static final Logger log = LoggerFactory.getLogger(OutstandingApprovalService.class);
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final int TOP_APPROVERS = 10;

    /** Readable names of the known workflow processes (kept in step with the front-end list). */
    private static final Map<String, String> PROCESS_LABELS = new LinkedHashMap<>();
    static {
        PROCESS_LABELS.put("EnquiryAction", "Process Enquiry");
        PROCESS_LABELS.put("ICCApproval", "ICC In-Principle Approval");
        PROCESS_LABELS.put("Prelim Risk Assessment", "Prelim Risk Assessment");
        PROCESS_LABELS.put("Prelim Risk Notification", "Prelim Risk Notification");
        PROCESS_LABELS.put("ApplicationFee", "Application Fee");
        PROCESS_LABELS.put("Appraisal", "Appraisal");
        PROCESS_LABELS.put("BoardApproval", "Board Approval");
        PROCESS_LABELS.put("Board Approval", "Board Approval");
        PROCESS_LABELS.put("Sanction", "Sanction");
        PROCESS_LABELS.put("Monitoring", "Monitoring");
        PROCESS_LABELS.put("BusinessPartner", "Business Partner");
        PROCESS_LABELS.put("Partner", "Business Partner");
        PROCESS_LABELS.put("ReferenceInterestRateValue", "Reference Interest Rate");
        PROCESS_LABELS.put("CollateralManagement", "Collateral Management");
    }

    @Autowired(required = false)
    private ProcessEngine processEngine;

    private final UserRepository userRepository;
    private final List<String> viewRoles;
    private final int overdueThresholdDays;

    public OutstandingApprovalService(UserRepository userRepository,
                                      @Value("${administration.approval-report.roles:}") String viewRoles,
                                      @Value("${administration.approval-report.overdue-days:7}") int overdueThresholdDays) {
        this.userRepository = userRepository;
        this.viewRoles = Arrays.stream(viewRoles.split(","))
                .map(String::trim).filter(role -> !role.isEmpty()).collect(Collectors.toList());
        this.overdueThresholdDays = overdueThresholdDays < 1 ? 7 : overdueThresholdDays;
    }

    /** Filter values; any may be null/blank. */
    public static class Filter {
        public String processName;
        public String approver;
        public String requestor;
        public String search;
        public Integer minAgeDays;
    }

    // ------------------------------------------------------------------------------------------------ reading

    public ReportPage getReport(Filter filter, Integer page, Integer size, String principalName) {
        ReportPage result = new ReportPage();
        result.setGeneratedOn(Instant.now().toString());
        User user = principalName == null ? null : userRepository.findByEmail(principalName);
        result.setUserRole(user != null ? user.getRole() : null);
        result.setProcesses(new ArrayList<>());
        boolean canView = canView(user);
        result.setCanView(canView);
        if (!canView) {
            return result;
        }

        List<Row> all = rows();
        result.setProcesses(processOptions(all));
        result.setApprovers(all.stream().map(Row::getApproverName).filter(OutstandingApprovalService::notBlank)
                .distinct().sorted().collect(Collectors.toList()));

        List<Row> filtered = all.stream().filter(row -> matches(row, filter)).collect(Collectors.toList());
        result.setSummary(summarize(filtered));
        result.setTotal(filtered.size());

        int pageSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        int lastPage = filtered.isEmpty() ? 0 : (filtered.size() - 1) / pageSize;
        int pageIndex = page == null || page < 0 ? 0 : Math.min(page, lastPage);
        result.setPage(pageIndex);
        result.setSize(pageSize);
        result.setRows(filtered.stream().skip((long) pageIndex * pageSize).limit(pageSize).collect(Collectors.toList()));
        return result;
    }

    /** All matching rows as CSV (no paging). Throws if the user may not view the report. */
    public String toCsv(Filter filter, String principalName) {
        User user = principalName == null ? null : userRepository.findByEmail(principalName);
        if (!canView(user)) {
            return "";
        }
        List<Row> rows = rows().stream().filter(row -> matches(row, filter)).collect(Collectors.toList());
        String[] headers = {"Task Id", "Process", "Business Process Id", "Loan Contract", "Project",
                "Requestor", "Requestor Email", "Approver", "Approver Email", "Assignee", "Created On",
                "Age (days)", "Priority", "Process Instance Id"};
        StringBuilder csv = new StringBuilder(String.join(",", headers)).append("\r\n");
        for (Row r : rows) {
            csv.append(String.join(",",
                    csv(r.getTaskId()), csv(r.getProcessLabel()), csv(r.getBusinessProcessId()),
                    csv(r.getLoanContractId()), csv(r.getProjectName()), csv(r.getRequestorName()),
                    csv(r.getRequestorEmail()), csv(r.getApproverName()), csv(r.getApproverEmail()),
                    csv(r.getAssignee()), csv(r.getCreatedOn()), csv(String.valueOf(r.getAgeDays())),
                    csv(String.valueOf(r.getPriority())), csv(r.getProcessInstanceId()))).append("\r\n");
        }
        return csv.toString();
    }

    // ------------------------------------------------------------------------------------------- Activiti access

    /**
     * Active approval tasks as rows. This is the only method that touches Activiti (ACT_RU_TASK and related tables);
     * overridden in tests to supply records without a running engine.
     */
    protected List<Row> rows() {
        if (processEngine == null) {
            log.warn("No Activiti ProcessEngine available; the outstanding approvals report is empty.");
            return new ArrayList<>();
        }
        long now = System.currentTimeMillis();
        List<Row> rows = new ArrayList<>();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery()
                .active()
                .includeProcessVariables()
                .orderByTaskCreateTime().desc()
                .list();
        for (Task task : tasks) {
            if (task.isSuspended()) {
                continue;
            }
            rows.add(toRow(task.getId(), task.getName(), task.getAssignee(), task.getCreateTime(),
                    task.getPriority(), task.getProcessInstanceId(), task.getProcessVariables(), now));
        }
        return rows;
    }

    /** Builds a report row from a task and its process variables (the variable names the workflow sets). */
    protected Row toRow(String taskId, String taskName, String assignee, Date createTime, int priority,
                        String processInstanceId, Map<String, Object> variables, long now) {
        Map<String, Object> vars = variables == null ? new LinkedHashMap<>() : variables;
        Row row = new Row();
        row.setTaskId(taskId);
        row.setTaskName(taskName);
        row.setAssignee(assignee);
        row.setPriority(priority);
        row.setProcessInstanceId(processInstanceId);
        String processName = str(vars.get("processName"));
        row.setProcessName(processName);
        row.setProcessLabel(label(processName));
        row.setBusinessProcessId(str(vars.get("LoanProcessId")));
        row.setLoanContractId(str(vars.get("loanContractId")));
        row.setProjectName(str(vars.get("projectName")));
        row.setRequestorName(str(vars.get("requestorName")));
        row.setRequestorEmail(str(vars.get("requestorEmail")));
        row.setApproverName(str(vars.get("approverName")));
        row.setApproverEmail(str(vars.get("approverEmail")));
        if (createTime != null) {
            row.setCreatedOn(createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(ISO_DATE));
            long days = (now - createTime.getTime()) / 86_400_000L;
            row.setAgeDays(Math.max(0, days));
        }
        return row;
    }

    // ------------------------------------------------------------------------------------------- filter & summarize

    private boolean matches(Row row, Filter filter) {
        if (filter == null) {
            return true;
        }
        if (notBlank(filter.processName) && !filter.processName.equals(row.getProcessName())) {
            return false;
        }
        if (notBlank(filter.approver) && !containsIgnoreCase(row.getApproverName(), filter.approver)
                && !containsIgnoreCase(row.getApproverEmail(), filter.approver)
                && !containsIgnoreCase(row.getAssignee(), filter.approver)) {
            return false;
        }
        if (notBlank(filter.requestor) && !containsIgnoreCase(row.getRequestorName(), filter.requestor)
                && !containsIgnoreCase(row.getRequestorEmail(), filter.requestor)) {
            return false;
        }
        if (filter.minAgeDays != null && row.getAgeDays() < filter.minAgeDays) {
            return false;
        }
        if (notBlank(filter.search)) {
            String s = filter.search;
            boolean hit = containsIgnoreCase(row.getProcessLabel(), s) || containsIgnoreCase(row.getBusinessProcessId(), s)
                    || containsIgnoreCase(row.getLoanContractId(), s) || containsIgnoreCase(row.getProjectName(), s)
                    || containsIgnoreCase(row.getRequestorName(), s) || containsIgnoreCase(row.getRequestorEmail(), s)
                    || containsIgnoreCase(row.getApproverName(), s) || containsIgnoreCase(row.getApproverEmail(), s)
                    || containsIgnoreCase(row.getAssignee(), s) || containsIgnoreCase(row.getTaskName(), s);
            if (!hit) {
                return false;
            }
        }
        return true;
    }

    private Summary summarize(List<Row> rows) {
        Summary summary = new Summary();
        summary.setTotal(rows.size());
        summary.setOverdueThresholdDays(overdueThresholdDays);
        if (rows.isEmpty()) {
            return summary;
        }
        summary.setOldestAgeDays(rows.stream().mapToLong(Row::getAgeDays).max().orElse(0));
        summary.setAverageAgeDays(Math.round(rows.stream().mapToLong(Row::getAgeDays).average().orElse(0) * 10.0) / 10.0);
        summary.setOverdue(rows.stream().filter(r -> r.getAgeDays() >= overdueThresholdDays).count());
        summary.setDistinctApprovers((int) rows.stream().map(OutstandingApprovalService::approverKey)
                .filter(OutstandingApprovalService::notBlank).distinct().count());

        Map<String, Long> byProcess = rows.stream().collect(Collectors.groupingBy(
                r -> notBlank(r.getProcessName()) ? r.getProcessName() : "(unknown)", TreeMap::new, Collectors.counting()));
        summary.setByProcess(byProcess.entrySet().stream()
                .map(e -> new Count(e.getKey(), label(e.getKey()), e.getValue()))
                .sorted(Comparator.comparingLong(Count::getCount).reversed())
                .collect(Collectors.toList()));

        Map<String, Long> byApprover = rows.stream().collect(Collectors.groupingBy(
                r -> notBlank(approverKey(r)) ? approverKey(r) : "(unassigned)", Collectors.counting()));
        summary.setByApprover(byApprover.entrySet().stream()
                .map(e -> new Count(e.getKey(), e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(Count::getCount).reversed().thenComparing(Count::getLabel))
                .limit(TOP_APPROVERS)
                .collect(Collectors.toList()));
        return summary;
    }

    private List<Count> processOptions(List<Row> rows) {
        Map<String, Long> counts = rows.stream().filter(r -> notBlank(r.getProcessName()))
                .collect(Collectors.groupingBy(Row::getProcessName, TreeMap::new, Collectors.counting()));
        return counts.entrySet().stream().map(e -> new Count(e.getKey(), label(e.getKey()), e.getValue()))
                .sorted(Comparator.comparing(Count::getLabel)).collect(Collectors.toList());
    }

    private boolean canView(User user) {
        if (viewRoles.isEmpty()) {
            return true;
        }
        return user != null && user.isStatus() && user.getRole() != null && viewRoles.contains(user.getRole().trim());
    }

    // ------------------------------------------------------------------------------------------- helpers

    private static String approverKey(Row row) {
        return notBlank(row.getApproverName()) ? row.getApproverName()
                : notBlank(row.getApproverEmail()) ? row.getApproverEmail() : row.getAssignee();
    }

    private static String label(String processName) {
        if (!notBlank(processName)) {
            return "(unknown)";
        }
        return PROCESS_LABELS.getOrDefault(processName, processName);
    }

    private static String str(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static boolean containsIgnoreCase(String value, String part) {
        return value != null && value.toLowerCase().contains(part.toLowerCase().trim());
    }

    private static String csv(String value) {
        String v = value == null ? "" : value;
        if (v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }
}
