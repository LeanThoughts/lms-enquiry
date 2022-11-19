package pfs.lms.enquiry.action.projectproposal.projectdetail;

import lombok.*;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"projectProposal"}, callSuper = false)
public class ProjectDetail extends AggregateRoot<ProjectDetail> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private String projectName;
    private String borrowerName;
    private String promoterName;
    private String loanPurpose;
    private Double projectCapacity;
    private String projectCapacityUnit;
    private String state;
    private String district;
    private String productType;
    private String loanClass;
    private String assistanceType; // (Loan Purpose)
    private String financingType;
    private String projectType;
    private String projectCoreSector;
    private String renewableFlag;
    private String policyExposure;
    private String endUseOfFunds;
    private Double fees;
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
