package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

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
public class ICCReasonForDelayService implements IICCReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final ICCReasonForDelayRepository iccReasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ICCReasonForDelay create(ICCReasonForDelayResource iccReasonForDelayResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(iccReasonForDelayResource.getLoanApplicationId());

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

        ICCReasonForDelay iccReasonForDelay = new ICCReasonForDelay();
        iccReasonForDelay.setIccApproval(iccApproval);
        iccReasonForDelay.setReasonForDelay(iccReasonForDelayResource.getReasonForDelay());
        iccReasonForDelay.setDate(iccReasonForDelayResource.getDate());
        iccReasonForDelay = iccReasonForDelayRepository.save(iccReasonForDelay);
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

        return iccReasonForDelay;
    }

    @Override
    public ICCReasonForDelay update(ICCReasonForDelayResource iccReasonForDelayResource, String username)
            throws CloneNotSupportedException {

        ICCReasonForDelay iccReasonForDelay = iccReasonForDelayRepository.findById(iccReasonForDelayResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(iccReasonForDelayResource.getId().toString()));

        Object oldICCFurtherDetail = iccReasonForDelay.clone();

        iccReasonForDelay.setReasonForDelay(iccReasonForDelayResource.getReasonForDelay());
        iccReasonForDelay.setDate(iccReasonForDelayResource.getDate());
        iccReasonForDelay = iccReasonForDelayRepository.save(iccReasonForDelay);

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

        return iccReasonForDelay;
    }

    @Override
    public ICCReasonForDelay delete(UUID iccFurtherDetailId, String username) {
        ICCReasonForDelay iccReasonForDelay = iccReasonForDelayRepository.findById(iccFurtherDetailId)
                .orElseThrow(() -> new EntityNotFoundException(iccFurtherDetailId.toString()));
        iccReasonForDelayRepository.delete(iccReasonForDelay);
        return iccReasonForDelay;
    }
}
