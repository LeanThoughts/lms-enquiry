package pfs.lms.enquiry.documentation.reasonfordelay;

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
public class DocumentationReasonForDelayController {

    private final DocumentationReasonForDelayService service;

    @PostMapping("/documentationReasonForDelays/create")
    public ResponseEntity<DocumentationReasonForDelay> createReasonForDelay(@RequestBody DocumentationReasonForDelayResource resource,
                                                                            HttpServletRequest request) {
        return ResponseEntity.ok(service.createReasonForDelay(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/documentationReasonForDelays/update")
    public ResponseEntity<DocumentationReasonForDelay> updateReasonForDelay(@RequestBody DocumentationReasonForDelayResource resource,
                                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.updateReasonForDelay(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/documentationReasonForDelays/delete/{id}")
    public ResponseEntity<DocumentationReasonForDelay> updateReasonForDelay(@PathVariable("id") UUID id,
                                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.deleteReasonForDelay(id,request.getUserPrincipal().getName()));
    }
}
