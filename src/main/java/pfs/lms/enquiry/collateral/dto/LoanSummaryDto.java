package pfs.lms.enquiry.collateral.dto;

import java.util.UUID;

/** The loan a collateral list belongs to, as shown in the page headers. */
public class LoanSummaryDto {

    private UUID loanApplicationId;
    private String loanContractId;
    private Long enquiryNo;
    private String projectName;
    private String borrowerName;
    private String borrowerNumber;
    private String functionalStatusDescription;

    public LoanSummaryDto() {
    }

    public UUID getLoanApplicationId() { return loanApplicationId; }
    public void setLoanApplicationId(UUID loanApplicationId) { this.loanApplicationId = loanApplicationId; }

    public String getLoanContractId() { return loanContractId; }
    public void setLoanContractId(String loanContractId) { this.loanContractId = loanContractId; }

    public Long getEnquiryNo() { return enquiryNo; }
    public void setEnquiryNo(Long enquiryNo) { this.enquiryNo = enquiryNo; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public String getBorrowerNumber() { return borrowerNumber; }
    public void setBorrowerNumber(String borrowerNumber) { this.borrowerNumber = borrowerNumber; }

    public String getFunctionalStatusDescription() { return functionalStatusDescription; }
    public void setFunctionalStatusDescription(String functionalStatusDescription) { this.functionalStatusDescription = functionalStatusDescription; }
}
