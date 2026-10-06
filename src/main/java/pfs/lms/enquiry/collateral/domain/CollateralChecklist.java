package pfs.lms.enquiry.collateral.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.LoanApplication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Collateral checklist of a loan application: the header above the collaterals ({@link CollateralItem}, 1:N).
 * There is one checklist per loan application. It carries the workflow status; the workflow itself is added later.
 */
@Entity
@Table(name = "collateral_checklist")
public class CollateralChecklist extends AggregateRoot<CollateralChecklist> {

    /** Workflow status of a checklist that has not been sent for approval (portal convention). */
    public static final int STATUS_NOT_SENT_FOR_APPROVAL = 0;
    public static final String STATUS_NOT_SENT_FOR_APPROVAL_TEXT = "Not Sent for Approval";
    /** Workflow status codes of the portal (as for business partners and reference interest rates). */
    public static final int STATUS_SENT_FOR_APPROVAL = 2;
    public static final String STATUS_SENT_FOR_APPROVAL_TEXT = "Sent for Approval";
    public static final int STATUS_APPROVED = 3;
    public static final String STATUS_APPROVED_TEXT = "Approved";
    public static final int STATUS_REJECTED = 4;
    public static final String STATUS_REJECTED_TEXT = "Rejected";

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;

    /** SAP loan contract number (RANL), kept for the migration and the SAP integration. */
    @Column(length = 13)
    private String loanContractId;

    private Integer workFlowStatusCode;

    @Column(length = 100)
    private String workFlowStatusDescription;

    /** Activiti process instance of the current or last approval workflow. */
    @Column(length = 64)
    private String processInstanceId;

    /** Reason given by the approver when the checklist was rejected last. */
    @Column(length = 500)
    private String rejectionReason;

    public CollateralChecklist() {
    }

    /** True while the checklist waits for approval: the collaterals cannot be changed then. */
    public boolean isInApproval() {
        return workFlowStatusCode != null && workFlowStatusCode == STATUS_SENT_FOR_APPROVAL;
    }

    public UUID getLoanApplicationId() {
        return loanApplication != null ? loanApplication.getId() : null;
    }

    public LoanApplication getLoanApplication() { return loanApplication; }
    public void setLoanApplication(LoanApplication loanApplication) { this.loanApplication = loanApplication; }

    public String getLoanContractId() { return loanContractId; }
    public void setLoanContractId(String loanContractId) { this.loanContractId = loanContractId; }

    public Integer getWorkFlowStatusCode() { return workFlowStatusCode; }
    public void setWorkFlowStatusCode(Integer workFlowStatusCode) { this.workFlowStatusCode = workFlowStatusCode; }

    public String getWorkFlowStatusDescription() { return workFlowStatusDescription; }
    public void setWorkFlowStatusDescription(String workFlowStatusDescription) { this.workFlowStatusDescription = workFlowStatusDescription; }

    public String getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
