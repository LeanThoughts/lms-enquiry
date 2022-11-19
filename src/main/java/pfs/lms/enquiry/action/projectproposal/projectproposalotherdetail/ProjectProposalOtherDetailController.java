package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

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
public class ProjectProposalOtherDetailController {

    private final IProjectProposalOtherDetailService projectProposalOtherDetailService;

    @PostMapping("/projectProposalOtherDetails/create")
    public ResponseEntity<ProjectProposalOtherDetail> create(@RequestBody ProjectProposalOtherDetailResource resource,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok(projectProposalOtherDetailService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/projectProposalOtherDetails/update")
    public ResponseEntity<ProjectProposalOtherDetail> update(@RequestBody ProjectProposalOtherDetailResource resource,
                                                             HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(projectProposalOtherDetailService.update(resource, request.getUserPrincipal().getName()));
    }
}
