package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingCodeRepository;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class CreditRatingCodeController {

    private final CreditRatingCodeRepository creditRatingCodeRepository;
    @GetMapping("/creditRatingCodes")
    public ResponseEntity<List<CreditRatingCode>> findAll(HttpServletRequest request) {

        List<CreditRatingCode> creditRatingCodes = creditRatingCodeRepository.findAll();
        creditRatingCodes.sort(Comparator.comparing(o ->o.getValue()));

        return ResponseEntity.ok(creditRatingCodes);
    }

}
