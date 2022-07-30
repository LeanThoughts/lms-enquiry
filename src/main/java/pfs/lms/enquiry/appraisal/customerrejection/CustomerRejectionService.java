package pfs.lms.enquiry.appraisal.customerrejection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerRejectionService implements ICustomerRejectionService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final CustomerRejectionRepository customerRejectionRepository;
    private final IChangeDocumentService changeDocumentService;


    @Override
    public CustomerRejection createCustomerRejection(CustomerRejectionResource customerRejectionResource,String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(customerRejectionResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),
                            obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");

                    return obj;
                });
        CustomerRejection customerRejection = new CustomerRejection();
        customerRejection.setLoanAppraisal(loanAppraisal);
        customerRejection.setDate(customerRejectionResource.getDate());
        customerRejection.setCategory(customerRejectionResource.getCategory());
        customerRejection.setReasonForRejection(customerRejectionResource.getReasonForRejection());
        customerRejection = customerRejectionRepository.save(customerRejection);

        // Change Documents for Customer rejection
        changeDocumentService.createChangeDocument(
                customerRejection.getLoanAppraisal().getId(),
                customerRejection.getId().toString(),
                loanAppraisal.getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                customerRejection,
                "Created",
                username,
                "Appraisal", "Customer Rejection");
        return customerRejection;
    }

    @Override
    public CustomerRejection updateCustomerRejection(CustomerRejectionResource customerRejectionResource, String username) throws CloneNotSupportedException {
        CustomerRejection customerRejection =
                customerRejectionRepository.findById(customerRejectionResource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(customerRejectionResource.getId().toString()));

        Object oldCustomerRejection = customerRejection.clone();

        customerRejection.setDate(customerRejectionResource.getDate());
        customerRejection.setCategory(customerRejectionResource.getCategory());
        customerRejection.setReasonForRejection(customerRejectionResource.getReasonForRejection());
        customerRejection = customerRejectionRepository.save(customerRejection);

        // Change Documents for Customer Rejection
        changeDocumentService.createChangeDocument(
                customerRejection.getLoanAppraisal().getId(),
                customerRejection.getId().toString(),
                customerRejection.getLoanAppraisal().getId().toString(),
                customerRejection.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldCustomerRejection,
                customerRejection,
                "Updated",
                username,
                "Appraisal", "Customer Rejection" );
        return customerRejection;
    }
}
