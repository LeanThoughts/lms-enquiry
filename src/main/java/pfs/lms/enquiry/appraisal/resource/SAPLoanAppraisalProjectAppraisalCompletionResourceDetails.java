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
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;


    @JsonProperty(value = "Dateofprojectappraisalcomplete")
    private String dateOfProjectAppraisalCompletion;

    @JsonProperty(value = "Agendanoteapprovalbydira")
    private String agendaNoteApprovalByDirA;
    @JsonProperty(value = "Agendanoteapprovalbydirb")
    private String agendaNoteApprovalByDirB;
    @JsonProperty(value = "Agendanoteapprovalbymdandceo")
    private String agendaNoteApprovalByMDAndCEO;
    @JsonProperty(value = "Agendanotesubmissiontocosecy")
    private String agendaNoteSubmissionToCoSecy;
    @JsonProperty(value = "Remarks")
    private String remarks;



}
