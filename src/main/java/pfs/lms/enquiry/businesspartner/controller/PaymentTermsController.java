package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.PaymentTerms;
import pfs.lms.enquiry.businesspartner.repository.CountryCodeRepository;
import pfs.lms.enquiry.businesspartner.repository.PaymentTermsRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class PaymentTermsController {

    private final PaymentTermsRepository paymentTermsRepository;
    @GetMapping("/paymenterms")
    public ResponseEntity<List<PaymentTerms>> findAll(HttpServletRequest request) {

        List<PaymentTerms> paymentTerms = paymentTermsRepository.findAll();
        paymentTerms.sort(Comparator.comparing(o ->o.getId()));

        return ResponseEntity.ok(paymentTerms);
    }

}
