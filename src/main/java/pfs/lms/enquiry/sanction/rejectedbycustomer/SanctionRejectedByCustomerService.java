package pfs.lms.enquiry.sanction.rejectedbycustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.sanction.Sanction;
import pfs.lms.enquiry.sanction.SanctionRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SanctionRejectedByCustomerService implements ISanctionRejectedByCustomerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final SanctionRepository sanctionRepository;
    private final SanctionRejectedByCustomerRepository sanctionRejectedByCustomerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public SanctionRejectedByCustomer create(SanctionRejectedByCustomerResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Sanction sanction = sanctionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Sanction obj = new Sanction();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = sanctionRepository.save(obj);
                    // Change Documents for Sanction Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Sanction", "Header");
                    return obj;
                });
        SanctionRejectedByCustomer sanctionRejectedByCustomer = new SanctionRejectedByCustomer();
        sanctionRejectedByCustomer.setSanction(sanction);
        sanctionRejectedByCustomer.setApprovalByBoardMeetingNumber(resource.getApprovalByBoardMeetingNumber());
        sanctionRejectedByCustomer.setMeetingDate(resource.getMeetingDate());
        sanctionRejectedByCustomer.setRejectionCategory(resource.getRejectionCategory());
        sanctionRejectedByCustomer.setDetails(resource.getDetails());
        sanctionRejectedByCustomer = sanctionRejectedByCustomerRepository.save(sanctionRejectedByCustomer);


        changeDocumentService.createChangeDocument(
                sanctionRejectedByCustomer.getSanction().getId(),
                sanctionRejectedByCustomer.getId().toString(),
                sanctionRejectedByCustomer.getSanction().getId().toString(),
                sanctionRejectedByCustomer.getSanction().getLoanApplication().getLoanContractId(),
                null,
                sanctionRejectedByCustomer,
                "Created",
                username,
                "Sanction", "SanctionRejectedByCustomer" );

        return sanctionRejectedByCustomer;
    }

    @Override
    public SanctionRejectedByCustomer update(SanctionRejectedByCustomerResource resource, String username) throws CloneNotSupportedException {
        SanctionRejectedByCustomer sanctionRejectedByCustomer = sanctionRejectedByCustomerRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = sanctionRejectedByCustomer.clone();

        sanctionRejectedByCustomer.setApprovalByBoardMeetingNumber(resource.getApprovalByBoardMeetingNumber());
        sanctionRejectedByCustomer.setMeetingDate(resource.getMeetingDate());
        sanctionRejectedByCustomer.setRejectionCategory(resource.getRejectionCategory());
        sanctionRejectedByCustomer.setDetails(resource.getDetails());
        sanctionRejectedByCustomer = sanctionRejectedByCustomerRepository.save(sanctionRejectedByCustomer);

        changeDocumentService.createChangeDocument(
                sanctionRejectedByCustomer.getSanction().getId(),
                sanctionRejectedByCustomer.getId().toString(),
                sanctionRejectedByCustomer.getSanction().getId().toString(),
                sanctionRejectedByCustomer.getSanction().getLoanApplication().getLoanContractId(),
                oldObject,
                sanctionRejectedByCustomer,
                "Updated",
                username,
                "Sanction", "SanctionRejectedByCustomer" );

        return sanctionRejectedByCustomer;
    }

    @Override
    public SanctionRejectedByCustomer delete(UUID sanctionRejectedByCustomerId,String username) throws CloneNotSupportedException {
        SanctionRejectedByCustomer sanctionRejectedByCustomer = sanctionRejectedByCustomerRepository
                .findById(sanctionRejectedByCustomerId)
                .orElseThrow(() -> new EntityNotFoundException(sanctionRejectedByCustomerId.toString()));
        sanctionRejectedByCustomerRepository.delete(sanctionRejectedByCustomer);
        changeDocumentService.createChangeDocument(
                sanctionRejectedByCustomer.getSanction().getId(),
                sanctionRejectedByCustomer.getId().toString(),
                sanctionRejectedByCustomer.getSanction().getId().toString(),
                sanctionRejectedByCustomer.getSanction().getLoanApplication().getLoanContractId(),
                null,
                sanctionRejectedByCustomer,
                "Deleted",
                username,
                "Sanction", "SanctionRejectedByCustomer" );
        return sanctionRejectedByCustomer;
    }
}
