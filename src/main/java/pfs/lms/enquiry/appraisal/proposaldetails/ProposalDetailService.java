package pfs.lms.enquiry.appraisal.proposaldetails;

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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProposalDetailService implements IProposalDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProposalDetailRepository proposalDetailRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ProposalDetail createProposalDetail(ProposalDetailResource proposalDetailResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(proposalDetailResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = loanAppraisalRepository.save(obj);
                    // Change Documents for Appraisal Header
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

        // Change Documents for Proposal Detail
        changeDocumentService.createChangeDocument(
                proposalDetail.getLoanAppraisal().getId(),
                proposalDetail.getId().toString(),
                proposalDetail.getLoanAppraisal().getId().toString(),
                proposalDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                proposalDetail,
                "Created",
                username,
                "Appraisal", "Proposal Detail" );

        return proposalDetail;
    }

    @Override
    public ProposalDetail updateProposalDetail(ProposalDetailResource proposalDetailResource, String username) throws CloneNotSupportedException {
        ProposalDetail proposalDetail = proposalDetailRepository.findById(proposalDetailResource.getId())
            .orElseThrow(() -> new EntityNotFoundException(proposalDetailResource.getId().toString()));

        Object oldProposalDetail = proposalDetail.clone();

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

        // Change Documents for Proposal Detail

        changeDocumentService.createChangeDocument(
                proposalDetail.getLoanAppraisal().getId(),
                proposalDetail.getId().toString(),
                proposalDetail.getLoanAppraisal().getId().toString(),
                proposalDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldProposalDetail,
                proposalDetail,
                "Updated",
                username,
                "Appraisal", "Proposal Detail" );

        return proposalDetail;
    }
}
