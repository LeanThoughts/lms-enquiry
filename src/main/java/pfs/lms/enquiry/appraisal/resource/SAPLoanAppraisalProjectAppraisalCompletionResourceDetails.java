package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;

import java.time.LocalDate;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalProjectAppraisalCompletionResourceDetails {

    @JsonProperty(value = "Id")
    private String Id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;


    @JsonProperty(value = "DateOfProjectAppraisalCompletion")
    private String dateOfProjectAppraisalCompletion;

    @JsonProperty(value = "AgendaNoteApprovalByDirA")
    private String agendaNoteApprovalByDirA;
    @JsonProperty(value = "AgendaNoteApprovalByDirB")
    private String agendaNoteApprovalByDirB;
    @JsonProperty(value = "AgendaNoteApprovalByMDAndCEO")
    private String agendaNoteApprovalByMDAndCEO;
    @JsonProperty(value = "AgendaNoteSubmissionToCoSecy")
    private String agendaNoteSubmissionToCoSecy;
    @JsonProperty(value = "Remarks")
    private String remarks;



}
