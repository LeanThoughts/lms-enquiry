package pfs.lms.enquiry.monitoring.cla;


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
public class CLAService implements ICLAService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final CLARepository claRepository;
    private final CLAReportAndFeeRepository claReportAndFeeRepository;
    
    @Override
    public CommonLoanAgreement saveCLA(CLAResource resource, String username) throws CloneNotSupportedException {

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
        CommonLoanAgreement commonLoanAgreement = resource.getCommonLoanAgreement();
        commonLoanAgreement.setSerialNumber(claRepository.findByLoanMonitor(loanMonitor).size() + 1);
        commonLoanAgreement.setLoanMonitor(loanMonitor);
        commonLoanAgreement.setAdvisor(resource.getCommonLoanAgreement().getAdvisor());
        commonLoanAgreement.setBpCode(resource.getCommonLoanAgreement().getBpCode());
        commonLoanAgreement.setName(resource.getCommonLoanAgreement().getName());
        commonLoanAgreement.setDateOfAppointment(resource.getCommonLoanAgreement().getDateOfAppointment());
        commonLoanAgreement.setContractPeriodFrom(resource.getCommonLoanAgreement().getContractPeriodFrom());
        commonLoanAgreement.setContractPeriodTo(resource.getCommonLoanAgreement().getContractPeriodTo());
        commonLoanAgreement.setContactPerson(resource.getCommonLoanAgreement().getContactPerson());
        commonLoanAgreement.setContactNumber(resource.getCommonLoanAgreement().getContactNumber());
        commonLoanAgreement.setEmail(resource.getCommonLoanAgreement().getEmail());
        commonLoanAgreement = claRepository.save(commonLoanAgreement);

        // Create Change Document for LIE
//        changeDocumentService.createChangeDocument(
//                loanMonitor.getId(),lendersInsuranceAdvisor.getId(),null,
//                loanApplication.getLoanContractId(),
//                null,
//                loanMonitor,
//                "Created",
//                username,
//                "Monitoring" , "Lenders Independent Engineer" );

        return commonLoanAgreement;
    }

    @Override
    public CommonLoanAgreement updateCLA(CLAResource resource, String username) throws CloneNotSupportedException {
        CommonLoanAgreement llc
                = claRepository.getOne(resource.getCommonLoanAgreement().getId());

        //Clone the LIE Object for Change Document
        Object oldLendersIndependentEngineer = llc.clone();

        llc.setAdvisor(resource.getCommonLoanAgreement().getAdvisor());
        llc.setBpCode(resource.getCommonLoanAgreement().getBpCode());
        llc.setName(resource.getCommonLoanAgreement().getName());
        llc.setDateOfAppointment(resource.getCommonLoanAgreement().getDateOfAppointment());
        llc.setContractPeriodFrom(resource.getCommonLoanAgreement().getContractPeriodFrom());
        llc.setContractPeriodTo(resource.getCommonLoanAgreement().getContractPeriodTo());
        llc.setContactPerson(resource.getCommonLoanAgreement().getContactPerson());
        llc.setContactNumber(resource.getCommonLoanAgreement().getContactNumber());
        llc.setEmail(resource.getCommonLoanAgreement().getEmail());
        llc = claRepository.save(llc);

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
    public List<CLAResource> getCommonLoanAgreements(String loanApplicationId) {
        List<CLAResource> claResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<CommonLoanAgreement> list = claRepository.findByLoanMonitor(loanMonitor);
            list.forEach(
                    commonLoanAgreement -> {
                        CLAResource CLAResource = new CLAResource();
                        CLAResource.setLoanApplicationId(loanApplication.getId());
                        CLAResource.setCommonLoanAgreement(commonLoanAgreement);
                        claResources.add(CLAResource);
                    }
            );
        }
        Collections.sort(claResources, Comparator.comparingInt((CLAResource a) ->
                a.getCommonLoanAgreement().getSerialNumber()).reversed());
        return claResources;
    }

    @Override
    public CLAReportAndFee saveCLAReportAndFee(CLAReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        CommonLoanAgreement commonLoanAgreement = claRepository.getOne(resource.getCommonLoanAgreementId());
        CLAReportAndFee claReportAndFee = resource.getClaReportAndFee();
        claReportAndFee.setSerialNumber(claReportAndFeeRepository.findByCommonLoanAgreement(commonLoanAgreement).size() + 1);
        claReportAndFee.setCommonLoanAgreement(commonLoanAgreement);
        claReportAndFee = claReportAndFeeRepository.save(claReportAndFee);

        // Create Change Document for LIE Report And Fee
//        changeDocumentService.createChangeDocument(
//                lendersInsuranceAdvisor.getLoanMonitor().getId(), liaReportAndFee.getId(),lendersInsuranceAdvisor.getId(),
//                lendersInsuranceAdvisor.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                liaReportAndFee,
//                "Created",
//                username,
//                "Monitoring" , "LIE Report And Fee" );

        return claReportAndFee;
    }

    @Override
    public CLAReportAndFee updateCLAReportAndFee(CLAReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        CLAReportAndFee existingClaReportAndFee
                = claReportAndFeeRepository.getOne(resource.getClaReportAndFee().getId());

        Object oldClaReportAndFee = existingClaReportAndFee.clone();

        existingClaReportAndFee.setReportType(resource.getClaReportAndFee().getReportType());
        existingClaReportAndFee.setDateOfReceipt(resource.getClaReportAndFee().getDateOfReceipt());
        existingClaReportAndFee.setInvoiceDate(resource.getClaReportAndFee().getInvoiceDate());
        existingClaReportAndFee.setInvoiceNo(resource.getClaReportAndFee().getInvoiceNo());
        existingClaReportAndFee.setFeeAmount(resource.getClaReportAndFee().getFeeAmount());
        existingClaReportAndFee.setStatusOfFeeReceipt(resource.getClaReportAndFee().getStatusOfFeeReceipt());
        existingClaReportAndFee.setStatusOfFeePaid(resource.getClaReportAndFee().getStatusOfFeePaid());
        existingClaReportAndFee.setDocumentTitle(resource.getClaReportAndFee().getDocumentTitle());
        existingClaReportAndFee.setDocumentType(resource.getClaReportAndFee().getDocumentType());
        existingClaReportAndFee.setNextReportDate(resource.getClaReportAndFee().getNextReportDate());
        existingClaReportAndFee.setFileReference(resource.getClaReportAndFee().getFileReference());
        existingClaReportAndFee = claReportAndFeeRepository.save(existingClaReportAndFee);

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

        return existingClaReportAndFee;
    }

    @Override
    public List<CLAReportAndFeeResource> getCLAReportAndFee(String lendersInsuranceAdvisorId) {
        List<CLAReportAndFeeResource> claReportAndFeeResources = new ArrayList<>();
        CommonLoanAgreement commonLoanAgreement = claRepository.getOne(lendersInsuranceAdvisorId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(commonLoanAgreement != null) {
            List<CLAReportAndFee> claReportAndFees
                    = claReportAndFeeRepository.findByCommonLoanAgreement(commonLoanAgreement);
            claReportAndFees.forEach(
                    claReportAndFee -> {
                        CLAReportAndFeeResource CLAReportAndFeeResource = new CLAReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        CLAReportAndFeeResource.setCommonLoanAgreementId(commonLoanAgreement.getId());
                        CLAReportAndFeeResource.setClaReportAndFee(claReportAndFee);
                        claReportAndFeeResources.add(CLAReportAndFeeResource);
                    }
            );
        }
        Collections.sort(claReportAndFeeResources, Comparator.comparingInt((CLAReportAndFeeResource a) ->
                a.getClaReportAndFee().getSerialNumber()).reversed());
        return claReportAndFeeResources;
    }
}
