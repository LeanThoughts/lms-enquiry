package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class ApplicationFeeProjectDetailService implements IApplicationFeeProjectDetailService {
    private final IChangeDocumentService changeDocumentService;

    private final ApplicationFeeProjectDetailRepository applicationFeeProjectDetailRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationFeeRepository applicationFeeRepository;

    @Override
    public ApplicationFeeProjectDetail create(ApplicationFeeProjectDetailResource applicationFeeProjectDetailResource,
            String username) {
                
        LoanApplication loanApplication = loanApplicationRepository.getOne(applicationFeeProjectDetailResource.getLoanApplicationId());

        ApplicationFee applicationFee = applicationFeeRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ApplicationFee obj = new ApplicationFee();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = applicationFeeRepository.save(obj);

                    // Change Documents for ApplicationFee Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "ApplicationFee", "Header");

                    return obj;
                });

        ApplicationFeeProjectDetail applicationFeeProjectDetail = new ApplicationFeeProjectDetail();
        applicationFeeProjectDetail.setApplicationFee(applicationFee);
        // Use BeanUtils to copy properties from resource to entity
        BeanUtils.copyProperties(applicationFeeProjectDetailResource, applicationFeeProjectDetail, 
            "id", "applicationFee", "createdAt", "updatedAt");
        applicationFeeProjectDetail = applicationFeeProjectDetailRepository.save(applicationFeeProjectDetail);

        return applicationFeeProjectDetail;
    }

    @Override
    public ApplicationFeeProjectDetail update(ApplicationFeeProjectDetailResource applicationFeeProjectDetailResource,
            String username) throws CloneNotSupportedException {
        ApplicationFeeProjectDetail applicationFeeProjectDetail = applicationFeeProjectDetailRepository.getOne(applicationFeeProjectDetailResource.getId());
        Object oldApplicationFeeProjectDetail = applicationFeeProjectDetail.clone();

        BeanUtils.copyProperties(applicationFeeProjectDetailResource, applicationFeeProjectDetail,
            "id", "applicationFee", "createdAt", "updatedAt");
        applicationFeeProjectDetail = applicationFeeProjectDetailRepository.save(applicationFeeProjectDetail);



        changeDocumentService.createChangeDocument(
                applicationFeeProjectDetail.getId(),
                applicationFeeProjectDetail.getId().toString(),
                applicationFeeProjectDetail.getApplicationFee().getId().toString(),
                applicationFeeProjectDetail.getApplicationFee().getLoanApplication().getEnquiryNo().getId().toString(),
                oldApplicationFeeProjectDetail,
                applicationFeeProjectDetail,
                "Updated",
                username,
                "ApplicationFee", "ApplicationFeeProjectDetail");


        return applicationFeeProjectDetail;
    }

    @Override
    public ApplicationFeeProjectDetail getApplicationFeeProjectDetail(UUID applicationFeeId) {
        return applicationFeeProjectDetailRepository.findByApplicationFeeId(applicationFeeId);
    }

    private LoanApplication saveLoanApplication(ApplicationFeeProjectDetail applicationFeeProjectDetail, String username) throws CloneNotSupportedException {

        Object oldLoanApplication = applicationFeeProjectDetail.getApplicationFee().getLoanApplication().clone();

        LoanApplication loanApplication = applicationFeeProjectDetail.getApplicationFee().getLoanApplication();
        loanApplication.setProjectName(applicationFeeProjectDetail.getProjectName());
        loanApplication.setPromoterName(applicationFeeProjectDetail.getPromoterName());
        loanApplication.setLoanPurpose(applicationFeeProjectDetail.getLoanPurpose());
        loanApplication.setProjectCapacity(applicationFeeProjectDetail.getProjectCapacity());
        loanApplication.setProjectCapacityUnit(applicationFeeProjectDetail.getProjectCapacityUnit());
        loanApplication.setProjectLocationState(applicationFeeProjectDetail.getState());
        loanApplication.setProductCode(applicationFeeProjectDetail.getProductTypeCode());
        loanApplication.setTerm(applicationFeeProjectDetail.getTerm().toString());
        loanApplication.setEnquiryCompletionDate(applicationFeeProjectDetail.getEnquiryCompletionDate());

        loanApplication.setLoanType(applicationFeeProjectDetail.getLoanType());
        loanApplication.setLoanClass(applicationFeeProjectDetail.getLoanClass());
        loanApplication.setAssistanceType(applicationFeeProjectDetail.getAssistanceType());
        loanApplication.setFinancingType(applicationFeeProjectDetail.getFinancingType());
        loanApplication.setProjectType(applicationFeeProjectDetail.getProjectType());

        loanApplication.setProjectTypeCoreSector(applicationFeeProjectDetail.getProjectTypeCoreSector());
        loanApplication.setPurposeOfLoan(applicationFeeProjectDetail.getPurposeOfLoan());
        loanApplication.setLoanPurpose( applicationFeeProjectDetail.getLoanPurpose());
        loanApplication.setProjectCost(applicationFeeProjectDetail.getProjectCost());

        loanApplication.setPfsDebtAmount(applicationFeeProjectDetail.getPfsDebtAmount());
        loanApplication.setProjectDebtAmount(applicationFeeProjectDetail.getDebt());
        loanApplication.setEquity(applicationFeeProjectDetail.getPromoterContributionEquity());
        loanApplication.setDebtEquityRatio(applicationFeeProjectDetail.getDebtEquityRatio());
        loanApplication.setGrantSubsidyAmount(applicationFeeProjectDetail.getGrantSubsidyAmount());
        loanApplication.setDebtEquityRatioWithGrant(applicationFeeProjectDetail.getDebtEquityRatioWithGrant());

        loanApplication.setExpectedInterestRate(applicationFeeProjectDetail.getRateOfInterest());

        loanApplication.setTenorMonth(applicationFeeProjectDetail.getTenorYear());
        loanApplication.setTenorMonth(applicationFeeProjectDetail.getTenorMonths());
        loanApplication.setMoratoriumPeriod(applicationFeeProjectDetail.getMoratoriumPeriod());
        loanApplication.setMoratoriumPeriodUnit(applicationFeeProjectDetail.getMoratoriumPeriodUnit());
        loanApplication.setConstructionPeriod(applicationFeeProjectDetail.getConstructionPeriod());
        loanApplication.setConstructionPeriodUnit(applicationFeeProjectDetail.getConstructionPeriodUnit());

        loanApplicationRepository.save(loanApplication);

        changeDocumentService.createChangeDocument(
                loanApplication.getId(),
                loanApplication.getId().toString(),
                loanApplication.getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                oldLoanApplication,
                loanApplication,
                "Updated",
                username,
                "LoanApplication", "LoanApplication");


        return  loanApplication;
    }
}
