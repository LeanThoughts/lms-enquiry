package pfs.lms.enquiry.action.projectproposal.otherdetailsdocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

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

        // Change Documents for Other Details Document
        changeDocumentService.createChangeDocument(
                otherDetailsDocument.getProjectProposal().getId(),
                otherDetailsDocument.getId().toString(),
                otherDetailsDocument.getProjectProposal().getId().toString(),
                otherDetailsDocument.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                otherDetailsDocument,
                "Created",
                username,
                "EnquiryAction", "Other Details Document" );

        return otherDetailsDocument;
    }

    @Override
    public OtherDetailsDocument update(OtherDetailsDocumentResource resource, String username)
            throws CloneNotSupportedException {

        OtherDetailsDocument otherDetailsDocument =
                otherDetailsDocumentRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

          Object oldObject = otherDetailsDocument.clone();

        otherDetailsDocument.setDocumentName(resource.getDocumentName());
        otherDetailsDocument.setDocumentType(resource.getDocumentType());
        otherDetailsDocument.setFileReference(resource.getFileReference());
        otherDetailsDocument = otherDetailsDocumentRepository.save(otherDetailsDocument);

        // Change Documents for Other Details Document
        changeDocumentService.createChangeDocument(
                otherDetailsDocument.getProjectProposal().getId(),
                otherDetailsDocument.getId().toString(),
                otherDetailsDocument.getProjectProposal().getId().toString(),
                otherDetailsDocument.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                oldObject,
                otherDetailsDocument,
                "Updated",
                username,
                "EnquiryAction", "Other Details Document" );

        return otherDetailsDocument;
    }

    @Override
    public OtherDetailsDocument delete(UUID otherDetailsDocumentId, String username) {
        OtherDetailsDocument otherDetailsDocument = otherDetailsDocumentRepository.findById(otherDetailsDocumentId)
                .orElseThrow(() -> new EntityNotFoundException(otherDetailsDocumentId.toString()));
        otherDetailsDocumentRepository.delete(otherDetailsDocument);

        changeDocumentService.createChangeDocument(
                otherDetailsDocument.getId(),
                otherDetailsDocument.getId().toString(),
                otherDetailsDocument.getId().toString(),
                otherDetailsDocument.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                otherDetailsDocument,
                "Deleted",
                username,
                "EnquiryAction", "Other Details Document");

        return  otherDetailsDocument;
    }
}
