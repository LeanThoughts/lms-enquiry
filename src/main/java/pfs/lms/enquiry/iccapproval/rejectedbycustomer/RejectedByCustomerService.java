package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RejectedByCustomerService implements IRejectedByCustomerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final RejectedByCustomerRepository rejectedByCustomerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public RejectedByCustomer create(RejectedByCustomerResource rejectedByCustomerResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(rejectedByCustomerResource.getLoanApplicationId());

        ICCApproval iccApproval = iccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ICCApproval obj = new ICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = iccApprovalRepository.save(obj);

                    // Change Documents for Appraisal Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "Appraisal", "Header");

                    return obj;
                });

        RejectedByCustomer rejectedByCustomer = new RejectedByCustomer();
        rejectedByCustomer.setIccApproval(iccApproval);
        rejectedByCustomer.setMeetingNumber(rejectedByCustomerResource.getMeetingNumber());
        rejectedByCustomer.setDateOfRejection(rejectedByCustomerResource.getDateOfRejection());
        rejectedByCustomer.setRemarks(rejectedByCustomerResource.getRemarks());
        rejectedByCustomer.setRejectionCategory(rejectedByCustomerResource.getRejectionCategory());
        rejectedByCustomer = rejectedByCustomerRepository.save(rejectedByCustomer);
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanApplication.getLoanContractId(),
//                null,
//                loanPartner,
//                "Created",
//                username,
//                "Appraisal", "Loan Partner");

        return rejectedByCustomer;
    }

    @Override
    public RejectedByCustomer update(RejectedByCustomerResource rejectedByCustomerResource, String username)
            throws CloneNotSupportedException {

        RejectedByCustomer rejectedByCustomer = rejectedByCustomerRepository.findById(rejectedByCustomerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(rejectedByCustomerResource.getId().toString()));

        Object oldICCFurtherDetail = rejectedByCustomer.clone();

        rejectedByCustomer.setMeetingNumber(rejectedByCustomerResource.getMeetingNumber());
        rejectedByCustomer.setDateOfRejection(rejectedByCustomerResource.getDateOfRejection());
        rejectedByCustomer.setRemarks(rejectedByCustomerResource.getRemarks());
        rejectedByCustomer.setRejectionCategory(rejectedByCustomerResource.getRejectionCategory());
        rejectedByCustomer = rejectedByCustomerRepository.save(rejectedByCustomer);

        // Change Documents for  Loan Partner
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanPartner.getLoanApplication().getLoanContractId(),
//                oldLoanPartner,
//                loanPartner,
//                "Updated",
//                username,
//                "Appraisal", "Loan Partner");

        return rejectedByCustomer;
    }

    @Override
    public RejectedByCustomer delete(UUID rejectedByCustomerId, String username) {
        RejectedByCustomer rejectedByCustomer = rejectedByCustomerRepository.findById(rejectedByCustomerId)
                .orElseThrow(() -> new EntityNotFoundException(rejectedByCustomerId.toString()));
        rejectedByCustomerRepository.delete(rejectedByCustomer);
        return rejectedByCustomer;
    }
}
