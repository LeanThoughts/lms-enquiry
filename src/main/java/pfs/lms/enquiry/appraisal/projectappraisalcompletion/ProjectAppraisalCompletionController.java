package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

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
public class ProjectAppraisalCompletionController {

    private final ProjectAppraisalCompletionService projectAppraisalCompletionService;

    @PostMapping("/projectAppraisalCompletions/create")
    public ResponseEntity<ProjectAppraisalCompletion> createProjectAppraisalCompletion(
                @RequestBody ProjectAppraisalCompletionResource projectAppraisalCompletionResource,
                HttpServletRequest request) {
        return ResponseEntity.ok(projectAppraisalCompletionService.
                createProjectAppraisalCompletion(projectAppraisalCompletionResource,request.getUserPrincipal().getName()));
    }

    @PutMapping("/projectAppraisalCompletions/update")
    public ResponseEntity<ProjectAppraisalCompletion> updateProjectAppraisalCompletion(
                @RequestBody ProjectAppraisalCompletionResource projectAppraisalCompletionResource,
                HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(projectAppraisalCompletionService.
                updateProjectAppraisalCompletion(projectAppraisalCompletionResource,request.getUserPrincipal().getName()));
    }
}
