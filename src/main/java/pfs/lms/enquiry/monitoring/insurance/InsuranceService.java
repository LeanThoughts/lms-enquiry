package pfs.lms.enquiry.monitoring.insurance;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.service.impl.LoanMonitoringService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class InsuranceService implements IInsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final IChangeDocumentService changeDocumentService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitoringService loanMonitoringService;

    @Override
    public Insurance saveInsurance(InsuranceResource resource, String username ) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);

        Insurance insurance = new Insurance();
        insurance.setDocumentTitle(resource.getDocumentTitle());
        insurance.setDocumentType(resource.getDocumentType());
        insurance.setFileReference(resource.getFileReference());
        insurance.setLoanMonitor(loanMonitor);
        insurance.setValidFrom(resource.getValidFrom());
        insurance.setValidTo(resource.getValidTo());
        insurance = insuranceRepository.save(insurance);

        // Change Documents for Promoter Details
//        changeDocumentService.createChangeDocument(
//                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
//                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                promoterDetail,
//                "Created",
//                username,
//                "Monitoring", "Promoter Details Item");

        return insurance;
    }

    @Override
    public Insurance updateInsurance(InsuranceResource resource, String username) throws CloneNotSupportedException {

        Insurance insurance = insuranceRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Insurance oldInsurance = (Insurance) insurance.clone();

        insurance.setDocumentTitle(resource.getDocumentTitle());
        insurance.setDocumentType(resource.getDocumentType());
        insurance.setFileReference(resource.getFileReference());
        insurance.setValidFrom(resource.getValidFrom());
        insurance.setValidTo(resource.getValidTo());
        insurance = insuranceRepository.save(insurance);
        insurance = insuranceRepository.save(insurance);

        // Change Documents for Promoter Details
//        changeDocumentService.createChangeDocument(
//                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
//                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldPromoterDetailItem,
//                promoterDetail,
//                "Updated",
//                username,
//                "Monitoring", "Promoter Details Item");

        return insurance;
    }
}
