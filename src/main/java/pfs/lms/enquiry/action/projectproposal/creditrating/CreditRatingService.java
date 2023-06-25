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

        // Change Documents for Credit Rating Resource
        changeDocumentService.createChangeDocument(
                creditRating.getProjectProposal().getEnquiryAction().getId(),
                creditRating.getId().toString(),
                creditRating.getProjectProposal().getId().toString(),
                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
                null,
                creditRating,
                "Created",
                username,
                "EnquiryAction", "Credit Rating" );

        return creditRating;
    }

    @Override
    public CreditRating update(CreditRatingResource resource, String username)
            throws CloneNotSupportedException {

        CreditRating creditRating =
                creditRatingRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

      Object oldCreditRating = creditRating.clone();

        creditRating.setCreditRatingAgency(resource.getCreditRatingAgency());
        creditRating.setCreditRating(resource.getCreditRating());
        creditRating.setCreditStandingInstruction(resource.getCreditStandingInstruction());
        creditRating.setCreditStandingText(resource.getCreditStandingText());
        creditRating = creditRatingRepository.save(creditRating);

         // Change Documents for Credit Rating
        changeDocumentService.createChangeDocument(
                creditRating.getProjectProposal().getId(),
                creditRating.getId().toString(),
                creditRating.getProjectProposal().getId().toString(),
                creditRating.getProjectProposal().getEnquiryAction().getLoanApplication().getLoanContractId(),
                oldCreditRating,
                creditRating,
                "Updated",
                username,
                "EnquiryAction", "Credit Rating" );

        return creditRating;
    }
}
