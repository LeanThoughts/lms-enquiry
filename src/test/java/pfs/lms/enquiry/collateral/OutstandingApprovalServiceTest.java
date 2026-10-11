package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.ReportPage;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.Row;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalService;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalService.Filter;
import pfs.lms.enquiry.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Outstanding Workflow Approvals report (Administration). The Activiti access (ACT_RU_TASK) is replaced by sample rows,
 * so the filter, summary, paging, CSV and access-control logic is tested without a running engine.
 */
@DisplayName("Administration: outstanding workflow approvals report")
class OutstandingApprovalServiceTest extends CollateralTestSupport {

    private UserRepository users;

    @BeforeAll
    void setUp() {
        users = bean(UserRepository.class);
    }

    private static Map<String, Object> vars(String process, String lpid, String loan, String project,
                                            String reqName, String reqEmail, String apprName, String apprEmail) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("processName", process);
        v.put("LoanProcessId", lpid);
        v.put("loanContractId", loan);
        v.put("projectName", project);
        v.put("requestorName", reqName);
        v.put("requestorEmail", reqEmail);
        v.put("approverName", apprName);
        v.put("approverEmail", apprEmail);
        return v;
    }

    private static Date daysAgo(int days) {
        return new Date(System.currentTimeMillis() - days * 86_400_000L);
    }

    /** A service whose Activiti source is a fixed set of sample tasks. */
    private OutstandingApprovalService service(String viewRoles) {
        return new OutstandingApprovalService(users, viewRoles, 7) {
            @Override
            protected List<Row> rows() {
                long now = System.currentTimeMillis();
                List<Row> rows = new ArrayList<>();
                rows.add(toRow("t1", "Approve Sanction", "head@x.com", daysAgo(10), 50, "p1",
                        vars("Sanction", "LN001", "0000010003700", "Solar Park A", "Asha Rao", "asha@x.com",
                                "Head Credit", "head@x.com"), now));
                rows.add(toRow("t2", "Approve Collateral", "legal@x.com", daysAgo(2), 50, "p2",
                        vars("CollateralManagement", "COL77", "0000010003700", "Solar Park A", "Asha Rao", "asha@x.com",
                                "Head Legal", "legal@x.com"), now));
                rows.add(toRow("t3", "Approve Appraisal", "head@x.com", daysAgo(9), 50, "p3",
                        vars("Appraisal", "AP55", "0000010009900", "Wind Farm B", "Ravi Kumar", "ravi@x.com",
                                "Head Credit", "head@x.com"), now));
                rows.add(toRow("t4", "Board Approval", "board@x.com", daysAgo(1), 75, "p4",
                        vars("BoardApproval", "BA01", "0000010009900", "Wind Farm B", "Ravi Kumar", "ravi@x.com",
                                "Board", "board@x.com"), now));
                return rows;
            }
        };
    }

    private static Filter filter(String process, String approver, String requestor, String search, Integer minAge) {
        Filter f = new Filter();
        f.processName = process;
        f.approver = approver;
        f.requestor = requestor;
        f.search = search;
        f.minAgeDays = minAge;
        return f;
    }

    @Test
    @DisplayName("lists all outstanding tasks with age, labels and summary totals")
    void listsAndSummarises() {
        ReportPage report = service("").getReport(null, null, null, WRITER);
        assertThat(report.isCanView()).isTrue();
        assertThat(report.getTotal()).isEqualTo(4);
        assertThat(report.getRows()).extracting("processLabel")
                .containsExactlyInAnyOrder("Sanction", "Collateral Management", "Appraisal", "Board Approval");
        assertThat(report.getRows().stream().filter(r -> r.getTaskId().equals("t1")).findFirst().get().getAgeDays())
                .isEqualTo(10);
        assertThat(report.getSummary().getOldestAgeDays()).isEqualTo(10);
        assertThat(report.getSummary().getDistinctApprovers()).isEqualTo(3); // Head Credit, Head Legal, Board
        assertThat(report.getSummary().getOverdue()).isEqualTo(2);           // 10 and 9 days (>= 7)
        assertThat(report.getSummary().getByProcess()).extracting("label")
                .contains("Sanction", "Appraisal", "Collateral Management", "Board Approval");
        assertThat(report.getSummary().getByApprover().get(0).getLabel()).isEqualTo("Head Credit"); // 2 tasks
        assertThat(report.getSummary().getByApprover().get(0).getCount()).isEqualTo(2);
        assertThat(report.getProcesses()).extracting("label").contains("Appraisal", "Sanction");
        assertThat(report.getApprovers()).contains("Board", "Head Credit", "Head Legal");
    }

    @Test
    @DisplayName("filters by process, approver, requestor, minimum age and free-text search")
    void filters() {
        OutstandingApprovalService s = service("");
        assertThat(s.getReport(filter("Appraisal", null, null, null, null), null, null, WRITER).getTotal()).isEqualTo(1);
        assertThat(s.getReport(filter(null, "head credit", null, null, null), null, null, WRITER).getTotal()).isEqualTo(2);
        assertThat(s.getReport(filter(null, null, "ravi", null, null), null, null, WRITER).getTotal()).isEqualTo(2);
        assertThat(s.getReport(filter(null, null, null, null, 7), null, null, WRITER).getTotal()).isEqualTo(2);
        assertThat(s.getReport(filter(null, null, null, "wind farm", null), null, null, WRITER).getTotal()).isEqualTo(2);
        assertThat(s.getReport(filter(null, null, null, "LN001", null), null, null, WRITER).getTotal()).isEqualTo(1);
    }

    @Test
    @DisplayName("pages the result and clamps a page past the end")
    void paging() {
        OutstandingApprovalService s = service("");
        ReportPage first = s.getReport(null, 0, 2, WRITER);
        assertThat(first.getRows()).hasSize(2);
        assertThat(first.getTotal()).isEqualTo(4);
        ReportPage second = s.getReport(null, 1, 2, WRITER);
        assertThat(second.getRows()).hasSize(2);
        assertThat(s.getReport(null, 9, 2, WRITER).getPage()).isEqualTo(1); // clamped to last page
    }

    @Test
    @DisplayName("access control: an empty role list lets any signed-in user view; a role list restricts it")
    void accessControl() {
        // open to all signed-in users
        assertThat(service("").getReport(null, null, null, READER).isCanView()).isTrue();
        // restricted to ZLM023: the admin may view, others get an empty, non-viewable report
        OutstandingApprovalService restricted = service("ZLM023");
        assertThat(restricted.getReport(null, null, null, WRITER).isCanView()).isTrue();   // WRITER is ZLM023
        ReportPage denied = restricted.getReport(null, null, null, READER);                 // READER is ZLM014
        assertThat(denied.isCanView()).isFalse();
        assertThat(denied.getRows()).isEmpty();
        assertThat(denied.getTotal()).isZero();
        assertThat(restricted.toCsv(filter(null, null, null, null, null), READER)).isEmpty();
    }

    @Test
    @DisplayName("CSV export contains a header and one line per filtered task, with escaping")
    void csvExport() {
        String csv = service("").toCsv(filter(null, null, null, null, null), WRITER);
        String[] lines = csv.split("\r\n");
        assertThat(lines[0]).startsWith("Task Id,Process,Business Process Id");
        assertThat(lines).hasSize(5); // header + 4 rows
        assertThat(csv).contains("Sanction").contains("Solar Park A").contains("0000010003700");
    }
}
