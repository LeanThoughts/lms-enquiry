package pfs.lms.enquiry.monitoring.endusecertificate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.service.impl.LoanMonitoringService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class EndUseCertificateService implements IEndUseCertificateService {

    private final EndUseCertificateRepository endUseCertificateRepository;
    private final IChangeDocumentService changeDocumentService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitoringService loanMonitoringService;
    private final LoanMonitorRepository loanMonitorRepository;

    @Override
    public EndUseCertificate saveEndUseCertificate(EndUseCertificateResource resource, String username ) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);

        EndUseCertificate endUseCertificate = new EndUseCertificate();
        endUseCertificate.setSerialNumber(endUseCertificateRepository.findByLoanMonitor(loanMonitor).size() + 1);
        endUseCertificate.setDocumentTitle(resource.getEndUseCertificate().getDocumentTitle());
        endUseCertificate.setDocumentType(resource.getEndUseCertificate().getDocumentType());
        endUseCertificate.setFileReference(resource.getEndUseCertificate().getFileReference());
        endUseCertificate.setLoanMonitor(loanMonitor);
        endUseCertificate.setEndUseCertificateDate(resource.getEndUseCertificate().getEndUseCertificateDate());
        endUseCertificate.setEndUseCertificateDueDate(resource.getEndUseCertificate().getEndUseCertificateDueDate());
        endUseCertificate.setEventDate(resource.getEndUseCertificate().getEventDate());
        endUseCertificate.setRemarks(resource.getEndUseCertificate().getRemarks());
        endUseCertificate = endUseCertificateRepository.save(endUseCertificate);

        // Change Documents for End Use Certificate Details
        changeDocumentService.createChangeDocument(
                endUseCertificate.getLoanMonitor().getId(),
                endUseCertificate.getId().toString(),null,
                endUseCertificate.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                endUseCertificate,
                "Created",
                username,
                "Monitoring", "End Use Certificate");

        return endUseCertificate;
    }

    @Override
    public EndUseCertificate updateEndUseCertificate(EndUseCertificateResource resource, String username) throws CloneNotSupportedException {

        EndUseCertificate endUseCertificate = endUseCertificateRepository.findById(resource.getEndUseCertificate().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getEndUseCertificate().getId().toString()));

        EndUseCertificate oldEndUseCertificate = (EndUseCertificate) endUseCertificate.clone();

        endUseCertificate.setSerialNumber(resource.getEndUseCertificate().getSerialNumber());
        endUseCertificate.setDocumentTitle(resource.getEndUseCertificate().getDocumentTitle());
        endUseCertificate.setDocumentType(resource.getEndUseCertificate().getDocumentType());
        endUseCertificate.setFileReference(resource.getEndUseCertificate().getFileReference());
        endUseCertificate.setEndUseCertificateDate(resource.getEndUseCertificate().getEndUseCertificateDate());
        endUseCertificate.setEndUseCertificateDueDate(resource.getEndUseCertificate().getEndUseCertificateDueDate());
        endUseCertificate.setEventDate(resource.getEndUseCertificate().getEventDate());
        endUseCertificate.setRemarks(resource.getEndUseCertificate().getRemarks());
        endUseCertificate = endUseCertificateRepository.save(endUseCertificate);

        // Change Documents for End Use Certificate Details
        changeDocumentService.createChangeDocument(
                endUseCertificate.getLoanMonitor().getId(),
                endUseCertificate.getId().toString(),
                null,
                endUseCertificate.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldEndUseCertificate,
                endUseCertificate,
                "Updated",
                username,
                "Monitoring", "End Use Certificate");

        return endUseCertificate;
    }

    @Override
    public List<EndUseCertificate> getEndUseCertificates(UUID loanApplicationId) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(loanApplicationId);
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        return endUseCertificateRepository.findByLoanMonitorOrderBySerialNumberDesc(loanMonitor);
    }

    @Override
    public EndUseCertificate deleteEndUseCertificate(UUID endUseCertificateId, String username) {
        EndUseCertificate endUseCertificate = endUseCertificateRepository.getOne(endUseCertificateId);
        LoanMonitor loanMonitor = endUseCertificate.getLoanMonitor();


        // Change Documents for End Use Certificate Delete
        changeDocumentService.createChangeDocument(
                endUseCertificate.getLoanMonitor().getId(),
                endUseCertificate.getId().toString(),
                endUseCertificate.getLoanMonitor().getId().toString(),
                endUseCertificate.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                endUseCertificate,
                "Deleted",
                username,
                "Monitoring", "End Use Certificate");

        endUseCertificateRepository.delete(endUseCertificate);
        updateSerialNumbers(loanMonitor);
        return endUseCertificate;
    }

    private void updateSerialNumbers(LoanMonitor loanMonitor) {
        List<EndUseCertificate> endUseCertificates = endUseCertificateRepository.findByLoanMonitorOrderBySerialNumberDesc(loanMonitor);
        int size = endUseCertificates.size();
        for(EndUseCertificate endUseCertificate : endUseCertificates) {
            if (endUseCertificate.getSerialNumber() != size) {
                endUseCertificate.setSerialNumber(size);
                endUseCertificateRepository.save(endUseCertificate);
            }
            size--;
        }
    }
}
