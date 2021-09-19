package pfs.lms.enquiry.appraisal.proposaldetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ProposalDetailController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProposalDetailRepository proposalDetailRepository;

    @PostMapping("/proposalDetails/create")
    public ResponseEntity<ProposalDetail> createProposalDetail(@RequestBody ProposalDetailResource proposalDetailResource,
                                                      HttpServletRequest request) {

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
        return ResponseEntity.ok(proposalDetail);
    }

    @PutMapping("/proposalDetails/update")
    public ResponseEntity<ProposalDetail> updateProposalDetail(@RequestBody ProposalDetailResource proposalDetailResource,
                                                                   HttpServletRequest request) {

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
        return ResponseEntity.ok(proposalDetail);
    }
}
