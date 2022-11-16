package pfs.lms.enquiry.action.projectproposal.projectcost;

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
public class ProjectCost extends AggregateRoot<ProjectCost> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private Double projectCost;
    private Double debt;
    private Double equity;
    private Double pfsDebtAmount;
    private Double debtEquityRatio;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
