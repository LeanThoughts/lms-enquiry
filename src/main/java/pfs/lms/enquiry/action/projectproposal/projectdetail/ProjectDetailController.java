package pfs.lms.enquiry.action.projectproposal.projectdetail;

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
public class ProjectDetailController {

    private final IProjectDetailService projectDetailService;

    @PostMapping("/projectDetails/create")
    public ResponseEntity<ProjectDetail> create(@RequestBody ProjectDetailResource resource,
                                                HttpServletRequest request) {
        return ResponseEntity.ok(projectDetailService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/projectDetails/update")
    public ResponseEntity<ProjectDetail> update(@RequestBody ProjectDetailResource resource,
                                                HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(projectDetailService.update(resource, request.getUserPrincipal().getName()));
    }
}
