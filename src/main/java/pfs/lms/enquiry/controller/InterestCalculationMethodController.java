package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.InterestCalculationMethod;
import pfs.lms.enquiry.repository.InterestCalculationMethodRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class InterestCalculationMethodController {



    @Autowired
    InterestCalculationMethodRepository interestCalculationMethodRepository;


    @RequestMapping(value = "/interestcalcmethod/all", method = {RequestMethod.GET})
    public ResponseEntity<List<InterestCalculationMethod>> getIntCalcMethods(HttpServletRequest request) {

        List<InterestCalculationMethod>  interestCalculationMethods = interestCalculationMethodRepository.findAll();

        return ResponseEntity.ok(interestCalculationMethods);

    }


}
