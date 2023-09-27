package pfs.lms.enquiry.boardapproval.reasonfordelay;

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
public class BoardApprovalReasonForDelayController {

    private final BoardApprovalReasonForDelayService service;

    @PostMapping("/boardApprovalReasonForDelays/create")
    public ResponseEntity<BoardApprovalReasonForDelay> createReasonForDelay(@RequestBody BoardApprovalReasonForDelayResource resource,
                                                                            HttpServletRequest request) {
        return ResponseEntity.ok(service.createReasonForDelay(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/boardApprovalReasonForDelays/update")
    public ResponseEntity<BoardApprovalReasonForDelay> updateReasonForDelay(@RequestBody BoardApprovalReasonForDelayResource resource,
                                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.updateReasonForDelay(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/boardApprovalReasonForDelays/delete/{id}")
    public ResponseEntity<BoardApprovalReasonForDelay> updateReasonForDelay(@PathVariable("id") UUID id,
                                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.deleteReasonForDelay(id ,request.getUserPrincipal().getName()));
    }
}
