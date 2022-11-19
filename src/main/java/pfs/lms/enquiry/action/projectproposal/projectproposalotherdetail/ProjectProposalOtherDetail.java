package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

import lombok.*;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"projectProposal"}, callSuper = false)
public class ProjectProposalOtherDetail extends AggregateRoot<ProjectProposalOtherDetail> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private String sourceAndCashFlow;
    private LocalDate optimumDateOfLoan;
    private String consolidatedGroupLeverage;
    private Double totalDebtTNW;
    private Double tolTNW;
    private Double totalDebtTNWPercentage;
    private Double tolTNWPercentage;
    private String delayInDebtServicing;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
