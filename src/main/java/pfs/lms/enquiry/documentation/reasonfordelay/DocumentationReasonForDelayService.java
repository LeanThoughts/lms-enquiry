package pfs.lms.enquiry.documentation.reasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.documentation.Documentation;
import pfs.lms.enquiry.documentation.DocumentationRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DocumentationReasonForDelayService implements IDocumentationReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentationRepository documentationRepository;
    private final DocumentationReasonForDelayRepository documentationReasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public DocumentationReasonForDelay createReasonForDelay(DocumentationReasonForDelayResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Documentation documentation = documentationRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Documentation obj = new Documentation();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = documentationRepository.save(obj);
                    // Change Documents for Sanction Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "Sanction", "Header");
                    return obj;
                });
        DocumentationReasonForDelay documentationReasonForDelay = new DocumentationReasonForDelay();
        documentationReasonForDelay.setDocumentation(documentation);
        documentationReasonForDelay.setSerialNumber(documentationReasonForDelayRepository.
                findByDocumentationId(documentation.getId()).size() + 1);
        documentationReasonForDelay.setDate(resource.getDate());
        documentationReasonForDelay.setReason(resource.getReason());
        documentationReasonForDelay.setDeleteFlag(false);
        documentationReasonForDelay = documentationReasonForDelayRepository.save(documentationReasonForDelay);

        // Change Documents for SanctionReasonForDelay
//        changeDocumentService.createChangeDocument(
//                documentationReasonForDelay.getDocumentation().getId(),
//                documentationReasonForDelay.getId().toString(),
//                documentationReasonForDelay.getDocumentation().getId().toString(),
//                documentationReasonForDelay.getDocumentation().getLoanApplication().getLoanContractId(),
//                null,
//                documentationReasonForDelay,
//                "Created",
//                username,
//                "Sanction", "SanctionReasonForDelay" );

        return documentationReasonForDelay;
    }

    @Override
    public DocumentationReasonForDelay updateReasonForDelay(DocumentationReasonForDelayResource resource, String username) throws CloneNotSupportedException {
        DocumentationReasonForDelay documentationReasonForDelay =
                documentationReasonForDelayRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = documentationReasonForDelay.clone();

        documentationReasonForDelay.setDate(resource.getDate());
        documentationReasonForDelay.setReason(resource.getReason());
        documentationReasonForDelay = documentationReasonForDelayRepository.save(documentationReasonForDelay);

        // Change Documents for SanctionReasonForDelay
//        changeDocumentService.createChangeDocument(
//                documentationReasonForDelay.getDocumentation().getId(),
//                documentationReasonForDelay.getId().toString(),
//                documentationReasonForDelay.getDocumentation().getId().toString(),
//                documentationReasonForDelay.getDocumentation().getLoanApplication().getLoanContractId(),
//                oldObject,
//                documentationReasonForDelay,
//                "Updated",
//                username,
//                "Sanction", "SanctionReasonForDelay" );

        return documentationReasonForDelay;
    }

    @Override
    public DocumentationReasonForDelay deleteReasonForDelay(UUID documentationReasonForDelayId, String username) throws CloneNotSupportedException {
        DocumentationReasonForDelay documentationReasonForDelay = documentationReasonForDelayRepository.findById(documentationReasonForDelayId).
                orElseThrow(() -> new EntityNotFoundException(documentationReasonForDelayId.toString()));
        documentationReasonForDelay.setDeleteFlag(true);
        documentationReasonForDelay = documentationReasonForDelayRepository.save(documentationReasonForDelay);

        // Change Documents for SanctionReasonForDelay
//        changeDocumentService.createChangeDocument(
//                documentationReasonForDelay.getDocumentation().getId(),
//                documentationReasonForDelay.getId().toString(),
//                documentationReasonForDelay.getDocumentation().getId().toString(),
//                documentationReasonForDelay.getDocumentation().getLoanApplication().getLoanContractId(),
//                null,
//                documentationReasonForDelay,
//                "Deleted",
//                username,
//                "Sanction", "SanctionReasonForDelay" );
        return documentationReasonForDelay;
    }
}
