package pfs.lms.enquiry.appraisal.riskrating;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TermLoanRiskRatingResource {

    private UUID id;
    private UUID loanApplicationId;

    private String year;
    private String financialRatio;
    private String approvalRisk;
    private String offTakeRisk;
    private String fuelRisk;
    private String reputationRisk;
    private String financingStructure;
    private String sponsorSupport;
    private String securityPackage;
    private String constructionRisk;
    private String exposure;
    private String technologyRisk;
    private String overallRisk;
}
