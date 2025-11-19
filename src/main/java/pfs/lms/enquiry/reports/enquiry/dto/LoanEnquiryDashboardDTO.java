package pfs.lms.enquiry.reports.enquiry.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoanEnquiryDashboardDTO {

    public LoanEnquiryDashboardDTO() {
        enquiryPendingICCCount = 0;
        enquiryClearedByICCCount = 0;
        enquiryApprovedByBoardCount = 0;
        enquiryPendingICCAmount = 0D;
        enquiryClearedByICCAmount = 0D;
        enquiryApprovedByBoardAmount = 0D;
    }

    private LocalDate reportDate;

    private Integer enquiryPendingICCCount;
    private Integer enquiryClearedByICCCount;
    private Integer enquiryApprovedByBoardCount;

    private Double enquiryPendingICCAmount;
    private Double enquiryClearedByICCAmount;
    private Double enquiryApprovedByBoardAmount;



}
