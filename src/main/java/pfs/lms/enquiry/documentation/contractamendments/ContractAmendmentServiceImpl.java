package pfs.lms.enquiry.documentation.contractamendments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;
import pfs.lms.enquiry.businesspartner.repository.SanctionAuthorityRepository;
import pfs.lms.enquiry.documentation.Documentation;
import pfs.lms.enquiry.documentation.DocumentationRepository;
import pfs.lms.enquiry.documentation.legalcounsel.LegalCounsel;
import pfs.lms.enquiry.documentation.llcfee.LLCFee;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ContractAmendmentServiceImpl  implements  IContractAmendmentService{
    private final SanctionAuthorityRepository sanctionAuthorityRepository;

    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentationRepository documentationRepository;
    private final ContractAmendmentRepository contractAmendmentRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ContractAmendment create(ContractAmendmentResource resource, String username) {
        SanctionAuthority sanctionAuthority = new SanctionAuthority();

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Documentation documentation = documentationRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Documentation obj = new Documentation();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = documentationRepository.save(obj);
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Documentation", "Header");
                    return obj;
                });
        if ( resource.getSanctioningAuthorityCode() != null ) {
              sanctionAuthority = sanctionAuthorityRepository.findByCode(resource.getSanctioningAuthorityCode());
        }
        ContractAmendment contractAmendment = new ContractAmendment();
        contractAmendment.setDocumentation(documentation);
        contractAmendment.setSerialNumber(resource.getSerialNumber());
        contractAmendment.setApprovalDate(resource.getApprovalDate());
        contractAmendment.setSanctionAuthority( sanctionAuthority );
        contractAmendment.setReasonForChange(resource.getReasonForChange());
        contractAmendment.setReferenceClausesOfContractAgreement(resource.getReferenceClausesOfContractAgreement());
        contractAmendment.setRemarks(resource.getRemarks());
        contractAmendment.setDocumentName(resource.getDocumentName());
        contractAmendment.setDocumentType(resource.getDocumentType());
        contractAmendment.setFileReference(resource.getFileReference());
        contractAmendment.setDeleteFlag(false);
        contractAmendment = contractAmendmentRepository.save(contractAmendment);


        changeDocumentService.createChangeDocument(
                contractAmendment.getId(),
                contractAmendment.getId().toString(),
                contractAmendment.getDocumentation().getId().toString(),
                contractAmendment.getDocumentation().getLoanApplication().getLoanContractId(),
                null,
                contractAmendment,
                "Created",
                username,
                "Documentation", "ContractAmendment" );
        return contractAmendment;
    }

    @Override
    public ContractAmendment update(ContractAmendmentResource resource, String username) throws CloneNotSupportedException {

        SanctionAuthority sanctionAuthority = new SanctionAuthority();

        ContractAmendment contractAmendment =
                contractAmendmentRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = contractAmendment.clone();

        if ( resource.getSanctioningAuthorityCode() != null ) {
            sanctionAuthority = sanctionAuthorityRepository.findByCode(resource.getSanctioningAuthorityCode());
        }

        contractAmendment.setSerialNumber(resource.getSerialNumber());
        contractAmendment.setApprovalDate(resource.getApprovalDate());
        contractAmendment.setSanctionAuthority(sanctionAuthority);
        contractAmendment.setReasonForChange(resource.getReasonForChange());
        contractAmendment.setReferenceClausesOfContractAgreement(resource.getReferenceClausesOfContractAgreement());
        contractAmendment.setRemarks(resource.getRemarks());
        contractAmendment.setDocumentName(resource.getDocumentName());
        contractAmendment.setDocumentType(resource.getDocumentType());
        contractAmendment.setFileReference(resource.getFileReference());
        contractAmendment.setDeleteFlag(false);
        contractAmendment = contractAmendmentRepository.save(contractAmendment);

        changeDocumentService.createChangeDocument(
                contractAmendment.getId(),
                contractAmendment.getId().toString(),
                contractAmendment.getDocumentation().getId().toString(),
                contractAmendment.getDocumentation().getLoanApplication().getLoanContractId(),
                oldObject,
                contractAmendment,
                "Updated",
                username,
                "Documentation", "ContractAmendment" );
        return contractAmendment;
    }

    @Override
    public ContractAmendment delete(UUID contractAmendmentId, String username) throws CloneNotSupportedException {
        ContractAmendment contractAmendment = contractAmendmentRepository.findById(contractAmendmentId).
                orElseThrow(() -> new EntityNotFoundException(contractAmendmentId.toString()));
        contractAmendment.setDeleteFlag(true);
        contractAmendment = contractAmendmentRepository.save(contractAmendment);
        changeDocumentService.createChangeDocument(
                contractAmendment.getId(),
                contractAmendment.getId().toString(),
                contractAmendment.getDocumentation().getId().toString(),
                contractAmendment.getDocumentation().getLoanApplication().getLoanContractId(),
                null,
                contractAmendment,
                "Deleted",
                username,
                "Documentation", "ContractAmendment" );
        return contractAmendment;
    }
}
