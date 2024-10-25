package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import lombok.*;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"applicationFee", }, callSuper = false)
public class ApplicationFeeProjectDetail extends AggregateRoot<ApplicationFeeProjectDetail> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ApplicationFee applicationFee;

    private String projectName;
    private String promoterName;
    private String loanPurpose;
    private Double projectCapacity;
    private String projectCapacityUnit;
    private String state;
    private String productTypeCode;
    private Double term;
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

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
