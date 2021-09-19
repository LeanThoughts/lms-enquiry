package pfs.lms.enquiry.appraisal.loanappraisalkyc;

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
import java.time.LocalDate;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class KnowYourCustomerController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;

    @PostMapping("/knowYourCustomers/create")
    public ResponseEntity<KnowYourCustomer> createKYC(@RequestBody KnowYourCustomerResource knowYourCustomerResource,
                                                                   HttpServletRequest request) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(knowYourCustomerResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        KnowYourCustomer knowYourCustomer = new KnowYourCustomer();
        knowYourCustomer.setLoanAppraisal(loanAppraisal);
        knowYourCustomer.setSerialNumber(knowYourCustomerRepository.findByLoanAppraisalId(loanAppraisal.getId()).size() + 1);
        knowYourCustomer.setDateOfCompletion(knowYourCustomerResource.getDateOfCompletion());
        knowYourCustomer.setDocumentName(knowYourCustomerResource.getDocumentName());
        knowYourCustomer.setFileReference(knowYourCustomerResource.getFileReference());
        knowYourCustomer.setRemarks(knowYourCustomerResource.getRemarks());
        if (knowYourCustomerResource.getFileReference() != null)
            knowYourCustomer.setUploadDate(LocalDate.now());
        knowYourCustomer = knowYourCustomerRepository.save(knowYourCustomer);
        return ResponseEntity.ok(knowYourCustomer);
    }

    @PutMapping("/knowYourCustomers/update")
    public ResponseEntity<KnowYourCustomer> updateKYC(@RequestBody KnowYourCustomerResource knowYourCustomerResource,
                                                                   HttpServletRequest request) {

        KnowYourCustomer knowYourCustomer = knowYourCustomerRepository.findById(knowYourCustomerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(knowYourCustomerResource.getId().toString()));
        knowYourCustomer.setDateOfCompletion(knowYourCustomerResource.getDateOfCompletion());
        knowYourCustomer.setDocumentName(knowYourCustomerResource.getDocumentName());
        knowYourCustomer.setFileReference(knowYourCustomerResource.getFileReference());
        knowYourCustomer.setRemarks(knowYourCustomerResource.getRemarks());
        knowYourCustomer = knowYourCustomerRepository.save(knowYourCustomer);
        return ResponseEntity.ok(knowYourCustomer);
    }
}
