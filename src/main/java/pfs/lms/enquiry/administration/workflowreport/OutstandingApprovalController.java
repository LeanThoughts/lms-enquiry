package pfs.lms.enquiry.administration.workflowreport;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalDtos.ReportPage;
import pfs.lms.enquiry.administration.workflowreport.OutstandingApprovalService.Filter;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * REST API of the Outstanding Workflow Approvals report (Administration), under /api from {@link ApiController}.
 * Read-only: a paged JSON report and a CSV export of the same, filtered, data.
 */
@ApiController
public class OutstandingApprovalController {

    private static final String BASE = "/administration/outstanding-approvals";

    private final OutstandingApprovalService service;

    public OutstandingApprovalController(OutstandingApprovalService service) {
        this.service = service;
    }

    @GetMapping(BASE)
    public ResponseEntity<ReportPage> getReport(@RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String processName,
                                                @RequestParam(required = false) String approver,
                                                @RequestParam(required = false) String requestor,
                                                @RequestParam(required = false) String search,
                                                @RequestParam(required = false) Integer minAgeDays,
                                                HttpServletRequest request) {
        return ResponseEntity.ok(service.getReport(filter(processName, approver, requestor, search, minAgeDays),
                page, size, signedInUser(request)));
    }

    @GetMapping(BASE + "/export")
    public ResponseEntity<Resource> export(@RequestParam(required = false) String processName,
                                           @RequestParam(required = false) String approver,
                                           @RequestParam(required = false) String requestor,
                                           @RequestParam(required = false) String search,
                                           @RequestParam(required = false) Integer minAgeDays,
                                           HttpServletRequest request) {
        String csv = service.toCsv(filter(processName, approver, requestor, search, minAgeDays), signedInUser(request));
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);
        String fileName = "outstanding-approvals-" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(new ByteArrayResource(bytes));
    }

    private static Filter filter(String processName, String approver, String requestor, String search, Integer minAgeDays) {
        Filter filter = new Filter();
        filter.processName = processName;
        filter.approver = approver;
        filter.requestor = requestor;
        filter.search = search;
        filter.minAgeDays = minAgeDays;
        return filter;
    }

    private static String signedInUser(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        if (user == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
