package pfs.lms.enquiry.businesspartner.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SAPBusinessPartnerLoanContactResourceDetail {

    @JsonProperty(value = "Businesspartner")
    private String busPartnerNumber;

    @JsonProperty(value = "Slno")
    private String serialNumber;

    @JsonProperty(value = "Sel")
    private String selection;
    @JsonProperty(value = "LoanNumber")
    private String loanNumber;
    @JsonProperty(value = "Name")
    private String name;
    @JsonProperty(value = "BranchAddr")
    private String branchAddress;
    @JsonProperty(value = "Designation")
    private String designation;
    @JsonProperty(value = "Department")
    private String department;
    @JsonProperty(value = "TelNumber")
    private String telephoneNumber;
    @JsonProperty(value = "LandNumber")
    private String landLineNumber;
    @JsonProperty(value = "SmtpAddr")
    private String email;
    @JsonProperty(value = "FaxNumber")
    private String faxNumber;

}
