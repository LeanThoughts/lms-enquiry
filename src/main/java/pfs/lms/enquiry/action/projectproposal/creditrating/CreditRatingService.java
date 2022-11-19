package pfs.lms.enquiry.action.projectproposal.creditrating;

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
public class CreditRatingService implements ICreditRatingService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final CreditRatingRepository creditRatingRepository;

    @Override
    public CreditRating create(CreditRatingResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        CreditRating creditRating = new CreditRating();
        creditRating.setProjectProposal(projectProposal);
        creditRating.setCreditRatingAgency(resource.getCreditRatingAgency());
        creditRating.setCreditRating(resource.getCreditRating());
        creditRating.setCreditStandingInstruction(resource.getCreditStandingInstruction());
        creditRating.setCreditStandingText(resource.getCreditStandingText());
        creditRating = creditRatingRepository.save(creditRating);

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

        return creditRating;
    }

    @Override
    public CreditRating update(CreditRatingResource resource, String username)
            throws CloneNotSupportedException {

        CreditRating creditRating =
                creditRatingRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        // Object oldRejectByPFS = projectProposal.clone();

        creditRating.setCreditRatingAgency(resource.getCreditRatingAgency());
        creditRating.setCreditRating(resource.getCreditRating());
        creditRating.setCreditStandingInstruction(resource.getCreditStandingInstruction());
        creditRating.setCreditStandingText(resource.getCreditStandingText());
        creditRating = creditRatingRepository.save(creditRating);

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

        return creditRating;
    }
}
