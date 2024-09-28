package pfs.lms.enquiry.iccapproval.risknotification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.domain.WorkflowApprover;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.mail.service.RiskNotificationEmailService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;
import pfs.lms.enquiry.riskassessment.RiskAssessmentRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RiskNotificationService implements IRiskNotificationService {
    private final RiskAssessmentRepository riskAssessmentRepository;

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final RiskNotificationRepository riskNotificationRepository;
    private final IChangeDocumentService changeDocumentService;
    private final RiskNotificationEmailService riskNotificationEmailService;
    private final WorkflowApproverRepository workflowApproverRepository;
    private final UserRepository userRepository;

    @Override
    public RiskNotification create(RiskNotificationResource riskNotificationResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(riskNotificationResource.getLoanApplicationId());

        ICCApproval iccApproval = iccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ICCApproval obj = new ICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = iccApprovalRepository.save(obj);

                    // Change Documents for ICCApproval
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "ICCApproval", "Header");

                    return obj;
                });

        RiskNotification riskNotification = new RiskNotification();
        riskNotification.setIccApproval(iccApproval);
        riskNotification.setSerialNumber(riskNotificationRepository.findByIccApprovalId(iccApproval.getId()).size() + 1);
        riskNotification.setNotificationDate(riskNotificationResource.getNotificationDate());
        riskNotification.setRemarks(riskNotificationResource.getRemarks());
        riskNotification = riskNotificationRepository.save(riskNotification);

        changeDocumentService.createChangeDocument(
                riskNotification.getId(),
                riskNotification.getId().toString(),
                riskNotification.getId().toString(),
                riskNotification.getIccApproval().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                riskNotification,
                "Created",
                username,
                "ICCApproval", "Preliminary Risk Notification");

        return riskNotification;
    }

    @Override
    public RiskNotification update(RiskNotificationResource riskNotificationResource, String username)
            throws CloneNotSupportedException {

        RiskNotification riskNotification = riskNotificationRepository.findById(riskNotificationResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(riskNotificationResource.getId().toString()));

        Object oldRiskNotification = riskNotification.clone();

        riskNotification.setNotificationDate(riskNotificationResource.getNotificationDate());
        riskNotification.setRemarks(riskNotificationResource.getRemarks());
        riskNotification = riskNotificationRepository.save(riskNotification);

        // Change Documents for  Risk Notification
        changeDocumentService.createChangeDocument(
                riskNotification.getId(),
                riskNotification.getId().toString(),
                riskNotification.getId().toString(),
                riskNotification.getIccApproval().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                oldRiskNotification,
                "Updated",
                username,
                "ICCApproval", "Preliminary Risk Notification");

        return riskNotification;
    }

    @Override
    public RiskNotification sendNotification(RiskNotificationResource riskNotificationResource, String username) throws Exception {

        LoanApplication loanApplication = loanApplicationRepository.getOne(riskNotificationResource.getLoanApplicationId());

        //Get Risk Department Head
         WorkflowApprover workflowApprover = workflowApproverRepository.findByProcessName("PrelimRiskAssessment");
         if (workflowApprover == null ){
             throw new Exception("Workflow approver email not maintained for process name PrelimRiskAssessment");
        }
        User user = userRepository.findByEmail(workflowApprover.getApproverEmail());
         if (user == null) {
             throw new Exception("PrelimRiskAssessment: User not found for email : " + workflowApprover.getApproverEmail());
         }

        riskNotificationEmailService.sendPremlimRiskNotification(user,loanApplication,riskNotificationResource.getRemarks());
         RiskNotification riskNotification = riskNotificationRepository.getOne(riskNotificationResource.getId());
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
