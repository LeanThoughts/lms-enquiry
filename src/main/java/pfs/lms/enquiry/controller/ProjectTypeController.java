package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.repository.ProjectTypeRepository;
import pfs.lms.enquiry.service.IBankMasterService;

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
