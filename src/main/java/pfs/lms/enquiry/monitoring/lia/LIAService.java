package pfs.lms.enquiry.monitoring.lia;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.service.ILoanAppraisalService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.service.ILoanMonitoringService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LIAService implements ILIAService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final LIARepository liaRepository;
    private final LIAReportAndFeeRepository liaReportAndFeeRepository;
    private final ILoanMonitoringService loanMonitoringService;
    private final ILoanAppraisalService loanAppraisalService;

    private final IChangeDocumentService changeDocumentService;
    
    @Override
    public LendersInsuranceAdvisor saveLIA(LIAResource resource, String username) throws CloneNotSupportedException {


        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);
        LoanAppraisal loanAppraisal = loanAppraisalService.createLoanAppraisal(loanApplication, username);


        LendersInsuranceAdvisor lendersInsuranceAdvisor = resource.getLendersInsuranceAdvisor();
        lendersInsuranceAdvisor.setSerialNumber(liaRepository.findByLoanMonitor(loanMonitor).size() + 1);
        lendersInsuranceAdvisor.setLoanMonitor(loanMonitor);
        lendersInsuranceAdvisor.setLoanAppraisal(loanAppraisal);
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

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(lendersInsuranceAdvisor.getLoanMonitor(),
                lendersInsuranceAdvisor.getLoanAppraisal(),resource.getModuleName());


        // Create Change Document for LIA
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                lendersInsuranceAdvisor.getId(),
                null,
                loanApplication.getLoanContractId(),
                null,
                lendersInsuranceAdvisor,
                "Created",
                username,
                resource.getModuleName() , "Lenders Insurance Advisor" );

        return lendersInsuranceAdvisor;
    }

    @Override
    public LendersInsuranceAdvisor updateLIA(LIAResource resource, String username) throws CloneNotSupportedException {
        LendersInsuranceAdvisor lia
                = liaRepository.getOne(resource.getLendersInsuranceAdvisor().getId());

        //Clone the LIE Object for Change Document
        Object oldLia = lia.clone();

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

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(lia.getLoanMonitor(),
                lia.getLoanAppraisal(),resource.getModuleName());


        //Create Change Document
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                lia.getId(),
                null,
                lia.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldLia,
                lia,
                "Updated",
                username,
                "Monitoring", "Lenders Insurance Advisor" );

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

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(liaReportAndFee.getLendersInsuranceAdvisor().getLoanMonitor(),
                liaReportAndFee.getLendersInsuranceAdvisor().getLoanAppraisal(),resource.getModuleName());

        // Create Change Document for LIA Report and Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                liaReportAndFee.getId(),
                lendersInsuranceAdvisor.getId(),
                lendersInsuranceAdvisor.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                liaReportAndFee,
                "Created",
                username,
                resource.getModuleName(), "LIA Report And Fee" );

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
        existingliaReportAndFee.setDocumentType(resource.getLiaReportAndFee().getDocumentType());
        existingliaReportAndFee.setNextReportDate(resource.getLiaReportAndFee().getNextReportDate());
        existingliaReportAndFee.setFileReference(resource.getLiaReportAndFee().getFileReference());
        existingliaReportAndFee.setReportDate(resource.getLiaReportAndFee().getReportDate());
        existingliaReportAndFee.setPercentageCompletion(resource.getLiaReportAndFee().getPercentageCompletion());
        existingliaReportAndFee.setRemarks(resource.getLiaReportAndFee().getRemarks());
        existingliaReportAndFee = liaReportAndFeeRepository.save(existingliaReportAndFee);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(existingliaReportAndFee.getLendersInsuranceAdvisor().getLoanMonitor(),
                existingliaReportAndFee.getLendersInsuranceAdvisor().getLoanAppraisal(),resource.getModuleName());


        // Create Change Document for LIA Report And Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                existingliaReportAndFee.getId(),
                existingliaReportAndFee.getLendersInsuranceAdvisor().getId(),
                existingliaReportAndFee.getLendersInsuranceAdvisor().getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldLiaReportAndFee,
                existingliaReportAndFee,
                "Updated",
                username,
                resource.getModuleName(), "LIA Report And Fee" );

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
