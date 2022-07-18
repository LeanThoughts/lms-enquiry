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

public class SAPLoanAppraisalExternalRatingCorpLoanResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "RatingYear")
    private String  ratingYear;

    @JsonProperty(value = "FinRatioAnalysis")
    private String  finRatioAnalysis;

    @JsonProperty(value = "PurposeOfLoan")
    private String  purposeOfLoan;

    @JsonProperty(value = "FinStrucAndSecPackage")
    private String  finStrucAndSecPackage;

    @JsonProperty(value = "RepCapabBorrowerPromoter")
    private String  repCapabBorrowerPromoter;

    @JsonProperty(value = "CorGoverToRepRisk")
    private String  corGoverToRepRisk;

    @JsonProperty(value = "ConOfLoBanksAndFis")
    private String  conOfLoBanksAndFis;

    @JsonProperty(value = "DevOpPolicyGuide")
    private String  devOpPolicyGuide;

    @JsonProperty(value = "ExposureConcentration")
    private String  exposureConcentration;













}
