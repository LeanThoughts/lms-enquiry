package pfs.lms.enquiry.applicationfee.termsheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TermSheetService implements ITermSheetService {

    private static final String DRAFT = "Draft";
    private static final String FINAL = "Final";

    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationFeeRepository applicationFeeRepository;
    private final TermSheetRepository termSheetRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public TermSheet create(TermSheetResource termSheetResource, String username) {

        String status = termSheetResource.getStatus();
        if (!DRAFT.equals(status) && !FINAL.equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Term-sheet status must be Draft or Final.");
        }

        LoanApplication loanApplication = loanApplicationRepository.getOne(termSheetResource.getLoanApplicationId());

        // Term sheets can only exist under an existing application fee, so a new application fee needs no validation
        ApplicationFee applicationFee = applicationFeeRepository.findByLoanApplication(loanApplication).orElse(null);
        if (applicationFee != null) {
            validateNewTermSheet(termSheetRepository.findByApplicationFeeIdOrderBySerialNumber(applicationFee.getId()),
                    termSheetResource);
            applicationFee.setModified(true);
            applicationFee = applicationFeeRepository.save(applicationFee);
        }
        else {
            applicationFee = createApplicationFee(loanApplication, username);
        }

        TermSheet termSheet = new TermSheet();
        termSheet.setApplicationFee(applicationFee);
        termSheet.setSerialNumber(1);
        termSheet.setIssuanceDate(termSheetResource.getIssuanceDate());
        termSheet.setAcceptanceDate(termSheetResource.getAcceptanceDate());
        termSheet.setFileReference(termSheetResource.getFileReference());
        termSheet.setStatus(status);
        termSheet = termSheetRepository.save(termSheet);
        changeDocumentService.createChangeDocument(
                termSheet.getId(),
                termSheet.getId().toString(),
                termSheet.getApplicationFee().getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                null,
                termSheet,
                "Created",
                username,
                "ApplicationFee", "TermSheet");

        return termSheet;
    }

    /**
     * Only one Draft and one Final term-sheet are allowed, and the Draft must not be issued after the Final is accepted.
     * Either one can be created first.
     */
    private void validateNewTermSheet(List<TermSheet> existingTermSheets, TermSheetResource termSheetResource) {
        String status = termSheetResource.getStatus();
        if (existingTermSheets.stream().anyMatch(termSheet -> status.equals(termSheet.getStatus()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Term-sheet with status " + status + " already exists.");
        }

        TermSheet draftTermSheet = DRAFT.equals(status) ? null : findByStatus(existingTermSheets, DRAFT);
        TermSheet finalTermSheet = FINAL.equals(status) ? null : findByStatus(existingTermSheets, FINAL);
        LocalDate issuanceDate = draftTermSheet != null ? draftTermSheet.getIssuanceDate() : termSheetResource.getIssuanceDate();
        LocalDate acceptanceDate = finalTermSheet != null ? finalTermSheet.getAcceptanceDate() : termSheetResource.getAcceptanceDate();

        if ((draftTermSheet != null || finalTermSheet != null) && issuanceDate != null && acceptanceDate != null
                && acceptanceDate.isBefore(issuanceDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FINAL.equals(status)
                    ? "Final term-sheet acceptance date cannot be before the issuance date of the existing draft term-sheet."
                    : "Draft term-sheet issuance date cannot be after the acceptance date of the existing final term-sheet.");
        }
    }

    private TermSheet findByStatus(List<TermSheet> termSheets, String status) {
        return termSheets.stream().filter(termSheet -> status.equals(termSheet.getStatus())).findFirst().orElse(null);
    }

    private ApplicationFee createApplicationFee(LoanApplication loanApplication, String username) {
        ApplicationFee applicationFee = new ApplicationFee();
        applicationFee.setLoanApplication(loanApplication);
        applicationFee.setLoanContractId(loanApplication.getLoanContractId());
        applicationFee.setModified(true);
        applicationFee = applicationFeeRepository.save(applicationFee);

        // Change Documents for ApplicationFee Header
        changeDocumentService.createChangeDocument(
                applicationFee.getId(), applicationFee.getId().toString(), applicationFee.getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                applicationFee,
                "Created",
                username,
                "ApplicationFee", "Header");

        return applicationFee;
    }

    @Override
    public TermSheet update(TermSheetResource termSheetResource, String username)
            throws CloneNotSupportedException {

        TermSheet termSheet = termSheetRepository.findById(termSheetResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(termSheetResource.getId().toString()));

        Object oldTermSheet = termSheet.clone();

//        termSheet.setStatus(termSheetResource.getStatus());
        termSheet.setIssuanceDate(termSheetResource.getIssuanceDate());
        termSheet.setAcceptanceDate(termSheetResource.getAcceptanceDate());
        termSheet.setFileReference(termSheetResource.getFileReference());
        termSheet = termSheetRepository.save(termSheet);

        ApplicationFee applicationFee = applicationFeeRepository.getOne(termSheet.getApplicationFee().getId());
        applicationFee.setModified(true);
        applicationFeeRepository.save(applicationFee);

         changeDocumentService.createChangeDocument(
                 termSheet.getId(),
                 termSheet.getId().toString(),
                 termSheet.getApplicationFee().getId().toString(),
                 termSheet.getApplicationFee().getLoanApplication().getEnquiryNo().getId().toString(),
                 oldTermSheet,
                 termSheet,
                "Updated",
                username,
                 "ApplicationFee", "TermSheet");

        return termSheet;
    }

    @Override
    public TermSheet delete(UUID termSheetId, String username) {
        TermSheet termSheet = termSheetRepository.findById(termSheetId)
                .orElseThrow(() -> new EntityNotFoundException(termSheetId.toString()));
        
        ApplicationFee applicationFee = applicationFeeRepository.getOne(termSheet.getApplicationFee().getId());
        applicationFee.setModified(true);
        applicationFeeRepository.save(applicationFee);

        termSheetRepository.delete(termSheet);

        changeDocumentService.createChangeDocument(
                termSheet.getId(),
                termSheet.getId().toString(),
                termSheet.getApplicationFee().getId().toString(),
                termSheet.getApplicationFee().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                termSheet,
                "Deleted",
                username,
                "ApplicationFee", "TermSheet");

        return termSheet;
    }
}
