package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.ReferenceInterestRate;
import pfs.lms.enquiry.domain.ReferenceInterestSign;
import pfs.lms.enquiry.repository.ReferenceInterestRateRepository;
import pfs.lms.enquiry.repository.ReferenceInterestSignRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class ReferenceInterestSignController {



    @Autowired
    ReferenceInterestSignRepository referenceInterestSignRepository;


    @RequestMapping(value = "/referenceinterestratesign/all", method = {RequestMethod.GET})
    public ResponseEntity<List<ReferenceInterestSign>> getRefInterestRateSigns(HttpServletRequest request) {

        List<ReferenceInterestSign> referenceInterestSigns = referenceInterestSignRepository.findAll();

        return ResponseEntity.ok(referenceInterestSigns);

    }


}
