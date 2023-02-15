package pfs.lms.enquiry.monitoring.llc;


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
public class LLCService implements ILLCService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final LLCRepository LLCRepository;
    private final LLCReportAndFeeRepository LLCReportAndFeeRepository;
    
    @Override
    public LendersLegalCouncil saveLLC(LLCResource resource, String username) throws CloneNotSupportedException {

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
        LendersLegalCouncil lendersLegalCouncil = resource.getLendersLegalCouncil();
        lendersLegalCouncil.setSerialNumber(LLCRepository.findByLoanMonitor(loanMonitor).size() + 1);
        lendersLegalCouncil.setLoanMonitor(loanMonitor);
        lendersLegalCouncil.setAdvisor(resource.getLendersLegalCouncil().getAdvisor());
        lendersLegalCouncil.setBpCode(resource.getLendersLegalCouncil().getBpCode());
        lendersLegalCouncil.setName(resource.getLendersLegalCouncil().getName());
        lendersLegalCouncil.setDateOfAppointment(resource.getLendersLegalCouncil().getDateOfAppointment());
        lendersLegalCouncil.setContractPeriodFrom(resource.getLendersLegalCouncil().getContractPeriodFrom());
        lendersLegalCouncil.setContractPeriodTo(resource.getLendersLegalCouncil().getContractPeriodTo());
        lendersLegalCouncil.setContactPerson(resource.getLendersLegalCouncil().getContactPerson());
        lendersLegalCouncil.setContactNumber(resource.getLendersLegalCouncil().getContactNumber());
        lendersLegalCouncil.setEmail(resource.getLendersLegalCouncil().getEmail());
        lendersLegalCouncil = LLCRepository.save(lendersLegalCouncil);

        // Create Change Document for LIE
//        changeDocumentService.createChangeDocument(
//                loanMonitor.getId(),lendersInsuranceAdvisor.getId(),null,
//                loanApplication.getLoanContractId(),
//                null,
//                loanMonitor,
//                "Created",
//                username,
//                "Monitoring" , "Lenders Independent Engineer" );

        return lendersLegalCouncil;
    }

    @Override
    public LendersLegalCouncil updateLLC(LLCResource resource, String username) throws CloneNotSupportedException {
        LendersLegalCouncil llc
                = LLCRepository.getOne(resource.getLendersLegalCouncil().getId());

        //Clone the LIE Object for Change Document
        Object oldLendersIndependentEngineer = llc.clone();

        llc.setAdvisor(resource.getLendersLegalCouncil().getAdvisor());
        llc.setBpCode(resource.getLendersLegalCouncil().getBpCode());
        llc.setName(resource.getLendersLegalCouncil().getName());
        llc.setDateOfAppointment(resource.getLendersLegalCouncil().getDateOfAppointment());
        llc.setContractPeriodFrom(resource.getLendersLegalCouncil().getContractPeriodFrom());
        llc.setContractPeriodTo(resource.getLendersLegalCouncil().getContractPeriodTo());
        llc.setContactPerson(resource.getLendersLegalCouncil().getContactPerson());
        llc.setContactNumber(resource.getLendersLegalCouncil().getContactNumber());
        llc.setEmail(resource.getLendersLegalCouncil().getEmail());
        llc = LLCRepository.save(llc);

        //Create Change Document
//        changeDocumentService.createChangeDocument(
//                existingLendersIndependentEngineer.getLoanMonitor().getId(),existingLendersIndependentEngineer.getId(),null,
//                existingLendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldLendersIndependentEngineer,
//                existingLendersIndependentEngineer,
//                "Updated",
//                username,
//                "Monitoring", "Lenders Independent Engineer" );

        return llc;
    }

    @Override
    public List<LLCResource> getLendersLegalCouncils(String loanApplicationId) {
        List<LLCResource> llcResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<LendersLegalCouncil> list = LLCRepository.findByLoanMonitor(loanMonitor);
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
        LendersLegalCouncil lendersLegalCouncil = LLCRepository.getOne(resource.getLendersLegalCouncilId());
        LLCReportAndFee llcReportAndFee = resource.getLlcReportAndFee();
        llcReportAndFee.setSerialNumber(LLCReportAndFeeRepository.findByLendersLegalCouncil(lendersLegalCouncil).size() + 1);
        llcReportAndFee.setLendersLegalCouncil(lendersLegalCouncil);
        llcReportAndFee = LLCReportAndFeeRepository.save(llcReportAndFee);

        // Create Change Document for LIE Report and Fee
//        changeDocumentService.createChangeDocument(
//                lendersInsuranceAdvisor.getLoanMonitor().getId(), liaReportAndFee.getId(),lendersInsuranceAdvisor.getId(),
//                lendersInsuranceAdvisor.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                liaReportAndFee,
//                "Created",
//                username,
//                "Monitoring" , "LIE Report And Fee" );

        return llcReportAndFee;
    }

    @Override
    public LLCReportAndFee updateLLCReportAndFee(LLCReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        LLCReportAndFee existingliaReportAndFee
                = LLCReportAndFeeRepository.getOne(resource.getLlcReportAndFee().getId());

        Object oldLiaReportAndFee = existingliaReportAndFee.clone();

        existingliaReportAndFee.setReportType(resource.getLlcReportAndFee().getReportType());
        existingliaReportAndFee.setDateOfReceipt(resource.getLlcReportAndFee().getDateOfReceipt());
        existingliaReportAndFee.setInvoiceDate(resource.getLlcReportAndFee().getInvoiceDate());
        existingliaReportAndFee.setInvoiceNo(resource.getLlcReportAndFee().getInvoiceNo());
        existingliaReportAndFee.setFeeAmount(resource.getLlcReportAndFee().getFeeAmount());
        existingliaReportAndFee.setStatusOfFeeReceipt(resource.getLlcReportAndFee().getStatusOfFeeReceipt());
        existingliaReportAndFee.setStatusOfFeePaid(resource.getLlcReportAndFee().getStatusOfFeePaid());
        existingliaReportAndFee.setDocumentTitle(resource.getLlcReportAndFee().getDocumentTitle());
        existingliaReportAndFee.setNextReportDate(resource.getLlcReportAndFee().getNextReportDate());
        existingliaReportAndFee.setFileReference(resource.getLlcReportAndFee().getFileReference());
        existingliaReportAndFee = LLCReportAndFeeRepository.save(existingliaReportAndFee);

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
    public List<LLCReportAndFeeResource> getLLCReportAndFee(String lendersInsuranceAdvisorId) {
        List<LLCReportAndFeeResource> llcReportAndFeeResources = new ArrayList<>();
        LendersLegalCouncil lendersLegalCouncil = LLCRepository.getOne(lendersInsuranceAdvisorId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(lendersLegalCouncil != null) {
            List<LLCReportAndFee> llcReportAndFees
                    = LLCReportAndFeeRepository.findByLendersLegalCouncil(lendersLegalCouncil);
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
}
