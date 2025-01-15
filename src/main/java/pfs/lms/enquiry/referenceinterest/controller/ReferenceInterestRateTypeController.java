package pfs.lms.enquiry.referenceinterest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateRepository;

import java.util.List;

@ApiController
@RequiredArgsConstructor
@Slf4j
public class ReferenceInterestRateTypeController {

    @Autowired
    private ReferenceInterestRateRepository referenceInterestRateRepository;

    @GetMapping("/refinterestratetypes")
    public ResponseEntity getReferenceIntersestRateTypes() {

        List<ReferenceInterestRate> referenceInterestRateTypes = referenceInterestRateRepository.findAll();
        return ResponseEntity.ok(referenceInterestRateTypes);

    }

}
