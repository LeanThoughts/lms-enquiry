package pfs.lms.enquiry.action.rejectbycustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RejectByCustomerService implements IRejectByCustomerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final RejectByCustomerRepository rejectByCustomerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public RejectByCustomer create(RejectByCustomerResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    EnquiryAction obj = new EnquiryAction();
                    obj.setLoanApplication(loanApplication);
                    obj = enquiryActionRepository.save(obj);
                    // Change Documents for EnquiryAction Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "EnquiryAction", "Header");
                    return obj;
                });
        RejectByCustomer rejectByCustomer = new RejectByCustomer();
        rejectByCustomer.setEnquiryAction(enquiryAction);
        rejectByCustomer.setRejectionCategory(resource.getRejectionCategory());
        rejectByCustomer.setRejectionReason(resource.getRejectionReason());
        rejectByCustomer.setRejectionDate(resource.getRejectionDate());
        rejectByCustomer = rejectByCustomerRepository.save(rejectByCustomer);

        // Change Documents for Reject By Customer
        changeDocumentService.createChangeDocument(
                rejectByCustomer.getEnquiryAction().getId(),
                rejectByCustomer.getId().toString(),
                rejectByCustomer.getEnquiryAction().getId().toString(),
                rejectByCustomer.getEnquiryAction().getLoanApplication().getLoanContractId(),
                null,
                rejectByCustomer,
                "Created",
                username,
                "EnquiryAction", "Reject By Customer" );

        return rejectByCustomer;
    }

    @Override
    public RejectByCustomer update(RejectByCustomerResource resource, String username)
            throws CloneNotSupportedException {
        RejectByCustomer rejectByCustomer =
                rejectByCustomerRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldRejectByPFS = rejectByCustomer.clone();

        rejectByCustomer.setRejectionCategory(resource.getRejectionCategory());
        rejectByCustomer.setRejectionReason(resource.getRejectionReason());
        rejectByCustomer.setRejectionDate(resource.getRejectionDate());
        rejectByCustomer = rejectByCustomerRepository.save(rejectByCustomer);

        // Change Documents for Reject By Customer
        changeDocumentService.createChangeDocument(
                rejectByCustomer.getEnquiryAction().getId(),
                rejectByCustomer.getId().toString(),
                rejectByCustomer.getEnquiryAction().getId().toString(),
                rejectByCustomer.getEnquiryAction().getLoanApplication().getLoanContractId(),
                oldRejectByPFS,
                rejectByCustomer,
                "Updated",
                username,
                "EnquiryAction", "Reject By Customer");

        return rejectByCustomer;
    }
}
