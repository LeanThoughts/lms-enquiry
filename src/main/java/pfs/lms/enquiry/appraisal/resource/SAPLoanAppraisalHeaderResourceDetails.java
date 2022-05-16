package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalHeaderResourceDetails {

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "LoanContract")
    private String loanContract;

    @JsonProperty(value = "ProcessInstanceId")
    private String processInstanceId;

    @JsonProperty(value = "WorkflowStatusCode")
    private String workflowStatusCode;

    @JsonProperty(value = "WorkflowStatusDesc")
    private String workflowStatusDesc;




}
