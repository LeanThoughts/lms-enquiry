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
public class TermLoanRiskRatingService implements ITermLoanRiskRatingService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final TermLoanRiskRatingRepository termLoanRiskRatingRepository;

    @Override
    public TermLoanRiskRating createTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(termLoanRiskRatingResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        TermLoanRiskRating termLoanRiskRating = new TermLoanRiskRating();
        termLoanRiskRating.setLoanAppraisal(loanAppraisal);
        termLoanRiskRating.setApprovalRisk(termLoanRiskRatingResource.getApprovalRisk());
        termLoanRiskRating.setConstructionRisk(termLoanRiskRatingResource.getConstructionRisk());
        termLoanRiskRating.setExposure(termLoanRiskRatingResource.getExposure());
        termLoanRiskRating.setFinancialRatio(termLoanRiskRatingResource.getFinancialRatio());
        termLoanRiskRating.setFinancingStructure(termLoanRiskRatingResource.getFinancingStructure());
        termLoanRiskRating.setFuelRisk(termLoanRiskRatingResource.getFuelRisk());
        termLoanRiskRating.setOffTakeRisk(termLoanRiskRatingResource.getOffTakeRisk());
        termLoanRiskRating.setOverallRisk(termLoanRiskRatingResource.getOverallRisk());
        termLoanRiskRating.setReputationRisk(termLoanRiskRatingResource.getReputationRisk());
        termLoanRiskRating.setSecurityPackage(termLoanRiskRatingResource.getSecurityPackage());
        termLoanRiskRating.setSponsorSupport(termLoanRiskRatingResource.getSponsorSupport());
        termLoanRiskRating.setYear(termLoanRiskRatingResource.getYear());
        termLoanRiskRating = termLoanRiskRatingRepository.save(termLoanRiskRating);
        return termLoanRiskRating;
    }

    @Override
    public TermLoanRiskRating updateTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource) {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingRepository.findById(termLoanRiskRatingResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(termLoanRiskRatingResource.getId().toString()));
        termLoanRiskRating.setApprovalRisk(termLoanRiskRatingResource.getApprovalRisk());
        termLoanRiskRating.setConstructionRisk(termLoanRiskRatingResource.getConstructionRisk());
        termLoanRiskRating.setExposure(termLoanRiskRatingResource.getExposure());
        termLoanRiskRating.setFinancialRatio(termLoanRiskRatingResource.getFinancialRatio());
        termLoanRiskRating.setFinancingStructure(termLoanRiskRatingResource.getFinancingStructure());
        termLoanRiskRating.setFuelRisk(termLoanRiskRatingResource.getFuelRisk());
        termLoanRiskRating.setOffTakeRisk(termLoanRiskRatingResource.getOffTakeRisk());
        termLoanRiskRating.setOverallRisk(termLoanRiskRatingResource.getOverallRisk());
        termLoanRiskRating.setReputationRisk(termLoanRiskRatingResource.getReputationRisk());
        termLoanRiskRating.setSecurityPackage(termLoanRiskRatingResource.getSecurityPackage());
        termLoanRiskRating.setSponsorSupport(termLoanRiskRatingResource.getSponsorSupport());
        termLoanRiskRating.setYear(termLoanRiskRatingResource.getYear());
        termLoanRiskRating = termLoanRiskRatingRepository.save(termLoanRiskRating);
        return termLoanRiskRating;
    }

    @Override
    public TermLoanRiskRating deleteTermLoanRiskRating(UUID termLoanRiskRatingId) {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingRepository.findById(termLoanRiskRatingId)
                .orElseThrow(() -> new EntityNotFoundException(termLoanRiskRatingId.toString()));
        termLoanRiskRatingRepository.deleteById(termLoanRiskRatingId);
        return termLoanRiskRating;
    }
}
