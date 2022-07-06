package pfs.lms.enquiry.monitoring.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancialsRepository;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancialsResource;
import pfs.lms.enquiry.monitoring.domain.*;
import pfs.lms.enquiry.monitoring.lfa.*;
import pfs.lms.enquiry.monitoring.lie.*;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameter;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterRepository;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterResource;
import pfs.lms.enquiry.monitoring.promoterdetails.PromoterDetailRepository;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancialsRepository;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancialsResource;
import pfs.lms.enquiry.monitoring.repository.*;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.monitoring.service.ILoanMonitoringService;
import pfs.lms.enquiry.monitoring.tra.*;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor

public class LoanMonitoringService implements ILoanMonitoringService {

    private final LIERepository lieRepository;
    private  final LoanMonitorRepository loanMonitorRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    private final LIEReportAndFeeRepository lieReportAndFeeRepository;

    private final LFARepository lfaRepository;
    private final LFAReportAndFeeRepository lfaReportAndFeeRepository;

    private final TRARepository traRepository;
    private final TRAStatementRepository traStatementRepository;

    private final TermsAndConditionsRepository termsAndConditionsRepository;

    private final SecurityComplianceRepository securityComplianceRepository;

    private final SiteVisitRepository siteVisitRepository;

    private final OperatingParameterRepository operatingParameterRepository;

    private final RateOfInterestRepository rateOfInterestRepository;

    private final BorrowerFinancialsRepository borrowerFinancialsRepository;

    private final PromoterFinancialsRepository promoterfinancialsRepository;

    private final FinancialCovenantsRepository financialCovenantsRepository;

    private final PromoterDetailRepository promoterDetailRepository;

    private final LoanAppraisalRepository loanAppraisalRepository;

    @Autowired
    private  IChangeDocumentService changeDocumentService;

    @Override
    @Transactional
    public LendersIndependentEngineer saveLIE(LIEResource resource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor.setWorkFlowStatusCode(01); loanMonitor.setWorkFlowStatusDescription("Created");
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(), null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");

        }
        LendersIndependentEngineer lendersIndependentEngineer = resource.getLendersIndependentEngineer();
        lendersIndependentEngineer.setSerialNumber(lieRepository.findByLoanMonitor(loanMonitor).size() + 1);
        lendersIndependentEngineer.setLoanMonitor(loanMonitor);
        lendersIndependentEngineer.setAdvisor(resource.getLendersIndependentEngineer().getAdvisor());
        lendersIndependentEngineer.setBpCode(resource.getLendersIndependentEngineer().getBpCode());
        lendersIndependentEngineer.setName(resource.getLendersIndependentEngineer().getName());
        lendersIndependentEngineer.setDateOfAppointment(resource.getLendersIndependentEngineer().getDateOfAppointment());
        lendersIndependentEngineer.setContractPeriodFrom(resource.getLendersIndependentEngineer().getContractPeriodFrom());
        lendersIndependentEngineer.setContractPeriodTo(resource.getLendersIndependentEngineer().getContractPeriodTo());
        lendersIndependentEngineer.setContactPerson(resource.getLendersIndependentEngineer().getContactPerson());
        lendersIndependentEngineer.setContactNumber(resource.getLendersIndependentEngineer().getContactNumber());
        lendersIndependentEngineer.setEmail(resource.getLendersIndependentEngineer().getEmail());
        lendersIndependentEngineer = lieRepository.save(lendersIndependentEngineer);

