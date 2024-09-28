package pfs.lms.enquiry.iccapproval.risknotification;

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
public class RiskNotificationService implements IRiskNotificationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final RiskNotificationRepository riskNotificationRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public RiskNotification create(RiskNotificationResource riskNotificationResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(riskNotificationResource.getLoanApplicationId());

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

        RiskNotification riskNotification = new RiskNotification();
        riskNotification.setIccApproval(iccApproval);
        riskNotification.setSerialNumber(riskNotificationRepository.findByIccApprovalId(iccApproval.getId()).size() + 1);
        riskNotification.setNotificationDate(riskNotificationResource.getNotificationDate());
        riskNotification.setRemarks(riskNotificationResource.getRemarks());
        riskNotification = riskNotificationRepository.save(riskNotification);
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

        return riskNotification;
    }

    @Override
    public RiskNotification update(RiskNotificationResource riskNotificationResource, String username)
            throws CloneNotSupportedException {

        RiskNotification riskNotification = riskNotificationRepository.findById(riskNotificationResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(riskNotificationResource.getId().toString()));

        Object oldICCFurtherDetail = riskNotification.clone();

        riskNotification.setNotificationDate(riskNotificationResource.getNotificationDate());
        riskNotification.setRemarks(riskNotificationResource.getRemarks());
        riskNotification = riskNotificationRepository.save(riskNotification);

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

        return riskNotification;
    }

    @Override
    public RiskNotification delete(UUID riskNotificationId, String username) {
        RiskNotification riskNotification = riskNotificationRepository.findById(riskNotificationId)
                .orElseThrow(() -> new EntityNotFoundException(riskNotificationId.toString()));
        riskNotificationRepository.delete(riskNotification);
        return riskNotification;
    }
}
