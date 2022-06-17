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
public class TermLoanRiskRating extends AggregateRoot<TermLoanRiskRating> implements Cloneable {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

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

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
