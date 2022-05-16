package pfs.lms.enquiry.appraisal.reasonfordelay;

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
public class ReasonForDelayController {

    private final ReasonForDelayService reasonForDelayService;

    @PostMapping("/reasonForDelays/create")
    public ResponseEntity<ReasonForDelay> createReasonForDelay(@RequestBody ReasonForDelayResource reasonForDelayResource,
                HttpServletRequest request) {
        return ResponseEntity.ok(reasonForDelayService.createReasonForDelay(reasonForDelayResource,request.getUserPrincipal().getName()));
    }

    @PutMapping("/reasonForDelays/update")
    public ResponseEntity<ReasonForDelay> updateReasonForDelay(@RequestBody ReasonForDelayResource reasonForDelayResource,
                HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(reasonForDelayService.updateReasonForDelay(reasonForDelayResource,request.getUserPrincipal().getName()));
    }
}
