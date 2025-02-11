package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BmcIccApproval;
import pfs.lms.enquiry.bmcapproval.BMCICCApprovalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BMCRejectedByCustomerService implements IBMCRejectedByCustomerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository bmcICCApprovalRepository;
    private final BMCRejectedByCustomerRepository bmcRejectedByCustomerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BmcRejectedByCustomer create(BMCRejectedByCustomerResource bmcRejectedByCustomerResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmcRejectedByCustomerResource.getLoanApplicationId());

        BmcIccApproval BMCICCApproval = bmcICCApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcIccApproval obj = new BmcIccApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmcICCApprovalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "BmcIccApproval", "Header");

                    return obj;
                });

        BmcRejectedByCustomer bmcRejectedByCustomer = new BmcRejectedByCustomer();
        bmcRejectedByCustomer.setBmcICCApproval(BMCICCApproval);
        bmcRejectedByCustomer.setMeetingNumber(bmcRejectedByCustomerResource.getMeetingNumber());
        bmcRejectedByCustomer.setDateOfRejection(bmcRejectedByCustomerResource.getDateOfRejection());
        bmcRejectedByCustomer.setRemarks(bmcRejectedByCustomerResource.getRemarks());
        bmcRejectedByCustomer.setRejectionCategory(bmcRejectedByCustomerResource.getRejectionCategory());
        bmcRejectedByCustomer = bmcRejectedByCustomerRepository.save(bmcRejectedByCustomer);

        changeDocumentService.createChangeDocument(
                bmcRejectedByCustomer.getId(),
                bmcRejectedByCustomer.getId().toString(),
                bmcRejectedByCustomer.getBmcICCApproval().getId().toString(),
                BMCICCApproval.getLoanApplication().getLoanContractId(),
                null,
                bmcRejectedByCustomer,
                "Created",
                username,
                "BmcApprovalByIcc", "BmcRejectedByCustomer");

        return bmcRejectedByCustomer;
    }

    @Override
    public BmcRejectedByCustomer update(BMCRejectedByCustomerResource bmcRejectedByCustomerResource, String username)
            throws CloneNotSupportedException {

        BmcRejectedByCustomer bmcRejectedByCustomer = bmcRejectedByCustomerRepository.findById(bmcRejectedByCustomerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmcRejectedByCustomerResource.getId().toString()));

        Object oldBmcRejectedByCustomer = bmcRejectedByCustomer.clone();

        bmcRejectedByCustomer.setMeetingNumber(bmcRejectedByCustomerResource.getMeetingNumber());
        bmcRejectedByCustomer.setDateOfRejection(bmcRejectedByCustomerResource.getDateOfRejection());
        bmcRejectedByCustomer.setRemarks(bmcRejectedByCustomerResource.getRemarks());
        bmcRejectedByCustomer.setRejectionCategory(bmcRejectedByCustomerResource.getRejectionCategory());
        bmcRejectedByCustomer = bmcRejectedByCustomerRepository.save(bmcRejectedByCustomer);

        BmcIccApproval bmcICCApproval = bmcICCApprovalRepository.getOne(bmcRejectedByCustomer.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcICCApprovalRepository.save(bmcICCApproval);

        changeDocumentService.createChangeDocument(
                bmcRejectedByCustomer.getId(),
                bmcRejectedByCustomer.getId().toString(),
                bmcRejectedByCustomer.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                oldBmcRejectedByCustomer,
                bmcRejectedByCustomer,
                "Updated",
                username,
                "BmcApprovalByIcc", "BmcRejectedByCustomer");

        return bmcRejectedByCustomer;
    }

    @Override
    public BmcRejectedByCustomer delete(UUID bmcRejectedByCustomerId, String username) {
        BmcRejectedByCustomer bmcRejectedByCustomer = bmcRejectedByCustomerRepository.findById(bmcRejectedByCustomerId)
                .orElseThrow(() -> new EntityNotFoundException(bmcRejectedByCustomerId.toString()));

        BmcIccApproval bmcICCApproval = bmcICCApprovalRepository.getOne(bmcRejectedByCustomer.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcICCApprovalRepository.save(bmcICCApproval);

        bmcRejectedByCustomerRepository.delete(bmcRejectedByCustomer);

        changeDocumentService.createChangeDocument(
                bmcRejectedByCustomer.getId(),
                bmcRejectedByCustomer.getId().toString(),
                bmcRejectedByCustomer.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                null,
                bmcRejectedByCustomer,
                "Deleted",
                username,
                "BmcApprovalByIcc", "BmcRejectedByCustomer");

        return bmcRejectedByCustomer;
    }
}
