package pfs.lms.enquiry.appraisal.riskrating;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
public class TermLoanRiskRatingService implements ITermLoanRiskRatingService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final TermLoanRiskRatingRepository termLoanRiskRatingRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public TermLoanRiskRating createTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(termLoanRiskRatingResource.getLoanApplicationId());
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
        termLoanRiskRating.setTechnologyRisk(termLoanRiskRatingResource.getTechnologyRisk());
        termLoanRiskRating = termLoanRiskRatingRepository.save(termLoanRiskRating);

        changeDocumentService.createChangeDocument(
                termLoanRiskRating.getLoanAppraisal().getId(),
                termLoanRiskRating.getId().toString(),
                termLoanRiskRating.getLoanAppraisal().getId().toString(),
                termLoanRiskRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                termLoanRiskRating,
                "Created",
                username,
                "Appraisal", "External Rating Term Loan" );


        return termLoanRiskRating;
    }

    @SneakyThrows
    @Override
    public TermLoanRiskRating updateTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource, String username) throws CloneNotSupportedException {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingRepository.findById(termLoanRiskRatingResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(termLoanRiskRatingResource.getId().toString()));

        TermLoanRiskRating oldTermLoanRiskRating = (TermLoanRiskRating) termLoanRiskRating.clone();

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
        termLoanRiskRating.setTechnologyRisk(termLoanRiskRatingResource.getTechnologyRisk());
        termLoanRiskRating = termLoanRiskRatingRepository.save(termLoanRiskRating);

        changeDocumentService.createChangeDocument(
                termLoanRiskRating.getLoanAppraisal().getId(),
                termLoanRiskRating.getId().toString(),
                termLoanRiskRating.getLoanAppraisal().getId().toString(),
                termLoanRiskRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldTermLoanRiskRating,
                termLoanRiskRating,
                "Updated",
                username,
                "Appraisal", "External Rating Term Loan" );

        return termLoanRiskRating;
    }

    @Override
    public TermLoanRiskRating deleteTermLoanRiskRating(UUID termLoanRiskRatingId , String username) {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingRepository.findById(termLoanRiskRatingId)
                .orElseThrow(() -> new EntityNotFoundException(termLoanRiskRatingId.toString()));
        termLoanRiskRatingRepository.deleteById(termLoanRiskRatingId);

        changeDocumentService.createChangeDocument(
                termLoanRiskRating.getLoanAppraisal().getId(),
                termLoanRiskRating.getId().toString(),
                termLoanRiskRating.getLoanAppraisal().getId().toString(),
                termLoanRiskRating.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                termLoanRiskRating,
                "Deleted",
                username,
                "Appraisal", "External Rating Term Loan" );


        return termLoanRiskRating;
    }
}
