package pfs.lms.enquiry.documentation.llcfee;

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
public class LLCFeeService implements ILLCFeeService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentationRepository documentationRepository;
    private final LLCFeeRepository LLCFeeRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public LLCFee create(LLCFeeResource resource, String username) {
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
        LLCFee llcFee = new LLCFee();
        llcFee.setDocumentation(documentation);
        llcFee.setSerialNumber(LLCFeeRepository.findByDocumentationId(documentation.getId()).size() + 1);
        llcFee.setInvoiceDate(resource.getInvoiceDate());
        llcFee.setInvoiceNumber(resource.getInvoiceNumber());
        llcFee.setFeeName(resource.getFeeName());
        llcFee.setStatusOfFeeReceipt(resource.getStatusOfFeeReceipt());
        llcFee.setFeeAmount(resource.getFeeAmount());
        llcFee.setRemarks(resource.getRemarks());
        llcFee.setDocumentType(resource.getDocumentType());
        llcFee.setDocumentName(resource.getDocumentName());
        llcFee.setFileReference(resource.getFileReference());
        llcFee.setDeleteFlag(false);
        llcFee = LLCFeeRepository.save(llcFee);

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

        return llcFee;
    }

    @Override
    public LLCFee update(LLCFeeResource resource, String username) throws CloneNotSupportedException {
        LLCFee llcFee =
                LLCFeeRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = llcFee.clone();

        llcFee.setInvoiceDate(resource.getInvoiceDate());
        llcFee.setInvoiceNumber(resource.getInvoiceNumber());
        llcFee.setFeeName(resource.getFeeName());
        llcFee.setStatusOfFeeReceipt(resource.getStatusOfFeeReceipt());
        llcFee.setFeeAmount(resource.getFeeAmount());
        llcFee.setRemarks(resource.getRemarks());
        llcFee.setDocumentType(resource.getDocumentType());
        llcFee.setDocumentName(resource.getDocumentName());
        llcFee.setFileReference(resource.getFileReference());
        llcFee = LLCFeeRepository.save(llcFee);

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

        return llcFee;
    }

    @Override
    public LLCFee delete(UUID legalCounselId, String username) throws CloneNotSupportedException {
        LLCFee llcFee = LLCFeeRepository.findById(legalCounselId).
                orElseThrow(() -> new EntityNotFoundException(legalCounselId.toString()));
        llcFee.setDeleteFlag(true);
        llcFee = LLCFeeRepository.save(llcFee);

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
        return llcFee;
    }
}
