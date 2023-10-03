package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gguptha on 09/11/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPSanctionLetterDetailsResource {
    @JsonProperty (value = "CompanyCode")
    private String companyCode;
    @JsonProperty (value = "LoanContract")
    private String loanContract;
    @JsonProperty (value = "SerialNo")
    private String serialNo;

    @JsonProperty (value = "SanctionType")
    private String sanctionType;
    @JsonProperty (value = "AmendSancDate")
    private String amendSancDate;
    @JsonProperty (value = "SanctionIssueOfferDate")
    private String sanctionIssueOfferDate;
    @JsonProperty (value = "SanctionOfferDateUntil")
    private String sanctionOfferDateUntil;

    @JsonProperty (value = "SanctionOfferAcceptanceDate")
    private String sanctionOfferAcceptanceDate;
    @JsonProperty (value = "SanctionOfferAcceptedFlag")
    private String sanctionOfferAcceptedFlag;
    @JsonProperty (value = "OriginalSanctionAmount")
    private String originalSanctionAmount;
    @JsonProperty (value = "RevisedSanctionAmount")
    private String revisedSanctionAmount;

    @JsonProperty (value = "OriginalInterestRate")
    private String originalInterestRate;
    @JsonProperty (value = "RevisedInterestDate")
    private String revisedInterestDate;

    @JsonProperty (value = "DocumentType")
    private String documentType;
    @JsonProperty (value = "DocumentTypeDesc")
    private String documentTypeDesc;
    @JsonProperty (value = "DocumentId")
    private String documentId;

    @JsonProperty (value = "Remarks")
    private String remarks;
}
