package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pfs.lms.enquiry.monitoring.domain.SiteVisit;
import pfs.lms.enquiry.monitoring.repository.SiteVisitRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class SiteVisitController {

    private final SiteVisitRepository siteVisitRepository;

    @DeleteMapping("/siteVisits/delete/{id}")
    public ResponseEntity<SiteVisit> deleteSiteVisit(@PathVariable("id") UUID siteVisitId) {
        SiteVisit siteVisit = siteVisitRepository.findById(siteVisitId.toString())
                .orElseThrow(() -> new EntityNotFoundException(siteVisitId.toString()));
        siteVisitRepository.delete(siteVisit);
        return ResponseEntity.ok(siteVisit);
    }
}
