package pfs.lms.enquiry.collateral.dto;

import pfs.lms.enquiry.collateral.domain.ProjectEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Project(Coll.) Valuation screen of a loan: the loan, its Major Collateral Type (one value for the loan, kept on
 * every evaluation row as in SAP table ZLMS_COLL_EVAL), the evaluations and whether they may be changed right now.
 */
public class ProjectEvaluationDto {

    private UUID loanApplicationId;
    private String loanContractId;
    /** The loan as the collateral list shows it (contract, enquiry, borrower, project). */
    private LoanSummaryDto loan;
    /** Major Collateral Type of the loan (1 Others, 2 CRE, 3 CRE-RH); null until set. */
    private String majorCollateralType;
    /** The collateral checklist of the loan, when it exists (the evaluations are approved with it). */
    private UUID checklistId;
    private Integer checklistWorkFlowStatusCode;
    private String checklistWorkFlowStatusDescription;
    /** False while the checklist waits for approval: evaluations cannot be created, changed or deleted then. */
    private boolean changeable = true;
    private List<ProjectEvaluation> evaluations = new ArrayList<>();

    public UUID getLoanApplicationId() { return loanApplicationId; }
    public void setLoanApplicationId(UUID loanApplicationId) { this.loanApplicationId = loanApplicationId; }
    public String getLoanContractId() { return loanContractId; }
    public void setLoanContractId(String loanContractId) { this.loanContractId = loanContractId; }
    public LoanSummaryDto getLoan() { return loan; }
    public void setLoan(LoanSummaryDto loan) { this.loan = loan; }
    public String getMajorCollateralType() { return majorCollateralType; }
    public void setMajorCollateralType(String majorCollateralType) { this.majorCollateralType = majorCollateralType; }
    public UUID getChecklistId() { return checklistId; }
    public void setChecklistId(UUID checklistId) { this.checklistId = checklistId; }
    public Integer getChecklistWorkFlowStatusCode() { return checklistWorkFlowStatusCode; }
    public void setChecklistWorkFlowStatusCode(Integer code) { this.checklistWorkFlowStatusCode = code; }
    public String getChecklistWorkFlowStatusDescription() { return checklistWorkFlowStatusDescription; }
    public void setChecklistWorkFlowStatusDescription(String text) { this.checklistWorkFlowStatusDescription = text; }
    public boolean isChangeable() { return changeable; }
    public void setChangeable(boolean changeable) { this.changeable = changeable; }
    public List<ProjectEvaluation> getEvaluations() { return evaluations; }
    public void setEvaluations(List<ProjectEvaluation> evaluations) { this.evaluations = evaluations; }
}
