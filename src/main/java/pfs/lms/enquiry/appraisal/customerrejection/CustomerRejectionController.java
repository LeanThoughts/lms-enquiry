package pfs.lms.enquiry.appraisal.customerrejection;

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
public class CustomerRejectionController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final CustomerRejectionRepository customerRejectionRepository;

    @PostMapping("/customerRejections/create")
    public ResponseEntity<CustomerRejection> createProjectAppraisalCompletion(
                @RequestBody CustomerRejectionResource customerRejectionResource,
                HttpServletRequest request) {

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
        return ResponseEntity.ok(customerRejection);
    }

    @PutMapping("/customerRejections/update")
    public ResponseEntity<CustomerRejection> updateProjectAppraisalCompletion(
                @RequestBody CustomerRejectionResource customerRejectionResource,
                HttpServletRequest request) {

        CustomerRejection customerRejection =
                customerRejectionRepository.findById(customerRejectionResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(customerRejectionResource.getId().toString()));
        customerRejection.setDate(customerRejectionResource.getDate());
        customerRejection.setCategory(customerRejectionResource.getCategory());
        customerRejection.setReasonForRejection(customerRejectionResource.getReasonForRejection());
        customerRejection = customerRejectionRepository.save(customerRejection);
        return ResponseEntity.ok(customerRejection);
    }
}
