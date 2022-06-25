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

public class SAPLoanAppraisalKYCResourceDetails {
    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "Loanpartnerid")
    private String  loanpartnerid;

    @JsonProperty(value = "Documenttype")
    private String  documenttype;

    @JsonProperty(value = "Documentname")
    private String  documentname;

    @JsonProperty(value = "Filereference")
    private String filereference;

    @JsonProperty(value = "Remarks")
    private String remarks;

    @JsonProperty(value = "Dateofcompletion")
    private String  dateOfCompletion;

}
