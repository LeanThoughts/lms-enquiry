package pfs.lms.enquiry.iccapproval.approvalbyicc;

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
public class ApprovalByICCService implements IApprovalByICCService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final ApprovalByICCRepository approvalByIccRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ApprovalByICC create(ApprovalByICCResource approvalByIccResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(approvalByIccResource.getLoanApplicationId());

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

        ApprovalByICC approvalByIcc = new ApprovalByICC();
        approvalByIcc.setIccApproval(iccApproval);
        approvalByIcc.setMeetingDate(approvalByIccResource.getMeetingDate());
        approvalByIcc.setMeetingNumber(approvalByIccResource.getMeetingNumber());
        approvalByIcc.setRemarks(approvalByIccResource.getRemarks());
        approvalByIcc = approvalByIccRepository.save(approvalByIcc);
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

        return approvalByIcc;
    }

    @Override
    public ApprovalByICC update(ApprovalByICCResource approvalByIccResource, String username)
            throws CloneNotSupportedException {

        ApprovalByICC approvalByIcc = approvalByIccRepository.findById(approvalByIccResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(approvalByIccResource.getId().toString()));

        Object oldICCFurtherDetail = approvalByIcc.clone();

        approvalByIcc.setMeetingDate(approvalByIccResource.getMeetingDate());
        approvalByIcc.setMeetingNumber(approvalByIccResource.getMeetingNumber());
        approvalByIcc.setRemarks(approvalByIccResource.getRemarks());
        approvalByIcc = approvalByIccRepository.save(approvalByIcc);

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

        return approvalByIcc;
    }

    @Override
    public ApprovalByICC delete(UUID approvalByICCId, String username) {
        ApprovalByICC approvalByIcc = approvalByIccRepository.findById(approvalByICCId)
                .orElseThrow(() -> new EntityNotFoundException(approvalByICCId.toString()));
        approvalByIccRepository.delete(approvalByIcc);
        return approvalByIcc;
    }
}
