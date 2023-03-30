package pfs.lms.enquiry.monitoring.loanDocumentation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
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
        loanDocumentation.setSerialNumber(loanDocumentationRepository.findByLoanMonitor(loanMonitor).size() + 1);
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

        LoanDocumentation loanDocumentation = loanDocumentationRepository.save(existingLoanDocumentation);

        // Change Documents for LoanDocumentation
        changeDocumentService.createChangeDocument(
                loanDocumentation.getLoanMonitor().getId(),
                loanDocumentation.getId().toString(),
                null,
                loanDocumentation.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLoanDocumentation,
                existingLoanDocumentation,
                "Updated",
                username,
                "Monitoring", "Loan Documentation");

        return loanDocumentation;
    }

    @Override
    public List<LoanDocumentation> getLoanDocumentation(String loanApplicationId, String name) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        return loanDocumentationRepository.findByLoanMonitor(loanMonitor);
    }

    @Override
    public LoanDocumentation deleteLoanDocumentation(UUID loanDocumentationId, String moduleName, String username) {
        LoanDocumentation loanDocumentation = loanDocumentationRepository.getOne(loanDocumentationId);
        LoanMonitor loanMonitor = loanDocumentation.getLoanMonitor();

//        UUID loanBusinessProcessObjectId =
//                loanMonitoringService.getLoanBusinessProcessObjectId(loanDocumentation.getLoanMonitor(),
//                        loanDocumentation.getLoanAppraisal() ,moduleName);

        // Create Change Document for LLC
        changeDocumentService.createChangeDocument(
                loanDocumentation.getLoanMonitor().getId(),
                loanDocumentation.getId().toString(),
                loanDocumentation.getLoanMonitor().getId().toString(),
                loanDocumentation.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                loanDocumentation,
                "Deleted",
                username,
                moduleName, "Loan Documentation" );

        loanDocumentationRepository.delete(loanDocumentation);
        updateLoanDocumentationSerialNumbers(loanMonitor);
        return loanDocumentation;
    }

    private void updateLoanDocumentationSerialNumbers(LoanMonitor loanMonitor) {
        List<LoanDocumentation> LoanDocumentations = loanDocumentationRepository.findByLoanMonitor(loanMonitor);
        int size = LoanDocumentations.size();
        for(LoanDocumentation LoanDocumentation: LoanDocumentations) {
            if (LoanDocumentation.getSerialNumber() != size) {
                LoanDocumentation.setSerialNumber(size);
                loanDocumentationRepository.save(LoanDocumentation);
            }
            size--;
        }
    }
}
