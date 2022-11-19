package pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealGuaranteeTimelineService implements IDealGuaranteeTimelineService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final DealGuaranteeTimelineRepository dealGuaranteeTimelineRepository;

    @Override
    public DealGuaranteeTimeline create(DealGuaranteeTimelineResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        DealGuaranteeTimeline dealGuaranteeTimeline = new DealGuaranteeTimeline();
        dealGuaranteeTimeline.setProjectProposal(projectProposal);
        dealGuaranteeTimeline.setDealTransactionStructure(resource.getDealTransactionStructure());
        dealGuaranteeTimeline.setDeviations(resource.getDeviations());
        dealGuaranteeTimeline.setDisbursementStageSchedule(resource.getDisbursementStageSchedule());
        dealGuaranteeTimeline.setEsmsCategorization(resource.getEsmsCategorization());
        dealGuaranteeTimeline.setExistingRelationsPFSPTC(resource.getExistingRelationsPFSPTC());
        dealGuaranteeTimeline.setFundingArrangement(resource.getFundingArrangement());
        dealGuaranteeTimeline.setOffensesEnquiry(resource.getOffensesEnquiry());
        dealGuaranteeTimeline.setOtherProjectDetails(resource.getOtherProjectDetails());
        dealGuaranteeTimeline.setStatusOfPBGAndMABG(resource.getStatusOfPBGAndMABG());
        dealGuaranteeTimeline.setStrengths(resource.getStrengths());
        dealGuaranteeTimeline.setTimelinesMilestones(resource.getTimelinesMilestones());
        dealGuaranteeTimeline = dealGuaranteeTimelineRepository.save(dealGuaranteeTimeline);

        // Change Documents for Project Proposal
//        changeDocumentService.createChangeDocument(
//                projectProposal.getEnquiryAction().getId(),
//                projectProposal.getId().toString(),
//                projectProposal.getEnquiryAction().getId().toString(),
//                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                null,
//                projectProposal,
//                "Created",
//                username,
//                "EnquiryAction", "Project Proposal" );

        return dealGuaranteeTimeline;
    }

    @Override
    public DealGuaranteeTimeline update(DealGuaranteeTimelineResource resource, String username)
            throws CloneNotSupportedException {

        DealGuaranteeTimeline dealGuaranteeTimeline =
                dealGuaranteeTimelineRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        // Object oldRejectByPFS = projectProposal.clone();

        dealGuaranteeTimeline.setDealTransactionStructure(resource.getDealTransactionStructure());
        dealGuaranteeTimeline.setDeviations(resource.getDeviations());
        dealGuaranteeTimeline.setDisbursementStageSchedule(resource.getDisbursementStageSchedule());
        dealGuaranteeTimeline.setEsmsCategorization(resource.getEsmsCategorization());
        dealGuaranteeTimeline.setExistingRelationsPFSPTC(resource.getExistingRelationsPFSPTC());
        dealGuaranteeTimeline.setFundingArrangement(resource.getFundingArrangement());
        dealGuaranteeTimeline.setOffensesEnquiry(resource.getOffensesEnquiry());
        dealGuaranteeTimeline.setOtherProjectDetails(resource.getOtherProjectDetails());
        dealGuaranteeTimeline.setStatusOfPBGAndMABG(resource.getStatusOfPBGAndMABG());
        dealGuaranteeTimeline.setStrengths(resource.getStrengths());
        dealGuaranteeTimeline.setTimelinesMilestones(resource.getTimelinesMilestones());
        dealGuaranteeTimeline = dealGuaranteeTimelineRepository.save(dealGuaranteeTimeline);

        // Change Documents for Project Proposal
//        changeDocumentService.createChangeDocument(
//                projectProposal.getEnquiryAction().getId(),
//                projectProposal.getId().toString(),
//                projectProposal.getEnquiryAction().getId().toString(),
//                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                oldRejectByPFS,
//                projectProposal,
//                "Updated",
//                username,
//                "EnquiryAction", "Project Proposal" );

        return dealGuaranteeTimeline;
    }
}
