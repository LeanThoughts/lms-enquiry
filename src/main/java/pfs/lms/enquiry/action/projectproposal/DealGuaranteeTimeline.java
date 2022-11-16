package pfs.lms.enquiry.action.projectproposal;

import lombok.*;
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
public class DealGuaranteeTimeline extends AggregateRoot<DealGuaranteeTimeline> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private String dealTransactionStructure;
    private String statusOfPBGAndMABG;
    private String timelinesMilestones;
    private String strengths;
    private String fundingArrangement;
    private String disbursementStageSchedule;
    private String offensesEnquiry;
    private String existingRelationsPFSPTC;
    private String deviations;
    private String esmsCategorization;
    private String otherProjectDetails;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
