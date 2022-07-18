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

public class SAPLoanAppraisalExternalRatingTermLoanResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "RatingYear")
    private String  ratingYear;

    @JsonProperty(value = "FinRatioStochProb")
    private String  finRatioStochProb;

    @JsonProperty(value = "ApprovalRisk")
    private String  approvalRisk;

    @JsonProperty(value = "OffTakeRelRisk")
    private String  offTakeRelRisk;

    @JsonProperty(value = "FuelAnalogRisk")
    private String  fuelAnalogRisk;

    @JsonProperty(value = "SignIssLeadRepRisk")
    private String  signIssLeadRepRisk;

    @JsonProperty(value = "FinancingStructure")
    private String  financingStructure;

    @JsonProperty(value = "SponsorSupport")
    private String  sponsorSupport;

    @JsonProperty(value = "SecurityPackage")
    private String  securityPackage;
    @JsonProperty(value = "ConstrRelRisk")
    private String  constrRelRisk;
    @JsonProperty(value = "ExposureConcentration")
    private String  exposureConcentration;
    @JsonProperty(value = "DesignTechRelRisk")
    private String  designTechRelRisk;
    @JsonProperty(value = "OverallRiskRating")
    private String  overallRiskRating;


}
