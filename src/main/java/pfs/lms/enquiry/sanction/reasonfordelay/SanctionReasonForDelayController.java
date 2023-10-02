package pfs.lms.enquiry.sanction.reasonfordelay;

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
public class SanctionReasonForDelayController {

    private final SanctionReasonForDelayService service;

    @PostMapping("/sanctionReasonForDelays/create")
    public ResponseEntity<SanctionReasonForDelay> createReasonForDelay(@RequestBody SanctionReasonForDelayResource resource,
                                                                       HttpServletRequest request) {
        return ResponseEntity.ok(service.createReasonForDelay(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/sanctionReasonForDelays/update")
    public ResponseEntity<SanctionReasonForDelay> updateReasonForDelay(@RequestBody SanctionReasonForDelayResource resource,
                                                                       HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.updateReasonForDelay(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/sanctionReasonForDelays/delete/{id}")
    public ResponseEntity<SanctionReasonForDelay> updateReasonForDelay(@PathVariable("id") UUID id,
                                                                       HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.deleteReasonForDelay(id,request.getUserPrincipal().getName()));
    }
}
