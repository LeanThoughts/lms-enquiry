package pfs.lms.enquiry.service.workflow.impl;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalService;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.BoardApprovalRepository;
import pfs.lms.enquiry.boardapproval.BoardApprovalService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.exception.HandledException;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.domain.WorkflowApprover;
import pfs.lms.enquiry.dto.WorkflowTaskDTO;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;
import pfs.lms.enquiry.service.workflow.IWorkflowService;
import pfs.lms.enquiry.vault.FileSystemStorage;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by sajeev on 09-Mar-21.
 */
@Service
@Component
@RequiredArgsConstructor
public class WorkflowService implements IWorkflowService {

    @Value( "${spring.activiti.mail-server-user-name}" )
    private String username;

    @Autowired
    private LoanMonitorRepository loanMonitorRepository;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private WorkflowApproverRepository workflowApproverRepository;

    @Autowired
    private LoanAppraisalRepository loanAppraisalRepository;

    @Autowired
    private EnquiryActionRepository enquiryActionRepository;

    @Autowired
    private BoardApprovalRepository boardApprovalRepository;


    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(FileSystemStorage.class);

    private final ProjectProposalService projectProposalService;

    private final BoardApprovalService boardApprovalService;


    @Override
    public Object startWorkflowProcessInstance(UUID businessProcessId,
                                               String requestorName,
                                               String requestorEmail,
                                               String processName) throws HandledException {





        Map<String, Object> variables = new HashMap<>();
        String processInstanceId =  new String();
        LoanMonitor loanMonitor = new LoanMonitor();
        LoanAppraisal loanAppraisal = new LoanAppraisal();
        EnquiryAction enquiryAction = new EnquiryAction();
        BoardApproval boardApproval = new BoardApproval();
        LoanApplication loanApplication = new LoanApplication();
        String loanContractId = null;
        String loanEnquiryId = null;
        String objectId = null;
        String processDescription = null;


        switch (processName) {
            case "Monitoring":
                //Fetch the Entity
                 loanMonitor = loanMonitorRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "02" - Sent for Approval
                loanMonitor.setWorkFlowStatusCode(02); loanMonitor.setWorkFlowStatusDescription("Sent for Approval");
                loanContractId =  loanMonitor.getLoanApplication().getLoanContractId();
                loanApplication = loanApplicationRepository.findByLoanContractId(loanContractId);
                objectId = loanContractId;
                processDescription = "Monitoring";
                break;
            case "Appraisal" :
                //Fetch the Entity
                loanAppraisal = loanAppraisalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "02" - Sent for Approval
                loanAppraisal.setWorkFlowStatusCode(02); loanAppraisal.setWorkFlowStatusDescription("Sent for Approval");
                loanContractId =  loanAppraisal.getLoanApplication().getLoanContractId();
                loanApplication = loanApplicationRepository.findByLoanContractId(loanContractId);
                objectId = loanContractId;
                processDescription = "Appraisal";
                break;
            case "EnquiryAction" :
                //Fetch the Entity
                enquiryAction = enquiryActionRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "02" - Sent for Approval
                enquiryAction.setWorkFlowStatusCode(02); enquiryAction.setWorkFlowStatusDescription("Sent for Approval");
                loanApplication = enquiryAction.getLoanApplication();
                objectId = loanApplication.getEnquiryNo().getId().toString();
                processDescription = "Process Enquiry";
                break;
            case "BoardApproval" :
                //Fetch the Entity
                boardApproval = boardApprovalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "02" - Sent for Approval
                boardApproval.setWorkFlowStatusCode(02); boardApproval.setWorkFlowStatusDescription("Sent for Approval");
                loanApplication = boardApproval.getLoanApplication();
                objectId = loanApplication.getEnquiryNo().getId().toString();
                processDescription = "Board Approval";
                break;
        }


        //Deterimine Approver Name and Email
        WorkflowApprover workflowApprover = workflowApproverRepository.findByProcessName(processName);
        if ( workflowApprover == null){
            throw new HandledException("000", "Workflow approver not mainatained for process : " +processName);
        }

        User user = userRepository.findByEmail(requestorEmail);
        String requestorFullName =  user.getFirstName() + " " + user.getLastName();

        //Fill the process Variables
        variables.put("LoanProcessId",businessProcessId);
        variables.put("approverEmail", workflowApprover.getApproverEmail());
        variables.put("approverName", workflowApprover.getApproverName());
        variables.put("requestorName", requestorFullName);
        variables.put("requestorEmail", requestorEmail);
        variables.put("loanContractId"  , objectId);
        variables.put("fromEmail" , username);
        variables.put("processName"  , processDescription);
        variables.put("requestDate", DateTime.now().toString());
        variables.put("projectName", loanApplication.getProjectName());
        variables.put("workflowStatus", "In Approval");


        log.info("ABOUT TO START WORKFLOW......... PROCESS NAME     :  "  + processName);
        log.info("ABOUT TO START WORKFLOW......... FROM EMAIL       : "  + username);
        log.info("ABOUT TO START WORKFLOW......... LOAN CONTRACT    : "  + loanContractId);

        runtimeService = processEngine.getRuntimeService();


        try {

            ProcessInstance processInstance =  runtimeService.startProcessInstanceByKey("LoansOneLevelApproval", variables);
            processInstanceId = processInstance.getProcessInstanceId();

            System.out.println("STARTING WORKFLOW DONE : Process Instance : " + processInstance.getProcessInstanceId());

        } catch (Exception ex) {
            log.info ("Exception starting workflow process ------------------------------");
            log.info(ex.toString());
        }

        switch (processName) {
            case "Monitoring":
                //Save entity with the Process Instance and workflow status code
                loanMonitor.setProcessInstanceId(processInstanceId);
                loanMonitor = loanMonitorRepository.save(loanMonitor);
                return loanMonitor;
            case "Appraisal" :
                //Save entity with the Process Instance and workflow status code
                loanAppraisal.setProcessInstanceId(processInstanceId);
                loanAppraisal = loanAppraisalRepository.save(loanAppraisal);
                return loanAppraisal;
            case "EnquiryAction" :
                //Save entity with the Process Instance and workflow status code
                enquiryAction.setProcessInstanceId(processInstanceId);
                enquiryAction = enquiryActionRepository.save(enquiryAction);
                return enquiryAction;
            case "BoardApproval" :
                //Save entity with the Process Instance and workflow status code
                boardApproval.setProcessInstanceId(processInstanceId);
                boardApproval = boardApprovalRepository.save(boardApproval);
                return boardApproval;
        }

        return null;
    }

