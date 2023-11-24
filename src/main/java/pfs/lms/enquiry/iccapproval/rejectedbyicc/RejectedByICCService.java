package pfs.lms.enquiry.iccapproval.rejectedbyicc;

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
public class RejectedByICCService implements IRejectedByICCService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final RejectedByICCRepository rejectedByIccRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public RejectedByICC create(RejectedByICCResource rejectedByIccResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(rejectedByIccResource.getLoanApplicationId());

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

        RejectedByICC rejectedByIcc = new RejectedByICC();
        rejectedByIcc.setIccApproval(iccApproval);
        rejectedByIcc.setMeetingDate(rejectedByIccResource.getMeetingDate());
        rejectedByIcc.setMeetingNumber(rejectedByIccResource.getMeetingNumber());
        rejectedByIcc.setReasonForRejection(rejectedByIccResource.getReasonForRejection());
        rejectedByIcc = rejectedByIccRepository.save(rejectedByIcc);
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

        return rejectedByIcc;
    }

    @Override
    public RejectedByICC update(RejectedByICCResource rejectedByIccResource, String username)
            throws CloneNotSupportedException {

        RejectedByICC rejectedByIcc = rejectedByIccRepository.findById(rejectedByIccResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(rejectedByIccResource.getId().toString()));

        Object oldICCFurtherDetail = rejectedByIcc.clone();

        rejectedByIcc.setMeetingDate(rejectedByIccResource.getMeetingDate());
        rejectedByIcc.setMeetingNumber(rejectedByIccResource.getMeetingNumber());
        rejectedByIcc.setReasonForRejection(rejectedByIccResource.getReasonForRejection());
        rejectedByIcc = rejectedByIccRepository.save(rejectedByIcc);

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

        return rejectedByIcc;
    }

    @Override
    public RejectedByICC delete(UUID rejectedByICCId, String username) {
        RejectedByICC rejectedByIcc = rejectedByIccRepository.findById(rejectedByICCId)
                .orElseThrow(() -> new EntityNotFoundException(rejectedByICCId.toString()));
        rejectedByIccRepository.delete(rejectedByIcc);
        return rejectedByIcc;
    }
}
