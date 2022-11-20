package pfs.lms.enquiry.action.projectproposal.otherdetailsdocuent;

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
public class OtherDetailsDocumentService implements IOtherDetailsDocumentService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final OtherDetailsDocumentRepository otherDetailsDocumentRepository;

    @Override
    public OtherDetailsDocument create(OtherDetailsDocumentResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        OtherDetailsDocument otherDetailsDocument = new OtherDetailsDocument();
        otherDetailsDocument.setProjectProposal(projectProposal);
        otherDetailsDocument.setDocumentName(resource.getDocumentName());
        otherDetailsDocument.setDocumentType(resource.getDocumentType());
        otherDetailsDocument.setFileReference(resource.getFileReference());
        otherDetailsDocument = otherDetailsDocumentRepository.save(otherDetailsDocument);

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

        return otherDetailsDocument;
    }

    @Override
    public OtherDetailsDocument update(OtherDetailsDocumentResource resource, String username)
            throws CloneNotSupportedException {

        OtherDetailsDocument otherDetailsDocument =
                otherDetailsDocumentRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        // Object oldRejectByPFS = projectProposal.clone();

        otherDetailsDocument.setDocumentName(resource.getDocumentName());
        otherDetailsDocument.setDocumentType(resource.getDocumentType());
        otherDetailsDocument.setFileReference(resource.getFileReference());
        otherDetailsDocument = otherDetailsDocumentRepository.save(otherDetailsDocument);

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

        return otherDetailsDocument;
    }
}
