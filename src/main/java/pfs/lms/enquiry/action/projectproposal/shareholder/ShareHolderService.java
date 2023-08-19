package pfs.lms.enquiry.action.projectproposal.shareholder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareHolderService implements IShareHolderService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final ShareHolderRepository shareHolderRepository;

    @Override
    public ShareHolder create(ShareHolderResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        ShareHolder shareHolder = new ShareHolder();
        shareHolder.setProjectProposal(projectProposal);
        shareHolder.setCompanyName(resource.getCompanyName());
        shareHolder.setEquityCapital(resource.getEquityCapital());
        shareHolder.setPercentageHolding(resource.getPercentageHolding());
        shareHolder = shareHolderRepository.save(shareHolder);

        // Change Documents for Share Holder
        changeDocumentService.createChangeDocument(
                shareHolder.getProjectProposal().getId(),
                shareHolder.getId().toString(),
                shareHolder.getProjectProposal().getId().toString(),
                shareHolder.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                projectProposal,
                "Created",
                username,
                "EnquiryAction", "Share Holder" );

        return shareHolder;
    }

    @Override
    public ShareHolder update(ShareHolderResource resource, String username)
            throws CloneNotSupportedException {

        ShareHolder shareHolder =
                shareHolderRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

         Object oldObject = shareHolder.clone();

        shareHolder.setCompanyName(resource.getCompanyName());
        shareHolder.setEquityCapital(resource.getEquityCapital());
        shareHolder.setPercentageHolding(resource.getPercentageHolding());
        shareHolder = shareHolderRepository.save(shareHolder);

        // Change Documents for Share Holder
        changeDocumentService.createChangeDocument(
                shareHolder.getProjectProposal().getId(),
                shareHolder.getId().toString(),
                shareHolder.getProjectProposal().getId().toString(),
                shareHolder.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                oldObject,
                shareHolder,
                "Updated",
                username,
                "EnquiryAction", "Share Holder" );

        return shareHolder;
    }

    @Override
    public ShareHolder delete(UUID shareHolderId, String username) {
        ShareHolder shareHolder = shareHolderRepository.findById(shareHolderId)
                .orElseThrow(() -> new EntityNotFoundException(shareHolderId.toString()));
        shareHolderRepository.delete(shareHolder);
        changeDocumentService.createChangeDocument(
                shareHolder.getId(),
                shareHolder.getId().toString(),
                shareHolder.getId().toString(),
                shareHolder.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                shareHolder,
                "Deleted",
                username,
                "EnquiryAction", "Share Holder");

        return  shareHolder;
    }
}
