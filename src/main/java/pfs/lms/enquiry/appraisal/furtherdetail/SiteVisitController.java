package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.SiteVisit;
import pfs.lms.enquiry.monitoring.repository.SiteVisitRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class SiteVisitController {

    private final SiteVisitRepository siteVisitRepository;
    private final IChangeDocumentService     changeDocumentService;

    @DeleteMapping("/siteVisits/delete/{id}")
    public ResponseEntity<SiteVisit> deleteSiteVisit(@PathVariable("id") UUID siteVisitId) {
        SiteVisit siteVisit = siteVisitRepository.findById(siteVisitId.toString())
                .orElseThrow(() -> new EntityNotFoundException(siteVisitId.toString()));

        //LoanApplication loanApplication = siteVisit.get

        siteVisitRepository.delete(siteVisit);

//
//        // Change Documents for Site Visit
//        changeDocumentService.createChangeDocument(
//                siteVisit.getId(),siteVisit.getId().toString(),null,
//                loanApplication.getLoanContractId(),
//                null,
//                siteVisit,
//                "Created",
//                username,
//                "Appraisal", "Header");

        return ResponseEntity.ok(siteVisit);
    }
}
