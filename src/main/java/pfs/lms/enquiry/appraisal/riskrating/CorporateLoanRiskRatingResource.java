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
public class CorporateLoanRiskRatingResource {

    private UUID id;
    private UUID loanApplicationId;

    private String year;
    private String financialRatio;
    private String purposeOfLoan;
    private String financingStructure;
    private String repaymentCapability;
    private String corporateGovernancePractice;
    private String conductOfLoan;
    private String deviationWithOperationalPolicy;
    private String exposure;
}
