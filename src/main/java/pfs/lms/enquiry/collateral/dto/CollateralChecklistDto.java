package pfs.lms.enquiry.collateral.dto;

import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The collateral list of a loan: checklist header (null id until the first collateral is created),
 * the loan and its collaterals.
 */
public class CollateralChecklistDto {

    private UUID id;
    private Integer workFlowStatusCode;
    private String workFlowStatusDescription;
    private String rejectionReason;
    private LoanSummaryDto loan;
    private List<CollateralItem> items = new ArrayList<>();

    public CollateralChecklistDto() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Integer getWorkFlowStatusCode() { return workFlowStatusCode; }
    public void setWorkFlowStatusCode(Integer workFlowStatusCode) { this.workFlowStatusCode = workFlowStatusCode; }

    public String getWorkFlowStatusDescription() { return workFlowStatusDescription; }
    public void setWorkFlowStatusDescription(String workFlowStatusDescription) { this.workFlowStatusDescription = workFlowStatusDescription; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LoanSummaryDto getLoan() { return loan; }
    public void setLoan(LoanSummaryDto loan) { this.loan = loan; }

    public List<CollateralItem> getItems() { return items; }
    public void setItems(List<CollateralItem> items) { this.items = items; }
}
