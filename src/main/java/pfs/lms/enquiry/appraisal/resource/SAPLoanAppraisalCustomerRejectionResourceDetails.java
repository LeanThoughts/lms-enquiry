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

public class SAPLoanAppraisalCustomerRejectionResourceDetails {

    @JsonProperty(value = "Id")
    private String Id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "ReasonForRejection")
    private String reasonForRejection;

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "Category")
    private String category;




}
