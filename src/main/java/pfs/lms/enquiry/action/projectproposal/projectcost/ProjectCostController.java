package pfs.lms.enquiry.action.projectproposal.projectcost;

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
public class ProjectCostController {

    private final IProjectCostService projectCostService;

    @PostMapping("/projectCosts/create")
    public ResponseEntity<ProjectCost> create(@RequestBody ProjectCostResource resource,
                                              HttpServletRequest request) {
        return ResponseEntity.ok(projectCostService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/projectCosts/update")
    public ResponseEntity<ProjectCost> update(@RequestBody ProjectCostResource resource,
                                              HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(projectCostService.update(resource, request.getUserPrincipal().getName()));
    }
}
