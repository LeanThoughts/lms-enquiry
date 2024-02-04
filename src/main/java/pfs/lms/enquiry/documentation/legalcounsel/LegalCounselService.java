package pfs.lms.enquiry.documentation.legalcounsel;

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
public class LegalCounselService implements ILegalCounselService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentationRepository documentationRepository;
    private final LegalCounselRepository legalCounselRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public LegalCounsel create(LegalCounselResource resource, String username) {
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
        LegalCounsel legalCounsel = new LegalCounsel();
        legalCounsel.setDocumentation(documentation);
        legalCounsel.setSerialNumber(legalCounselRepository.findByDocumentationId(documentation.getId()).size() + 1);
        legalCounsel.setBpCode(resource.getBpCode());
        legalCounsel.setName(resource.getName());
        legalCounsel.setStartDate(resource.getStartDate());
        legalCounsel.setEndDate(resource.getEndDate());
        legalCounsel.setRemarks(resource.getRemarks());
        legalCounsel.setDocumentType(resource.getDocumentType());
        legalCounsel.setDocumentName(resource.getDocumentName());
        legalCounsel.setFileReference(resource.getFileReference());
        legalCounsel.setDeleteFlag(false);
        legalCounsel = legalCounselRepository.save(legalCounsel);

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

        return legalCounsel;
    }

    @Override
    public LegalCounsel update(LegalCounselResource resource, String username) throws CloneNotSupportedException {
        LegalCounsel legalCounsel =
                legalCounselRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = legalCounsel.clone();

        legalCounsel.setBpCode(resource.getBpCode());
        legalCounsel.setName(resource.getName());
        legalCounsel.setStartDate(resource.getStartDate());
        legalCounsel.setEndDate(resource.getEndDate());
        legalCounsel.setRemarks(resource.getRemarks());
        legalCounsel.setDocumentType(resource.getDocumentType());
        legalCounsel.setDocumentName(resource.getDocumentName());
        legalCounsel.setFileReference(resource.getFileReference());
        legalCounsel.setDeleteFlag(false);
        legalCounsel = legalCounselRepository.save(legalCounsel);

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

        return legalCounsel;
    }

    @Override
    public LegalCounsel delete(UUID legalCounselId, String username) throws CloneNotSupportedException {
        LegalCounsel legalCounsel = legalCounselRepository.findById(legalCounselId).
                orElseThrow(() -> new EntityNotFoundException(legalCounselId.toString()));
        legalCounsel.setDeleteFlag(true);
        legalCounsel = legalCounselRepository.save(legalCounsel);

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
        return legalCounsel;
    }
}
