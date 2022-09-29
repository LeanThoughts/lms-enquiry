package pfs.lms.enquiry.action.rejectbypfs;

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
public class RejectByPfsService implements IRejectByPfsService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final RejectByPfsRepository rejectByPFSRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public RejectByPfs create(RejectByPfsResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    EnquiryAction obj = new EnquiryAction();
                    obj.setLoanApplication(loanApplication);
                    obj = enquiryActionRepository.save(obj);
                    // Change Documents for EnquiryAction Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "EnquiryAction", "Header");
                    return obj;
                });
        RejectByPfs rejectByPFS = new RejectByPfs();
        rejectByPFS.setEnquiryAction(enquiryAction);
        rejectByPFS.setRejectionCategory(resource.getRejectionCategory());
        rejectByPFS.setRejectionReason(resource.getRejectionReason());
        rejectByPFS.setRejectionDate(resource.getRejectionDate());
        rejectByPFS = rejectByPFSRepository.save(rejectByPFS);

        // Change Documents for Reject By PFS
//        changeDocumentService.createChangeDocument(
//                rejectByPFS.getEnquiryAction().getId(),
//                rejectByPFS.getId().toString(),
//                rejectByPFS.getEnquiryAction().getId().toString(),
//                rejectByPFS.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                null,
//                rejectByPFS,
//                "Created",
//                username,
//                "EnquiryAction", "Reject By PFS" );

        return rejectByPFS;
    }

    @Override
    public RejectByPfs update(RejectByPfsResource resource, String username)
            throws CloneNotSupportedException {
        RejectByPfs rejectByPFS =
                rejectByPFSRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldRejectByPFS = rejectByPFS.clone();

        rejectByPFS.setRejectionCategory(resource.getRejectionCategory());
        rejectByPFS.setRejectionReason(resource.getRejectionReason());
        rejectByPFS.setRejectionDate(resource.getRejectionDate());
        rejectByPFS = rejectByPFSRepository.save(rejectByPFS);

        // Change Documents for Reject By PFS
//        changeDocumentService.createChangeDocument(
//                rejectByPFS.getEnquiryAction().getId(),
//                rejectByPFS.getId().toString(),
//                rejectByPFS.getEnquiryAction().getId().toString(),
//                rejectByPFS.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                oldRejectByPFS,
//                rejectByPFS,
//                "Updated",
//                username,
//                "EnquiryAction", "Reject By PFS" );

        return rejectByPFS;
    }
}
