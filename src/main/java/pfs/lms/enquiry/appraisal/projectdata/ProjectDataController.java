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
    public ResponseEntity<ProjectData> getProjectData(@PathVariable UUID loanAppraisalId, HttpServletRequest request) {
        ProjectData projectData = projectDataService.getProjectData(loanAppraisalId);
        if (projectData != null)
            return ResponseEntity.ok(projectData);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/projectDatas/create")
    public ResponseEntity<ProjectData> createProjectData(@RequestBody ProjectDataResource projectDataResource,
                                                            HttpServletRequest request) {
        ProjectData projectData = projectDataService.createProjectData(projectDataResource,request.getUserPrincipal().getName());
        return ResponseEntity.ok(projectData);
    }

    @PutMapping("/projectDatas/update")
    public ResponseEntity<ProjectData> updateProjectData(@RequestBody ProjectDataResource projectDataResource,
                                                            HttpServletRequest request) throws CloneNotSupportedException {
        ProjectData projectData = projectDataService.updateProjectData(projectDataResource, request.getUserPrincipal().getName(), request);
        return ResponseEntity.ok(projectData);
    }
}