    @Override
    public Object approveTask(String processInstanceId, UUID businessProcessId, String processName, String username) throws CloneNotSupportedException {


        Map<String, Object> variables = new HashMap<>();
        LoanMonitor loanMonitor = new LoanMonitor();
        LoanAppraisal loanAppraisal = new LoanAppraisal();
        EnquiryAction enquiryAction = new EnquiryAction();
        BoardApproval boardApproval = new BoardApproval();
        String loanContractId = null;
        String loanEnquiryId = null;


        switch (processName) {
            case "Monitoring":
                //Fetch the Entity
                loanMonitor = loanMonitorRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "03" - Approved
                loanMonitor.setWorkFlowStatusCode(03); loanMonitor.setWorkFlowStatusDescription("Approved");
                loanContractId =  loanMonitor.getLoanApplication().getLoanContractId();
                processInstanceId = loanMonitor.getProcessInstanceId();
                break;
            case "Appraisal" :
                //Fetch the Entity
                loanAppraisal = loanAppraisalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "03" - Approved
                loanAppraisal.setWorkFlowStatusCode(03); loanAppraisal.setWorkFlowStatusDescription("Approved");
                loanContractId =  loanAppraisal.getLoanApplication().getLoanContractId();
                processInstanceId = loanAppraisal.getProcessInstanceId();
                break;
            case "Process Enquiry" :
                //Fetch the Entity
                enquiryAction = enquiryActionRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "03" - Approved
                enquiryAction.setWorkFlowStatusCode(03); enquiryAction.setWorkFlowStatusDescription("Approved");
                loanEnquiryId =  enquiryAction.getLoanApplication().getEnquiryNo().getId().toString();
                processInstanceId = enquiryAction.getProcessInstanceId();
                break;
            case "Board Approval" :
                //Fetch the Entity
                boardApproval = boardApprovalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "03" - Approved
                boardApproval.setWorkFlowStatusCode(03); boardApproval.setWorkFlowStatusDescription("Approved");
                loanEnquiryId =  boardApproval.getLoanApplication().getEnquiryNo().getId().toString();
                processInstanceId = boardApproval.getProcessInstanceId();
                break;
        }

        // Fetch the Task
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        // Prepare Variables
        variables.put("workflowStatus", "TRUE");


        System.out.println("--------------- Workflow APPROVAL Task Execution  Started @ : " + DateTime.now());

        try {
            taskService.complete(task.getId(), variables);
        } catch (Exception ex) {
         log.info("WorkFlow Approval Exception : " +ex.getMessage());
         return null;
        }
        System.out.println("--------------- Workflow APPROVAL Task Execution Finished @ : " + DateTime.now());

        switch (processName) {
            case "Monitoring":
                //Save entity with the new workflow status code
                loanMonitor.setWorkFlowStatusDescription("Approved");
                loanMonitor.setWorkFlowStatusCode(3);
                loanMonitor.setProcessInstanceId(processInstanceId);
                loanMonitorRepository.save(loanMonitor);
                return loanMonitor;
            case "Appraisal" :
                //Save entity with the new workflow status code
                loanAppraisal.setWorkFlowStatusDescription("Approved");
                loanAppraisal.setWorkFlowStatusCode(3);
                loanAppraisal.setProcessInstanceId(processInstanceId);
                loanAppraisalRepository.save(loanAppraisal);
                return loanAppraisal;
            case "Process Enquiry" :
                //Save entity with the new workflow status code
                enquiryAction.setWorkFlowStatusDescription("Approved");
                enquiryAction.setWorkFlowStatusCode(3);
                enquiryAction.setProcessInstanceId(processInstanceId);
                enquiryActionRepository.save(enquiryAction); enquiryActionRepository.flush();
                projectProposalService.processApprovedEnquiry(enquiryAction,username);
                return enquiryAction;
            case "Board Approval" :
                //Save entity with the new workflow status code
                boardApproval.setWorkFlowStatusDescription("Approved");
                boardApproval.setWorkFlowStatusCode(3);
                boardApproval.setProcessInstanceId(processInstanceId);
                boardApprovalRepository.save(boardApproval); boardApprovalRepository.flush();
                boardApprovalService.processApprovedBoardApproval(boardApproval,username);
                return enquiryAction;
        }

        return null;

     }

