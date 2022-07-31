package pfs.lms.enquiry.appraisal.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejectionRepository;
import pfs.lms.enquiry.appraisal.furtherdetail.FurtherDetail;
import pfs.lms.enquiry.appraisal.furtherdetail.FurtherDetailRepository;
import pfs.lms.enquiry.appraisal.loanpartner.ILoanPartnerService;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerResource;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletionRepository;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.appraisal.projectdata.ProjectDataRepository;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalCustomerRejectionResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalFurtherDetailResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalHeaderResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalProjectDataResource;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.lfa.LFARepository;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lie.LIERepository;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResource;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResourceDetails;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.SAPIntegrationRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.FileResource;
import pfs.lms.enquiry.sapintegrationservice.ISAPFileUploadIntegrationService;
import pfs.lms.enquiry.sapintegrationservice.ISAPLoanProcessesIntegrationService;
import pfs.lms.enquiry.service.ISAPIntegrationService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;
import pfs.lms.enquiry.vault.FilePointer;
import pfs.lms.enquiry.vault.FileStorage;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by sajeev on 08-May-2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanAppraisalScheduledTaskUpdateLoanContractIdPartner {

    private final LoanAppraisalRepository loanAppraisalRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final LIERepository lieRepository;
    private final LFARepository lfaRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;
    private final ILoanPartnerService loanPartnerService;

    private String userName = "admin@pfs-portal";

    @Scheduled(fixedRateString = "${batch.loanAppraisalScheduledTaskUpdateLoanContractId}",initialDelayString = "${batch.initialDelay}")
    public void updateLoanContractId() throws ParseException, IOException {
        log.info("---------------updateLoanContractId ");

        List<LoanAppraisal> loanAppraisalList = loanAppraisalRepository.findAll();

        for (LoanAppraisal loanAppraisal : loanAppraisalList) {
            if (loanAppraisal.getLoanContractId() == null) {
                if (loanAppraisal.getLoanApplication().getLoanContractId() != null) {
                    loanAppraisal.setLoanContractId(loanAppraisal.getLoanApplication().getLoanContractId());
                    loanAppraisalRepository.save(loanAppraisal);

                    changeDocumentService.createChangeDocument(
                            loanAppraisal.getId(),
                            loanAppraisal.getId().toString(),
                            loanAppraisal.getId().toString(),
                            loanAppraisal.getLoanContractId(),
                            loanAppraisal, loanAppraisal,
                            "Updated",
                            "portal-admin",
                            "Appraisal",
                            "Header");

                }
            }
        }
    }
    @Scheduled(fixedRateString = "${batch.updateMainLoanPartner}",initialDelayString = "${batch.initialDelay}")
    public void updateLoanPartnerFromLoanApplication() throws ParseException, IOException {

        log.info("---------------updateLoanPartnerFromLoanApplication ");


        Boolean createLoanPartner = true;

        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        for (LoanApplication loanApplication: loanApplications) {

            createLoanPartner = true;

            if (loanApplication.getLoanContractId() == null) {
                continue;
            }

            LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplicationId(loanApplication.getId());
            if (loanAppraisal == null) {
                loanAppraisal = new LoanAppraisal();
                loanAppraisal.setLoanApplication(loanApplication);
                loanAppraisal.setLoanContractId(loanApplication.getLoanContractId());
                loanAppraisalRepository.save(loanAppraisal);
                // Change Documents for Appraisal Header
                changeDocumentService.createChangeDocument(
                        loanAppraisal.getId(),
                        loanAppraisal.getId().toString(),
                        loanAppraisal.getId().toString(),
                        loanApplication.getLoanContractId(),
                        null,
                        loanAppraisal,
                        "Created",
                        userName,
                        "Appraisal", "Header");

            }
            List<LoanPartner> loanPartners = loanPartnerRepository.findByLoanAppraisalId(loanAppraisal.getId().toString());
            for (LoanPartner loanPartner : loanPartners) {

                log.info("Checking Loan Partner  " + loanPartner.getBusinessPartnerId() + " Contract Id :" + loanApplication.getLoanContractId() + "role :" + loanPartner.getRoleType());
                if (loanPartner.getRoleType().equals("TR0100"))
                    continue;
                String partnerInLoanApplication = loanApplication.getbusPartnerNumber();
                String partnerInLoanPartner     = loanPartner.getBusinessPartnerId();
                partnerInLoanApplication.replaceFirst("^0+(?!$)", "");
                partnerInLoanPartner.replaceFirst("^0+(?!$)", "");

                log.info("partnerInLoanApplication : " + partnerInLoanApplication);
                log.info("partnerInLoanPartner     : " + partnerInLoanPartner);
                if (partnerInLoanApplication.equals(partnerInLoanPartner)) {
                    log.info("Partner in loanApplication EQUAL TO  PartnerInLoanPartner" );
                    createLoanPartner = false;
                    continue;
                } else {
                    log.info("Partner in loanApplication NOT EQUAL TO  PartnerInLoanPartner" );

                }
            }

            Partner partner = new Partner();

            if (createLoanPartner == true) {
                LoanPartnerResource loanPartnerResource = new LoanPartnerResource();
                loanPartnerResource.setLoanApplicationId(loanApplication.getId());
                loanPartnerResource.setRoleType("TR0100");
                loanPartnerResource.setBusinessPartnerId(loanApplication.getbusPartnerNumber());
                loanPartnerResource.setRoleDescription("Main Loan Partner");
                try {
                      partner = partnerRepository.findByPartyNumber(Integer.parseInt(loanApplication.getbusPartnerNumber()));
                } catch (Exception ex) {
                    loanPartnerResource.setBusinessPartnerName(" ");

                }
                if (partner != null)
                    loanPartnerResource.setBusinessPartnerName(partner.getPartyName1() + " " +partner.getPartyName2());
                else
                    loanPartnerResource.setBusinessPartnerName(" ");

                loanPartnerResource.setStartDate(loanApplication.getLoanEnquiryDate());
                loanPartnerResource.setKycRequired(true);
                loanPartnerResource.setSerialNumber(loanPartners.size() + 1);
                try {
                    if (loanApplication.getLoanContractId() == null) {
                        log.info("Null Loan Contract"); continue;
                    }
                    log.info("Creating Main Partner: " + loanPartnerResource.getBusinessPartnerId() + " for Loan Contract " + loanApplication.getLoanContractId());
                    loanPartnerService.createLoanPartner(loanPartnerResource, userName);
                } catch (Exception exception) {
                    log.info("Error creating Loan Partner from Batch Process LoanAppraisalScheduledTaskUpdateLoanContractIdPartner ");
                }

            }
        }
    }
    @Scheduled(fixedRateString = "${batch.loanAppraisalScheduledTaskUpdateLoanPartner}",initialDelayString = "${batch.initialDelay}")
    public void updatePartnerList() throws ParseException, IOException {

        log.info("---------------updatePartnerList ");

        List<LoanAppraisal> loanAppraisalList = loanAppraisalRepository.findAll();
        List<LoanMonitor> loanMonitorList = loanMonitorRepository.findAll();

        for (LoanMonitor loanMonitor: loanMonitorList) {

            log.info("Updating Partner List for Loan Monitor :" + loanMonitor.getLoanApplication().getLoanContractId());
            log.info("Updating Partner List for Loan Monitor :" + loanMonitor.getId());


            LoanApplication loanApplication = loanApplicationRepository.getOne(loanMonitor.getLoanApplication().getId());
            if (loanApplication.getLoanContractId() == null)
                continue;

            LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                    .orElseGet(() -> {
                        LoanAppraisal obj = new LoanAppraisal();
                        obj.setLoanApplication(loanApplication);
                        obj = loanAppraisalRepository.save(obj);

                        // Change Documents for Appraisal Header
                        changeDocumentService.createChangeDocument(
                                obj.getId(),
                                obj.getId().toString(),
                                obj.getId().toString(),
                                loanApplication.getLoanContractId(),
                                null,
                                obj,
                                "Created",
                                "portal-admin",
                                "Appraisal", "Header");

                        return obj;
                    });

            List<LoanPartner> loanPartnerList = new ArrayList<>();
            loanPartnerList =  loanPartnerRepository.findByLoanApplicationIdOrderBySerialNumberDesc(loanApplication.getId());

            Integer loanPartnersCount = loanPartnerList.size();

            // Fetch list of LIE for Monitoring
            List<LendersIndependentEngineer> lendersIndependentEngineers = new ArrayList<>();
            lendersIndependentEngineers = lieRepository.findByLoanMonitor(loanMonitor);

            for (LendersIndependentEngineer lendersIndependentEngineer: lendersIndependentEngineers) {
                LoanPartner loanPartner = loanPartnerRepository.findByLoanApplicationAndBusinessPartnerId(loanApplication,lendersIndependentEngineer.getBpCode());

                // Add Loan Partner with the ROLE for LIE
                if (loanPartner == null) {

                    Integer businessPartnerId = Integer.parseInt(lendersIndependentEngineer.getBpCode());
                    Partner partner = partnerRepository.findByPartyNumber(businessPartnerId);

                    LoanPartnerResource loanPartnerResource = new LoanPartnerResource();
                    loanPartnerResource.setLoanApplicationId(loanApplication.getId());
                    loanPartnerResource.setRoleType("ZLM003");
                    loanPartnerResource.setBusinessPartnerId(businessPartnerId.toString());
                    loanPartnerResource.setStartDate(lendersIndependentEngineer.getContractPeriodFrom());
                    loanPartnerResource.setRoleDescription("Lenders Engineer");
                    loanPartnerResource.setBusinessPartnerName(partner.getPartyName1() + " " + partner.getPartyName2());
                    loanPartnerResource.setSerialNumber(loanPartnersCount + 1);
                    loanPartnerResource.setKycStatus("Not Done");
                    loanPartnerResource.setKycRequired(false);
                    loanPartnerService.createLoanPartner(loanPartnerResource,"portal-admin");

                }
            }

            // Fetch list of LIE for Monitoring
            List<LendersFinancialAdvisor> lendersFinancialAdvisors = new ArrayList<>();
            lendersFinancialAdvisors = lfaRepository.findByLoanMonitor(loanMonitor);

            loanPartnerList =  loanPartnerRepository.findByLoanApplicationIdOrderBySerialNumberDesc(loanApplication.getId());
            loanPartnersCount = loanPartnerList.size();


            for (LendersFinancialAdvisor lendersFinancialAdvisor: lendersFinancialAdvisors) {
                LoanPartner loanPartner = loanPartnerRepository.findByLoanApplicationAndBusinessPartnerId(loanApplication,lendersFinancialAdvisor.getBpCode());

                // Add Loan Partner with the ROLE for LIE
                if (loanPartner == null) {
                    LoanPartner loanPartnerLFA = new LoanPartner();
                    Integer businessPartnerId = Integer.parseInt(loanPartner.getBusinessPartnerId());

                    Partner partner = partnerRepository.findByPartyNumber(businessPartnerId);

                    LoanPartnerResource loanPartnerResource = new LoanPartnerResource();
                    loanPartnerResource.setLoanApplicationId(loanApplication.getId());
                    loanPartnerResource.setRoleType("ZLM003");
                    loanPartnerResource.setBusinessPartnerId(businessPartnerId.toString());
                    loanPartnerResource.setStartDate(lendersFinancialAdvisor.getContractPeriodFrom());
                    loanPartnerResource.setRoleDescription("Lenders Financial Advisor");
                    loanPartnerResource.setBusinessPartnerName(partner.getPartyName1() + " " + partner.getPartyName2());
                    loanPartnerResource.setSerialNumber(loanPartnersCount + 1);
                    loanPartnerResource.setKycStatus("Not Done");
                    loanPartnerResource.setKycRequired(false);
                    loanPartnerService.createLoanPartner(loanPartnerResource,"portal-admin");
                }
            }
         }
    }

}

