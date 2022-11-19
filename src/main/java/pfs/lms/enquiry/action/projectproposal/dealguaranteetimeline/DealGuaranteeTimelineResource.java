package pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DealGuaranteeTimelineResource {

    private UUID id;
    private UUID projectProposalId;

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
}