    @Override
    public Object rejectTask(String processInstanceId, UUID businessProcessId, String processName, String rejectionReason, String requestorEmail) {



        Map<String, Object> variables = new HashMap<>();
        LoanMonitor loanMonitor = new LoanMonitor();
        LoanAppraisal loanAppraisal = new LoanAppraisal();
        EnquiryAction enquiryAction = new EnquiryAction();
        BoardApproval boardApproval = new BoardApproval();
        String loanContractId = null;
        String loanEnquiryId = null;


        switch (processName) {
            case "Monitoring":
                //Fetch the Entity
                loanMonitor = loanMonitorRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                loanMonitor.setWorkFlowStatusCode(4); loanMonitor.setWorkFlowStatusDescription("Rejected");
                loanContractId =  loanMonitor.getLoanApplication().getLoanContractId();
                processInstanceId = loanMonitor.getProcessInstanceId();
                break;
            case "Appraisal" :
                //Fetch the Entity
                loanAppraisal = loanAppraisalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                loanAppraisal.setWorkFlowStatusCode(4); loanAppraisal.setWorkFlowStatusDescription("Rejected");
                loanContractId =  loanAppraisal.getLoanApplication().getLoanContractId();
                processInstanceId = loanAppraisal.getProcessInstanceId();
                break;
            case "Process Enquiry" :
                //Fetch the Entity
                enquiryAction = enquiryActionRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                enquiryAction.setWorkFlowStatusCode(4); enquiryAction.setWorkFlowStatusDescription("Rejected");
                loanEnquiryId =  enquiryAction.getLoanApplication().getEnquiryNo().getId().toString();
                processInstanceId = enquiryAction.getProcessInstanceId();
                break;
            case "Board Approval" :
                //Fetch the Entity
                boardApproval = boardApprovalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                boardApproval.setWorkFlowStatusCode(4); boardApproval.setWorkFlowStatusDescription("Rejected");
                loanEnquiryId =  boardApproval.getLoanApplication().getEnquiryNo().getId().toString();
                processInstanceId = boardApproval.getProcessInstanceId();
                break;
        }


        // Fetch the Task
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        if ( task == null) {
            //Raise Exception
        }

        // Prepare Variables
        variables.put("workflowStatus", "FALSE");
        variables.put("rejectionReason", rejectionReason);
//        variables.put("requestorEmail",  requestorEmail);

        System.out.println("--------------- Workflow REJECTION Task Execution  Started @ : " + DateTime.now());

        try {
            taskService.complete(task.getId(), variables);
        } catch (Exception ex) {
            log.info("WorkFlow REJECTION Exception : " +ex.getMessage());
        }
        System.out.println("--------------- Workflow REJECTION Task Execution Finished @ : " + DateTime.now());

        switch (processName) {
            case "Monitoring":
                //Save entity with the new workflow status code
                loanMonitor.setWorkFlowStatusDescription("Rejected");
                loanMonitor.setWorkFlowStatusCode(4);
                loanMonitor = loanMonitorRepository.save(loanMonitor);
                 return loanMonitor;
            case "Appraisal" :
                //Fetch the Entity
                loanAppraisal = loanAppraisalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                loanAppraisal.setWorkFlowStatusCode(4); loanAppraisal.setWorkFlowStatusDescription("Rejected");
                loanContractId =  loanAppraisal.getLoanApplication().getLoanContractId();
                processInstanceId = loanAppraisal.getProcessInstanceId();
                break;
            case "Process Enquiry" :
                //Fetch the Entity
                enquiryAction = enquiryActionRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                enquiryAction.setWorkFlowStatusCode(4); enquiryAction.setWorkFlowStatusDescription("Rejected");
                loanEnquiryId =  enquiryAction.getLoanApplication().getEnquiryNo().getId().toString();
                processInstanceId = enquiryAction.getProcessInstanceId();
                break;
            case "Board Approval" :
                //Fetch the Entity
                boardApproval = boardApprovalRepository.getOne(businessProcessId);
                // Set the Work Flow Status Code "04" - Rejected
                boardApproval.setWorkFlowStatusCode(4); boardApproval.setWorkFlowStatusDescription("Rejected");
                loanEnquiryId =  boardApproval.getLoanApplication().getEnquiryNo().getId().toString();
                processInstanceId = boardApproval.getProcessInstanceId();
                break;
        }

        return null;
    }


