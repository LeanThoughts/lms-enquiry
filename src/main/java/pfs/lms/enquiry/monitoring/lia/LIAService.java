package pfs.lms.enquiry.monitoring.lia;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LIAService implements ILIAService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final LIARepository liaRepository;
    private final LIAReportAndFeeRepository liaReportAndFeeRepository;
    
    @Override
    public LendersInsuranceAdvisor saveLIA(LIAResource resource, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor.setWorkFlowStatusCode(01); loanMonitor.setWorkFlowStatusDescription("Created");
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
//            changeDocumentService.createChangeDocument(
//                    loanMonitor.getId(), loanMonitor.getId().toString(), null,
//                    loanApplication.getLoanContractId(),
//                    null,
//                    loanMonitor,
//                    "Created",
//                    username,
//                    "Monitoring", "Header");

        }
        LendersInsuranceAdvisor lendersInsuranceAdvisor = resource.getLendersInsuranceAdvisor();
        lendersInsuranceAdvisor.setSerialNumber(liaRepository.findByLoanMonitor(loanMonitor).size() + 1);
        lendersInsuranceAdvisor.setLoanMonitor(loanMonitor);
        lendersInsuranceAdvisor.setAdvisor(resource.getLendersInsuranceAdvisor().getAdvisor());
        lendersInsuranceAdvisor.setBpCode(resource.getLendersInsuranceAdvisor().getBpCode());
        lendersInsuranceAdvisor.setName(resource.getLendersInsuranceAdvisor().getName());
        lendersInsuranceAdvisor.setDateOfAppointment(resource.getLendersInsuranceAdvisor().getDateOfAppointment());
        lendersInsuranceAdvisor.setContractPeriodFrom(resource.getLendersInsuranceAdvisor().getContractPeriodFrom());
        lendersInsuranceAdvisor.setContractPeriodTo(resource.getLendersInsuranceAdvisor().getContractPeriodTo());
        lendersInsuranceAdvisor.setContactPerson(resource.getLendersInsuranceAdvisor().getContactPerson());
        lendersInsuranceAdvisor.setContactNumber(resource.getLendersInsuranceAdvisor().getContactNumber());
        lendersInsuranceAdvisor.setEmail(resource.getLendersInsuranceAdvisor().getEmail());
        lendersInsuranceAdvisor = liaRepository.save(lendersInsuranceAdvisor);

        // Create Change Document for LIE
