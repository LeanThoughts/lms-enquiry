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

public class SAPLoanAppraisalSyndicateConsortiumResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "Bankkey")
    private String bankkey;
    @JsonProperty(value = "Bankname")
    private String bankname;
    @JsonProperty(value = "Serialnumber")
    private String serialnumber;
    @JsonProperty(value = "Sanctionedamount")
    private String sanctionedamount;
    @JsonProperty(value = "Disbursedamount")
    private String disbursedamount;
    @JsonProperty(value = "Currency")
    private String currency;
    @JsonProperty(value = "Approvalstatus")
    private String approvalstatus;
    @JsonProperty(value = "Documentstage")
    private String documentstage;
    @JsonProperty(value = "Disbursementstatus")
    private String disbursementstatus;
    @JsonProperty(value = "Leadbank")
    private String leadbank;



}
