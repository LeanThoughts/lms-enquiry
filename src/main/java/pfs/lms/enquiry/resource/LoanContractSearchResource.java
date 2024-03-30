package pfs.lms.enquiry.resource;

import lombok.Data;

@Data
public class LoanContractSearchResource {
    //private LocalDate enquiryDateFrom;
    //private LocalDate enquiryDateTo;
    private String accountStatus;
    private String partyName;
    private String loanClass;
    private String  financingType;
    private Integer borrowerCodeFrom;
    private Integer borrowerCodeTo;
    private Integer loanNumberFrom;
    private Integer loanNumberTo;
    private String projectLocationState;
    private String projectType;
    private String assistanceType;
    private String technicalStatus;
    private String enquiryNumber;
}
