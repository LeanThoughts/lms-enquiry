package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.ConditionType;
import pfs.lms.enquiry.repository.ConditionTypeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class ConditionTypeController {



    @Autowired
    ConditionTypeRepository conditionTypeRepository;


    @RequestMapping(value = "/conditiontype/all", method = {RequestMethod.GET})
    public ResponseEntity<List<ConditionType>> getConditionTypes(HttpServletRequest request) {

        List<ConditionType> conditionTypes = conditionTypeRepository.findAll();

        return ResponseEntity.ok(conditionTypes);

    }


}
