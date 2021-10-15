package pfs.lms.enquiry.appraisal.proposaldetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProposalDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private Double rateOfInterestPreCod;
    private Double rateOfInterestPostCod;
    private Integer spreadReset;
    private String spreadResetUnit;
    private Double effectiveRateOfInterest;
    private Integer constructionPeriod;
    private String constructionPeriodUnit;
    private Integer moratoriumPeriod;
    private String moratoriumPeriodUnit;
    private Integer repaymentPeriod;
    private String repaymentPeriodUnit;
    private Integer tenor;
    private String tenorUnit;
    private Integer availabilityPeriod;
    private String availabilityPeriodUnit;
    private Double prePaymentCharges;
    private Double feeDetailsSchedule;
}
