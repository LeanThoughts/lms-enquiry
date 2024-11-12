package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingAgencyRepository;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingCodeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class CreditRatingAgencyController {

    private final CreditRatingAgencyRepository creditRatingAgencyRepository;
    @GetMapping("/creditRatingAgencies")
    public ResponseEntity<List<CreditRatingAgency>> findAll(HttpServletRequest request) {

        List<CreditRatingAgency> creditRatingAgencies = creditRatingAgencyRepository.findAll();
        creditRatingAgencies.sort(Comparator.comparing(o ->o.getValue()));

        return ResponseEntity.ok(creditRatingAgencies);
    }

}
