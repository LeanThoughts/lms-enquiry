package pfs.lms.enquiry.enquiriesexcelupload;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
public class ExcelEnquiry extends AggregateRoot<ExcelEnquiry> {

    private Long serialNumber;
    private Long sapEnquiryId;

    private String borrowerName;
    private String groupName;
    private String projectType;
    private String typeOfAssistance;
    private String proposalType;
    private String iccReadinessStatus;
    private String remarksOnIccReadiness;
    private String presentedInIcc;
    private String iccStatus;
    private String reasonForIccStatus;
    private String iccMeetingNumber;
    private String remarksForIccApproval;

    private LocalDate dateOfLeadGeneration;
    private LocalDate iccClearanceDate;

    private Double amountRequested;
    private Double borrowerRequestedROI;
    private Double amountApproved;
    private Double iccApprovedRoi;

    private String comments;
}
