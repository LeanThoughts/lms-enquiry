package pfs.lms.enquiry.collateral.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.service.ICollateralService;
import pfs.lms.enquiry.collateral.service.impl.CollateralChangeDocumentService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.domain.WorkflowApprover;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Approval workflow of collateral checklists. Follows the existing WorkflowService: the one-level approval process
 * "LoansOneLevelApproval" with the same process variables, so the task appears in the approver's inbox and the
 * existing approval / rejection e-mails are sent. The approver is the workflow approver maintained for process
 * {@value ICollateralWorkflowService#PROCESS_NAME} and the requestor's department, or for no department.
 */
@Service
@Transactional
public class CollateralWorkflowService implements ICollateralWorkflowService {

    private static final Logger log = LoggerFactory.getLogger(CollateralWorkflowService.class);
    private static final String PROCESS_DEFINITION_KEY = "LoansOneLevelApproval";

    private final ProcessEngine processEngine;
    private final CollateralChecklistRepository checklistRepository;
    private final CollateralItemRepository itemRepository;
    private final WorkflowApproverRepository workflowApproverRepository;
    private final UserRepository userRepository;
    private final ICollateralService collateralService;
    private final CollateralChangeDocumentService changeDocumentService;
    private final String fromEmail;

    public CollateralWorkflowService(ProcessEngine processEngine,
                                     CollateralChecklistRepository checklistRepository,
                                     CollateralItemRepository itemRepository,
                                     WorkflowApproverRepository workflowApproverRepository,
                                     UserRepository userRepository,
                                     ICollateralService collateralService,
                                     CollateralChangeDocumentService changeDocumentService,
                                     @Value("${spring.activiti.mail-server-user-name:}") String fromEmail) {
        this.processEngine = processEngine;
        this.checklistRepository = checklistRepository;
        this.itemRepository = itemRepository;
        this.workflowApproverRepository = workflowApproverRepository;
        this.userRepository = userRepository;
        this.collateralService = collateralService;
        this.changeDocumentService = changeDocumentService;
        this.fromEmail = fromEmail;
    }

    @Override
    public CollateralChecklistDto startWorkflowProcessInstance(UUID checklistId, String requestorEmail) {
        CollateralChecklist checklist = findChecklist(checklistId);
        if (checklist.isInApproval()) {
            throw new LmsException("The collateral checklist is already waiting for approval.", HttpStatus.CONFLICT);
        }
        if (itemRepository.countByChecklist_Id(checklist.getId()) == 0) {
            throw new LmsException("Create at least one collateral before sending the checklist for approval.",
                    HttpStatus.PRECONDITION_FAILED);
        }

        User user = requestorEmail == null ? null : userRepository.findByEmail(requestorEmail);
        String requestorName = user != null
                ? (Objects.toString(user.getFirstName(), "") + " " + Objects.toString(user.getLastName(), "")).trim()
                : requestorEmail;
        WorkflowApprover approver = findApprover(user != null ? user.getRiskDepartment() : null);

        LoanApplication loan = checklist.getLoanApplication();
        String loanContractId = checklist.getLoanContractId() != null ? checklist.getLoanContractId()
                : loan.getLoanContractId();

        // Same process variables as the existing WorkflowService
        Map<String, Object> variables = new HashMap<>();
        variables.put("LoanProcessId", checklist.getId());
        variables.put("approverEmail", approver.getApproverEmail());
        variables.put("approverName", approver.getApproverName());
        variables.put("requestorName", requestorName);
        variables.put("requestorEmail", requestorEmail);
        variables.put("loanContractId", loanContractId);
        variables.put("fromEmail", fromEmail);
        variables.put("processName", PROCESS_NAME);
        variables.put("requestDate", LocalDateTime.now().toString());
        variables.put("projectName", loan.getProjectName());
        variables.put("workflowStatus", "In Approval");

        String processInstanceId;
        try {
            ProcessInstance processInstance = processEngine.getRuntimeService()
                    .startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
            processInstanceId = processInstance.getProcessInstanceId();
        } catch (RuntimeException ex) {
            log.error("Collateral workflow could not be started for checklist {}", checklist.getId(), ex);
            throw new LmsException("The approval workflow could not be started: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Collateral workflow started for loan {}: process instance {}, approver {}", loanContractId,
                processInstanceId, approver.getApproverEmail());

        checklist.setProcessInstanceId(processInstanceId);
        checklist.setRejectionReason(null);
        setStatus(checklist, CollateralChecklist.STATUS_SENT_FOR_APPROVAL,
                CollateralChecklist.STATUS_SENT_FOR_APPROVAL_TEXT, requestorEmail);
        record(checklist, CollateralChangeDocumentService.SENT_FOR_APPROVAL, requestorEmail);
        return collateralService.getChecklist(loan.getId());
    }

    @Override
    public CollateralChecklistDto approveTask(String processInstanceId, UUID checklistId, String userName) {
        CollateralChecklist checklist = findChecklist(checklistId);
        completeTask(checklist, processInstanceId, "TRUE", null, userName);
        setStatus(checklist, CollateralChecklist.STATUS_APPROVED, CollateralChecklist.STATUS_APPROVED_TEXT, userName);
        record(checklist, CollateralChangeDocumentService.APPROVED, userName);
        return collateralService.getChecklist(checklist.getLoanApplication().getId());
    }

    @Override
    public CollateralChecklistDto rejectTask(String processInstanceId, UUID checklistId, String rejectionReason,
                                             String userName) {
        CollateralChecklist checklist = findChecklist(checklistId);
        completeTask(checklist, processInstanceId, "FALSE", rejectionReason, userName);
        checklist.setRejectionReason(rejectionReason == null || rejectionReason.length() <= 500
                ? rejectionReason : rejectionReason.substring(0, 500));
        setStatus(checklist, CollateralChecklist.STATUS_REJECTED, CollateralChecklist.STATUS_REJECTED_TEXT, userName);
        record(checklist, CollateralChangeDocumentService.REJECTED, userName);
        return collateralService.getChecklist(checklist.getLoanApplication().getId());
    }

    // ------------------------------------------------------------------------------------------------ helpers

    /** Completes the approver's task; the workflow sends the approval or rejection e-mail. */
    private void completeTask(CollateralChecklist checklist, String taskOrProcessInstanceId, String workflowStatus,
                              String rejectionReason, String approverEmail) {
        if (!checklist.isInApproval()) {
            throw new LmsException("The collateral checklist is not waiting for approval.", HttpStatus.CONFLICT);
        }
        TaskService taskService = processEngine.getTaskService();
        Task task = findTask(taskService, taskOrProcessInstanceId, checklist.getProcessInstanceId());
        if (task == null) {
            throw new LmsException("No open approval task for this collateral checklist.", HttpStatus.NOT_FOUND);
        }
        if (approverEmail != null && task.getAssignee() != null && !task.getAssignee().equalsIgnoreCase(approverEmail)) {
            throw new LmsException("Only the approver " + task.getAssignee() + " can approve or reject this checklist.",
                    HttpStatus.FORBIDDEN);
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("workflowStatus", workflowStatus);
        if (rejectionReason != null) {
            variables.put("rejectionReason", rejectionReason);
        }
        try {
            taskService.complete(task.getId(), variables);
        } catch (RuntimeException ex) {
            log.error("Collateral workflow task {} could not be completed", task.getId(), ex);
            throw new LmsException("The approval task could not be completed: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** The inbox sends the task id; other callers may send the process instance id. */
    private static Task findTask(TaskService taskService, String taskOrProcessInstanceId, String checklistProcessInstanceId) {
        Task task = null;
        if (taskOrProcessInstanceId != null && !taskOrProcessInstanceId.isEmpty()) {
            task = taskService.createTaskQuery().taskId(taskOrProcessInstanceId).singleResult();
            if (task == null) {
                task = taskService.createTaskQuery().processInstanceId(taskOrProcessInstanceId).singleResult();
            }
        }
        if (task == null && checklistProcessInstanceId != null) {
            task = taskService.createTaskQuery().processInstanceId(checklistProcessInstanceId).singleResult();
        }
        if (task != null && checklistProcessInstanceId != null
                && !checklistProcessInstanceId.equals(task.getProcessInstanceId())) {
            throw new LmsException("The task does not belong to this collateral checklist.", HttpStatus.CONFLICT);
        }
        return task;
    }

    /** Approver for the requestor's department, else the approver maintained without department. */
    private WorkflowApprover findApprover(String departmentCode) {
        WorkflowApprover probe = new WorkflowApprover();
        probe.setProcessName(PROCESS_NAME);
        List<WorkflowApprover> approvers = workflowApproverRepository.findAll(Example.of(probe));
        return approvers.stream()
                .filter(approver -> departmentCode != null && departmentCode.equals(approver.getDepartmentCode()))
                .findFirst()
                .orElseGet(() -> approvers.stream()
                        .filter(approver -> approver.getDepartmentCode() == null || approver.getDepartmentCode().isEmpty())
                        .findFirst()
                        .orElseThrow(() -> new LmsException("Workflow approver not maintained for process " + PROCESS_NAME
                                + (departmentCode != null ? " and department " + departmentCode : "")
                                + ". Maintain it under Administration > Workflow Approvers.", HttpStatus.PRECONDITION_FAILED)));
    }

    private CollateralChecklist findChecklist(UUID checklistId) {
        if (checklistId == null) {
            throw new LmsException("The collateral checklist is missing.", HttpStatus.PRECONDITION_FAILED);
        }
        return checklistRepository.findById(checklistId)
                .orElseThrow(() -> new LmsException("Collateral checklist not found.", HttpStatus.NOT_FOUND));
    }

    private void setStatus(CollateralChecklist checklist, int code, String text, String userName) {
        checklist.setWorkFlowStatusCode(code);
        checklist.setWorkFlowStatusDescription(text);
        checklist.setChangedOn(LocalDate.now());
        checklist.setChangedAt(LocalTime.now());
        checklist.setChangedByUserName(userName);
        checklistRepository.save(checklist);
    }

    private void record(CollateralChecklist checklist, String action, String userName) {
        changeDocumentService.record(checklist.getLoanApplication(), checklist.getId(), checklist.getId(),
                "Collateral checklist of loan " + checklist.getLoanContractId(), null, checklist, action, userName,
                CollateralChangeDocumentService.SUB_PROCESS_CHECKLIST);
    }
}
