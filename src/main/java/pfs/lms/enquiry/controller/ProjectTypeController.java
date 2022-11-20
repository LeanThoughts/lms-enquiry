package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.BankMaster;
import pfs.lms.enquiry.domain.ProjectType;
import pfs.lms.enquiry.repository.BankMasterRepository;
import pfs.lms.enquiry.repository.ProjectTypeRepository;
import pfs.lms.enquiry.resource.ProjectTypeResource;
import pfs.lms.enquiry.service.IBankMasterService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class ProjectTypeController {

    @Autowired
    IBankMasterService bankMasterService;

    @Autowired
    ProjectTypeRepository projectTypeRepository;

//    @RequestMapping(value = "/projectTypes", method = {RequestMethod.GET})
//    public ResponseEntity<List<ProjectTypeResource>> getProjectType(HttpServletRequest request) {
//
//        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
//
//        List<ProjectTypeResource> projectTypeResources = new ArrayList<>();
//        for (ProjectType projectType:projectTypeList) {
//
//            ProjectTypeResource projectTypeResource = new ProjectTypeResource();
//            projectTypeResource.setCode(projectType.getCode());
//            projectTypeResource.setId(projectType.getId());
//            projectTypeResource.setValue(projectType.getValue());
//            projectTypeResources.add(projectTypeResource);
//        }
//
//        return ResponseEntity.ok(projectTypeResources);
//
//    }


}
