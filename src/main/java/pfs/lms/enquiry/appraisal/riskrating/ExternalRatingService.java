package pfs.lms.enquiry.appraisal.riskrating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
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
public class ExternalRatingService implements IExternalRatingService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ExternalRatingRepository externalRatingRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ExternalRating createExternalRating(ExternalRatingResource externalRatingResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(externalRatingResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = loanAppraisalRepository.save(obj);
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");

                    return obj;
                });
        ExternalRating externalRating = new ExternalRating();
        externalRating.setLoanAppraisal(loanAppraisal);
        externalRating.setSerialNumber(externalRatingRepository
                .findByLoanAppraisalIdOrderBySerialNumber(loanAppraisal.getId()).size() + 1);
        externalRating.setValidityDate(externalRatingResource.getValidityDate());
        externalRating.setRating(externalRatingResource.getRating());
        externalRating.setRatingAgency(externalRatingResource.getRatingAgency());
        externalRating.setCreditStanding(externalRatingResource.getCreditStanding());
        externalRating.setCreditStandingInstruction(externalRatingResource.getCreditStandingInstruction());
        externalRating.setCreditStandingText(externalRatingResource.getCreditStandingText());
        externalRating = externalRatingRepository.save(externalRating);

        // Change Documents for External Rating Corporate Loan
//        changeDocumentService.createChangeDocument(
//                externalRating.getLoanAppraisal().getId(),
//                externalRating.getId().toString(),
//                externalRating.getLoanAppraisal().getId().toString(),
//                externalRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
//                null,
//                externalRating,
//                "Created",
//                username,
//                "Appraisal", "External Rating Corporate Loan" );


        return externalRating;
    }

    @Override
    public ExternalRating updateExternalRating(ExternalRatingResource externalRatingResource, String username) throws CloneNotSupportedException {
        ExternalRating externalRating = externalRatingRepository.findById(externalRatingResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(externalRatingResource.getId().toString()));

        ExternalRating oldExternalRating = (ExternalRating) externalRating.clone();

        externalRating.setValidityDate(externalRatingResource.getValidityDate());
        externalRating.setRating(externalRatingResource.getRating());
        externalRating.setRatingAgency(externalRatingResource.getRatingAgency());
        externalRating.setCreditStanding(externalRatingResource.getCreditStanding());
        externalRating.setCreditStandingInstruction(externalRatingResource.getCreditStandingInstruction());
        externalRating.setCreditStandingText(externalRatingResource.getCreditStandingText());
        externalRating = externalRatingRepository.save(externalRating);

        // Change Documents for External Rating Corporate Loan
//        changeDocumentService.createChangeDocument(
//                externalRating.getLoanAppraisal().getId(),
//                externalRating.getId().toString(),
//                externalRating.getLoanAppraisal().getId().toString(),
//                externalRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
//                oldExternalRating,
//                externalRating,
//                "Updated",
//                username,
//                "Appraisal", "External Rating Corporate Loan" );


        return externalRating;
    }

    @Override
    public ExternalRating deleteExternalRating(UUID externalRatingId, String username) {
        ExternalRating externalRating = externalRatingRepository.findById(externalRatingId)
                .orElseThrow(() -> new EntityNotFoundException(externalRatingId.toString()));
        externalRatingRepository.deleteById(externalRatingId);

        // Change Documents for External Rating Corporate Loan
//        changeDocumentService.createChangeDocument(
//                externalRating.getLoanAppraisal().getId(),
//                externalRating.getId().toString(),
//                externalRating.getLoanAppraisal().getId().toString(),
//                externalRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
//                null,
//                externalRating,
//                "Deleted",
//                username,
//                "Appraisal", "External Rating Corporate Loan" );

        return externalRating;
    }
}
