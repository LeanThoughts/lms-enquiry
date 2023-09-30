package pfs.lms.enquiry.sanction.sanctionletter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.sanction.Sanction;
import pfs.lms.enquiry.sanction.SanctionRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SanctionLetterService implements ISanctionLetterService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final SanctionRepository sanctionRepository;
    private final SanctionLetterRepository sanctionLetterRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public SanctionLetter create(SanctionLetterResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Sanction sanction = sanctionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Sanction obj = new Sanction();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = sanctionRepository.save(obj);
                    // Change Documents for Sanction Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Sanction", "Header");
                    return obj;
                });
        SanctionLetter sanctionLetter = new SanctionLetter();
        sanctionLetter.setSanction(sanction);
        sanctionLetter.setSerialNumber(sanctionLetterRepository.findBySanctionId(sanction.getId()).size() + 1);
        sanctionLetter.setSanctionLetterAcceptanceDate(resource.getSanctionLetterAcceptanceDate());
        sanctionLetter.setSanctionLetterIssueDate(resource.getSanctionLetterIssueDate());
        sanctionLetter.setBorrowerRequestLetterDate(resource.getBorrowerRequestLetterDate());
        sanctionLetter.setRemarks(resource.getRemarks());
        sanctionLetter.setDocumentTitle(resource.getDocumentTitle());
        sanctionLetter.setDocumentType(resource.getDocumentType());
        sanctionLetter.setFileReference(resource.getFileReference());
        sanctionLetter = sanctionLetterRepository.save(sanctionLetter);

         changeDocumentService.createChangeDocument(
                 sanctionLetter.getSanction().getId(),
                 sanctionLetter.getId().toString(),
                 sanctionLetter.getSanction().getId().toString(),
                 sanctionLetter.getSanction().getLoanApplication().getLoanContractId(),
                null,
                 sanctionLetter,
                "Created",
                username,
                "Sanction", "SanctionLetter" );

        return sanctionLetter;
    }

    @Override
    public SanctionLetter update(SanctionLetterResource resource, String username) throws CloneNotSupportedException {
        SanctionLetter sanctionLetter = sanctionLetterRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = sanctionLetter.clone();

        sanctionLetter.setSanctionLetterAcceptanceDate(resource.getSanctionLetterAcceptanceDate());
        sanctionLetter.setSanctionLetterIssueDate(resource.getSanctionLetterIssueDate());
        sanctionLetter.setBorrowerRequestLetterDate(resource.getBorrowerRequestLetterDate());
        sanctionLetter.setRemarks(resource.getRemarks());
        sanctionLetter.setDocumentTitle(resource.getDocumentTitle());
        sanctionLetter.setDocumentType(resource.getDocumentType());
        sanctionLetter.setFileReference(resource.getFileReference());
        sanctionLetter = sanctionLetterRepository.save(sanctionLetter);

        changeDocumentService.createChangeDocument(
                sanctionLetter.getSanction().getId(),
                sanctionLetter.getId().toString(),
                sanctionLetter.getSanction().getId().toString(),
                sanctionLetter.getSanction().getLoanApplication().getLoanContractId(),
                oldObject,
                sanctionLetter,
                "Updated",
                username,
                "Sanction", "SanctionLetter" );

        return sanctionLetter;
    }

    @Override
    public SanctionLetter delete(UUID sanctionLetterId, String username) throws CloneNotSupportedException {
        SanctionLetter sanctionLetter = sanctionLetterRepository
                .findById(sanctionLetterId)
                .orElseThrow(() -> new EntityNotFoundException(sanctionLetterId.toString()));
        sanctionLetterRepository.delete(sanctionLetter);
        changeDocumentService.createChangeDocument(
                sanctionLetter.getSanction().getId(),
                sanctionLetter.getId().toString(),
                sanctionLetter.getSanction().getId().toString(),
                sanctionLetter.getSanction().getLoanApplication().getLoanContractId(),
                null,
                sanctionLetter,
                "Deleted",
                username,
                "Sanction", "SanctionLetter" );
        return sanctionLetter;
    }
}
