package pfs.lms.enquiry.action.projectproposal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimelineRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectProposalService implements IProjectProposalService {

    private final IChangeDocumentService changeDocumentService;

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final ProjectProposalRepository projectProposalRepository;
    private final DealGuaranteeTimelineRepository dealGuaranteeTimelineRepository;

//    @Override
//    public ProjectProposalResource getProjectProposal(UUID enquiryActionId) {
//        ProjectProposal projectProposal = projectProposalRepository.findByEnquiryActionId(enquiryActionId)
//                .orElseGet(() -> {
//                    return null;
//                });
//        ProjectProposalResource projectProposalResource = null;
//        if (projectProposal != null) {
//            projectProposalResource = new ProjectProposalResource();
//            projectProposalResource.setId(projectProposal.getId());
//            DealGuaranteeTimeline dealGuaranteeTimeline = dealGuaranteeTimelineRepository.
//                    findByProjectProposalId(projectProposal.getId());
//            projectProposalResource.setDealGuaranteeTimeline(dealGuaranteeTimeline);
//        }
//        return projectProposalResource;
//    }

    @Override
    public ProjectProposal create(ProjectProposalResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    EnquiryAction obj = new EnquiryAction();
                    obj.setLoanApplication(loanApplication);
                    obj = enquiryActionRepository.save(obj);
                    // Change Documents for EnquiryAction Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "EnquiryAction", "Header");
                    return obj;
                });
        ProjectProposal projectProposal = new ProjectProposal();
        projectProposal.setEnquiryAction(enquiryAction);
        projectProposal.setSerialNumber(projectProposalRepository.
                findByEnquiryActionId(enquiryAction.getId()).size() + 1);
        projectProposal.setAdditionalDetails(resource.getAdditionalDetails());
        projectProposal.setProposalFormSharingDate(resource.getProposalFormSharingDate());
        projectProposal.setDocumentName(resource.getDocumentName());
        projectProposal.setDocumentType(resource.getDocumentType());
        projectProposal.setDocumentVersion(resource.getDocumentVersion());
        projectProposal.setProposalStatus(resource.getProposalStatus());
        projectProposal = projectProposalRepository.save(projectProposal);

        // Change Documents for Project Proposal
        changeDocumentService.createChangeDocument(
                projectProposal.getEnquiryAction().getId(),
                projectProposal.getId().toString(),
                projectProposal.getEnquiryAction().getId().toString(),
                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
                null,
                projectProposal,
                "Created",
                username,
                "EnquiryAction", "Project Proposal" );

        return projectProposal;
    }

    @Override
    public ProjectProposal update(ProjectProposalResource resource, String username)
            throws CloneNotSupportedException {
        ProjectProposal projectProposal =
                projectProposalRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldRejectByPFS = projectProposal.clone();

        projectProposal.setAdditionalDetails(resource.getAdditionalDetails());
        projectProposal.setProposalFormSharingDate(resource.getProposalFormSharingDate());
        projectProposal.setDocumentName(resource.getDocumentName());
        projectProposal.setDocumentType(resource.getDocumentType());
        projectProposal.setDocumentVersion(resource.getDocumentVersion());
        projectProposal.setProposalStatus(resource.getProposalStatus());
        projectProposal = projectProposalRepository.save(projectProposal);

        // Change Documents for Project Proposal
        changeDocumentService.createChangeDocument(
                projectProposal.getEnquiryAction().getId(),
                projectProposal.getId().toString(),
                projectProposal.getEnquiryAction().getId().toString(),
                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
                oldRejectByPFS,
                projectProposal,
                "Updated",
                username,
                "EnquiryAction", "Project Proposal" );

        return projectProposal;
    }
}
