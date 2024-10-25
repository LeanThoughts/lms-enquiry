package pfs.lms.enquiry.applicationfee.termsheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TermSheetService implements ITermSheetService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationFeeRepository applicationFeeRepository;
    private final TermSheetRepository termSheetRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public TermSheet create(TermSheetResource termSheetResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(termSheetResource.getLoanApplicationId());

        ApplicationFee applicationFee = applicationFeeRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ApplicationFee obj = new ApplicationFee();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = applicationFeeRepository.save(obj);

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

        List<TermSheet> draftTermSheets = termSheetRepository.findByApplicationFeeIdAndStatus(applicationFee.getId(), 
        "Draft");
        List<TermSheet> finalTermSheets = termSheetRepository.findByApplicationFeeIdAndStatus(applicationFee.getId(), 
        "Final");
        TermSheet termSheet = null;
        String status = termSheetResource.getStatus();
        boolean isDraft = "Draft".equals(status);
        boolean isFinal = "Final".equals(status);

        if ((isDraft && !draftTermSheets.isEmpty()) || (isFinal && !finalTermSheets.isEmpty())) {
            throw new RuntimeException("Term-sheet with status " + status + " already exists.");
        }

        if (isDraft && draftTermSheets.isEmpty() || isFinal && finalTermSheets.isEmpty()) {
            termSheet = new TermSheet();
            termSheet.setApplicationFee(applicationFee);
            termSheet.setSerialNumber(1);
            termSheet.setIssuanceDate(termSheetResource.getIssuanceDate());
            termSheet.setAcceptanceDate(termSheetResource.getAcceptanceDate());
            termSheet.setFileReference(termSheetResource.getFileReference());
            termSheet.setStatus(status);

            if (isFinal) {
                TermSheet draftTermSheet = draftTermSheets.get(0);
                if (termSheetResource.getAcceptanceDate().isBefore(draftTermSheet.getIssuanceDate())) {
                    throw new RuntimeException("Final term-sheet date cannot be before the date of the existing draft term-sheet.");
                }
            }

            termSheet = termSheetRepository.save(termSheet);
        }
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanApplication.getLoanContractId(),
//                null,
//                loanPartner,
//                "Created",
//                username,
//                "Appraisal", "Loan Partner");

        return termSheet;
    }

    @Override
    public TermSheet update(TermSheetResource termSheetResource, String username)
            throws CloneNotSupportedException {

        TermSheet termSheet = termSheetRepository.findById(termSheetResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(termSheetResource.getId().toString()));

        Object oldFormalRequest = termSheet.clone();

//        termSheet.setStatus(termSheetResource.getStatus());
        termSheet.setIssuanceDate(termSheetResource.getIssuanceDate());
        termSheet.setAcceptanceDate(termSheetResource.getAcceptanceDate());
        termSheet.setFileReference(termSheetResource.getFileReference());
        termSheet = termSheetRepository.save(termSheet);

        // Change Documents for  Loan Partner
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanPartner.getLoanApplication().getLoanContractId(),
//                oldLoanPartner,
//                loanPartner,
//                "Updated",
//                username,
//                "Appraisal", "Loan Partner");

        return termSheet;
    }

    @Override
    public TermSheet delete(UUID termSheetId, String username) {
        TermSheet termSheet = termSheetRepository.findById(termSheetId)
                .orElseThrow(() -> new EntityNotFoundException(termSheetId.toString()));
        termSheetRepository.delete(termSheet);
        return termSheet;
    }
}
