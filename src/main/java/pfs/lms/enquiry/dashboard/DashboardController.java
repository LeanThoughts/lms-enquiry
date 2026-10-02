package pfs.lms.enquiry.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.config.ApiController;

import javax.servlet.http.HttpServletRequest;

@ApiController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResource> getDashboard(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.getDashboard(request));
    }
}
