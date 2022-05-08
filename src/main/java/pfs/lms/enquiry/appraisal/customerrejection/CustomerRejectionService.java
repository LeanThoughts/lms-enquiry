package pfs.lms.enquiry.appraisal.customerrejection;

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
public class CustomerRejectionService implements ICustomerRejectionService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final CustomerRejectionRepository customerRejectionRepository;


    @Override
    public CustomerRejection createCustomerRejection(CustomerRejectionResource customerRejectionResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(customerRejectionResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        CustomerRejection customerRejection = new CustomerRejection();
        customerRejection.setLoanAppraisal(loanAppraisal);
        customerRejection.setDate(customerRejectionResource.getDate());
        customerRejection.setCategory(customerRejectionResource.getCategory());
        customerRejection.setReasonForRejection(customerRejectionResource.getReasonForRejection());
        customerRejection = customerRejectionRepository.save(customerRejection);
        return customerRejection;
    }

    @Override
    public CustomerRejection updateCustomerRejection(CustomerRejectionResource customerRejectionResource) {
        CustomerRejection customerRejection =
                customerRejectionRepository.findById(customerRejectionResource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(customerRejectionResource.getId().toString()));
        customerRejection.setDate(customerRejectionResource.getDate());
        customerRejection.setCategory(customerRejectionResource.getCategory());
        customerRejection.setReasonForRejection(customerRejectionResource.getReasonForRejection());
        customerRejection = customerRejectionRepository.save(customerRejection);
        return customerRejection;
    }
}