//        changeDocumentService.createChangeDocument(
//                loanMonitor.getId(),lendersInsuranceAdvisor.getId(),null,
//                loanApplication.getLoanContractId(),
//                null,
//                loanMonitor,
//                "Created",
//                username,
//                "Monitoring" , "Lenders Independent Engineer" );

        return lendersInsuranceAdvisor;
    }

    @Override
    public LendersInsuranceAdvisor updateLIA(LIAResource resource, String username) throws CloneNotSupportedException {
        LendersInsuranceAdvisor lia
                = liaRepository.getOne(resource.getLendersInsuranceAdvisor().getId());

        //Clone the LIE Object for Change Document
        Object oldLendersIndependentEngineer = lia.clone();

        lia.setAdvisor(resource.getLendersInsuranceAdvisor().getAdvisor());
        lia.setBpCode(resource.getLendersInsuranceAdvisor().getBpCode());
        lia.setName(resource.getLendersInsuranceAdvisor().getName());
        lia.setDateOfAppointment(resource.getLendersInsuranceAdvisor().getDateOfAppointment());
        lia.setContractPeriodFrom(resource.getLendersInsuranceAdvisor().getContractPeriodFrom());
        lia.setContractPeriodTo(resource.getLendersInsuranceAdvisor().getContractPeriodTo());
        lia.setContactPerson(resource.getLendersInsuranceAdvisor().getContactPerson());
        lia.setContactNumber(resource.getLendersInsuranceAdvisor().getContactNumber());
        lia.setEmail(resource.getLendersInsuranceAdvisor().getEmail());
        lia = liaRepository.save(lia);

        //Create Change Document
//        changeDocumentService.createChangeDocument(
//                existingLendersIndependentEngineer.getLoanMonitor().getId(),existingLendersIndependentEngineer.getId(),null,
//                existingLendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldLendersIndependentEngineer,
//                existingLendersIndependentEngineer,
//                "Updated",
//                username,
//                "Monitoring", "Lenders Independent Engineer" );

        return lia;
    }

    @Override
    public List<LIAResource> getLendersInsuranceAdvisors(String loanApplicationId) {
        List<LIAResource> liaResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<LendersInsuranceAdvisor> list = liaRepository.findByLoanMonitor(loanMonitor);
            list.forEach(
                    lendersInsuranceAdvisor -> {
                        LIAResource liaResource = new LIAResource();
                        liaResource.setLoanApplicationId(loanApplication.getId());
                        liaResource.setLendersInsuranceAdvisor(lendersInsuranceAdvisor);
                        liaResources.add(liaResource);
                    }
            );
        }
        Collections.sort(liaResources, Comparator.comparingInt((LIAResource a) ->
                a.getLendersInsuranceAdvisor().getSerialNumber()).reversed());
        return liaResources;
    }

    @Override
    public LIAReportAndFee saveLIAReportAndFee(LIAReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        LendersInsuranceAdvisor lendersInsuranceAdvisor = liaRepository.getOne(resource.getLendersInsuranceAdvisorId());
        LIAReportAndFee liaReportAndFee = resource.getLiaReportAndFee();
        liaReportAndFee.setSerialNumber(liaReportAndFeeRepository.findByLendersInsuranceAdvisor(lendersInsuranceAdvisor).size() + 1);
        liaReportAndFee.setLendersInsuranceAdvisor(lendersInsuranceAdvisor);
        liaReportAndFee = liaReportAndFeeRepository.save(liaReportAndFee);

        // Create Change Document for LIE Report and Fee
//        changeDocumentService.createChangeDocument(
//                lendersInsuranceAdvisor.getLoanMonitor().getId(), liaReportAndFee.getId(),lendersInsuranceAdvisor.getId(),
//                lendersInsuranceAdvisor.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                liaReportAndFee,
//                "Created",
//                username,
//                "Monitoring" , "LIE Report And Fee" );

        return liaReportAndFee;
    }

    @Override
    public LIAReportAndFee updateLIAReportAndFee(LIAReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        LIAReportAndFee existingliaReportAndFee
                = liaReportAndFeeRepository.getOne(resource.getLiaReportAndFee().getId());

        Object oldLiaReportAndFee = existingliaReportAndFee.clone();

        existingliaReportAndFee.setReportType(resource.getLiaReportAndFee().getReportType());
        existingliaReportAndFee.setDateOfReceipt(resource.getLiaReportAndFee().getDateOfReceipt());
        existingliaReportAndFee.setInvoiceDate(resource.getLiaReportAndFee().getInvoiceDate());
        existingliaReportAndFee.setInvoiceNo(resource.getLiaReportAndFee().getInvoiceNo());
        existingliaReportAndFee.setFeeAmount(resource.getLiaReportAndFee().getFeeAmount());
        existingliaReportAndFee.setStatusOfFeeReceipt(resource.getLiaReportAndFee().getStatusOfFeeReceipt());
        existingliaReportAndFee.setStatusOfFeePaid(resource.getLiaReportAndFee().getStatusOfFeePaid());
        existingliaReportAndFee.setDocumentTitle(resource.getLiaReportAndFee().getDocumentTitle());
        existingliaReportAndFee.setNextReportDate(resource.getLiaReportAndFee().getNextReportDate());
        existingliaReportAndFee.setFileReference(resource.getLiaReportAndFee().getFileReference());
        existingliaReportAndFee = liaReportAndFeeRepository.save(existingliaReportAndFee);

        // Create Change Document for LIE Report And Fee
//        changeDocumentService.createChangeDocument(
//                existingliaReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getId(),
//                existingliaReportAndFee.getId(),existingliaReportAndFee.getLendersIndependentEngineer().getId(),
//                existingliaReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldLieReportAndFee,
//                existingliaReportAndFee,
//                "Updated",
//                username,
//                "Monitoring" , "LIE Report And Fee" );

        return existingliaReportAndFee;
    }

    @Override
    public List<LIAReportAndFeeResource> getLIAReportAndFee(String lendersInsuranceAdvisorId) {
        List<LIAReportAndFeeResource>  liaReportAndFeeResources  = new ArrayList<>();
        LendersInsuranceAdvisor lendersInsuranceAdvisor = liaRepository.getOne(lendersInsuranceAdvisorId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(lendersInsuranceAdvisor != null) {
            List<LIAReportAndFee> liaReportAndFees
                    = liaReportAndFeeRepository.findByLendersInsuranceAdvisor(lendersInsuranceAdvisor);
            liaReportAndFees.forEach(
                    liaReportAndFee -> {
                        LIAReportAndFeeResource liaReportAndFeeResource = new LIAReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        liaReportAndFeeResource.setLendersInsuranceAdvisorId(lendersInsuranceAdvisor.getId());
                        liaReportAndFeeResource.setLiaReportAndFee(liaReportAndFee);
                        liaReportAndFeeResources.add(liaReportAndFeeResource);
                    }
            );
        }
        Collections.sort(liaReportAndFeeResources, Comparator.comparingInt((LIAReportAndFeeResource a) ->
                a.getLiaReportAndFee().getSerialNumber()).reversed());
        return liaReportAndFeeResources;
    }
}
