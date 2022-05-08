package pfs.lms.enquiry.appraisal.proposaldetails;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDetail extends AggregateRoot<ProposalDetail> implements Cloneable {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

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

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
