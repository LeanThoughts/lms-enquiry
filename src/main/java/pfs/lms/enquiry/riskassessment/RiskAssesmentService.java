package pfs.lms.enquiry.riskassessment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletion;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletionRepository;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletionResource;
import pfs.lms.enquiry.action.enquirycompletion.IEnquiryCompletionService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskAssesmentService implements IRiskAssessmentService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    private final IChangeDocumentService changeDocumentService;

    @Override
    public RiskAssessment processRejection(RiskAssessment riskAssessment, String username) throws CloneNotSupportedException {
        Object OldRiskAssessment = riskAssessment.clone();
        riskAssessment.setWorkFlowStatusCode(04);
        riskAssessment.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                riskAssessment.getId(),
                riskAssessment.getId().toString(),
                null,
                riskAssessment.getLoanApplication().getLoanContractId(),
                OldRiskAssessment,
                riskAssessment,
                "Updated",
                username,
                "Risk Assessment", "Header");
        riskAssessmentRepository.save(riskAssessment);

        return riskAssessment;
    }

    @Override
    public RiskAssessment processRiskAssessmentApproval(RiskAssessment riskAssessment, String username) throws Exception {

        LoanApplication loanApplication = riskAssessment.getLoanApplication();
        Object oldLoanApplication = loanApplication.clone();

        if (loanApplication == null){
            throw new Exception("Loan Application is NULL during Preliminary Risk Assessment Approval : " + riskAssessment.getId());
        }

        loanApplication.setFunctionalStatus(10);
        loanApplication.setFunctionalStatusDescription("Preliminary Risk Assessment Completed");

        loanApplication.setPostedInSAP(0);
        loanApplication.setTechnicalStatus(4);

        // Change Documents for Enquiry Completion
        changeDocumentService.createChangeDocument(
                loanApplication.getId(),
                loanApplication.getId().toString(),
                loanApplication.getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                oldLoanApplication,
                loanApplication,
                "Updated",
                username,
                "LoanApplication", "Loan Application");


        loanApplicationRepository.save(loanApplication);
        return riskAssessment;
    }
}
