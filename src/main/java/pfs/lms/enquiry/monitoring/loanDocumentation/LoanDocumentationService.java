package pfs.lms.enquiry.monitoring.loanDocumentation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.npa.INPAService;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPARepository;
import pfs.lms.enquiry.monitoring.npa.NPAResource;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.service.ILoanMonitoringService;
import pfs.lms.enquiry.repository.DocumentationStatusRepository;
import pfs.lms.enquiry.repository.DocumentationTypeRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.impl.ChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class LoanDocumentationService implements ILoanDocumentationService {

    private final LoanDocumentationRepository loanDocumentationRepository;

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ChangeDocumentService changeDocumentService;
    private final ILoanMonitoringService loanMonitoringService;
    private final DocumentationTypeRepository documentationTypeRepository;
    private final DocumentationStatusRepository documentationStatusRepository;

    @Override
    public LoanDocumentation saveLoanDocumentation(LoanDocumentationResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if (loanMonitor == null)
            loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication,username);

        LoanDocumentation loanDocumentation = resource.getLoanDocumentation();
        loanDocumentation.setLoanMonitor(loanMonitor);
        loanDocumentation = loanDocumentationRepository.save(loanDocumentation);

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),
                loanDocumentation.getId().toString(),
                loanMonitor.getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                loanMonitor,
                "Created",
                username,
                "Monitoring", "Loan Documentation");

        return loanDocumentation;
    }

    @Override
    public LoanDocumentation updateLoanDocumentation(LoanDocumentationResource resource, String username) throws CloneNotSupportedException {
        final LoanDocumentation existingLoanDocumentation = loanDocumentationRepository
                .findById(resource.getLoanDocumentation().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getLoanDocumentation().getId().toString()));

        Object oldLoanDocumentation = existingLoanDocumentation.clone();

        existingLoanDocumentation.setSerialNumber(resource.getLoanDocumentation().getSerialNumber());
        existingLoanDocumentation.setDocumentationTypeCode( resource.getLoanDocumentation().getDocumentationTypeCode());
        existingLoanDocumentation.setDocumentationTypeDescription(documentationTypeRepository.findByCode(resource.getLoanDocumentation().getDocumentationTypeCode()).getValue());
        existingLoanDocumentation.setExecutionDate( resource.getLoanDocumentation().getExecutionDate());
        existingLoanDocumentation.setApprovalDate( resource.getLoanDocumentation().getApprovalDate());
        existingLoanDocumentation.setLoanDocumentationStatusCode( resource.getLoanDocumentation().getLoanDocumentationStatusCode());
        existingLoanDocumentation.setLoanDocumentationStatusCodeDescription(documentationStatusRepository.findByCode(resource.getLoanDocumentation().getLoanDocumentationStatusCode()).getValue());
        existingLoanDocumentation.setDocumentTitle(resource.getLoanDocumentation().getDocumentTitle() );
        existingLoanDocumentation.setDocumentType(resource.getLoanDocumentation().getDocumentType() );
        existingLoanDocumentation.setFileReference(resource.getLoanDocumentation().getFileReference() );
        existingLoanDocumentation.setRemarks( resource.getLoanDocumentation().getRemarks());

        LoanDocumentation npa = loanDocumentationRepository.save(existingLoanDocumentation);

        // Change Documents for LoanDocumentation
        changeDocumentService.createChangeDocument(
                npa.getLoanMonitor().getId(), npa.getId().toString(),null,
                npa.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLoanDocumentation,
                existingLoanDocumentation,
                "Updated",
                username,
                "Monitoring", "Loan Documentation");

        return npa;
    }

    @Override
    public List<LoanDocumentation> getLoanDocumentation(String loanApplicationId, String name) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        return loanDocumentationRepository.findByLoanMonitor(loanMonitor);
    }
}
