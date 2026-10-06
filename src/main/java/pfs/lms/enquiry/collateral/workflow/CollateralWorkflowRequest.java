package pfs.lms.enquiry.collateral.workflow;

import java.util.UUID;

/**
 * Request of the collateral workflow endpoints: the checklist (business process id), for approve / reject the task
 * or process instance id (the inbox sends the task id), and the rejection reason.
 */
public class CollateralWorkflowRequest {

    private UUID businessProcessId;
    private String processInstanceId;
    private String rejectionReason;

    public CollateralWorkflowRequest() {
    }

    public UUID getBusinessProcessId() { return businessProcessId; }
    public void setBusinessProcessId(UUID businessProcessId) { this.businessProcessId = businessProcessId; }

    public String getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
