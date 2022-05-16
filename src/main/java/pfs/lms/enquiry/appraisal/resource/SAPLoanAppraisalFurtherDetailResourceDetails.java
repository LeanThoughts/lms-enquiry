package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalFurtherDetailResourceDetails {

    @JsonProperty(value = "Id")
    private String Id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "FurtherDetails")
    private String furtherDetails;

    @JsonProperty(value = "Date")
    private String date;






}
