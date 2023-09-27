package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.ReferenceInterestRate;
import pfs.lms.enquiry.repository.ReferenceInterestRateRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class ReferenceInterestRateController {



    @Autowired
    ReferenceInterestRateRepository referenceInterestRateRepository;


    @RequestMapping(value = "/referenceinterestrates/all", method = {RequestMethod.GET})
    public ResponseEntity<List<ReferenceInterestRate>> getRefInterestRates(HttpServletRequest request) {

        List<ReferenceInterestRate> referenceInterestRates = referenceInterestRateRepository.findAll();

        return ResponseEntity.ok(referenceInterestRates);

    }


}