    @Override
    public   List<WorkflowTaskDTO>  getTasks(HttpServletRequest httpServletRequest) {

        TaskService taskService = processEngine.getTaskService();
        String userName = httpServletRequest.getUserPrincipal().getName();

        List<WorkflowTaskDTO> workflowTaskDTOList = new ArrayList<>();

        log.info(LocalDateTime.now() + ": USER NAME: " + httpServletRequest.getUserPrincipal().getName());

        List<Task>  tasks = taskService.createTaskQuery()
                .taskAssignee(userName)
                .includeProcessVariables()
                .orderByTaskCreateTime()
                .desc()
                .list();

        for (Task task: tasks) {

            // Only Active Tasks
            if (task.isSuspended() == false) {
                // System.out.println(task.getAssignee() + task.getId() + task.getProcessDefinitionId() );
                Map<String, Object> variables = task.getProcessVariables();

                WorkflowTaskDTO workflowTaskDTO = prepareWorkflowTask(task, variables);

                 // Eliminate Duplicate Workflow Tasks

                    workflowTaskDTOList.add(workflowTaskDTO);
            }
        }
        return workflowTaskDTOList;
    }



    private WorkflowTaskDTO prepareWorkflowTask ( Task task, Map<String, Object> variables) {

        WorkflowTaskDTO workflowTaskDTO = new WorkflowTaskDTO();
        workflowTaskDTO.setId(task.getId().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dateAsString = sdf.format(task.getCreateTime());

        workflowTaskDTO.setRequestDate(dateAsString);
        workflowTaskDTO.setLanContractId(variables.get("loanContractId").toString());
        workflowTaskDTO.setProjectName(variables.get("projectName").toString());
        workflowTaskDTO.setApproverEmail(variables.get("approverEmail").toString());
        workflowTaskDTO.setApproverName(variables.get("approverName").toString());
        workflowTaskDTO.setRequestDate(variables.get("requestDate").toString());
        workflowTaskDTO.setRequestorEmail(variables.get("requestorName").toString());
        workflowTaskDTO.setRequestorName(variables.get("requestorEmail").toString());
        workflowTaskDTO.setProcessName(variables.get("processName").toString());
        workflowTaskDTO.setBusinessProcessId(variables.get("LoanProcessId").toString());

        if (variables.get("workflowStatus") != null) {
            if (variables.get("workflowStatus").toString() == "TRUE")
                workflowTaskDTO.setStatus("Approved");

            if (variables.get("workflowStatus").toString() == "FALSE")
                workflowTaskDTO.setStatus("Rejected");

            if (variables.get("workflowStatus").toString() == "In Approval")
                workflowTaskDTO.setStatus("Approved");
        }
        else
            workflowTaskDTO.setStatus("In Approval");


        return workflowTaskDTO;

    }

}
