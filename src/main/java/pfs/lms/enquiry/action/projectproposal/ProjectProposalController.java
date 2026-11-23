package pfs.lms.enquiry.action.projectproposal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ProjectProposalController {

    private final ProjectProposalService projectProposalService;

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PostMapping("/projectProposals/create")
    public ResponseEntity<ProjectProposal> createProjectProposal(@RequestBody ProjectProposalResource resource,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(projectProposalService.create(resource,request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PutMapping("/projectProposals/update")
    public ResponseEntity<ProjectProposal> updateProjectProposal(@RequestBody ProjectProposalResource resource,
                                                                HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(projectProposalService.update(resource,request.getUserPrincipal().getName()));
    }
}
