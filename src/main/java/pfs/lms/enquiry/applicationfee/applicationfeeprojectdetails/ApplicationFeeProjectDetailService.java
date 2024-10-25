package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class ApplicationFeeProjectDetailService implements IApplicationFeeProjectDetailService {

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
        BeanUtils.copyProperties(applicationFeeProjectDetailResource, applicationFeeProjectDetail, 
            "id", "applicationFee", "createdAt", "updatedAt");
        applicationFeeProjectDetail = applicationFeeProjectDetailRepository.save(applicationFeeProjectDetail);
        return applicationFeeProjectDetail;
    }

    @Override
    public ApplicationFeeProjectDetail getApplicationFeeProjectDetail(UUID applicationFeeId) {
        return applicationFeeProjectDetailRepository.findByApplicationFeeId(applicationFeeId);
    }
}
