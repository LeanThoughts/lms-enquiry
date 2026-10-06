package pfs.lms.enquiry.collateral.workflow;

import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;

import java.util.UUID;

/**
 * Approval workflow of a collateral checklist, on the portal's one-level approval process (LoansOneLevelApproval),
 * like the existing WorkflowService does for business partners and reference interest rates.
 */
public interface ICollateralWorkflowService {

    /** Workflow process name: key of the workflow approver and process name of the inbox tasks. */
    String PROCESS_NAME = "CollateralManagement";

    /** Starts the approval of the checklist; the checklist status becomes "Sent for Approval". */
    CollateralChecklistDto startWorkflowProcessInstance(UUID checklistId, String requestorEmail);

    /** Approves the task of the checklist; status "Approved". */
    CollateralChecklistDto approveTask(String processInstanceId, UUID checklistId, String userName);

    /** Rejects the task of the checklist; status "Rejected", the reason is kept. */
    CollateralChecklistDto rejectTask(String processInstanceId, UUID checklistId, String rejectionReason, String userName);
}
