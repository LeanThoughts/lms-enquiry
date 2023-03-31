package pfs.lms.enquiry.monitoring.llc;


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
public class LLCService implements ILLCService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final LLCRepository llcRepository;
    private final LLCReportAndFeeRepository llcReportAndFeeRepository;
    private final ILoanMonitoringService loanMonitoringService;
    private final ILoanAppraisalService loanAppraisalService;
    private final IChangeDocumentService changeDocumentService;
    
    @Override
    public LendersLegalCouncil saveLLC(LLCResource resource, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);
        LoanAppraisal loanAppraisal = loanAppraisalService.createLoanAppraisal(loanApplication, username);

        LendersLegalCouncil lendersLegalCouncil = resource.getLendersLegalCouncil();
        lendersLegalCouncil.setSerialNumber(llcRepository.findByLoanMonitor(loanMonitor).size() + 1);
        lendersLegalCouncil.setLoanMonitor(loanMonitor);
        lendersLegalCouncil.setLoanAppraisal(loanAppraisal);
        lendersLegalCouncil.setAdvisor(resource.getLendersLegalCouncil().getAdvisor());
        lendersLegalCouncil.setBpCode(resource.getLendersLegalCouncil().getBpCode());
        lendersLegalCouncil.setName(resource.getLendersLegalCouncil().getName());
        lendersLegalCouncil.setDateOfAppointment(resource.getLendersLegalCouncil().getDateOfAppointment());
        lendersLegalCouncil.setContractPeriodFrom(resource.getLendersLegalCouncil().getContractPeriodFrom());
        lendersLegalCouncil.setContractPeriodTo(resource.getLendersLegalCouncil().getContractPeriodTo());
        lendersLegalCouncil.setContactPerson(resource.getLendersLegalCouncil().getContactPerson());
        lendersLegalCouncil.setContactNumber(resource.getLendersLegalCouncil().getContactNumber());
        lendersLegalCouncil.setEmail(resource.getLendersLegalCouncil().getEmail());
        lendersLegalCouncil = llcRepository.save(lendersLegalCouncil);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(lendersLegalCouncil.getLoanMonitor(),
                lendersLegalCouncil.getLoanAppraisal(),resource.getModuleName());

        // Create Change Document for LIE
            changeDocumentService.createChangeDocument(
                    loanBusinessProcessObjectId,
                    lendersLegalCouncil.getId(),
                    null,
                    loanApplication.getLoanContractId(),
                    null,
                    lendersLegalCouncil,
                    "Created",
                    username,
                    resource.getModuleName() , "Lenders Legal Counsel" );

        return lendersLegalCouncil;
    }

    @Override
    public LendersLegalCouncil updateLLC(LLCResource resource, String username) throws CloneNotSupportedException {
        LendersLegalCouncil llc
                = llcRepository.getOne(resource.getLendersLegalCouncil().getId());

        //Clone the LIE Object for Change Document
        Object oldLLC = llc.clone();

        llc.setAdvisor(resource.getLendersLegalCouncil().getAdvisor());
        llc.setBpCode(resource.getLendersLegalCouncil().getBpCode());
        llc.setName(resource.getLendersLegalCouncil().getName());
        llc.setDateOfAppointment(resource.getLendersLegalCouncil().getDateOfAppointment());
        llc.setContractPeriodFrom(resource.getLendersLegalCouncil().getContractPeriodFrom());
        llc.setContractPeriodTo(resource.getLendersLegalCouncil().getContractPeriodTo());
        llc.setContactPerson(resource.getLendersLegalCouncil().getContactPerson());
        llc.setContactNumber(resource.getLendersLegalCouncil().getContactNumber());
        llc.setEmail(resource.getLendersLegalCouncil().getEmail());
        llc = llcRepository.save(llc);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(llc.getLoanMonitor(),
                llc.getLoanAppraisal(),resource.getModuleName());

        //Create Change Document
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                llc.getId(),
                null,
                llc.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLLC,
                llc,
                "Updated",
                username,
                resource.getModuleName(), "Lenders Legal Counsel" );

        return llc;
    }

    @Override
    public List<LLCResource> getLendersLegalCouncils(String loanApplicationId) {
        List<LLCResource> llcResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<LendersLegalCouncil> list = llcRepository.findByLoanMonitor(loanMonitor);
            list.forEach(
                    lendersInsuranceAdvisor -> {
                        LLCResource llcResource = new LLCResource();
                        llcResource.setLoanApplicationId(loanApplication.getId());
                        llcResource.setLendersLegalCouncil(lendersInsuranceAdvisor);
                        llcResources.add(llcResource);
                    }
            );
        }
        Collections.sort(llcResources, Comparator.comparingInt((LLCResource a) ->
                a.getLendersLegalCouncil().getSerialNumber()).reversed());
        return llcResources;
    }

    @Override
    public LLCReportAndFee saveLLCReportAndFee(LLCReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        LendersLegalCouncil lendersLegalCouncil = llcRepository.getOne(resource.getLendersLegalCouncilId());
        LLCReportAndFee llcReportAndFee = resource.getLlcReportAndFee();
        llcReportAndFee.setSerialNumber(llcReportAndFeeRepository.findByLendersLegalCouncil(lendersLegalCouncil).size() + 1);
        llcReportAndFee.setLendersLegalCouncil(lendersLegalCouncil);
        llcReportAndFee = llcReportAndFeeRepository.save(llcReportAndFee);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(llcReportAndFee.getLendersLegalCouncil().getLoanMonitor(),
                llcReportAndFee.getLendersLegalCouncil().getLoanAppraisal() ,resource.getModuleName());

        // Create Change Document for LLC Report And Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                llcReportAndFee.getId(),
                llcReportAndFee.getId(),
                llcReportAndFee.getLendersLegalCouncil().getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                llcReportAndFee,
                "Created",
                username,
                resource.getModuleName() , "LLC Report And Fee" );

        return llcReportAndFee;
    }

    @Override
    public LLCReportAndFee updateLLCReportAndFee(LLCReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        LLCReportAndFee existingllcReportAndFee
                = llcReportAndFeeRepository.getOne(resource.getLlcReportAndFee().getId());

        Object oldllcReportAndFee = existingllcReportAndFee.clone();

        existingllcReportAndFee.setReportType(resource.getLlcReportAndFee().getReportType());
        existingllcReportAndFee.setDateOfReceipt(resource.getLlcReportAndFee().getDateOfReceipt());
        existingllcReportAndFee.setInvoiceDate(resource.getLlcReportAndFee().getInvoiceDate());
        existingllcReportAndFee.setInvoiceNo(resource.getLlcReportAndFee().getInvoiceNo());
        existingllcReportAndFee.setFeeAmount(resource.getLlcReportAndFee().getFeeAmount());
        existingllcReportAndFee.setStatusOfFeeReceipt(resource.getLlcReportAndFee().getStatusOfFeeReceipt());
        existingllcReportAndFee.setStatusOfFeePaid(resource.getLlcReportAndFee().getStatusOfFeePaid());
        existingllcReportAndFee.setDocumentTitle(resource.getLlcReportAndFee().getDocumentTitle());
        existingllcReportAndFee.setDocumentType(resource.getLlcReportAndFee().getDocumentType());
        existingllcReportAndFee.setNextReportDate(resource.getLlcReportAndFee().getNextReportDate());
        existingllcReportAndFee.setFileReference(resource.getLlcReportAndFee().getFileReference());
        existingllcReportAndFee.setReportDate(resource.getLlcReportAndFee().getReportDate());
        existingllcReportAndFee.setPercentageCompletion(resource.getLlcReportAndFee().getPercentageCompletion());
        existingllcReportAndFee.setRemarks(resource.getLlcReportAndFee().getRemarks());
        existingllcReportAndFee = llcReportAndFeeRepository.save(existingllcReportAndFee);

        UUID loanBusinessProcessObjectId = loanMonitoringService.getLoanBusinessProcessObjectId(existingllcReportAndFee.getLendersLegalCouncil().getLoanMonitor(),
                existingllcReportAndFee.getLendersLegalCouncil().getLoanAppraisal(),  resource.getModuleName());

        // Create Change Document for LLC Report And Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                existingllcReportAndFee.getId(),
                existingllcReportAndFee.getId(),
                existingllcReportAndFee.getLendersLegalCouncil().getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                existingllcReportAndFee,
                "Updated",
                username,
                resource.getModuleName(), "LLC Report And Fee" );

        return existingllcReportAndFee;
    }

    @Override
    public List<LLCReportAndFeeResource> getLLCReportAndFee(String lendersInsuranceAdvisorId) {
        List<LLCReportAndFeeResource> llcReportAndFeeResources = new ArrayList<>();
        LendersLegalCouncil lendersLegalCouncil = llcRepository.getOne(lendersInsuranceAdvisorId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);

        if(lendersLegalCouncil != null) {
            List<LLCReportAndFee> llcReportAndFees
                    = llcReportAndFeeRepository.findByLendersLegalCouncil(lendersLegalCouncil);
            llcReportAndFees.forEach(
                    llcReportAndFee -> {
                        LLCReportAndFeeResource llcReportAndFeeResource = new LLCReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        llcReportAndFeeResource.setLendersLegalCouncilId(lendersLegalCouncil.getId());
                        llcReportAndFeeResource.setLlcReportAndFee(llcReportAndFee);
                        llcReportAndFeeResources.add(llcReportAndFeeResource);
                    }
            );
        }
        Collections.sort(llcReportAndFeeResources, Comparator.comparingInt((LLCReportAndFeeResource a) ->
                a.getLlcReportAndFee().getSerialNumber()).reversed());
        return llcReportAndFeeResources;
    }

    @Override
    public LLCReportAndFee deleteLLCReportAndFee(UUID llcReportAndFeeId, String moduleName, String username) {
        LLCReportAndFee llcReportAndFee = llcReportAndFeeRepository.getOne(llcReportAndFeeId.toString());

        UUID loanBusinessProcessObjectId =
                loanMonitoringService.getLoanBusinessProcessObjectId(llcReportAndFee.getLendersLegalCouncil().getLoanMonitor(),
                        llcReportAndFee.getLendersLegalCouncil().getLoanAppraisal(),moduleName);

        // Create Change Document for LLC Report And Fee
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                llcReportAndFee.getId(),
                llcReportAndFee.getLendersLegalCouncil().getId(),
                llcReportAndFee.getLendersLegalCouncil().getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                llcReportAndFee,
                "Deleted",
                username,
                moduleName, "LLC Report And Fee" );

        llcReportAndFeeRepository.delete(llcReportAndFee);
        updateLLCReportAndFeeSerialNumbers(llcReportAndFee.getLendersLegalCouncil().getId());
        return llcReportAndFee;
    }

    private void updateLLCReportAndFeeSerialNumbers(String llcId) {
        LendersLegalCouncil lendersLegalCouncil = llcRepository.getOne(llcId);
        List<LLCReportAndFee> llcReportAndFees = llcReportAndFeeRepository.findByLendersLegalCouncil(lendersLegalCouncil);
        int size = llcReportAndFees.size();
        for(LLCReportAndFee llcReportAndFee: llcReportAndFees) {
            if (llcReportAndFee.getSerialNumber() != size) {
                llcReportAndFee.setSerialNumber(size);
                llcReportAndFeeRepository.save(llcReportAndFee);
            }
            size--;
        }
    }

    @Override
    public LendersLegalCouncil deleteLLC(UUID llcId, String moduleName, String username) {
        LendersLegalCouncil llc = llcRepository.getOne(llcId.toString());
        LoanMonitor loanMonitor = llc.getLoanMonitor();

        UUID loanBusinessProcessObjectId =
                loanMonitoringService.getLoanBusinessProcessObjectId(llc.getLoanMonitor(),
                        llc.getLoanAppraisal() ,moduleName);

        // Create Change Document for LLC
        changeDocumentService.createChangeDocument(
                loanBusinessProcessObjectId,
                llc.getId(),
                llc.getLoanAppraisal().getId().toString(),
                llc.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                llc,
                "Deleted",
                username,
                moduleName, "\"Lenders Legal Counsel\"" );


        llcRepository.delete(llc);
        updateLLCSerialNumbers(loanMonitor);
        return llc;
    }

    private void updateLLCSerialNumbers(LoanMonitor loanMonitor) {
        List<LendersLegalCouncil> lendersLegalCouncils = llcRepository.findByLoanMonitor(loanMonitor);
        int size = lendersLegalCouncils.size();
        for(LendersLegalCouncil lendersLegalCouncil: lendersLegalCouncils) {
            if (lendersLegalCouncil.getSerialNumber() != size) {
                lendersLegalCouncil.setSerialNumber(size);
                llcRepository.save(lendersLegalCouncil);
            }
            size--;
        }
    }
}
