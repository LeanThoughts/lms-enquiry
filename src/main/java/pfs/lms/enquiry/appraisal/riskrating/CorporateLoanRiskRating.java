package pfs.lms.enquiry.appraisal.riskrating;

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
public class CorporateLoanRiskRating extends AggregateRoot<CorporateLoanRiskRating> implements Cloneable {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private String year;
    private String financialRatio;
    private String purposeOfLoan;
    private String financingStructure;
    private String repaymentCapability;
    private String corporateGovernancePractice;
    private String conductOfLoan;
    private String deviationWithOperationalPolicy;
    private String exposure;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
