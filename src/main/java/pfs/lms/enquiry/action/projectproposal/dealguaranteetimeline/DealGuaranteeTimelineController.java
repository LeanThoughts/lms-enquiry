package pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline;

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
public class DealGuaranteeTimelineController {

    private final IDealGuaranteeTimelineService dealGuaranteeTimelineService;

    @PostMapping("/dealGuaranteeTimelines/create")
    public ResponseEntity<DealGuaranteeTimeline> createDealGuaranteeTimeline(@RequestBody DealGuaranteeTimelineResource resource,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(dealGuaranteeTimelineService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/dealGuaranteeTimelines/update")
    public ResponseEntity<DealGuaranteeTimeline> updateDealGuaranteeTimeline(@RequestBody DealGuaranteeTimelineResource resource,
                                                                HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(dealGuaranteeTimelineService.update(resource, request.getUserPrincipal().getName()));
    }
}
