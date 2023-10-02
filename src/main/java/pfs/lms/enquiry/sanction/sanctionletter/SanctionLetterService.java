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
                    // Change Documents for Appraisal Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "Appraisal", "Header");
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
        sanctionLetter.setType(resource.getType());
        sanctionLetter.setDateOfAmendment(resource.getDateOfAmendment());
        sanctionLetter.setOriginalSanctionAmount(resource.getOriginalSanctionAmount());
        sanctionLetter.setOriginalInterestRate(resource.getOriginalInterestRate());
        sanctionLetter.setRevisedSanctionAmount(resource.getRevisedSanctionAmount());
        sanctionLetter.setRevisedInterestRate(resource.getRevisedInterestRate());
        sanctionLetter.setSanctionLetterValidToDate(resource.getSanctionLetterValidToDate());

        sanctionLetter = sanctionLetterRepository.save(sanctionLetter);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                boardApprovalReasonForDelay.getBoardApproval().getId(),
//                boardApprovalReasonForDelay.getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getLoanContractId(),
//                null,
//                boardApprovalReasonForDelay,
//                "Created",
//                username,
//                "Appraisal", "Reason For Delay" );

        return sanctionLetter;
    }

    @Override
    public SanctionLetter update(SanctionLetterResource resource, String username) throws CloneNotSupportedException {
        SanctionLetter sanctionLetter = sanctionLetterRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = sanctionLetter.clone();

        sanctionLetter.setSanctionLetterAcceptanceDate(resource.getSanctionLetterAcceptanceDate());
        sanctionLetter.setSanctionLetterIssueDate(resource.getSanctionLetterIssueDate());
        sanctionLetter.setBorrowerRequestLetterDate(resource.getBorrowerRequestLetterDate());
        sanctionLetter.setRemarks(resource.getRemarks());
        sanctionLetter.setDocumentTitle(resource.getDocumentTitle());
        sanctionLetter.setDocumentType(resource.getDocumentType());
        sanctionLetter.setFileReference(resource.getFileReference());
        sanctionLetter.setType(resource.getType());
        sanctionLetter.setDateOfAmendment(resource.getDateOfAmendment());
        sanctionLetter.setOriginalSanctionAmount(resource.getOriginalSanctionAmount());
        sanctionLetter.setOriginalInterestRate(resource.getOriginalInterestRate());
        sanctionLetter.setRevisedSanctionAmount(resource.getRevisedSanctionAmount());
        sanctionLetter.setRevisedInterestRate(resource.getRevisedInterestRate());
        sanctionLetter.setSanctionLetterValidToDate(resource.getSanctionLetterValidToDate());
        sanctionLetter = sanctionLetterRepository.save(sanctionLetter);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                boardApprovalReasonForDelay.getBoardApproval().getId(),
//                boardApprovalReasonForDelay.getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getLoanContractId(),
//                oldReasonForDelay,
//                boardApprovalReasonForDelay,
//                "Updated",
//                username,
//                "Appraisal", "Reason For Delay" );

        return sanctionLetter;
    }

    @Override
    public SanctionLetter delete(UUID sanctionLetterId) throws CloneNotSupportedException {
        SanctionLetter sanctionLetter = sanctionLetterRepository
                .findById(sanctionLetterId)
                .orElseThrow(() -> new EntityNotFoundException(sanctionLetterId.toString()));
        sanctionLetterRepository.delete(sanctionLetter);
        return sanctionLetter;
    }
}
