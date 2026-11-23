package pfs.lms.enquiry.action.enquirycompletion;

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
public class EnquiryCompletionController {

    private final EnquiryCompletionService enquiryCompletionService;

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PostMapping("/enquiryCompletions/create")
    public ResponseEntity<EnquiryCompletion> createOtherDetail(@RequestBody EnquiryCompletionResource resource,
                                                               HttpServletRequest request) {
        return ResponseEntity.ok(enquiryCompletionService.create(resource, request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PutMapping("/enquiryCompletions/update")
    public ResponseEntity<EnquiryCompletion> updateOtherDetail(@RequestBody EnquiryCompletionResource resource,
                                                               HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(enquiryCompletionService.update(resource, request.getUserPrincipal().getName()));
    }
}
