package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.riskassessment.RiskAssessment;
import pfs.lms.enquiry.riskassessment.RiskAssessmentRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PreliminaryRiskAssessmentService implements IPreliminaryRiskAssessmentService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final PreliminaryRiskAssessmentRepository preliminaryRiskAssessmentRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public PreliminaryRiskAssessment create(PreliminaryRiskAssessmentResource preliminaryRiskAssessmentResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(preliminaryRiskAssessmentResource.getLoanApplicationId());

        RiskAssessment riskAssessment = riskAssessmentRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    RiskAssessment obj = new RiskAssessment();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = riskAssessmentRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Risk Assessment", "Header");

                    return obj;
                });

        PreliminaryRiskAssessment preliminaryRiskAssessment = new PreliminaryRiskAssessment();
        preliminaryRiskAssessment.setRiskAssessment(riskAssessment);
        preliminaryRiskAssessment.setDateOfAssessment(preliminaryRiskAssessmentResource.getDateOfAssessment());
        preliminaryRiskAssessment.setMdApprovalDate(preliminaryRiskAssessmentResource.getMdApprovalDate());
        preliminaryRiskAssessment.setRemarksByRiskDepartment(preliminaryRiskAssessmentResource.getRemarksByRiskDepartment());
        preliminaryRiskAssessment.setRemarks(preliminaryRiskAssessmentResource.getRemarks());
        preliminaryRiskAssessment = preliminaryRiskAssessmentRepository.save(preliminaryRiskAssessment);
        changeDocumentService.createChangeDocument(
                preliminaryRiskAssessment.getId(),
                preliminaryRiskAssessment.getRiskAssessment().getId().toString(),
                preliminaryRiskAssessment.getId().toString(),
                preliminaryRiskAssessment.getRiskAssessment().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                preliminaryRiskAssessment,
                "Created",
                username,
                "Risk Assessment", "Prelim Risk Assessment");

        return preliminaryRiskAssessment;
    }

    @Override
    public PreliminaryRiskAssessment update(PreliminaryRiskAssessmentResource preliminaryRiskAssessmentResource, String username)
            throws CloneNotSupportedException {

        PreliminaryRiskAssessment preliminaryRiskAssessment = preliminaryRiskAssessmentRepository.findById(preliminaryRiskAssessmentResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(preliminaryRiskAssessmentResource.getId().toString()));

        Object oldPreliminaryRiskAssessment = preliminaryRiskAssessment.clone();

        preliminaryRiskAssessment.setDateOfAssessment(preliminaryRiskAssessmentResource.getDateOfAssessment());
        preliminaryRiskAssessment.setMdApprovalDate(preliminaryRiskAssessmentResource.getMdApprovalDate());
        preliminaryRiskAssessment.setRemarksByRiskDepartment(preliminaryRiskAssessmentResource.getRemarksByRiskDepartment());
        preliminaryRiskAssessment.setRemarks(preliminaryRiskAssessmentResource.getRemarks());
        preliminaryRiskAssessment = preliminaryRiskAssessmentRepository.save(preliminaryRiskAssessment);

        // Change Documents for  Loan Partner
        changeDocumentService.createChangeDocument(
                preliminaryRiskAssessment.getId(),
                preliminaryRiskAssessment.getId().toString(),
                preliminaryRiskAssessment.getId().toString(),
                preliminaryRiskAssessment.getRiskAssessment().getLoanContractId(),
                oldPreliminaryRiskAssessment,
                preliminaryRiskAssessment,
                "Updated",
                username,
                "Appraisal", "Loan Partner");

        return preliminaryRiskAssessment;
    }

    @Override
    public PreliminaryRiskAssessment delete(UUID preliminaryRiskAssessmentId, String username) {
        PreliminaryRiskAssessment preliminaryRiskAssessment = preliminaryRiskAssessmentRepository.findById(preliminaryRiskAssessmentId)
                .orElseThrow(() -> new EntityNotFoundException(preliminaryRiskAssessmentId.toString()));
        preliminaryRiskAssessmentRepository.delete(preliminaryRiskAssessment);
        return preliminaryRiskAssessment;
    }
}
