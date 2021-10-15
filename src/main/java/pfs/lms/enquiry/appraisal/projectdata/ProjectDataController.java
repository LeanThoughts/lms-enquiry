package pfs.lms.enquiry.appraisal.projectdata;

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
public class ProjectDataController {

    private final IProjectDataService projectDataService;

    @GetMapping("/projectDatas/{loanAppraisalId}")
    public ResponseEntity<ProjectDataResource> getProjectData(@PathVariable UUID loanAppraisalId, HttpServletRequest request) {
        ProjectDataResource projectDataResource = projectDataService.getProjectData(loanAppraisalId);
        if (projectDataResource != null)
            return ResponseEntity.ok(projectDataResource);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/projectDatas/create")
    public ResponseEntity<ProjectDataResource> createProjectData(@RequestBody ProjectDataResource projectDataResource,
                                                            HttpServletRequest request) {
        projectDataResource = projectDataService.createProjectData(projectDataResource);
        return ResponseEntity.ok(projectDataResource);
    }

    @PutMapping("/projectDatas/update")
    public ResponseEntity<ProjectDataResource> updateProjectData(@RequestBody ProjectDataResource projectDataResource,
                                                            HttpServletRequest request) {
        projectDataResource = projectDataService.updateProjectData(projectDataResource);
        return ResponseEntity.ok(projectDataResource);
    }
}
