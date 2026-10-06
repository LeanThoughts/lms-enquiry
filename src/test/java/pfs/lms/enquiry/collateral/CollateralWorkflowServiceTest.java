package pfs.lms.enquiry.collateral;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.service.impl.CollateralChangeDocumentService;
import pfs.lms.enquiry.collateral.workflow.ICollateralWorkflowService;
import pfs.lms.enquiry.domain.WorkflowApprover;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/** Send for Approval, approve and reject a collateral checklist (process LoansOneLevelApproval). */
@DisplayName("Collateral checklist workflow")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollateralWorkflowServiceTest extends CollateralTestSupport {

    private ICollateralWorkflowService workflow;
    private ProcessEngine engine;
    private UUID loanId;
    private UUID checklistId;
    private UUID itemId;

    @BeforeAll
    void checklist() {
        workflow = bean(ICollateralWorkflowService.class);
        engine = bean(ProcessEngine.class);
        loanId = loan("0000010003400");
        CollateralItem item = createItem(loanId, "Z00007");
        itemId = item.getId();
        checklistId = collateralService.getChecklist(loanId).getId();
    }

    @Test
    @Order(1)
    @DisplayName("without a workflow approver the checklist cannot be sent (412)")
    void noApprover() {
        expectError(412, () -> workflow.startWorkflowProcessInstance(checklistId, LEGAL));
    }

    @Test
    @Order(2)
    @DisplayName("Send for Approval starts the process, sets status 2 and mails the approver")
    void send() {
        WorkflowApprover approver = new WorkflowApprover();
        approver.setProcessName("CollateralManagement");
        approver.setApproverName("Head Legal");
        approver.setApproverEmail(APPROVER);
        bean(WorkflowApproverRepository.class).save(approver);

        CollateralChecklistDto sent = workflow.startWorkflowProcessInstance(checklistId, LEGAL);

        assertThat(sent.getWorkFlowStatusCode()).isEqualTo(2);
        assertThat(sent.getWorkFlowStatusDescription()).isEqualTo("Sent for Approval");
        Task task = openTask();
        Map<String, Object> variables = task.getProcessVariables();
        assertThat(variables.get("processName")).isEqualTo("CollateralManagement");
        assertThat(String.valueOf(variables.get("LoanProcessId"))).isEqualTo(checklistId.toString());
        assertThat(variables.get("requestorEmail")).isEqualTo(LEGAL);
        assertThat(smtp.getMails()).anySatisfy(mail -> assertThat(mail.recipients).contains(APPROVER));
        assertThat(changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_CHECKLIST))
                .anySatisfy(text -> assertThat(text).startsWith(CollateralChangeDocumentService.SENT_FOR_APPROVAL));
    }

    @Test
    @Order(3)
    @DisplayName("while in approval: no second start and no changes to collaterals or child rows (409)")
    void locked() {
        expectError(409, () -> workflow.startWorkflowProcessInstance(checklistId, LEGAL));
        expectError(409, () -> {
            CollateralItem item = collateralService.getItem(itemId).getItem();
            item.setRemarks("changed");
            collateralService.updateItem(itemId, item, LEGAL);
        });
        expectError(409, () -> collateralService.createItem(loanId, newItem("ZRE001"), LEGAL));
        expectError(409, () -> collateralService.deleteItem(itemId, LEGAL));
        expectError(409, () -> childService.create(CollateralChildType.COVERAGE, itemId,
                json("{'effectiveFromDate':'2001-01-01'}"), LEGAL));
    }

    @Test
    @Order(4)
    @DisplayName("only the approver of the task may decide (403)")
    void otherUser() {
        Task task = openTask();
        expectError(403, () -> workflow.approveTask(task.getId(), checklistId, LEGAL));
    }

    @Test
    @Order(5)
    @DisplayName("reject stores the reason, sets status 4 and unlocks the checklist")
    void reject() {
        Task task = openTask();
        CollateralChecklistDto rejected = workflow.rejectTask(task.getId(), checklistId, "Security trustee missing", APPROVER);

        assertThat(rejected.getWorkFlowStatusCode()).isEqualTo(4);
        assertThat(rejected.getRejectionReason()).isEqualTo("Security trustee missing");
        assertThat(engine.getTaskService().createTaskQuery().taskAssignee(APPROVER).count()).isZero();

        CollateralItem item = collateralService.getItem(itemId).getItem();
        item.setRemarks("Trustee added");
        assertThat(collateralService.updateItem(itemId, item, LEGAL).getRemarks()).isEqualTo("Trustee added");
    }

    @Test
    @Order(6)
    @DisplayName("sent again and approved: status 3, the approver's e-mail is compared without case")
    void approve() {
        workflow.startWorkflowProcessInstance(checklistId, LEGAL);
        Task task = openTask();
        CollateralChecklistDto approved = workflow.approveTask(task.getId(), checklistId, APPROVER.toUpperCase());

        assertThat(approved.getWorkFlowStatusCode()).isEqualTo(3);
        assertThat(approved.getRejectionReason()).isNull();
        expectError(409, () -> workflow.approveTask(task.getId(), checklistId, APPROVER));
        assertThat(changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_CHECKLIST))
                .anySatisfy(text -> assertThat(text).startsWith(CollateralChangeDocumentService.APPROVED))
                .anySatisfy(text -> assertThat(text).startsWith(CollateralChangeDocumentService.REJECTED));
    }

    private Task openTask() {
        Task task = engine.getTaskService().createTaskQuery().taskAssignee(APPROVER).includeProcessVariables().singleResult();
        assertThat(task).as("open task of the approver").isNotNull();
        return task;
    }
}
