package pfs.lms.enquiry.applicationfee.formalrequest;

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
public class FormalRequestController {

    private final IFormalRequestService formalRequestService;

    @PostMapping("/formalRequests/create")
    public ResponseEntity<FormalRequest> create(@RequestBody FormalRequestResource formalRequestResource,
                                                HttpServletRequest request) {

        return ResponseEntity.ok(formalRequestService.create(formalRequestResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/formalRequests/update")
    public ResponseEntity<FormalRequest> update(@RequestBody FormalRequestResource formalRequestResource,
                                                HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(formalRequestService.update(formalRequestResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/formalRequests/delete/{id}")
    public ResponseEntity<FormalRequest> delete(@PathVariable("id") UUID loanEnhancementId, HttpServletRequest request) {
        FormalRequest formalRequest = formalRequestService.delete(loanEnhancementId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(formalRequest);
    }
}
