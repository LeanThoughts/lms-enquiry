package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.service.ILoanMonitoringService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.impl.ChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class NPAService implements INPAService {

    private final NPARepository npaRepository;

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ChangeDocumentService changeDocumentService;
    private final ILoanMonitoringService loanMonitoringService;

    @Override
    public NPA saveNPA(NPAResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if (loanMonitor == null)
            loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication,username);

        NPA npa = resource.getNpa();
        npa.setLoanMonitor(loanMonitor);
        npa = npaRepository.save(npa);

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),
                npa.getId().toString(),
                loanMonitor.getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                loanMonitor,
                "Created",
                username,
                "Monitoring", "NPA");

        return npa;
    }

    @Override
    public NPA updateNPA(NPAResource resource, String username) throws CloneNotSupportedException {
        final NPA existingNPA = npaRepository
                .findById(resource.getNpa().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getNpa().getId().toString()));

        Object oldNPA = existingNPA.clone();

        existingNPA.setAssetClass(resource.getNpa().getAssetClass());
        existingNPA.setNpaDeclarationDate(resource.getNpa().getNpaDeclarationDate());
        existingNPA.setTotalLoanAsset(resource.getNpa().getTotalLoanAsset());
        existingNPA.setSecuredLoanAsset(resource.getNpa().getSecuredLoanAsset());
        existingNPA.setUnSecuredLoanAsset(resource.getNpa().getUnSecuredLoanAsset());
        existingNPA.setRestructuringType(resource.getNpa().getRestructuringType());
        existingNPA.setSmaCategory(resource.getNpa().getSmaCategory());
        existingNPA.setFraudDate(resource.getNpa().getFraudDate());
        existingNPA.setImpairmentReserve(resource.getNpa().getImpairmentReserve());
        existingNPA.setProvisionAmount(resource.getNpa().getProvisionAmount());
        NPA npa = npaRepository.save(existingNPA);

        // Change Documents for NPA
        changeDocumentService.createChangeDocument(
                npa.getLoanMonitor().getId(),
                npa.getId().toString(),
                existingNPA.getLoanMonitor().getId().toString(),
                npa.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldNPA,
                npa,
                "Updated",
                username,
                "Monitoring", "NPA");

        return npa;
    }

    @Override
    public NPA getNPA(String loanApplicationId, String name) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        return npaRepository.findByLoanMonitor(loanMonitor);
    }
}