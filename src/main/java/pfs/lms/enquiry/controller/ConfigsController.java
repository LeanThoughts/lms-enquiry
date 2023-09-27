package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.TRAAccountType;
import pfs.lms.enquiry.repository.TRAAccountTypeRepository;
import pfs.lms.enquiry.resource.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApiController
@RequiredArgsConstructor


public class ConfigsController {

    @Autowired
    TRAAccountTypeRepository traAccountTypeRepository;

    /**
     * 01- Created
     * 02-Changed
     * 03-Submitted
     * 04-Approved
     * 05-Cancelled
     * 06-Rejected
     */

    @GetMapping("/technicalStatus")
    public ResponseEntity getTechnicalStatus() {
          List<TechnicalStatusResource> technicalStatusResources = new ArrayList<>();

        //technicalStatusResources.add(new TechnicalStatusResource("",""));
        technicalStatusResources.add(new TechnicalStatusResource("1","Created"));
        technicalStatusResources.add(new TechnicalStatusResource("2","Changed"));
        technicalStatusResources.add(new TechnicalStatusResource("3","Submitted"));
        technicalStatusResources.add(new TechnicalStatusResource("4","Taken up for Processing"));
        technicalStatusResources.add(new TechnicalStatusResource("5","Cancelled"));
        technicalStatusResources.add(new TechnicalStatusResource("6","Rejected"));

        return ResponseEntity.ok(technicalStatusResources);

    }

    @GetMapping("/traaccounttypes")
    public ResponseEntity getTRAAccountTypes() {


        List<TRAAccountType> traAccountTypes = traAccountTypeRepository.findAll();
        return ResponseEntity.ok(traAccountTypes);

    }


}
