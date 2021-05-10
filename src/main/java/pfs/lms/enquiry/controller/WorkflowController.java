package pfs.lms.enquiry.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.LoanMonitor;
import pfs.lms.enquiry.dto.WorkflowTaskDTO;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.LoanMonitorRepository;
import pfs.lms.enquiry.resource.WorkflowProcessRequestResource;
import pfs.lms.enquiry.resource.WorkflowRequestResource;
import pfs.lms.enquiry.service.workflow.IWorkflowService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

//
//import org.activiti.engine.RuntimeService;

/**
 * Created by sajeev on 15-Dec-18.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class WorkflowController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private LoanMonitorRepository loanMonitorRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @GetMapping("/tasklist")
    public ResponseEntity<List<WorkflowTaskDTO>> getTasks(HttpServletRequest httpServletRequest) {

        List<WorkflowTaskDTO> workflowTaskDTOList = new ArrayList<WorkflowTaskDTO>();

        workflowTaskDTOList  = workflowService.getTasks(httpServletRequest);

        return ResponseEntity.ok(workflowTaskDTOList);

    }


    @PutMapping("/startprocess")
    public ResponseEntity<Object> startProcess(@RequestBody WorkflowRequestResource workflowRequestResource,
                                               HttpServletRequest httpServletRequest) {


        Object processObject = workflowService.startWorkflowProcessInstance(
                                    workflowRequestResource.getBusinessProcessId(),
                                    httpServletRequest.getUserPrincipal().getName(),
                                    workflowRequestResource.getRequestorEmail(),
                                    workflowRequestResource.getProcessName());

        switch (workflowRequestResource.getProcessName()){
            case "Monitoring":
                return ResponseEntity.ok(processObject);
            case "Appraisal" :
                break;
        }


        return ResponseEntity.ok(null);

    }


    @PutMapping("/approvetask")
    public ResponseEntity<Object> approveTask(@RequestBody WorkflowProcessRequestResource workflowProcessRequestResource,
                                              HttpServletRequest httpServletRequest) {



        Object processObject = workflowService.approveTask(workflowProcessRequestResource.getProcessInstanceId(),
                                    workflowProcessRequestResource.getBusinessProcessId(),
                                    workflowProcessRequestResource.getProcessName() );



        return ResponseEntity.ok(processObject);

    }

    @PutMapping("/rejecttask")
    public ResponseEntity<Object> rejectTask(@RequestBody WorkflowProcessRequestResource workflowProcessRequestResource,
                                             HttpServletRequest httpServletRequest) {



        Object processObject = workflowService.rejectTask(workflowProcessRequestResource.getProcessInstanceId(),
                                        workflowProcessRequestResource.getBusinessProcessId(),
                                        workflowProcessRequestResource.getProcessName(),
                                        workflowProcessRequestResource.getRejectionReason(),
                                        httpServletRequest.getUserPrincipal().getName());


        return ResponseEntity.ok(processObject);

    }
}
