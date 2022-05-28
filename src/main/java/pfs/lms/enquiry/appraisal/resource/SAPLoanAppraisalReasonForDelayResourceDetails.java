package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalReasonForDelayResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "Statusofproposal")
    private String statusofproposal;

    @JsonProperty(value = "RsnForDelayDate")
    private String rsnForDelayDate;

    @JsonProperty(value = "Heldby")
    private String heldby;

    @JsonProperty(value = "Status")
    private String status;



}
