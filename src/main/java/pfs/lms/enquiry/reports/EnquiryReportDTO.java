package pfs.lms.enquiry.reports;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@Data
public class EnquiryReportDTO {

    private Integer serialNumber;
    private String processStatus;
    private String status;
    private LocalDate enquiryDate;
    private String enquiryNumber;
    private String loanContractNumber;
    private String businesPartnerNumber;
    private String businessPartnerName;
    private String projectName;
    private String groupName;
    private String loanClass;
    private String projectType;
    private String projectCapacity;
    private String loanType;
    private Double projectCostInCR;
    private Double loanAmountRequest;
    private String roiRequestedBy;
    private String createdBy;
    private String createdAt;
    private String approvedBy;
    private String approvalDate;
    private String iccClearanceStatus;



}
