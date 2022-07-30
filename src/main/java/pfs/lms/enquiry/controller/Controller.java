package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.mail.service.RiskNotificationService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.EmailId;
import pfs.lms.enquiry.resource.ProcessorResource;

@ApiController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final LoanApplicationRepository loanApplicationRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    RiskNotificationService riskNotificationService;

    @PutMapping("/loanEnquiry/assignProcessors")
    public ResponseEntity updateProcessors(@RequestBody ProcessorResource processorResource) {

        LoanApplication loanApplication = loanApplicationRepository.findByEnquiryNo(processorResource.getEnquiryNo());

        log.info("Risk Dept. Head   : " + processorResource.getRiskDepartmentHead());
        log.info("Project Officer   :" + processorResource.getProjectDepartmentInitiator());
        log.info("Monitoring Officer: " + processorResource.getMonitoringDepartmentInitiator());
        log.info("Risk Officer      : " + processorResource.getRiskDepartmentInitiator());



        User riskDeptHeadUser = userRepository.findByEmail(processorResource.getRiskDepartmentHead());
        User projectOfficer  = userRepository.findByEmail(processorResource.getProjectDepartmentInitiator());
        User monitoringOfficer = userRepository.findByEmail(processorResource.getMonitoringDepartmentInitiator());
        User riskOfficer = userRepository.findByEmail(processorResource.getRiskDepartmentInitiator());

        Boolean sendNotificationToProjectOfficer = false;
        Boolean sendNotificationToMonitoringOfficer = false;
        Boolean sendNotificationToRiskOfficer = false;

        // Set Empty strings in case of null values
        if (processorResource.getProjectDepartmentInitiator() ==null)
            processorResource.setRiskDepartmentInitiator("");
        if (processorResource.getMonitoringDepartmentInitiator() ==null)
            processorResource.setMonitoringDepartmentInitiator("");
        if (processorResource.getRiskDepartmentInitiator() == null)
            processorResource.setRiskDepartmentInitiator("");


        // Has the project officer changed?
        if (processorResource.getProjectDepartmentInitiator() != "") {
            if (processorResource.getProjectDepartmentInitiator().equals(loanApplication.getProjectDepartmentInitiator()) == false) {
                sendNotificationToProjectOfficer = true;
            }
        }

        // Has the Monitoring officer changed?
        if (processorResource.getMonitoringDepartmentInitiator() != "") {
            if (processorResource.getMonitoringDepartmentInitiator().equals(loanApplication.getMonitoringDepartmentInitiator()) == false) {
                sendNotificationToMonitoringOfficer = true;
            }
        }

        // Has the Risk officer changed?
        if (processorResource.getRiskDepartmentInitiator() != "") {
            if (processorResource.getRiskDepartmentInitiator().equals(loanApplication.getRiskDepartmentInitiator()) == false) {
                sendNotificationToRiskOfficer = true;
            }
        }


        loanApplication.updateProcessors(processorResource.getProjectDepartmentInitiator(),
                                         processorResource.getMonitoringDepartmentInitiator(),
                                         processorResource.getRiskDepartmentInitiator());

        loanApplication = loanApplicationRepository.save(loanApplication);


        // Send Notification to Project Officer
        if (sendNotificationToProjectOfficer == true) {
            riskNotificationService.sendOfficerAssignmentNotification(projectOfficer,loanApplication,"Project Officer");
        }
        // Send Notification to Risk Officer
        if (sendNotificationToRiskOfficer == true) {
            riskNotificationService.sendOfficerAssignmentNotification(riskOfficer,loanApplication,"Risk Officer");
        }
        // Send Notification to Monitoring Officer
        if (sendNotificationToMonitoringOfficer == true) {
            riskNotificationService.sendOfficerAssignmentNotification(monitoringOfficer,loanApplication,"Monitoring Officer");
        }
        // Send Notification to Risk Department Head if Risk Officer is not assigned yet
        if (loanApplication.getRiskDepartmentInitiator() == null ||loanApplication.getRiskDepartmentInitiator().equals("")) {
            riskNotificationService.sendRiskOfficerAssignmentNotification(riskDeptHeadUser, loanApplication);
        }

        return ResponseEntity.ok(loanApplication);
    }





}
