package pfs.lms.enquiry.appraisal.riskrating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorporateLoanRiskRatingService implements ICorporateLoanRiskRatingService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final CorporateLoanRiskRatingRepository corporateLoanRiskRatingRepository;

    @Override
    public CorporateLoanRiskRating createCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(corporateLoanRiskRatingResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
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
        return corporateLoanRiskRating;
    }

    @Override
    public CorporateLoanRiskRating updateCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource) {
        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingRepository.findById(corporateLoanRiskRatingResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(corporateLoanRiskRatingResource.getId().toString()));
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
        return corporateLoanRiskRating;
    }

    @Override
    public CorporateLoanRiskRating deleteCorporateLoanRiskRating(UUID corporateLoanRiskRatingId) {
        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingRepository.findById(corporateLoanRiskRatingId)
                .orElseThrow(() -> new EntityNotFoundException(corporateLoanRiskRatingId.toString()));
        corporateLoanRiskRatingRepository.deleteById(corporateLoanRiskRatingId);
        return corporateLoanRiskRating;
    }
}
