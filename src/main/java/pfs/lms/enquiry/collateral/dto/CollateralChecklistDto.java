package pfs.lms.enquiry.collateral.dto;

import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The collateral list of a loan: checklist header (null id until the first collateral is created),
 * the loan and its collaterals. Carries the checklist's technical (audit) data — who created and last changed it,
 * and when — for the Technical Info display.
 */
public class CollateralChecklistDto {

    private UUID id;
    private Integer workFlowStatusCode;
    private String workFlowStatusDescription;
    private String rejectionReason;
    private LoanSummaryDto loan;
    private List<CollateralItem> items = new ArrayList<>();

    // technical data of the checklist (AggregateRoot audit fields)
    private LocalDate createdOn;
    private LocalTime createdAt;
    private String createdByUserName;
    private LocalDate changedOn;
    private LocalTime changedAt;
    private String changedByUserName;

    public CollateralChecklistDto() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDate createdOn) { this.createdOn = createdOn; }

    public LocalTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedByUserName() { return createdByUserName; }
    public void setCreatedByUserName(String createdByUserName) { this.createdByUserName = createdByUserName; }

    public LocalDate getChangedOn() { return changedOn; }
    public void setChangedOn(LocalDate changedOn) { this.changedOn = changedOn; }

    public LocalTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalTime changedAt) { this.changedAt = changedAt; }

    public String getChangedByUserName() { return changedByUserName; }
    public void setChangedByUserName(String changedByUserName) { this.changedByUserName = changedByUserName; }

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
