package pfs.lms.enquiry.appraisal.proposaldetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalDetailService implements IProposalDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProposalDetailRepository proposalDetailRepository;

    @Override
    public ProposalDetail createProposalDetail(ProposalDetailResource proposalDetailResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(proposalDetailResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        ProposalDetail proposalDetail = new ProposalDetail();
        proposalDetail.setLoanAppraisal(loanAppraisal);
        proposalDetail.setRateOfInterestPreCod(proposalDetailResource.getRateOfInterestPreCod());
        proposalDetail.setRateOfInterestPostCod(proposalDetailResource.getRateOfInterestPostCod());
        proposalDetail.setSpreadReset(proposalDetailResource.getSpreadReset());
        proposalDetail.setSpreadResetUnit(proposalDetailResource.getSpreadResetUnit());
        proposalDetail.setEffectiveRateOfInterest(proposalDetailResource.getEffectiveRateOfInterest());
        proposalDetail.setConstructionPeriod(proposalDetailResource.getConstructionPeriod());
        proposalDetail.setConstructionPeriodUnit(proposalDetailResource.getConstructionPeriodUnit());
        proposalDetail.setMoratoriumPeriod(proposalDetailResource.getMoratoriumPeriod());
        proposalDetail.setMoratoriumPeriodUnit(proposalDetailResource.getMoratoriumPeriodUnit());
        proposalDetail.setRepaymentPeriod(proposalDetailResource.getRepaymentPeriod());
        proposalDetail.setRepaymentPeriodUnit(proposalDetailResource.getRepaymentPeriodUnit());
        proposalDetail.setTenor(proposalDetailResource.getTenor());
        proposalDetail.setTenorUnit(proposalDetailResource.getTenorUnit());
        proposalDetail.setAvailabilityPeriod(proposalDetailResource.getAvailabilityPeriod());
        proposalDetail.setAvailabilityPeriodUnit(proposalDetailResource.getAvailabilityPeriodUnit());
        proposalDetail.setPrePaymentCharges(proposalDetailResource.getPrePaymentCharges());
        proposalDetail.setFeeDetailsSchedule(proposalDetailResource.getFeeDetailsSchedule());
        proposalDetail = proposalDetailRepository.save(proposalDetail);
        return proposalDetail;
    }

    @Override
    public ProposalDetail updateProposalDetail(ProposalDetailResource proposalDetailResource) {
        ProposalDetail proposalDetail = proposalDetailRepository.findById(proposalDetailResource.getId())
            .orElseThrow(() -> new EntityNotFoundException(proposalDetailResource.getId().toString()));
        proposalDetail.setRateOfInterestPreCod(proposalDetailResource.getRateOfInterestPreCod());
        proposalDetail.setRateOfInterestPostCod(proposalDetailResource.getRateOfInterestPostCod());
        proposalDetail.setSpreadReset(proposalDetailResource.getSpreadReset());
        proposalDetail.setSpreadResetUnit(proposalDetailResource.getSpreadResetUnit());
        proposalDetail.setEffectiveRateOfInterest(proposalDetailResource.getEffectiveRateOfInterest());
        proposalDetail.setConstructionPeriod(proposalDetailResource.getConstructionPeriod());
        proposalDetail.setConstructionPeriodUnit(proposalDetailResource.getConstructionPeriodUnit());
        proposalDetail.setMoratoriumPeriod(proposalDetailResource.getMoratoriumPeriod());
        proposalDetail.setMoratoriumPeriodUnit(proposalDetailResource.getMoratoriumPeriodUnit());
        proposalDetail.setRepaymentPeriod(proposalDetailResource.getRepaymentPeriod());
        proposalDetail.setRepaymentPeriodUnit(proposalDetailResource.getRepaymentPeriodUnit());
        proposalDetail.setTenor(proposalDetailResource.getTenor());
        proposalDetail.setTenorUnit(proposalDetailResource.getTenorUnit());
        proposalDetail.setAvailabilityPeriod(proposalDetailResource.getAvailabilityPeriod());
        proposalDetail.setAvailabilityPeriodUnit(proposalDetailResource.getAvailabilityPeriodUnit());
        proposalDetail.setPrePaymentCharges(proposalDetailResource.getPrePaymentCharges());
        proposalDetail.setFeeDetailsSchedule(proposalDetailResource.getFeeDetailsSchedule());
        proposalDetail = proposalDetailRepository.save(proposalDetail);
        return proposalDetail;
    }
}
