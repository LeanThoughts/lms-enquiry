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
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorporateLoanRiskRatingService implements ICorporateLoanRiskRatingService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final CorporateLoanRiskRatingRepository corporateLoanRiskRatingRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public CorporateLoanRiskRating createCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(corporateLoanRiskRatingResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
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
        CorporateLoanRiskRating corporateLoanRiskRating = new CorporateLoanRiskRating();
        corporateLoanRiskRating.setLoanAppraisal(loanAppraisal);
        corporateLoanRiskRating.setYear(corporateLoanRiskRatingResource.getYear());
        corporateLoanRiskRating.setFinancialRatio(corporateLoanRiskRatingResource.getFinancialRatio());
        corporateLoanRiskRating.setPurposeOfLoan(corporateLoanRiskRatingResource.getPurposeOfLoan());
        corporateLoanRiskRating.setFinancingStructure(corporateLoanRiskRatingResource.getFinancingStructure());
        corporateLoanRiskRating.setRepaymentCapability(corporateLoanRiskRatingResource.getRepaymentCapability());
        corporateLoanRiskRating.setCorporateGovernancePractice(corporateLoanRiskRatingResource.
                getCorporateGovernancePractice());
        corporateLoanRiskRating.setConductOfLoan(corporateLoanRiskRatingResource.getConductOfLoan());
        corporateLoanRiskRating.setDeviationWithOperationalPolicy(corporateLoanRiskRatingResource.
                getDeviationWithOperationalPolicy());
        corporateLoanRiskRating.setExposure(corporateLoanRiskRatingResource.getExposure());
        corporateLoanRiskRating = corporateLoanRiskRatingRepository.save(corporateLoanRiskRating);

        // Change Documents for External Rating Corporate Loan
        changeDocumentService.createChangeDocument(
                corporateLoanRiskRating.getLoanAppraisal().getId(),
                corporateLoanRiskRating.getId().toString(),
                corporateLoanRiskRating.getLoanAppraisal().getId().toString(),
                corporateLoanRiskRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                corporateLoanRiskRating,
                "Created",
                username,
                "Appraisal", "External Rating Corporate Loan" );


        return corporateLoanRiskRating;
    }

    @Override
    public CorporateLoanRiskRating updateCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource, String username) throws CloneNotSupportedException {
        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingRepository.findById(corporateLoanRiskRatingResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(corporateLoanRiskRatingResource.getId().toString()));

        CorporateLoanRiskRating oldCorporateLoanRiskRating = (CorporateLoanRiskRating) corporateLoanRiskRating.clone();

        corporateLoanRiskRating.setYear(corporateLoanRiskRatingResource.getYear());
        corporateLoanRiskRating.setFinancialRatio(corporateLoanRiskRatingResource.getFinancialRatio());
        corporateLoanRiskRating.setPurposeOfLoan(corporateLoanRiskRatingResource.getPurposeOfLoan());
        corporateLoanRiskRating.setFinancingStructure(corporateLoanRiskRatingResource.getFinancingStructure());
        corporateLoanRiskRating.setRepaymentCapability(corporateLoanRiskRatingResource.getRepaymentCapability());
        corporateLoanRiskRating.setCorporateGovernancePractice(corporateLoanRiskRatingResource.
                getCorporateGovernancePractice());
        corporateLoanRiskRating.setConductOfLoan(corporateLoanRiskRatingResource.getConductOfLoan());
        corporateLoanRiskRating.setDeviationWithOperationalPolicy(corporateLoanRiskRatingResource.
                getDeviationWithOperationalPolicy());
        corporateLoanRiskRating.setExposure(corporateLoanRiskRatingResource.getExposure());
        corporateLoanRiskRating = corporateLoanRiskRatingRepository.save(corporateLoanRiskRating);

        // Change Documents for External Rating Corporate Loan
        changeDocumentService.createChangeDocument(
                corporateLoanRiskRating.getLoanAppraisal().getId(),
                corporateLoanRiskRating.getId().toString(),
                corporateLoanRiskRating.getLoanAppraisal().getId().toString(),
                corporateLoanRiskRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldCorporateLoanRiskRating,
                corporateLoanRiskRating,
                "Updated",
                username,
                "Appraisal", "External Rating Corporate Loan" );


        return corporateLoanRiskRating;
    }

    @Override
    public CorporateLoanRiskRating deleteCorporateLoanRiskRating(UUID corporateLoanRiskRatingId, String username) {
        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingRepository.findById(corporateLoanRiskRatingId)
                .orElseThrow(() -> new EntityNotFoundException(corporateLoanRiskRatingId.toString()));
        corporateLoanRiskRatingRepository.deleteById(corporateLoanRiskRatingId);

        // Change Documents for External Rating Corporate Loan
        changeDocumentService.createChangeDocument(
                corporateLoanRiskRating.getLoanAppraisal().getId(),
                corporateLoanRiskRating.getId().toString(),
                corporateLoanRiskRating.getLoanAppraisal().getId().toString(),
                corporateLoanRiskRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                corporateLoanRiskRating,
                "Deleted",
                username,
                "Appraisal", "External Rating Corporate Loan" );

        return corporateLoanRiskRating;
    }
}
