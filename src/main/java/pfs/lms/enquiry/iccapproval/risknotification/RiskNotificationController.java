package pfs.lms.enquiry.iccapproval.risknotification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class RiskNotificationController {

    private final IRiskNotificationService iRiskNotificationService;

    @PostMapping("/riskNotifications/create")
    public ResponseEntity<RiskNotification> create(@RequestBody RiskNotificationResource riskNotificationResource,
                                                   HttpServletRequest request) {

        return ResponseEntity.ok(iRiskNotificationService.create(riskNotificationResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/riskNotifications/update")
    public ResponseEntity<RiskNotification> update(@RequestBody RiskNotificationResource riskNotificationResource,
                                                   HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(iRiskNotificationService.update(riskNotificationResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/riskNotifications/delete/{id}")
    public ResponseEntity<RiskNotification> delete(@PathVariable("id") UUID riskNotificationId, HttpServletRequest request) {
        RiskNotification riskNotification = iRiskNotificationService.delete(riskNotificationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(riskNotification);
    }
}
