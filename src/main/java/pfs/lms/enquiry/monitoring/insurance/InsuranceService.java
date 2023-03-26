package pfs.lms.enquiry.monitoring.insurance;

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
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class InsuranceService implements IInsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final IChangeDocumentService changeDocumentService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitoringService loanMonitoringService;
    private final LoanMonitorRepository loanMonitorRepository;

    @Override
    public Insurance saveInsurance(InsuranceResource resource, String username ) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);

        Insurance insurance = new Insurance();
        insurance.setSerialNumber(insuranceRepository.findByLoanMonitor(loanMonitor).size() + 1);
        insurance.setDocumentTitle(resource.getInsurance().getDocumentTitle());
        insurance.setDocumentType(resource.getInsurance().getDocumentType());
        insurance.setFileReference(resource.getInsurance().getFileReference());
        insurance.setLoanMonitor(loanMonitor);
        insurance.setValidFrom(resource.getInsurance().getValidFrom());
        insurance.setValidTo(resource.getInsurance().getValidTo());
        insurance = insuranceRepository.save(insurance);

        // Change Documents for Insurance Details
        changeDocumentService.createChangeDocument(
                insurance.getLoanMonitor().getId(),
                insurance.getId().toString(),null,
                insurance.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                insurance,
                "Created",
                username,
                "Monitoring", "Insurance");

        return insurance;
    }

    @Override
    public Insurance updateInsurance(InsuranceResource resource, String username) throws CloneNotSupportedException {

        Insurance insurance = insuranceRepository.findById(resource.getInsurance().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getInsurance().getId().toString()));

        Insurance oldInsurance = (Insurance) insurance.clone();

        insurance.setSerialNumber(resource.getInsurance().getSerialNumber());
        insurance.setDocumentTitle(resource.getInsurance().getDocumentTitle());
        insurance.setDocumentType(resource.getInsurance().getDocumentType());
        insurance.setFileReference(resource.getInsurance().getFileReference());
        insurance.setValidFrom(resource.getInsurance().getValidFrom());
        insurance.setValidTo(resource.getInsurance().getValidTo());
        insurance = insuranceRepository.save(insurance);

        // Change Documents for Promoter Details
        changeDocumentService.createChangeDocument(
                insurance.getLoanMonitor().getId(),
                insurance.getId().toString(),
                null,
                insurance.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldInsurance,
                insurance,
                "Updated",
                username,
                "Monitoring", "Insurance");

        return insurance;
    }

    @Override
    public List<Insurance> getInsurances(UUID loanApplicationId) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(loanApplicationId);
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        return insuranceRepository.findByLoanMonitorOrderBySerialNumberDesc(loanMonitor);
    }
}
