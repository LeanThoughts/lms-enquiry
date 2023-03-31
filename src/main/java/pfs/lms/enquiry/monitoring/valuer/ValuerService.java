package pfs.lms.enquiry.monitoring.valuer;


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
public class ValuerService implements IValuerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ValuerRepository valuerRepository;
    private final ValuerReportAndFeeRepository valuerReportAndFeeRepository;
    private final ILoanMonitoringService loanMonitoringService;
    private final ILoanAppraisalService loanAppraisalService;
    private final IChangeDocumentService changeDocumentService;
    
    @Override
    public Valuer saveValuer(ValuerResource resource, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);
        LoanAppraisal loanAppraisal = loanAppraisalService.createLoanAppraisal(loanApplication, username);

        Valuer valuer = resource.getValuer();
        valuer.setSerialNumber(valuerRepository.findByLoanMonitor(loanMonitor).size() + 1);
        valuer.setLoanMonitor(loanMonitor);
        valuer.setLoanAppraisal(loanAppraisal);
        valuer.setAdvisor(resource.getValuer().getAdvisor());
        valuer.setBpCode(resource.getValuer().getBpCode());
        valuer.setName(resource.getValuer().getName());
        valuer.setDateOfAppointment(resource.getValuer().getDateOfAppointment());
        valuer.setContractPeriodFrom(resource.getValuer().getContractPeriodFrom());
        valuer.setContractPeriodTo(resource.getValuer().getContractPeriodTo());
        valuer.setContactPerson(resource.getValuer().getContactPerson());
        valuer.setContactNumber(resource.getValuer().getContactNumber());
        valuer.setEmail(resource.getValuer().getEmail());
        valuer = valuerRepository.save(valuer);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(valuer.getLoanMonitor(),
                valuer.getLoanAppraisal(),resource.getModuleName());

        // Create Change Document for Valuer
            changeDocumentService.createChangeDocument(
                    loanBusinessProcessObjectId,
                    valuer.getId(),
                    null,
                    loanApplication.getLoanContractId(),
                    null,
                    valuer,
                    "Created",
                    username,
                    resource.getModuleName() , "Valuer" );

        return valuer;
    }

    @Override
    public Valuer updateValuer(ValuerResource resource, String username) throws CloneNotSupportedException {
        Valuer valuer
                = valuerRepository.getOne(resource.getValuer().getId());

        //Clone the LIE Object for Change Document
        Object oldValuer = valuer.clone();

        valuer.setAdvisor(resource.getValuer().getAdvisor());
        valuer.setBpCode(resource.getValuer().getBpCode());
        valuer.setName(resource.getValuer().getName());
        valuer.setDateOfAppointment(resource.getValuer().getDateOfAppointment());
        valuer.setContractPeriodFrom(resource.getValuer().getContractPeriodFrom());
        valuer.setContractPeriodTo(resource.getValuer().getContractPeriodTo());
        valuer.setContactPerson(resource.getValuer().getContactPerson());
        valuer.setContactNumber(resource.getValuer().getContactNumber());
        valuer.setEmail(resource.getValuer().getEmail());
        valuer = valuerRepository.save(valuer);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(valuer.getLoanMonitor(),
                valuer.getLoanAppraisal(),resource.getModuleName());

        //Create Change Document
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                valuer.getId(),
                null,
                valuer.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldValuer,
                valuer,
                "Updated",
                username,
                resource.getModuleName(), "Valuer" );

        return valuer;
    }

    @Override
    public Valuer deleteValuer(UUID valuerId, String moduleName, String username) {
        Valuer valuer = valuerRepository.getOne(valuerId.toString());
        LoanMonitor loanMonitor = valuer.getLoanMonitor();

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(valuer.getLoanMonitor(),
                valuer.getLoanAppraisal(), moduleName);

        // Create Change Document for LIE Delete
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                valuer.getId(),
                null,
                valuer.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                valuer,
                "Deleted",
                username,
                moduleName, "Valuer" );

        valuerRepository.delete(valuer);
        updateValuerSerialNumbers(loanMonitor);

        return valuer;
    }

    private void updateValuerSerialNumbers(LoanMonitor loanMonitor) {
        List<Valuer> valuers = valuerRepository.findByLoanMonitorOrderBySerialNumberDesc(loanMonitor);
        int size = valuers.size();
        for(Valuer valuer: valuers) {
            if (valuer.getSerialNumber() != size) {
                valuer.setSerialNumber(size);
                valuerRepository.save(valuer);
            }
            size--;
        }
    }

    @Override
    public List<ValuerResource> getValuers(String loanApplicationId) {
        List<ValuerResource> valuers = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<Valuer> list = valuerRepository.findByLoanMonitor(loanMonitor);
            list.forEach(
                    valuer -> {
                        ValuerResource valuerResource = new ValuerResource();
                        valuerResource.setLoanApplicationId(loanApplication.getId());
                        valuerResource.setValuer(valuer);
                        valuers.add(valuerResource);
                    }
            );
        }
        Collections.sort(valuers, Comparator.comparingInt((ValuerResource a) ->
                a.getValuer().getSerialNumber()).reversed());
        return valuers;
    }

    @Override
    public ValuerReportAndFee saveValuerReportAndFee(ValuerReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        Valuer valuer = valuerRepository.getOne(resource.getValuerId());
        ValuerReportAndFee valuerReportAndFee = resource.getValuerReportAndFee();
        valuerReportAndFee.setSerialNumber(valuerReportAndFeeRepository.findByValuer(valuer).size() + 1);
        valuerReportAndFee.setValuer(valuer);
        valuerReportAndFee = valuerReportAndFeeRepository.save(valuerReportAndFee);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(valuerReportAndFee.getValuer().getLoanMonitor(),
                valuerReportAndFee.getValuer().getLoanAppraisal() ,resource.getModuleName());

        // Create Change Document for LLC Report And Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                valuerReportAndFee.getId(),
                valuerReportAndFee.getId(),
                valuerReportAndFee.getValuer().getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                valuerReportAndFee,
                "Created",
                username,
                resource.getModuleName() , "Valuer Report And Fee" );

        return valuerReportAndFee;
    }

    @Override
    public ValuerReportAndFee updateValuerReportAndFee(ValuerReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        ValuerReportAndFee valuerReportAndFee
                = valuerReportAndFeeRepository.getOne(resource.getValuerReportAndFee().getId());

        Object oldValuerReportAndFee = valuerReportAndFee.clone();

        valuerReportAndFee.setReportType(resource.getValuerReportAndFee().getReportType());
        valuerReportAndFee.setDateOfReceipt(resource.getValuerReportAndFee().getDateOfReceipt());
        valuerReportAndFee.setInvoiceDate(resource.getValuerReportAndFee().getInvoiceDate());
        valuerReportAndFee.setInvoiceNo(resource.getValuerReportAndFee().getInvoiceNo());
        valuerReportAndFee.setFeeAmount(resource.getValuerReportAndFee().getFeeAmount());
        valuerReportAndFee.setStatusOfFeeReceipt(resource.getValuerReportAndFee().getStatusOfFeeReceipt());
        valuerReportAndFee.setStatusOfFeePaid(resource.getValuerReportAndFee().getStatusOfFeePaid());
        valuerReportAndFee.setDocumentTitle(resource.getValuerReportAndFee().getDocumentTitle());
        valuerReportAndFee.setDocumentType(resource.getValuerReportAndFee().getDocumentType());
        valuerReportAndFee.setNextReportDate(resource.getValuerReportAndFee().getNextReportDate());
        valuerReportAndFee.setFileReference(resource.getValuerReportAndFee().getFileReference());
        valuerReportAndFee.setReportDate(resource.getValuerReportAndFee().getReportDate());
        valuerReportAndFee.setPercentageCompletion(resource.getValuerReportAndFee().getPercentageCompletion());
        valuerReportAndFee.setRemarks(resource.getValuerReportAndFee().getRemarks());
        valuerReportAndFee = valuerReportAndFeeRepository.save(valuerReportAndFee);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(valuerReportAndFee.getValuer().getLoanMonitor(),
                valuerReportAndFee.getValuer().getLoanAppraisal(),  resource.getModuleName());

        // Create Change Document for LLC Report And Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                valuerReportAndFee.getId(),
                valuerReportAndFee.getId(),
                valuerReportAndFee.getValuer().getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                valuerReportAndFee,
                "Created",
                username,
                resource.getModuleName(), "Valuer Report And Fee" );

        return valuerReportAndFee;
    }

    @Override
    public List<ValuerReportAndFeeResource> getValuerReportAndFees(String valuerId) {
        List<ValuerReportAndFeeResource> valuerReportAndFeeResources = new ArrayList<>();
        Valuer valuer = valuerRepository.getOne(valuerId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);

        if(valuer != null) {
            List<ValuerReportAndFee> valuerReportAndFees
                    = valuerReportAndFeeRepository.findByValuer(valuer);
            valuerReportAndFees.forEach(
                    valuerReportAndFee -> {
                        ValuerReportAndFeeResource valuerReportAndFeeResource = new ValuerReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        valuerReportAndFeeResource.setValuerId(valuer.getId());
                        valuerReportAndFeeResource.setValuerReportAndFee(valuerReportAndFee);
                        valuerReportAndFeeResources.add(valuerReportAndFeeResource);
                    }
            );
        }
        Collections.sort(valuerReportAndFeeResources, Comparator.comparingInt((ValuerReportAndFeeResource a) ->
                a.getValuerReportAndFee().getSerialNumber()).reversed());
        return valuerReportAndFeeResources;
    }

    @Override
    public ValuerReportAndFee deleteValuerReportAndFee(UUID valuerReportAndFeeId, String moduleName, String username) {
        ValuerReportAndFee valuerReportAndFee = valuerReportAndFeeRepository.getOne(valuerReportAndFeeId.toString());

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(lieReportAndFee.getLendersIndependentEngineer().getLoanMonitor(),
//                lieReportAndFee.getLendersIndependentEngineer().getLoanAppraisal(),moduleName);
//
//        // Create Change Document for LIE Delete
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                lieReportAndFee.getId(),
//                null,
//                lieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                lieReportAndFee,
//                "Deleted",
//                username,
//                moduleName, "LIE Report And Fee" );

        valuerReportAndFeeRepository.delete(valuerReportAndFee);
        updateValuerReportAndFeeSerialNumbers(valuerReportAndFee.getValuer().getId());
        return valuerReportAndFee;
    }

    private void updateValuerReportAndFeeSerialNumbers(String valuerId) {
        Valuer valuer = valuerRepository.getOne(valuerId);
        List<ValuerReportAndFee> valuerReportAndFees = valuerReportAndFeeRepository.findByValuer(valuer);
        int size = valuerReportAndFees.size();
        for(ValuerReportAndFee valuerReportAndFee: valuerReportAndFees) {
            if (valuerReportAndFee.getSerialNumber() != size) {
                valuerReportAndFee.setSerialNumber(size);
                valuerReportAndFeeRepository.save(valuerReportAndFee);
            }
            size--;
        }
    }

}
