package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.PlanningGroup;
import pfs.lms.enquiry.businesspartner.repository.PlanningGroupRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class PlanningGroupController {

    private final PlanningGroupRepository planningGroupRepository;
    @GetMapping("/planninggroups")
    public ResponseEntity<List<PlanningGroup>> findAll(HttpServletRequest request) {

        List<PlanningGroup> planningGroup = planningGroupRepository.findAll();
        planningGroup.sort(Comparator.comparing(o ->o.getId()));

        return ResponseEntity.ok(planningGroup);
    }

}
