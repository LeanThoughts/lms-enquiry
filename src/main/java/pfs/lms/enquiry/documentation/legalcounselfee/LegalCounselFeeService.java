package pfs.lms.enquiry.documentation.legalcounselfee;

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
public class LegalCounselFeeService implements ILegalCounselFeeService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentationRepository documentationRepository;
    private final LegalCounselFeeRepository LegalCounselFeeRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public LegalCounselFee create(LegalCounselFeeResource resource, String username) {
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
        LegalCounselFee legalCounselFee = new LegalCounselFee();
        legalCounselFee.setDocumentation(documentation);
        legalCounselFee.setSerialNumber(LegalCounselFeeRepository.findByDocumentationId(documentation.getId()).size() + 1);
        legalCounselFee.setInvoiceDate(resource.getInvoiceDate());
        legalCounselFee.setInvoiceNumber(resource.getInvoiceNumber());
        legalCounselFee.setFeeName(resource.getFeeName());
        legalCounselFee.setStatusOfFeeReceipt(resource.getStatusOfFeeReceipt());
        legalCounselFee.setFeeAmount(resource.getFeeAmount());
        legalCounselFee.setRemarks(resource.getRemarks());
        legalCounselFee.setDocumentType(resource.getDocumentType());
        legalCounselFee.setDocumentName(resource.getDocumentName());
        legalCounselFee.setFileReference(resource.getFileReference());
        legalCounselFee.setDeleteFlag(false);
        legalCounselFee = LegalCounselFeeRepository.save(legalCounselFee);

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

        return legalCounselFee;
    }

    @Override
    public LegalCounselFee update(LegalCounselFeeResource resource, String username) throws CloneNotSupportedException {
        LegalCounselFee legalCounselFee =
                LegalCounselFeeRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = legalCounselFee.clone();

        legalCounselFee.setInvoiceDate(resource.getInvoiceDate());
        legalCounselFee.setInvoiceNumber(resource.getInvoiceNumber());
        legalCounselFee.setFeeName(resource.getFeeName());
        legalCounselFee.setStatusOfFeeReceipt(resource.getStatusOfFeeReceipt());
        legalCounselFee.setFeeAmount(resource.getFeeAmount());
        legalCounselFee.setRemarks(resource.getRemarks());
        legalCounselFee.setDocumentType(resource.getDocumentType());
        legalCounselFee.setDocumentName(resource.getDocumentName());
        legalCounselFee.setFileReference(resource.getFileReference());
        legalCounselFee = LegalCounselFeeRepository.save(legalCounselFee);

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

        return legalCounselFee;
    }

    @Override
    public LegalCounselFee delete(UUID legalCounselFeeId, String username) throws CloneNotSupportedException {
        LegalCounselFee legalCounselFee = LegalCounselFeeRepository.findById(legalCounselFeeId).
                orElseThrow(() -> new EntityNotFoundException(legalCounselFeeId.toString()));
        legalCounselFee.setDeleteFlag(true);
        legalCounselFee = LegalCounselFeeRepository.save(legalCounselFee);

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
        return legalCounselFee;
    }
}
