package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.repository.CountryCodeRepository;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class CountryCodeController {

    private final CountryCodeRepository countryCodeRepository;
    @GetMapping("/countryCodes")
    public ResponseEntity<List<CountryCode>> findAll(HttpServletRequest request) {

        List<CountryCode> countryCodes = countryCodeRepository.findAll();
        countryCodes.sort(Comparator.comparing(o ->o.getValue()));

        return ResponseEntity.ok(countryCodes);
    }

}
