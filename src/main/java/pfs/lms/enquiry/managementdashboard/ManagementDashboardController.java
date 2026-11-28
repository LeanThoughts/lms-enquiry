package pfs.lms.enquiry.managementdashboard;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Dashboard;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Management Dashboard, under /api from {@link ApiController}:
 * {@code GET /management-dashboard?runDate=yyyy-MM-dd&amountInCrore=true}. Both parameters are optional: the run
 * date defaults to today and the amounts to Rs crore. Answers 404 with SAP's message when there is no snapshot for
 * the run date, 502 when SAP cannot be reached or reports another error.
 */
@ApiController
public class ManagementDashboardController {

    private final ManagementDashboardService service;

    public ManagementDashboardController(ManagementDashboardService service) {
        this.service = service;
    }

    @GetMapping("/management-dashboard")
    public ResponseEntity<Dashboard> get(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate runDate,
            @RequestParam(required = false) Boolean amountInCrore,
            HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(service.get(runDate, amountInCrore));
    }
}
