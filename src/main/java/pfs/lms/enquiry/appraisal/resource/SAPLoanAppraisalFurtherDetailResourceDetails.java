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

public class SAPLoanAppraisalFurtherDetailResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String iccApprovalId;

    @JsonProperty(value = "Furtherdetails")
    private String furtherDetails;

    @JsonProperty(value = "Furtherdetailsdate")
    private String date;






}
