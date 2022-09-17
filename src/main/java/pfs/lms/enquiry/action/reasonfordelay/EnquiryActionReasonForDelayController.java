package pfs.lms.enquiry.action.reasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class EnquiryActionReasonForDelayController {

    private final EnquiryActionReasonForDelayService reasonForDelayService;

    @PostMapping("/enquiryReasonForDelays/create")
    public ResponseEntity<EnquiryActionReasonForDelay> createReasonForDelay(@RequestBody EnquiryActionReasonForDelayResource resource,
                                                                            HttpServletRequest request) {
        return ResponseEntity.ok(reasonForDelayService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/enquiryReasonForDelays/update")
    public ResponseEntity<EnquiryActionReasonForDelay> updateReasonForDelay(@RequestBody EnquiryActionReasonForDelayResource resource,
                                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(reasonForDelayService.update(resource, request.getUserPrincipal().getName()));
    }
}
