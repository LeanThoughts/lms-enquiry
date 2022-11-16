package pfs.lms.enquiry.action.projectproposal.collateraldetail;

import lombok.*;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"projectProposal"}, callSuper = false)
public class CollateralDetail extends AggregateRoot<CollateralDetail> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private String collateralType;
    private String details;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
