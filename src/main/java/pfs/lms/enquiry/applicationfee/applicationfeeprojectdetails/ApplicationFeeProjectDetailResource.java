package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ApplicationFeeProjectDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private String projectName;
    private String promoterName;
    private String loanPurpose;
    private Double projectCapacity;
    private String projectCapacityUnit;
    private String state;
    private String productTypeCode;
    private String term;
    private LocalDate enquiryCompletionDate;
    private String loanType;
    private String loanClass;
    private String assistanceType;
    private String financingType;
    private String projectType;
    private String projectTypeCoreSector;
    private String purposeOfLoan;
    private Double projectCost;
    private Double debt;
    private Double promoterContributionEquity;
    private Double debtEquityRatio;
    private Double grantSubsidyAmount;
    private Double debtEquityRatioWithGrant;
    private Double pfsDebtAmount;
    private Double rateOfInterest;
    private Integer tenorYear;
    private Integer tenorMonths;
    private Integer moratoriumPeriod;
    private String moratoriumPeriodUnit;
    private Integer constructionPeriod;
    private String constructionPeriodUnit;
}