        // Create Change Document for LIE
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),lendersIndependentEngineer.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                loanMonitor,
                "Created",
                username,
                "Monitoring" , "Lenders Independent Engineer" );



        return lendersIndependentEngineer;
    }

    @Override
    public LendersIndependentEngineer updateLIE(LIEResource resource, String username) throws CloneNotSupportedException {

        LendersIndependentEngineer existingLendersIndependentEngineer
                = lieRepository.getOne(resource.getLendersIndependentEngineer().getId());

        //Clone the LIE Object for Change Document
         Object oldLendersIndependentEngineer = existingLendersIndependentEngineer.clone();

        existingLendersIndependentEngineer.setAdvisor(resource.getLendersIndependentEngineer().getAdvisor());
        existingLendersIndependentEngineer.setBpCode(resource.getLendersIndependentEngineer().getBpCode());
        existingLendersIndependentEngineer.setName(resource.getLendersIndependentEngineer().getName());
        existingLendersIndependentEngineer.setDateOfAppointment(resource.getLendersIndependentEngineer().getDateOfAppointment());
        existingLendersIndependentEngineer.setContractPeriodFrom(resource.getLendersIndependentEngineer().getContractPeriodFrom());
        existingLendersIndependentEngineer.setContractPeriodTo(resource.getLendersIndependentEngineer().getContractPeriodTo());
        existingLendersIndependentEngineer.setContactPerson(resource.getLendersIndependentEngineer().getContactPerson());
        existingLendersIndependentEngineer.setContactNumber(resource.getLendersIndependentEngineer().getContactNumber());
        existingLendersIndependentEngineer.setEmail(resource.getLendersIndependentEngineer().getEmail());
        existingLendersIndependentEngineer = lieRepository.save(existingLendersIndependentEngineer);

        //Create Change Document
        changeDocumentService.createChangeDocument(
                existingLendersIndependentEngineer.getLoanMonitor().getId(),existingLendersIndependentEngineer.getId(),null,
                existingLendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLendersIndependentEngineer,
                existingLendersIndependentEngineer,
                "Updated",
                username,
                "Monitoring", "Lenders Independent Engineer" );

        return existingLendersIndependentEngineer;
    }

    @Override
    public List<LIEResource> getLendersIndependentEngineers(String loanApplicationId, String name) {

        List<LIEResource> lendersIndependentEngineerResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<LendersIndependentEngineer> lendersIndependentEngineers
                    = lieRepository.findByLoanMonitor(loanMonitor);
            lendersIndependentEngineers.forEach(
                    lendersIndependentEngineer -> {
                        LIEResource lieResource = new LIEResource();
                        lieResource.setLoanApplicationId(loanApplication.getId());
                        lieResource.setLendersIndependentEngineer(lendersIndependentEngineer);
                        lendersIndependentEngineerResources.add(lieResource);
                    }
            );
        }
        Collections.sort(lendersIndependentEngineerResources, Comparator.comparingInt((LIEResource a) ->
                a.getLendersIndependentEngineer().getSerialNumber()).reversed());
        return lendersIndependentEngineerResources;
    }

    @Override
    @Transactional
    public LIEReportAndFee saveLIEReportAndFee(LIEReportAndFeeResource resource, String username) {


        LendersIndependentEngineer lendersIndependentEngineer = lieRepository.getOne(resource.getLendersIndependentEngineerId());
        LIEReportAndFee lieReportAndFee = resource.getLieReportAndFee();
        lieReportAndFee.setSerialNumber(lieReportAndFeeRepository.findByLendersIndependentEngineer(lendersIndependentEngineer).size() + 1);
        lieReportAndFee.setLendersIndependentEngineer(lendersIndependentEngineer);
        lieReportAndFee = lieReportAndFeeRepository.save(lieReportAndFee);

        // Create Change Document for LIE Report and Fee
        changeDocumentService.createChangeDocument(
                lendersIndependentEngineer.getLoanMonitor().getId(), lieReportAndFee.getId(),lendersIndependentEngineer.getId(),
                lendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                lieReportAndFee,
                "Created",
                username,
                "Monitoring" , "LIE Report And Fee" );

        return lieReportAndFee;
    }

    @Override
    public LIEReportAndFee updateLIEReportAndFee(LIEReportAndFeeResource resource, String username) throws CloneNotSupportedException {

        LIEReportAndFee existinglieReportAndFee
                = lieReportAndFeeRepository.getOne(resource.getLieReportAndFee().getId());

        Object oldLieReportAndFee = existinglieReportAndFee.clone();

        existinglieReportAndFee.setReportType(resource.getLieReportAndFee().getReportType());
        existinglieReportAndFee.setDateOfReceipt(resource.getLieReportAndFee().getDateOfReceipt());
        existinglieReportAndFee.setInvoiceDate(resource.getLieReportAndFee().getInvoiceDate());
        existinglieReportAndFee.setInvoiceNo(resource.getLieReportAndFee().getInvoiceNo());
        existinglieReportAndFee.setFeeAmount(resource.getLieReportAndFee().getFeeAmount());
        existinglieReportAndFee.setStatusOfFeeReceipt(resource.getLieReportAndFee().getStatusOfFeeReceipt());
        existinglieReportAndFee.setStatusOfFeePaid(resource.getLieReportAndFee().getStatusOfFeePaid());
        existinglieReportAndFee.setDocumentTitle(resource.getLieReportAndFee().getDocumentTitle());
        existinglieReportAndFee.setNextReportDate(resource.getLieReportAndFee().getNextReportDate());
        existinglieReportAndFee.setFileReference(resource.getLieReportAndFee().getFileReference());
        existinglieReportAndFee = lieReportAndFeeRepository.save(existinglieReportAndFee);

        // Create Change Document for LIE Report And Fee
        changeDocumentService.createChangeDocument(
                existinglieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getId(),
                existinglieReportAndFee.getId(),existinglieReportAndFee.getLendersIndependentEngineer().getId(),
                existinglieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLieReportAndFee,
                existinglieReportAndFee,
                "Updated",
                username,
                "Monitoring" , "LIE Report And Fee" );

        return existinglieReportAndFee;
    }

    @Override
    public List<LIEReportAndFeeResource> getLIEReportAndFee(String lendersIndependentEngineerId, String name) {

        List<LIEReportAndFeeResource>  lieReportAndFeeResources  = new ArrayList<>();
        LendersIndependentEngineer lendersIndependentEngineer = lieRepository.getOne(lendersIndependentEngineerId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(lendersIndependentEngineer != null) {
            List<LIEReportAndFee> lieReportAndFees
                    = lieReportAndFeeRepository.findByLendersIndependentEngineer(lendersIndependentEngineer);
            lieReportAndFees.forEach(
                    lieReportAndFee -> {
                        LIEReportAndFeeResource lieReportAndFeeResource = new LIEReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        lieReportAndFeeResource.setLendersIndependentEngineerId(lendersIndependentEngineer.getId());
                        lieReportAndFeeResource.setLieReportAndFee(lieReportAndFee);
                        lieReportAndFeeResources.add(lieReportAndFeeResource);
                    }
            );
        }
        Collections.sort(lieReportAndFeeResources, Comparator.comparingInt((LIEReportAndFeeResource a) ->
                a.getLieReportAndFee().getSerialNumber()).reversed());
        return lieReportAndFeeResources;
    }


    //LFA

    @Override
    @Transactional
    public LendersFinancialAdvisor saveLFA(LFAResource resource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for LFA
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }




        LendersFinancialAdvisor lendersFinancialAdvisor = resource.getLendersFinancialAdvisor();
        lendersFinancialAdvisor.setLoanMonitor(loanMonitor);
        lendersFinancialAdvisor.setSerialNumber(lfaRepository.findByLoanMonitor(loanMonitor).size() + 1);
        lendersFinancialAdvisor.setBpCode(resource.getLendersFinancialAdvisor().getBpCode());
        lendersFinancialAdvisor.setName(resource.getLendersFinancialAdvisor().getName());
        lendersFinancialAdvisor.setDateOfAppointment(resource.getLendersFinancialAdvisor().getDateOfAppointment());
        lendersFinancialAdvisor.setContractPeriodFrom(resource.getLendersFinancialAdvisor().getContractPeriodFrom());
        lendersFinancialAdvisor.setContractPeriodTo(resource.getLendersFinancialAdvisor().getContractPeriodTo());
        lendersFinancialAdvisor.setContactPerson(resource.getLendersFinancialAdvisor().getContactPerson());
        lendersFinancialAdvisor.setContactNumber(resource.getLendersFinancialAdvisor().getContactNumber());
        lendersFinancialAdvisor.setEmail(resource.getLendersFinancialAdvisor().getEmail());
        lendersFinancialAdvisor = lfaRepository.save(lendersFinancialAdvisor);

        // Create Change Document for LFA
        changeDocumentService.createChangeDocument(
                lendersFinancialAdvisor.getLoanMonitor().getId(),lendersFinancialAdvisor.getId(),null,
                lendersFinancialAdvisor.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                lendersFinancialAdvisor,
                "Created",
                username,
                "Monitoring" , "Lenders Financial Advisor" );


        return lendersFinancialAdvisor;
    }

    @Override
    public LendersFinancialAdvisor updateLFA(LFAResource resource, String username) throws CloneNotSupportedException {

        LendersFinancialAdvisor existingLendersFinancialAdvisor
                = lfaRepository.getOne(resource.getLendersFinancialAdvisor().getId());

        //Clone Object for Change Document
        Object oldLendersFinancialAdvisor = existingLendersFinancialAdvisor.clone();

                existingLendersFinancialAdvisor.setBpCode(resource.getLendersFinancialAdvisor().getBpCode());
        existingLendersFinancialAdvisor.setName(resource.getLendersFinancialAdvisor().getName());
        existingLendersFinancialAdvisor.setDateOfAppointment(resource.getLendersFinancialAdvisor().getDateOfAppointment());
        existingLendersFinancialAdvisor.setContractPeriodFrom(resource.getLendersFinancialAdvisor().getContractPeriodFrom());
        existingLendersFinancialAdvisor.setContractPeriodTo(resource.getLendersFinancialAdvisor().getContractPeriodTo());
        existingLendersFinancialAdvisor.setContactPerson(resource.getLendersFinancialAdvisor().getContactPerson());
        existingLendersFinancialAdvisor.setContactNumber(resource.getLendersFinancialAdvisor().getContactNumber());
        existingLendersFinancialAdvisor.setEmail(resource.getLendersFinancialAdvisor().getEmail());
        existingLendersFinancialAdvisor = lfaRepository.save(existingLendersFinancialAdvisor);

        // Create Change Document for LFA
        changeDocumentService.createChangeDocument(
                existingLendersFinancialAdvisor.getLoanMonitor().getId(),existingLendersFinancialAdvisor.getId(),null,
                existingLendersFinancialAdvisor.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLendersFinancialAdvisor,
                existingLendersFinancialAdvisor,
                "Updated",
                username,
                "Monitoring" , "Lenders Financial Advisor" );

        return existingLendersFinancialAdvisor;
    }

    @Override
    public List<LFAResource> getLendersFinancialAdvisors(String loanApplicationId, String name) {

        List<LFAResource> lendersFinancialAdvisorResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<LendersFinancialAdvisor> lendersFinancialAdvisors
                    = lfaRepository.findByLoanMonitor(loanMonitor);
            lendersFinancialAdvisors.forEach(
                    lendersFinancialAdvisor -> {
                        LFAResource lfaResource = new LFAResource();
                        lfaResource.setLoanApplicationId(loanApplication.getId());
                        lfaResource.setLendersFinancialAdvisor(lendersFinancialAdvisor);
                        lendersFinancialAdvisorResources.add(lfaResource);
                    }
            );
        }
        Collections.sort(lendersFinancialAdvisorResources, Comparator.comparingInt((LFAResource a) ->
                a.getLendersFinancialAdvisor().getSerialNumber()).reversed());
        return lendersFinancialAdvisorResources;
    }

    @Override
    public LFAReportAndFee saveLFAReportAndFee(LFAReportAndFeeResource resource, String username) {
        LendersFinancialAdvisor lendersFinancialAdvisor = lfaRepository.getOne(resource.getLendersFinancialAdvisorId());
        LFAReportAndFee lfaReportAndFee = resource.getLfaReportAndFee();
        lfaReportAndFee.setSerialNumber(lfaReportAndFeeRepository.findByLendersFinancialAdvisor(lendersFinancialAdvisor).size() + 1);
        lfaReportAndFee.setLendersFinancialAdvisor(lendersFinancialAdvisor);
        lfaReportAndFee = lfaReportAndFeeRepository.save(lfaReportAndFee);

        // Create Change Document for LFA Report and Fee
        changeDocumentService.createChangeDocument(
                lendersFinancialAdvisor.getLoanMonitor().getId(),lfaReportAndFee.getId(),lendersFinancialAdvisor.getId(),
                lendersFinancialAdvisor.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                lfaReportAndFee,
                "Created",
                username,
                "Monitoring" , "LFA Report and Fee" );



        return lfaReportAndFee;

    }

    @Override
    public LFAReportAndFee updateLFAReportAndFee(LFAReportAndFeeResource resource, String username) throws CloneNotSupportedException {
        LFAReportAndFee existinglfaReportAndFee
                = lfaReportAndFeeRepository.getOne(resource.getLfaReportAndFee().getId());

        Object oldLFAReportAndFee = new Object();
        try {
             oldLFAReportAndFee = existinglfaReportAndFee.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        existinglfaReportAndFee.setReportType(resource.getLfaReportAndFee().getReportType());
        existinglfaReportAndFee.setDateOfReceipt(resource.getLfaReportAndFee().getDateOfReceipt());
        existinglfaReportAndFee.setInvoiceDate(resource.getLfaReportAndFee().getInvoiceDate());
        existinglfaReportAndFee.setInvoiceNo(resource.getLfaReportAndFee().getInvoiceNo());
        existinglfaReportAndFee.setFeeAmount(resource.getLfaReportAndFee().getFeeAmount());
        existinglfaReportAndFee.setStatusOfFeeReceipt(resource.getLfaReportAndFee().getStatusOfFeeReceipt());
        existinglfaReportAndFee.setStatusOfFeePaid(resource.getLfaReportAndFee().getStatusOfFeePaid());
        existinglfaReportAndFee.setDocumentTitle(resource.getLfaReportAndFee().getDocumentTitle());
        existinglfaReportAndFee.setDocumentType(resource.getLfaReportAndFee().getDocumentType());
        existinglfaReportAndFee.setFileReference(resource.getLfaReportAndFee().getFileReference());
        existinglfaReportAndFee.setNextReportDate(resource.getLfaReportAndFee().getNextReportDate());
        existinglfaReportAndFee = lfaReportAndFeeRepository.save(existinglfaReportAndFee);

        // Create Change Document for LFA Report and Fee
        changeDocumentService.createChangeDocument(
                existinglfaReportAndFee.getLendersFinancialAdvisor().getLoanMonitor().getId(),
                existinglfaReportAndFee.getId(),existinglfaReportAndFee.getLendersFinancialAdvisor().getId(),
                existinglfaReportAndFee.getLendersFinancialAdvisor().getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldLFAReportAndFee,
                existinglfaReportAndFee,
                "Updated",
                username,
                "Monitoring" , "LFA Report and Fee" );


        return existinglfaReportAndFee;

    }

    @Override
    public List<LFAReportAndFeeResource> getLFAReportAndFee(String  lendersFinancialAdvisorId, String name) {
        List<LFAReportAndFeeResource>  lfaReportAndFeeResources  = new ArrayList<>();
        LendersFinancialAdvisor lendersFinancialAdvisor = lfaRepository.getOne(lendersFinancialAdvisorId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(lendersFinancialAdvisor != null) {
            List<LFAReportAndFee> lfaReportAndFees
                    = lfaReportAndFeeRepository.findByLendersFinancialAdvisor(lendersFinancialAdvisor);
            lfaReportAndFees.forEach(
                    lfaReportAndFee -> {
                        LFAReportAndFeeResource lfaReportAndFeeResource = new LFAReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        lfaReportAndFeeResource.setLendersFinancialAdvisorId(lendersFinancialAdvisor.getId());
                        lfaReportAndFeeResource.setLfaReportAndFee(lfaReportAndFee);
                        lfaReportAndFeeResources.add(lfaReportAndFeeResource);
                    }
            );
        }
        Collections.sort(lfaReportAndFeeResources, Comparator.comparingInt((LFAReportAndFeeResource a) ->
                a.getLfaReportAndFee().getSerialNumber()).reversed());
        return lfaReportAndFeeResources;

    }

    @Override
    public TrustRetentionAccount saveTRA(TRAResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");

        }
        TrustRetentionAccount trustRetentionAccount = resource.getTrustRetentionAccount();
        trustRetentionAccount.setLoanMonitor(loanMonitor);
        trustRetentionAccount.setSerialNumber(traRepository.findByLoanMonitor(loanMonitor).size() + 1);
        trustRetentionAccount.setBankKey(resource.getTrustRetentionAccount().getBankKey());
        trustRetentionAccount.setTraBankName(resource.getTrustRetentionAccount().getTraBankName());
        trustRetentionAccount.setBranch(resource.getTrustRetentionAccount().getBranch());
        trustRetentionAccount.setAddress(resource.getTrustRetentionAccount().getAddress());
        trustRetentionAccount.setBeneficiaryName(resource.getTrustRetentionAccount().getBeneficiaryName());
        trustRetentionAccount.setIfscCode(resource.getTrustRetentionAccount().getIfscCode());
        trustRetentionAccount.setAccountNumber(resource.getTrustRetentionAccount().getAccountNumber());
        trustRetentionAccount.setContactName(resource.getTrustRetentionAccount().getContactName());
        trustRetentionAccount.setTypeOfAccount(resource.getTrustRetentionAccount().getTypeOfAccount());
        trustRetentionAccount.setContactNumber(resource.getTrustRetentionAccount().getContactNumber());
        trustRetentionAccount.setEmail(resource.getTrustRetentionAccount().getEmail());
        trustRetentionAccount.setPfsAuthorisedPerson(resource.getTrustRetentionAccount().getPfsAuthorisedPerson());
        trustRetentionAccount.setPfsAuthorisedPersonBPCode(resource.getTrustRetentionAccount().getPfsAuthorisedPersonBPCode());
        trustRetentionAccount = traRepository.save(trustRetentionAccount);

        // Change Documents for TRA
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),trustRetentionAccount.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                trustRetentionAccount,
                "Created",
                username,
                "Monitoring", "TRA Account");


        return trustRetentionAccount;

    }

    @Override
    public TrustRetentionAccount updateTRA(TRAResource resource, String username) throws CloneNotSupportedException {
        TrustRetentionAccount existingTrustRetentionAccount
                = traRepository.getOne(resource.getTrustRetentionAccount().getId());

        Object oldTRARetentionAccount = existingTrustRetentionAccount.clone();

        existingTrustRetentionAccount.setBankKey(resource.getTrustRetentionAccount().getBankKey());
        existingTrustRetentionAccount.setTraBankName(resource.getTrustRetentionAccount().getTraBankName());
        existingTrustRetentionAccount.setBranch(resource.getTrustRetentionAccount().getBranch());
        existingTrustRetentionAccount.setAddress(resource.getTrustRetentionAccount().getAddress());
        existingTrustRetentionAccount.setBeneficiaryName(resource.getTrustRetentionAccount().getBeneficiaryName());
        existingTrustRetentionAccount.setIfscCode(resource.getTrustRetentionAccount().getIfscCode());
        existingTrustRetentionAccount.setAccountNumber(resource.getTrustRetentionAccount().getAccountNumber());
        existingTrustRetentionAccount.setContactName(resource.getTrustRetentionAccount().getContactName());
        existingTrustRetentionAccount.setTypeOfAccount(resource.getTrustRetentionAccount().getTypeOfAccount());
        existingTrustRetentionAccount.setContactNumber(resource.getTrustRetentionAccount().getContactNumber());
        existingTrustRetentionAccount.setEmail(resource.getTrustRetentionAccount().getEmail());
        existingTrustRetentionAccount.setPfsAuthorisedPerson(resource.getTrustRetentionAccount().getPfsAuthorisedPerson());
        existingTrustRetentionAccount.setPfsAuthorisedPersonBPCode(resource.getTrustRetentionAccount().getPfsAuthorisedPersonBPCode());
        existingTrustRetentionAccount = traRepository.save(existingTrustRetentionAccount);

        // Change Documents for TRA A/c
        changeDocumentService.createChangeDocument(
                existingTrustRetentionAccount.getLoanMonitor().getId(), existingTrustRetentionAccount.getId(),null,
                existingTrustRetentionAccount.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldTRARetentionAccount,
                existingTrustRetentionAccount,
                "Updated",
                username,
                "Monitoring", "TRA Account");

        return existingTrustRetentionAccount;
    }

    @Override
    public List<TRAResource> getTrustRetentionAccounts(String loanApplicationId, String name) {
        List<TRAResource> trustRetentionAccountrResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<TrustRetentionAccount> trustRetentionAccounts
                    = traRepository.findByLoanMonitor(loanMonitor);
            trustRetentionAccounts.forEach(
                    trustRetentionAccount -> {
                        TRAResource traResource = new TRAResource();
                        traResource.setLoanApplicationId(loanApplication.getId());
                        traResource.setTrustRetentionAccount(trustRetentionAccount);
                        trustRetentionAccountrResources.add(traResource);
                    }
            );
        }
        Collections.sort(trustRetentionAccountrResources, Comparator.comparingInt((TRAResource a) ->
                a.getTrustRetentionAccount().getSerialNumber()).reversed());
        return trustRetentionAccountrResources;

    }

    @Override
    public TrustRetentionAccountStatement saveTRAStatement(TRAStatementResource resource, String username) {
        TrustRetentionAccount trustRetentionAccount = traRepository.getOne(resource.getTrustRetentionAccountId());
        TrustRetentionAccountStatement trustRetentionAccountStatement = resource.getTrustRetentionAccountStatement();
        trustRetentionAccountStatement.setSerialNumber(traStatementRepository.findByTrustRetentionAccount(trustRetentionAccount).size() + 1);
        trustRetentionAccountStatement.setTrustRetentionAccount(trustRetentionAccount);
        trustRetentionAccountStatement = traStatementRepository.save(trustRetentionAccountStatement);

        // Change Documents for TRA A/c Stmt
        changeDocumentService.createChangeDocument(
                trustRetentionAccount.getLoanMonitor().getId(), trustRetentionAccountStatement.getId(),null,
                trustRetentionAccount.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                trustRetentionAccount,
                "Created",
                username,
                "Monitoring", "TRA Account Statement");


        return trustRetentionAccountStatement;

    }

    @Override
    public TrustRetentionAccountStatement updateTRAStatement(TRAStatementResource resource, String username) throws CloneNotSupportedException {
        TrustRetentionAccountStatement existingTrustRetentionAccountStatement
                = traStatementRepository.getOne(resource.getTrustRetentionAccountStatement().getId());

        Object oldTRAAccountStatement = existingTrustRetentionAccountStatement.clone();

        existingTrustRetentionAccountStatement.setViewRights(resource.getTrustRetentionAccountStatement().getViewRights());
        existingTrustRetentionAccountStatement.setRemarks(resource.getTrustRetentionAccountStatement().getRemarks());
        existingTrustRetentionAccountStatement.setPeriodQuarter(resource.getTrustRetentionAccountStatement().getPeriodQuarter());
        existingTrustRetentionAccountStatement.setPeriodYear(resource.getTrustRetentionAccountStatement().getPeriodYear());
        existingTrustRetentionAccountStatement.setDocumentType(resource.getTrustRetentionAccountStatement().getDocumentType());
        existingTrustRetentionAccountStatement.setFileReference(resource.getTrustRetentionAccountStatement().getFileReference());
        existingTrustRetentionAccountStatement = traStatementRepository.save(existingTrustRetentionAccountStatement);


        // Change Documents for TRA A/c Stmt
        changeDocumentService.createChangeDocument(
                existingTrustRetentionAccountStatement.getTrustRetentionAccount().getLoanMonitor().getId(),
                existingTrustRetentionAccountStatement.getId(),existingTrustRetentionAccountStatement.getTrustRetentionAccount().getId(),
                existingTrustRetentionAccountStatement.getTrustRetentionAccount().getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldTRAAccountStatement,
                existingTrustRetentionAccountStatement,
                "Updated",
                username,
                "Monitoring", "TRA Account Statement");


        return existingTrustRetentionAccountStatement;

    }

    @Override
    public List<TRAStatementResource> getTrustRetentionAccountStatements(String trustRetentionAccountId, String name) {
        List<TRAStatementResource>  traStatementResources  = new ArrayList<>();
        TrustRetentionAccount trustRetentionAccount = traRepository.getOne(trustRetentionAccountId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(trustRetentionAccount != null) {
            List<TrustRetentionAccountStatement> trustRetentionAccountStatements
                    = traStatementRepository.findByTrustRetentionAccount(trustRetentionAccount);
            trustRetentionAccountStatements.forEach(
                    trustRetentionAccountStatement -> {
                        TRAStatementResource traStatementResource = new TRAStatementResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        traStatementResource.setTrustRetentionAccountId(trustRetentionAccount.getId());
                        traStatementResource.setTrustRetentionAccountStatement(trustRetentionAccountStatement);
                        traStatementResources.add(traStatementResource);
                    }
            );
        }
        Collections.sort(traStatementResources, Comparator.comparingInt((TRAStatementResource a) ->
                a.getTrustRetentionAccountStatement().getSerialNumber()).reversed());
        return traStatementResources;

    }

    //Terms and Conditions
    @Override
    public TermsAndConditionsModification saveTermsAndConditions(TermsAndConditionsResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(),
                    loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        TermsAndConditionsModification termsAndConditions = resource.getTermsAndConditionsModification();
        termsAndConditions.setLoanMonitor(loanMonitor);
        termsAndConditions.setSerialNumber(termsAndConditionsRepository.findByLoanMonitor(loanMonitor).size() + 1);
        termsAndConditions.setDocumentType(resource.getTermsAndConditionsModification().getDocumentType());
        termsAndConditions.setDocumentTitle(resource.getTermsAndConditionsModification().getDocumentTitle());
        termsAndConditions.setCommunication(resource.getTermsAndConditionsModification().getCommunication());
        termsAndConditions.setBorrowerRequestLetterDate(resource.getTermsAndConditionsModification().getBorrowerRequestLetterDate());
        termsAndConditions.setDateOfIssueOfAmendedDocument(resource.getTermsAndConditionsModification().getDateOfIssueOfAmendedDocument());
        termsAndConditions.setRemarks(resource.getTermsAndConditionsModification().getRemarks());
        termsAndConditions.setFileReference(resource.getTermsAndConditionsModification().getFileReference());
        termsAndConditions.setAmendedDocumentType(resource.getTermsAndConditionsModification().getAmendedDocumentType());
        termsAndConditions.setDateOfIssueOfAmendedDocument(resource.getTermsAndConditionsModification().getDateOfIssueOfAmendedDocument());
        termsAndConditions.setAmendedDocumentRemarks(resource.getTermsAndConditionsModification().getAmendedDocumentRemarks());
        termsAndConditions.setAmendedDocumentTitle(resource.getTermsAndConditionsModification().getAmendedDocumentTitle());
        termsAndConditions.setAmendedDocumentFileReference(resource.getTermsAndConditionsModification().getAmendedDocumentFileReference());
        termsAndConditions = termsAndConditionsRepository.save(termsAndConditions);

        // Change Documents for Terms and Conditions
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),termsAndConditions.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                termsAndConditions,
                "Created",
                username,
                "Monitoring", "Terms and Conditions" );

        return termsAndConditions;

    }

    @Override
    public TermsAndConditionsModification updateTermsAndConditions(TermsAndConditionsResource resource, String username) throws CloneNotSupportedException {
        TermsAndConditionsModification existingTermsAndConditionsModification
                = termsAndConditionsRepository.getOne(resource.getTermsAndConditionsModification().getId());

        Object oldTermsAndConditionsMod = existingTermsAndConditionsModification.clone();

        existingTermsAndConditionsModification.setDocumentType(resource.getTermsAndConditionsModification().getDocumentType());
        existingTermsAndConditionsModification.setDocumentTitle(resource.getTermsAndConditionsModification().getDocumentTitle());
        existingTermsAndConditionsModification.setCommunication(resource.getTermsAndConditionsModification().getCommunication());
        existingTermsAndConditionsModification.setBorrowerRequestLetterDate(resource.getTermsAndConditionsModification().getBorrowerRequestLetterDate());
        existingTermsAndConditionsModification.setDateOfIssueOfAmendedSanctionLetter(resource.getTermsAndConditionsModification().getDateOfIssueOfAmendedSanctionLetter());
        existingTermsAndConditionsModification.setRemarks(resource.getTermsAndConditionsModification().getRemarks());
        existingTermsAndConditionsModification.setFileReference(resource.getTermsAndConditionsModification().getFileReference());
        existingTermsAndConditionsModification.setAmendedDocumentType(resource.getTermsAndConditionsModification().getAmendedDocumentType());
        existingTermsAndConditionsModification.setDateOfIssueOfAmendedDocument(resource.getTermsAndConditionsModification().getDateOfIssueOfAmendedDocument());
        existingTermsAndConditionsModification.setAmendedDocumentRemarks(resource.getTermsAndConditionsModification().getAmendedDocumentRemarks());
        existingTermsAndConditionsModification.setAmendedDocumentTitle(resource.getTermsAndConditionsModification().getAmendedDocumentTitle());
        existingTermsAndConditionsModification.setAmendedDocumentFileReference(resource.getTermsAndConditionsModification().getAmendedDocumentFileReference());
        existingTermsAndConditionsModification = termsAndConditionsRepository.save(existingTermsAndConditionsModification);

        // Change Documents for T&C Mod.
        changeDocumentService.createChangeDocument(
                existingTermsAndConditionsModification.getLoanMonitor().getId(),
                existingTermsAndConditionsModification.getId(),null,
                existingTermsAndConditionsModification.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldTermsAndConditionsMod,
                existingTermsAndConditionsModification,
                "Updated",
                username,
                "Monitoring", "Terms and Conditions" );


        return existingTermsAndConditionsModification;

    }


    @Override
    public List<TermsAndConditionsResource> getTermsAndConditions(String loanApplicationId, String name) {
        List<TermsAndConditionsResource> termsAndConditionsResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<TermsAndConditionsModification> termsAndConditions
                    = termsAndConditionsRepository.findByLoanMonitor(loanMonitor);
            termsAndConditions.forEach(
                    termsAndCondition -> {
                        TermsAndConditionsResource termsAndConditionsResource = new TermsAndConditionsResource();
                        termsAndConditionsResource.setLoanApplicationId(loanApplication.getId());
                        termsAndConditionsResource.setTermsAndConditionsModification(termsAndCondition);
                        termsAndConditionsResources.add(termsAndConditionsResource);
                    }
            );
        }
        Collections.sort(termsAndConditionsResources, Comparator.comparingInt((TermsAndConditionsResource a) ->
                a.getTermsAndConditionsModification().getSerialNumber()).reversed());
        return termsAndConditionsResources;


    }


    // Security Compliance
    @Override
    public SecurityCompliance saveSecurityCompliance(SecurityComplianceResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        SecurityCompliance securityCompliance = resource.getSecurityCompliance();
        securityCompliance.setLoanMonitor(loanMonitor);
        securityCompliance.setSerialNumber(securityComplianceRepository.findByLoanMonitor(loanMonitor).size() + 1);
        securityCompliance.setCollateralObjectType(resource.getSecurityCompliance().getCollateralObjectType());
        securityCompliance.setQuantity(resource.getSecurityCompliance().getQuantity());
        securityCompliance.setApplicability(resource.getSecurityCompliance().getApplicability());
        securityCompliance.setCollateralAgreementType(resource.getSecurityCompliance().getCollateralAgreementType());
        securityCompliance.setTimelines(resource.getSecurityCompliance().getTimelines());
        securityCompliance.setDateOfCreation(resource.getSecurityCompliance().getDateOfCreation());
        securityCompliance.setValidityDate(resource.getSecurityCompliance().getValidityDate());
        securityCompliance.setValue(resource.getSecurityCompliance().getValue());
        securityCompliance.setSecurityPerfectionDate(resource.getSecurityCompliance().getSecurityPerfectionDate());
        securityCompliance.setRemarks(resource.getSecurityCompliance().getRemarks());
        // securityCompliance.setActionPeriod(resource.getSecurityCompliance().getActionPeriod());
        securityCompliance.setEventType(resource.getSecurityCompliance().getEventType());
        securityCompliance.setEventDate(resource.getSecurityCompliance().getEventDate());
        securityCompliance.setTimelineDate(resource.getSecurityCompliance().getTimelineDate());
        securityCompliance.setLocation(resource.getSecurityCompliance().getLocation());
        securityCompliance.setAdditionalText(resource.getSecurityCompliance().getAdditionalText());
        securityCompliance.setRealEstateLandArea(resource.getSecurityCompliance().getRealEstateLandArea());
        securityCompliance.setAreaUnitOfMeasure(resource.getSecurityCompliance().getAreaUnitOfMeasure());
        securityCompliance.setSecurityNoOfUnits(resource.getSecurityCompliance().getSecurityNoOfUnits());
        securityCompliance.setSecurityFaceValueAmount(resource.getSecurityCompliance().getSecurityFaceValueAmount());
        securityCompliance.setHoldingPercentage(resource.getSecurityCompliance().getHoldingPercentage());

        securityCompliance = securityComplianceRepository.save(securityCompliance);

        // Change Documents for Sec. Compliance
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),
                securityCompliance.getId().toString(),
                null,
                loanApplication.getLoanContractId(),
                null,
                securityCompliance,
                "Created",
                username,
                "Monitoring", "Security Compliance" );


        return securityCompliance;

    }

    @Override
    public SecurityCompliance updateSecurityCompliance(SecurityComplianceResource resource, String username) throws CloneNotSupportedException {
        SecurityCompliance existingSecurityCompliance
                = securityComplianceRepository.getOne(resource.getSecurityCompliance().getId());

        Object oldSecurityCompliance = existingSecurityCompliance.clone();

        existingSecurityCompliance.setCollateralObjectType(resource.getSecurityCompliance().getCollateralObjectType());
        existingSecurityCompliance.setQuantity(resource.getSecurityCompliance().getQuantity());
        existingSecurityCompliance.setApplicability(resource.getSecurityCompliance().getApplicability());
        existingSecurityCompliance.setCollateralAgreementType(resource.getSecurityCompliance().getCollateralAgreementType());
        existingSecurityCompliance.setTimelines(resource.getSecurityCompliance().getTimelines());
        existingSecurityCompliance.setDateOfCreation(resource.getSecurityCompliance().getDateOfCreation());
        existingSecurityCompliance.setValidityDate(resource.getSecurityCompliance().getValidityDate());
        existingSecurityCompliance.setValue(resource.getSecurityCompliance().getValue());
        existingSecurityCompliance.setSecurityPerfectionDate(resource.getSecurityCompliance().getSecurityPerfectionDate());
        existingSecurityCompliance.setRemarks(resource.getSecurityCompliance().getRemarks());
        // existingSecurityCompliance.setActionPeriod(resource.getSecurityCompliance().getActionPeriod());
        existingSecurityCompliance.setEventType(resource.getSecurityCompliance().getEventType());
        existingSecurityCompliance.setEventDate(resource.getSecurityCompliance().getEventDate());
        existingSecurityCompliance.setTimelineDate(resource.getSecurityCompliance().getTimelineDate());
        existingSecurityCompliance.setLocation(resource.getSecurityCompliance().getLocation());
        existingSecurityCompliance.setAdditionalText(resource.getSecurityCompliance().getAdditionalText());
        existingSecurityCompliance.setRealEstateLandArea(resource.getSecurityCompliance().getRealEstateLandArea());
        existingSecurityCompliance.setAreaUnitOfMeasure(resource.getSecurityCompliance().getAreaUnitOfMeasure());
        existingSecurityCompliance.setSecurityNoOfUnits(resource.getSecurityCompliance().getSecurityNoOfUnits());
        existingSecurityCompliance.setSecurityFaceValueAmount(resource.getSecurityCompliance().getSecurityFaceValueAmount());
        existingSecurityCompliance.setHoldingPercentage(resource.getSecurityCompliance().getHoldingPercentage());

        existingSecurityCompliance = securityComplianceRepository.save(existingSecurityCompliance);


        // Change Documents for T&C Mod.
        changeDocumentService.createChangeDocument(
                existingSecurityCompliance.getLoanMonitor().getId(),
                existingSecurityCompliance.getId(),null,
                existingSecurityCompliance.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldSecurityCompliance,
                existingSecurityCompliance,
                "Updated",
                username,
                "Monitoring", "Security Compliance" );


        return existingSecurityCompliance;

    }

    @Override
    public List<SecurityComplianceResource> getSecurityCompliance(String loanApplicationId, String name) {
        List<SecurityComplianceResource> securityComplianceResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<SecurityCompliance> securityCompliances
                    = securityComplianceRepository.findByLoanMonitor(loanMonitor);
            securityCompliances.forEach(
                    securityCompliance -> {
                        SecurityComplianceResource securityComplianceResource = new SecurityComplianceResource();
                        securityComplianceResource.setLoanApplicationId(loanApplication.getId());
                        securityComplianceResource.setSecurityCompliance(securityCompliance);
                        securityComplianceResources.add(securityComplianceResource);
                    }
            );
        }
        Collections.sort(securityComplianceResources, Comparator.comparingInt((SecurityComplianceResource a) ->
                a.getSecurityCompliance().getSerialNumber()).reversed());
        return securityComplianceResources;

    }


    @Override
    public SiteVisit saveSiteVisit(SiteVisitResource resource, String app, String username) throws CloneNotSupportedException {

//        boolean createChangeDocument = false;
//
//        if (resource.getSiteVisit().getId().length() == 0)
//            createChangeDocument = true;

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        SiteVisit siteVisit = resource.getSiteVisit();
        siteVisit.setLoanApplication(loanApplication);

        LoanMonitor loanMonitor = null;
        LoanAppraisal loanAppraisal = null; //new LoanAppraisal();

        if (app.equals("monitoring")) {
            loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
            if (loanMonitor == null) {
                loanMonitor = new LoanMonitor();
                loanMonitor.setLoanApplication(loanApplication);
                loanMonitor = loanMonitorRepository.save(loanMonitor);
                siteVisit.setLoanMonitoringId(loanMonitor.getId().toString());
                // Change Documents for Monitoring Header
                changeDocumentService.createChangeDocument(
                        loanMonitor.getId(), loanMonitor.getId().toString(), loanMonitor.getId().toString(),
                        loanApplication.getLoanContractId(),
                        null,
                        loanMonitor,
                        "Created",
                        username,
                        "Monitoring", "Header");
             }
            else
                siteVisit.setLoanMonitoringId(loanMonitor.getId().toString());
        }
        else if (app.equals("appraisal"))
        {
            Optional<LoanAppraisal> loanAppraisalOptional = loanAppraisalRepository.findByLoanApplication(loanApplication);
            if (loanAppraisalOptional != null) {
                loanAppraisal = loanAppraisalOptional.get();
            } else {
                loanAppraisal = new LoanAppraisal();
                loanAppraisal.setLoanApplication(loanApplication);
                loanAppraisal = loanAppraisalRepository.save(loanAppraisal);
                // Change Documents for Monitoring Header
                changeDocumentService.createChangeDocument(
                        loanAppraisal.getId(), loanAppraisal.getId().toString(), loanAppraisal.getId().toString(),
                        loanApplication.getLoanContractId(),
                        null,
                        loanAppraisal,
                        "Created",
                        username,
                        "Appraisal", "Header");
            }

            siteVisit.setLoanAppraisalId(loanAppraisal.getId().toString());
        }

        Object oldSiteVisit = new SiteVisit();
        if (resource.getSiteVisit().getId().length() > 0) {
            siteVisit = siteVisitRepository.getOne(resource.getSiteVisit().getId());
            oldSiteVisit = siteVisit.clone();
        }
        // siteVisit.setSerialNumber(siteVisitRepository.findByLoanMonitor(loanMonitor).size() + 1);
        siteVisit.setSiteVisitType(resource.getSiteVisit().getSiteVisitType());
        siteVisit.setActualCOD(resource.getSiteVisit().getActualCOD());
        siteVisit.setDateOfLendersMeet(resource.getSiteVisit().getDateOfLendersMeet());
        siteVisit.setDateOfSiteVisit(resource.getSiteVisit().getDateOfSiteVisit());
        siteVisit.setDocumentType(resource.getSiteVisit().getDocumentType());
        siteVisit.setDocumentTitle(resource.getSiteVisit().getDocumentTitle());
        siteVisit.setFileReference(resource.getSiteVisit().getFileReference());
        siteVisit.setFiscalYear(resource.getSiteVisit().getFiscalYear());
        siteVisit = siteVisitRepository.save(siteVisit);


        // Change Documents for Site Visit

            if (app == "monitoring") {
                changeDocumentService.createChangeDocument(
                        loanMonitor.getId(), siteVisit.getId().toString(), loanAppraisal.getId().toString(),
                        loanApplication.getLoanContractId(),
                        null,
                        siteVisit,
                        "Created",
                        username,
                        "Monitoring", "Site Visit");
            } else {
                changeDocumentService.createChangeDocument(
                        loanAppraisal.getId(), siteVisit.getId().toString(), loanAppraisal.getId().toString(),
                        loanApplication.getLoanContractId(),
                        null,
                        siteVisit,
                        "Created",
                        username,
                        "Appraisal", "Site Visit");
            }





        return siteVisit;

    }

    @Override
    public SiteVisit updateSiteVisit(SiteVisitResource resource, String username) throws CloneNotSupportedException {
        SiteVisit existingSiteVisit
                = siteVisitRepository.getOne(resource.getSiteVisit().getId());

        Object oldSiteVisit = existingSiteVisit.clone();

        existingSiteVisit.setSerialNumber(resource.getSiteVisit().getSerialNumber());
        existingSiteVisit.setSiteVisitType(resource.getSiteVisit().getSiteVisitType());
        existingSiteVisit.setActualCOD(resource.getSiteVisit().getActualCOD());
        existingSiteVisit.setDateOfSiteVisit(resource.getSiteVisit().getDateOfSiteVisit());
        existingSiteVisit.setDateOfLendersMeet(resource.getSiteVisit().getDateOfLendersMeet());
        existingSiteVisit.setDocumentType(resource.getSiteVisit().getDocumentType());
        existingSiteVisit.setDocumentTitle(resource.getSiteVisit().getDocumentTitle());
        existingSiteVisit.setFileReference(resource.getSiteVisit().getFileReference());
        existingSiteVisit.setFiscalYear(resource.getSiteVisit().getFiscalYear());

        existingSiteVisit = siteVisitRepository.save(existingSiteVisit);

        String loanAppraisalId = existingSiteVisit.getLoanAppraisalId();
        String loanMonitorId = existingSiteVisit.getLoanMonitoringId();
        LoanApplication loanApplication = existingSiteVisit.getLoanApplication();

        //if (app == "monitoring") {
        if (loanMonitorId != null) {
         changeDocumentService.createChangeDocument(
                UUID.fromString(loanMonitorId), existingSiteVisit.getId().toString(), loanMonitorId,
                loanApplication.getLoanContractId(),
                oldSiteVisit,
                 existingSiteVisit,
                "Updated",
                username,
                "Monitoring", "Site Visit");
            }
         else {
            changeDocumentService.createChangeDocument(
                    UUID.fromString(loanAppraisalId), existingSiteVisit.getId().toString(), loanAppraisalId,
                    loanApplication.getLoanContractId(),
                    oldSiteVisit,
                    existingSiteVisit,
                    "Updated",
                    username,
                    "Appraisal", "Site Visit");
         }
        return existingSiteVisit;

    }

    @Override
    public List<SiteVisitResource> getSiteVisit(String loanApplicationId, String name) {
        List<SiteVisitResource> siteVisitResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<SiteVisit> siteVisits
                    = siteVisitRepository.findByLoanApplication(loanApplication);
            siteVisits.forEach(
                    siteVisit -> {
                        SiteVisitResource siteVisitResource = new SiteVisitResource();
                        siteVisitResource.setLoanApplicationId(loanApplication.getId());
                        siteVisitResource.setSiteVisit(siteVisit);
                        siteVisitResources.add(siteVisitResource);
                    }
            );
        }
        Collections.sort(siteVisitResources, Comparator.comparingInt((SiteVisitResource a) ->
                a.getSiteVisit().getSerialNumber()).reversed());
        return siteVisitResources;

    }

    //Operating Parameter
    @Override
    public OperatingParameter saveOperatingParameter(OperatingParameterResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        OperatingParameter operatingParameter = resource.getOperatingParameter();
        operatingParameter.setLoanMonitor(loanMonitor);
        operatingParameter.setSerialNumber(operatingParameterRepository.findByLoanMonitor(loanMonitor).size() + 1);
        operatingParameter = operatingParameterRepository.save(operatingParameter);

        // Change Documents for Operating Parameter
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(), operatingParameter.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                operatingParameter,
                "Created",
                username,
                "Monitoring", "Operating Parameter");

        return operatingParameter;

    }

    @Override
    public OperatingParameter updateOperatingParameter(OperatingParameterResource resource, String username) throws CloneNotSupportedException {
        OperatingParameter existingOperatingParameter
                = operatingParameterRepository.getOne(resource.getOperatingParameter().getId());

        Object oldExistingOperatingParameter = existingOperatingParameter.clone();

        existingOperatingParameter.setSerialNumber(resource.getOperatingParameter().getSerialNumber());
        existingOperatingParameter.setMonth(resource.getOperatingParameter().getMonth());
        existingOperatingParameter.setYear(resource.getOperatingParameter().getYear());
        existingOperatingParameter.setExportNetGeneration(resource.getOperatingParameter().getExportNetGeneration());
        existingOperatingParameter.setPlfCufActual(resource.getOperatingParameter().getPlfCufActual());
        existingOperatingParameter.setApplicableTariff(resource.getOperatingParameter().getApplicableTariff());
        existingOperatingParameter.setRevenue(resource.getOperatingParameter().getRevenue());
        existingOperatingParameter.setDateOfInvoice(resource.getOperatingParameter().getDateOfInvoice());
        existingOperatingParameter.setDateOfPaymentReceipt(resource.getOperatingParameter().getDateOfPaymentReceipt());
        existingOperatingParameter.setCarbonDiOxideEmission(resource.getOperatingParameter().getCarbonDiOxideEmission());
        existingOperatingParameter.setWaterSaved(resource.getOperatingParameter().getWaterSaved());
        existingOperatingParameter.setDocumentType(resource.getOperatingParameter().getDocumentType());
        existingOperatingParameter.setDocumentTitle(resource.getOperatingParameter().getDocumentTitle());
        existingOperatingParameter.setFileReference(resource.getOperatingParameter().getFileReference());
        existingOperatingParameter = operatingParameterRepository.save(existingOperatingParameter);


        // Change Documents for Operating Parameter
        changeDocumentService.createChangeDocument(
                existingOperatingParameter.getLoanMonitor().getId(), existingOperatingParameter.getId(),null,
                existingOperatingParameter.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldExistingOperatingParameter,
                existingOperatingParameter,
                "Updated",
                username,
                "Monitoring", "Operating Parameter");

        return existingOperatingParameter;
    }

    @Override
    public List<OperatingParameterResource> getOperatingParameter(String loanApplicationId, String name) {
        List<OperatingParameterResource> operatingParameterResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<OperatingParameter> operatingparameters
                    = operatingParameterRepository.findByLoanMonitor(loanMonitor);
            operatingparameters.forEach(
                    operatingParameter -> {
                        OperatingParameterResource operatingParameterResource = new OperatingParameterResource();
                        operatingParameterResource.setLoanApplicationId(loanApplication.getId());
                        operatingParameterResource.setOperatingParameter(operatingParameter);
                        operatingParameterResources.add(operatingParameterResource);
                    }
            );
        }
        Collections.sort(operatingParameterResources, Comparator.comparingInt((OperatingParameterResource a) ->
                a.getOperatingParameter().getSerialNumber()).reversed());
        return operatingParameterResources;

    }


    //Rate Of Interest

    @Override
    public RateOfInterest saveRateOfInterest(RateOfInterestResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        RateOfInterest rateOfInterest = resource.getRateOfInterest();
        rateOfInterest.setLoanMonitor(loanMonitor);
        rateOfInterest.setSerialNumber(rateOfInterestRepository.findByLoanMonitor(loanMonitor).size() + 1);

        rateOfInterest.setConditionType(resource.getRateOfInterest().getConditionType());
        rateOfInterest.setValidFromDate(resource.getRateOfInterest().getValidFromDate());
        rateOfInterest.setInterestTypeIndicator(resource.getRateOfInterest().getInterestTypeIndicator());
        rateOfInterest.setReferenceInterestRate(resource.getRateOfInterest().getReferenceInterestRate());
        rateOfInterest.setRefInterestSign(resource.getRateOfInterest().getRefInterestSign());
        rateOfInterest.setInterestRate(resource.getRateOfInterest().getInterestRate());
        rateOfInterest.setCalculationDate(resource.getRateOfInterest().getCalculationDate());
        rateOfInterest.setIsCalculationDateOnMonthEnd(resource.getRateOfInterest().getIsCalculationDateOnMonthEnd());
        rateOfInterest.setDueDate(resource.getRateOfInterest().getDueDate());
        rateOfInterest.setInterestPaymentFrequency(resource.getRateOfInterest().getInterestPaymentFrequency());
        rateOfInterest.setPaymentForm(resource.getRateOfInterest().getPaymentForm());
        rateOfInterest.setInterestCalculationMethod(resource.getRateOfInterest().getInterestCalculationMethod());
        rateOfInterest.setInterestPeriodStartDate(resource.getRateOfInterest().getInterestPeriodStartDate());
        rateOfInterest.setInterestPeriodUnit(resource.getRateOfInterest().getInterestPeriodUnit());
        rateOfInterest.setInterestPeriodFrequency(resource.getRateOfInterest().getInterestPeriodFrequency());
        rateOfInterest.setNextInterestResetDate(resource.getRateOfInterest().getNextInterestResetDate());
        rateOfInterest = rateOfInterestRepository.save(rateOfInterest);

        // Change Documents for Rate of Interest
        changeDocumentService.createChangeDocument(
                rateOfInterest.getLoanMonitor().getId(), rateOfInterest.getId(),null,
                rateOfInterest.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                rateOfInterest,
                "Created",
                username,
                "Monitoring", "Rate of Interest");

        return rateOfInterest;

    }

    @Override
    public RateOfInterest updateRateOfInterest(RateOfInterestResource resource, String username) throws CloneNotSupportedException {
        RateOfInterest existingRateOfInterest
                = rateOfInterestRepository.getOne(resource.getRateOfInterest().getId());

        Object oldExistingRateOfInterest = existingRateOfInterest.clone();

        existingRateOfInterest.setConditionType(resource.getRateOfInterest().getConditionType());
        existingRateOfInterest.setValidFromDate(resource.getRateOfInterest().getValidFromDate());
        existingRateOfInterest.setInterestTypeIndicator(resource.getRateOfInterest().getInterestTypeIndicator());
        existingRateOfInterest.setReferenceInterestRate(resource.getRateOfInterest().getReferenceInterestRate());
        existingRateOfInterest.setRefInterestSign(resource.getRateOfInterest().getRefInterestSign());
        existingRateOfInterest.setInterestRate(resource.getRateOfInterest().getInterestRate());
        existingRateOfInterest.setCalculationDate(resource.getRateOfInterest().getCalculationDate());
        existingRateOfInterest.setIsCalculationDateOnMonthEnd(resource.getRateOfInterest().getIsCalculationDateOnMonthEnd());
        existingRateOfInterest.setDueDate(resource.getRateOfInterest().getDueDate());
        existingRateOfInterest.setInterestPaymentFrequency(resource.getRateOfInterest().getInterestPaymentFrequency());
        existingRateOfInterest.setPaymentForm(resource.getRateOfInterest().getPaymentForm());
        existingRateOfInterest.setInterestCalculationMethod(resource.getRateOfInterest().getInterestCalculationMethod());
        existingRateOfInterest.setInterestPeriodStartDate(resource.getRateOfInterest().getInterestPeriodStartDate());
        existingRateOfInterest.setInterestPeriodUnit(resource.getRateOfInterest().getInterestPeriodUnit());
        existingRateOfInterest.setInterestPeriodFrequency(resource.getRateOfInterest().getInterestPeriodFrequency());
        existingRateOfInterest.setNextInterestResetDate(resource.getRateOfInterest().getNextInterestResetDate());

        existingRateOfInterest = rateOfInterestRepository.save(existingRateOfInterest);


        // Change Documents for Rate of Interest
        changeDocumentService.createChangeDocument(
                existingRateOfInterest.getLoanMonitor().getId(), existingRateOfInterest.getId(),null,
                existingRateOfInterest.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldExistingRateOfInterest,
                existingRateOfInterest,
                "Updated",
                username,
                "Monitoring", "Rate of Interest");


        return existingRateOfInterest;
    }

    @Override
    public List<RateOfInterestResource> getRateOfInterest(String loanApplicationId, String name) {
        List<RateOfInterestResource> rateOfInterestResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<RateOfInterest> rateOfInterests
                    = rateOfInterestRepository.findByLoanMonitor(loanMonitor);
            rateOfInterests.forEach(
                    rateOfInterest -> {
                        RateOfInterestResource rateOfInterestResource = new RateOfInterestResource();
                        rateOfInterestResource.setLoanApplicationId(loanApplication.getId());
                        rateOfInterestResource.setRateOfInterest(rateOfInterest);
                        rateOfInterestResources.add(rateOfInterestResource);
                    }
            );
        }
        Collections.sort(rateOfInterestResources, Comparator.comparingInt((RateOfInterestResource a) ->
                a.getRateOfInterest().getSerialNumber()).reversed());
        return rateOfInterestResources;

    }

    // Borrower Financials

    @Override
    public BorrowerFinancials saveBorrowerFinancials(BorrowerFinancialsResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        BorrowerFinancials borrowerFinancials = resource.getBorrowerFinancials();
        borrowerFinancials.setLoanMonitor(loanMonitor);
        borrowerFinancials.setSerialNumber(borrowerFinancialsRepository.findByLoanMonitor(loanMonitor).size() + 1);
        // borrowerFinancials.setBorrowerFinancialsId(resource.getBorrowerFinancials().getBorrowerFinancialsId());
        borrowerFinancials.setFiscalYear(resource.getBorrowerFinancials().getFiscalYear());
        borrowerFinancials.setTurnover(resource.getBorrowerFinancials().getTurnover());
        borrowerFinancials.setPat(resource.getBorrowerFinancials().getPat());
        borrowerFinancials.setNetWorth(resource.getBorrowerFinancials().getNetWorth());
        borrowerFinancials.setDateOfExternalRating(resource.getBorrowerFinancials().getDateOfExternalRating());
        borrowerFinancials.setNextDueDateOfExternalRating(resource.getBorrowerFinancials().getNextDueDateOfExternalRating());
        borrowerFinancials.setOverAllRating(resource.getBorrowerFinancials().getOverAllRating());
        borrowerFinancials.setAnnualReturnFileReference(resource.getBorrowerFinancials().getAnnualReturnFileReference());
        borrowerFinancials.setRatingFileReference(resource.getBorrowerFinancials().getRatingFileReference());
        borrowerFinancials = borrowerFinancialsRepository.save(borrowerFinancials);


        // Change Documents for Operating Parameter
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(), borrowerFinancials.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                borrowerFinancials,
                "Created",
                username,
                "Monitoring", "Borrower Financials");


        return borrowerFinancials;

    }

    @Override
    public BorrowerFinancials updateBorrowerFinancials(BorrowerFinancialsResource resource, String username) throws CloneNotSupportedException {
        BorrowerFinancials existingBorrowerFinancials
                = borrowerFinancialsRepository.getOne(resource.getBorrowerFinancials().getId());

        Object oldBorrowerFinancials = existingBorrowerFinancials.clone();

        existingBorrowerFinancials.setFiscalYear(resource.getBorrowerFinancials().getFiscalYear());
        existingBorrowerFinancials.setTurnover(resource.getBorrowerFinancials().getTurnover());
        existingBorrowerFinancials.setPat(resource.getBorrowerFinancials().getPat());
        existingBorrowerFinancials.setNetWorth(resource.getBorrowerFinancials().getNetWorth());
        existingBorrowerFinancials.setDateOfExternalRating(resource.getBorrowerFinancials().getDateOfExternalRating());
        existingBorrowerFinancials.setNextDueDateOfExternalRating(resource.getBorrowerFinancials().getNextDueDateOfExternalRating());
        existingBorrowerFinancials.setOverAllRating(resource.getBorrowerFinancials().getOverAllRating());
        existingBorrowerFinancials.setAnnualReturnFileReference(resource.getBorrowerFinancials().getAnnualReturnFileReference());
        existingBorrowerFinancials.setRatingFileReference(resource.getBorrowerFinancials().getRatingFileReference());
        existingBorrowerFinancials = borrowerFinancialsRepository.save(existingBorrowerFinancials);

        // Change Documents for Borrower Financials
        changeDocumentService.createChangeDocument(
                existingBorrowerFinancials.getLoanMonitor().getId(), existingBorrowerFinancials.getId(),null,
                existingBorrowerFinancials.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                oldBorrowerFinancials,
                "Updated",
                username,
                "Monitoring", "Borrower Financials");


        return existingBorrowerFinancials;


    }

    @Override
    public List<BorrowerFinancialsResource> getBorrowerFinancials(String loanApplicationId, String name) {
        List<BorrowerFinancialsResource> borrowerFinancialsResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<BorrowerFinancials> borrowerFinancials
                    = borrowerFinancialsRepository.findByLoanMonitor(loanMonitor);
            borrowerFinancials.forEach(
                    borrowerFinancial -> {
                        BorrowerFinancialsResource borrowerFinancialsResource = new BorrowerFinancialsResource();
                        borrowerFinancialsResource.setLoanApplicationId(loanApplication.getId());
                        borrowerFinancialsResource.setBorrowerFinancials(borrowerFinancial);
                        borrowerFinancialsResources.add(borrowerFinancialsResource);
                    }
            );
        }
        Collections.sort(borrowerFinancialsResources, Comparator.comparingInt((BorrowerFinancialsResource a) ->
                a.getBorrowerFinancials().getSerialNumber()).reversed());
        return borrowerFinancialsResources;
    }

    // Promoter Financials

    @Override
    public PromoterFinancials savePromoterFinancials(PromoterFinancialsResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        PromoterFinancials promoterfinancials = resource.getPromoterFinancials();
        promoterfinancials.setLoanMonitor(loanMonitor);
        promoterfinancials.setSerialNumber(promoterfinancialsRepository.findByLoanMonitor(loanMonitor).size() + 1);
        promoterfinancials.setBorrowerFinancialsId(resource.getPromoterFinancials().getBorrowerFinancialsId());
        promoterfinancials.setFiscalYear(resource.getPromoterFinancials().getFiscalYear());
        promoterfinancials.setTurnover(resource.getPromoterFinancials().getTurnover());
        promoterfinancials.setPat(resource.getPromoterFinancials().getPat());
        promoterfinancials.setNetWorth(resource.getPromoterFinancials().getNetWorth());
        promoterfinancials.setDateOfExternalRating(resource.getPromoterFinancials().getDateOfExternalRating());
        promoterfinancials.setNextDueDateOfExternalRating(resource.getPromoterFinancials().getNextDueDateOfExternalRating());
        promoterfinancials.setOverAllRating(resource.getPromoterFinancials().getOverAllRating());
        promoterfinancials.setAnnualReturnFileReference(resource.getPromoterFinancials().getAnnualReturnFileReference());
        promoterfinancials.setRatingFileReference(resource.getPromoterFinancials().getRatingFileReference());
        promoterfinancials = promoterfinancialsRepository.save(promoterfinancials);

        // Change Documents for Promoter Financials
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(), promoterfinancials.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                promoterfinancials,
                "Created",
                username,
                "Monitoring", "Promoter Financials");

        return promoterfinancials;

    }

    @Override
    public PromoterFinancials updatePromoterFinancials(PromoterFinancialsResource resource, String username) throws CloneNotSupportedException {
        PromoterFinancials existingpromoterfinancials
                = promoterfinancialsRepository.getOne(resource.getPromoterFinancials().getId());

        Object oldPromoterDetails = existingpromoterfinancials.clone();

        existingpromoterfinancials.setFiscalYear(resource.getPromoterFinancials().getFiscalYear());
        existingpromoterfinancials.setTurnover(resource.getPromoterFinancials().getTurnover());
        existingpromoterfinancials.setPat(resource.getPromoterFinancials().getPat());
        existingpromoterfinancials.setNetWorth(resource.getPromoterFinancials().getNetWorth());
        existingpromoterfinancials.setDateOfExternalRating(resource.getPromoterFinancials().getDateOfExternalRating());
        existingpromoterfinancials.setNextDueDateOfExternalRating(resource.getPromoterFinancials().getNextDueDateOfExternalRating());
        existingpromoterfinancials.setOverAllRating(resource.getPromoterFinancials().getOverAllRating());
        existingpromoterfinancials.setAnnualReturnFileReference(resource.getPromoterFinancials().getAnnualReturnFileReference());
        existingpromoterfinancials.setRatingFileReference(resource.getPromoterFinancials().getRatingFileReference());
        existingpromoterfinancials = promoterfinancialsRepository.save(existingpromoterfinancials);

        // Change Documents fooldPromoterDetailsr Promoter Financials
        changeDocumentService.createChangeDocument(
                existingpromoterfinancials.getLoanMonitor().getId(), existingpromoterfinancials.getId(),null,
                existingpromoterfinancials.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldPromoterDetails,
                existingpromoterfinancials,
                "Updated",
                username,
                "Monitoring", "Promoter Financials");


        return existingpromoterfinancials;


    }

    @Override
    public List<PromoterFinancialsResource> getPromoterFinancials(String loanApplicationId, String name) {
        List<PromoterFinancialsResource> promoterFinancialsResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<PromoterFinancials> promoterFinancials
                    = promoterfinancialsRepository.findByLoanMonitor(loanMonitor);
            promoterFinancials.forEach(
                    promoterFinancial -> {
                        PromoterFinancialsResource promoterFinancialsResource = new PromoterFinancialsResource();
                        promoterFinancialsResource.setLoanApplicationId(loanApplication.getId());
                        promoterFinancialsResource.setPromoterFinancials(promoterFinancial);
                        promoterFinancialsResources.add(promoterFinancialsResource);
                    }
            );
        }
        Collections.sort(promoterFinancialsResources, Comparator.comparingInt((PromoterFinancialsResource a) ->
                a.getPromoterFinancials().getSerialNumber()).reversed());
        return promoterFinancialsResources;
    }

    // Financial Covenants
    @Override
    public FinancialCovenants saveFinancialCovenants(FinancialCovenantsResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        FinancialCovenants financialCovenants = resource.getFinancialCovenants();
        financialCovenants.setLoanMonitor(loanMonitor);
        financialCovenants.setSerialNumber(financialCovenantsRepository.findByLoanMonitor(loanMonitor).size() + 1);
        financialCovenants.setFinancialCovenantType(resource.getFinancialCovenants().getFinancialCovenantType());
        financialCovenants.setFinancialYear(resource.getFinancialCovenants().getFinancialYear());
        financialCovenants.setDebtEquityRatio(resource.getFinancialCovenants().getDebtEquityRatio());
        financialCovenants.setDscr(resource.getFinancialCovenants().getDscr());
        financialCovenants.setTolTnw(resource.getFinancialCovenants().getTolTnw());
        financialCovenants.setRemarksForDeviation(resource.getFinancialCovenants().getRemarksForDeviation());
        financialCovenants = financialCovenantsRepository.save(financialCovenants);

        // Change Documents for Fin. Covenants
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(), financialCovenants.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                financialCovenants,
                "Created",
                username,
                "Monitoring", "Financial Covenants");


        return financialCovenants;

    }

    @Override
    public FinancialCovenants updateFinancialCovenants(FinancialCovenantsResource resource, String username) throws CloneNotSupportedException {
        FinancialCovenants existingFinancialCovenants
                = financialCovenantsRepository.getOne(resource.getFinancialCovenants().getId());

        Object oldFinancialCovenants = existingFinancialCovenants.clone();

        existingFinancialCovenants.setFinancialCovenantType(resource.getFinancialCovenants().getFinancialCovenantType());
        existingFinancialCovenants.setFinancialYear(resource.getFinancialCovenants().getFinancialYear());
        existingFinancialCovenants.setDebtEquityRatio(resource.getFinancialCovenants().getDebtEquityRatio());
        existingFinancialCovenants.setDscr(resource.getFinancialCovenants().getDscr());
        existingFinancialCovenants.setTolTnw(resource.getFinancialCovenants().getTolTnw());
        existingFinancialCovenants.setRemarksForDeviation(resource.getFinancialCovenants().getRemarksForDeviation());
        existingFinancialCovenants = financialCovenantsRepository.save(existingFinancialCovenants);


        // Change Documents for Financial Covenants
        changeDocumentService.createChangeDocument(
                existingFinancialCovenants.getLoanMonitor().getId(), existingFinancialCovenants.getId(),null,
                existingFinancialCovenants.getLoanMonitor().getLoanApplication().getLoanContractId(),
                existingFinancialCovenants,
                oldFinancialCovenants,
                "Updated",
                username,
                "Monitoring", "Financial Covenants");

        return existingFinancialCovenants;

    }

    @Override
    public List<FinancialCovenantsResource> getFinancialCovenants(String loanApplicationId, String name) {
        List<FinancialCovenantsResource> financialCovenantsResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<FinancialCovenants> financialCovenants
                    = financialCovenantsRepository.findByLoanMonitor(loanMonitor);
            financialCovenants.forEach(
                    financialCovenant -> {
                        FinancialCovenantsResource financialCovenantsResource = new FinancialCovenantsResource();
                        financialCovenantsResource.setLoanApplicationId(loanApplication.getId());
                        financialCovenantsResource.setFinancialCovenants(financialCovenant);
                        financialCovenantsResources.add(financialCovenantsResource);
                    }
            );
        }
        Collections.sort(financialCovenantsResources, Comparator.comparingInt((FinancialCovenantsResource a) ->
                a.getFinancialCovenants().getSerialNumber()).reversed());
        return financialCovenantsResources;

    }

    @Override
    public LoanMonitor getByLoanContractId(UUID loanContractId) {

        LoanApplication  loanApplication =  loanApplicationRepository.getOne(loanContractId);
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        return loanMonitor;
    }
}
