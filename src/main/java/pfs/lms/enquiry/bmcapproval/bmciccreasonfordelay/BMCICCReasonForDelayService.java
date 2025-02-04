package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BmcICCApproval;
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
public class BMCICCReasonForDelayService implements IBMCICCReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository BMCICCApprovalRepository;
    private final BMCICCReasonForDelayRepository BMCICCReasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BmcICCReasonForDelay create(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmciccReasonForDelayResource.getLoanApplicationId());

        BmcICCApproval BMCICCApproval = BMCICCApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcICCApproval obj = new BmcICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = BMCICCApprovalRepository.save(obj);

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

        BmcICCReasonForDelay BMCICCReasonForDelay = new BmcICCReasonForDelay();
        BMCICCReasonForDelay.setBmcICCApproval(BMCICCApproval);
        BMCICCReasonForDelay.setReasonForDelay(bmciccReasonForDelayResource.getReasonForDelay());
        BMCICCReasonForDelay.setDate(bmciccReasonForDelayResource.getDate());
        BMCICCReasonForDelay = BMCICCReasonForDelayRepository.save(BMCICCReasonForDelay);

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

        return BMCICCReasonForDelay;
    }

    @Override
    public BmcICCReasonForDelay update(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username)
            throws CloneNotSupportedException {

        BmcICCReasonForDelay BMCICCReasonForDelay = BMCICCReasonForDelayRepository.findById(bmciccReasonForDelayResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmciccReasonForDelayResource.getId().toString()));

        Object oldICCFurtherDetail = BMCICCReasonForDelay.clone();

        BMCICCReasonForDelay.setReasonForDelay(bmciccReasonForDelayResource.getReasonForDelay());
        BMCICCReasonForDelay.setDate(bmciccReasonForDelayResource.getDate());
        BMCICCReasonForDelay = BMCICCReasonForDelayRepository.save(BMCICCReasonForDelay);

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

        return BMCICCReasonForDelay;
    }

    @Override
    public BmcICCReasonForDelay delete(UUID iccFurtherDetailId, String username) {
        BmcICCReasonForDelay BMCICCReasonForDelay = BMCICCReasonForDelayRepository.findById(iccFurtherDetailId)
                .orElseThrow(() -> new EntityNotFoundException(iccFurtherDetailId.toString()));

        BmcICCApproval BMCICCApproval = BMCICCApprovalRepository.getOne(BMCICCReasonForDelay.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        BMCICCApprovalRepository.save(BMCICCApproval);

        BMCICCReasonForDelayRepository.delete(BMCICCReasonForDelay);
        
        return BMCICCReasonForDelay;
    }
}
