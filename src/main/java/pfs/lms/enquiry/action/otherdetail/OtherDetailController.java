package pfs.lms.enquiry.action.otherdetail;

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
public class OtherDetailController {

    private final OtherDetailService otherDetailService;

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PostMapping("/otherDetails/create")
    public ResponseEntity<OtherDetail> createOtherDetail(@RequestBody OtherDetailResource resource,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok(otherDetailService.create(resource, request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PutMapping("/otherDetails/update")
    public ResponseEntity<OtherDetail> updateOtherDetail(@RequestBody OtherDetailResource resource,
                                                             HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(otherDetailService.update(resource, request.getUserPrincipal().getName()));
    }
}
