package pfs.lms.enquiry.action.rejectbypfs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class RejectByPfsController {

    private final RejectByPfsService reasonForDelayService;

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PostMapping("/rejectByPfses/create")
    public ResponseEntity<RejectByPfs> createRejectByPfs(@RequestBody RejectByPfsResource resource,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(reasonForDelayService.create(resource, request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PutMapping("/rejectByPfses/update")
    public ResponseEntity<RejectByPfs> updateRejectByPfs(@RequestBody RejectByPfsResource resource,
                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(reasonForDelayService.update(resource, request.getUserPrincipal().getName()));
    }
}
