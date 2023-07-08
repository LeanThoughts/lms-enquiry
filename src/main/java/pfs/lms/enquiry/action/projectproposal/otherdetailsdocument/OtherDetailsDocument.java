package pfs.lms.enquiry.action.projectproposal.otherdetailsdocument;

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
public class OtherDetailsDocument extends AggregateRoot<OtherDetailsDocument> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private String documentType;
    private String documentName;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
