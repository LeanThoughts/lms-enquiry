package pfs.lms.enquiry.monitoring.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancialsRepository;
import pfs.lms.enquiry.monitoring.domain.*;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.lfa.LFARepository;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lia.LIAReportAndFee;
import pfs.lms.enquiry.monitoring.lia.LIAReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.lia.LIARepository;
import pfs.lms.enquiry.monitoring.lia.LendersInsuranceAdvisor;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.lie.LIERepository;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.llc.LLCReportAndFee;
import pfs.lms.enquiry.monitoring.llc.LLCReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.llc.LLCRepository;
import pfs.lms.enquiry.monitoring.llc.LendersLegalCouncil;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPADetail;
import pfs.lms.enquiry.monitoring.npa.NPADetailRepository;
import pfs.lms.enquiry.monitoring.npa.NPARepository;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameter;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLF;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLFRepository;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterRepository;
import pfs.lms.enquiry.monitoring.projectmonitoring.*;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancialsRepository;
import pfs.lms.enquiry.monitoring.repository.*;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.monitoring.valuer.Valuer;
import pfs.lms.enquiry.monitoring.valuer.ValuerReportAndFee;
import pfs.lms.enquiry.monitoring.valuer.ValuerReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.valuer.ValuerRepository;
import pfs.lms.enquiry.sapintegrationservice.ISAPFileUploadIntegrationService;
import pfs.lms.enquiry.sapintegrationservice.ISAPLoanProcessesIntegrationService;
import pfs.lms.enquiry.monitoring.tra.TRARepository;
import pfs.lms.enquiry.monitoring.tra.TRAStatementRepository;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccount;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;
import pfs.lms.enquiry.repository.*;
import pfs.lms.enquiry.resource.FileResource;
import pfs.lms.enquiry.service.ISAPIntegrationService;
import pfs.lms.enquiry.vault.FilePointer;
import pfs.lms.enquiry.vault.FileStorage;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sajeev on 11-Aug-19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanMonitoringScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${sap.lieUri}")
    private String lieUri;

    @Value("${sap.lieReportAndFeeUri}")
    private String lieReportAndFeeUri;

    @Value("${sap.monitorDocumentUri}")
    private String monitorDocumentUri;

    @Value("${sap.monitorServiceUri}")
    private String monitorServiceUri;

    @Value("${sap.monitorDocumentUri}")
    private String appraisalDocumentUri;


    private final ISAPIntegrationService isapIntegrationService;
    private final FileStorage fileStorage;
    private final UserRepository userRepository;
    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPLoanProcessesIntegrationService sapLoanMonitoringIntegrationService;
    private final ISAPFileUploadIntegrationService fileUploadIntegrationService;
    private final TRARepository traRepository;
    private final TRAStatementRepository traStatementRepository;
    private final LIERepository lieRepository;
    private final LIEReportAndFeeRepository lieReportAndFeeRepository;
    private final LFARepository lfaRepository;

    private final LIAReportAndFeeRepository liaReportAndFeeRepository;
    private final LIARepository liaRepository;


    private final LLCReportAndFeeRepository llcReportAndFeeRepository;
    private final LLCRepository llcRepository;

    private final ValuerRepository valuerRepository;

    private final ValuerReportAndFeeRepository valuerReportAndFeeRepository;


    private final LFAReportAndFeeRepository lfaReportAndFeeRepository;
    private final TermsAndConditionsRepository termsAndConditionsRepository;
    private final BorrowerFinancialsRepository borrowerFinancialsRepository;
    private final PromoterFinancialsRepository promoterFinancialsRepository;
    private final OperatingParameterRepository operatingParameterRepository;
    private final OperatingParameterPLFRepository operatingParameterPLFRepository;

    private final SecurityComplianceRepository securityComplianceRepository;
    private final SiteVisitRepository siteVisitRepository;
    private final RateOfInterestRepository rateOfInterestRepository;
    private final FinancialCovenantsRepository financialCovenantsRepository;
//    private final PromoterDetailRepository promoterDetailsRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ProjectMonitoringDataRepository projectMonitoringDataRepository;
    private final ProjectMonitoringDataItemRepository projectMonitoringDataItemRepository;
    private final ProjectMonitoringDataItemHistoryRepository projectMonitoringDataItemHistoryRepository;
    private final NPARepository npaRepository;
    private final NPADetailRepository npaDetailRepository;

    private final SAPLIEResource saplieResource;
    private final SAPLFAResource saplfaResource;
    private final SAPLLCResource sapllcResource;
    private final SAPLLCReportAndFeeResource sapllcReportAndFeeResource;

    private final SAPValuerResource sapValuerResource;
    private final SAPValuerReportAndFeeResource sapValuerReportAndFeeResource;



    private final SAPLIAResource sapliaResource;
     private final SAPLFAReportAndFeeResource saplfaReportAndFeeResource;
    private final SAPLIAReportAndFeeResource sapliaReportAndFeeResource;

    private final SAPLIEReportAndFeeResource saplieReportAndFeeResource;
    private final SAPDocumentAttachmentResource sapDocumentAttachmentResource;
    private final SAPTermsAndConditionsModificationResource sapTermsAndConditionsModificationResource;
    private final SAPSecurityComplianceResource sapSecurityComplianceResource;
    private final SAPSiteVisitResource sapSiteVisitResource;
    private final SAPPromoterFinancialsResource sapPromoterFinancialsResource;
    private final SAPOperatingParameterResource sapOperatingParameterResource;
    private final SAPOperatingParameterPLFCUFResource sapOperatingParameterPLFCUFResource;
    private final SAPMonitorHeaderResource sapMonitorHeaderResource;
    private final SAPInterestRateResource sapInterestRateResource;
    private final SAPFinancialCovenantsResource sapFinancialCovenantsResource;
    private final SAPBorrowerFinancialsResource sapBorrowerFinancialsResource;
    private final SAPPromoterDetailsResource sapPromoterDetailsResource;
    private final SAPPromoterDetailsItemResource sapPromoterDetailsItemResource;
    private final SAPTRAResource saptraResource;
    private final SAPTRAStatementResource saptraStatementResource;
    private final SAPProjectMonitoringDataResource sapProjectMonitoringDataResource;
    private final SAPProjectMonitoringHistoryResource sapProjectMonitoringHistoryResource;
    private final SAPProjectMonitoringDataItemResource sapProjectMonitoringDataItemResource;

    private final SAPNPADetailResource sapnpaDetailResource;
    private final SAPNPAResource sapnpaResource;

    @Scheduled(fixedRateString = "${batch.loanMonitoringScheduledTask}", initialDelayString = "${batch.initialDelay}")
    public void syncLoanApplicationsToBackend() throws ParseException, IOException {

        LendersIndependentEngineer lendersIndependentEngineer = new LendersIndependentEngineer();

        User lastChangedByUser = new User();
        lastChangedByUser = userRepository.findByEmail(lendersIndependentEngineer.getChangedByUserName());

        Object response = new Object();
        Object resource;

        //Collect SAPIntegrationPointer with the following  Posting Status = 0
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Monitoring", 0, 'C'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Monitoring", 2, 'C'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Monitoring", 0, 'U'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Monitoring", 2, 'U'));

        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 0, 'C'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 2, 'C'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 0, 'U'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 2, 'U'));


        Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
            public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });


        String serviceUri = new String();


        for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

            switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                case "Header":
                    log.info("Attempting to Post MonitorHeader to SAP AT :" + dateFormat.format(new Date()));
                    LoanMonitor loanMonitor = loanMonitorRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPMonitorHeaderResourceDetails sapMonitorHeaderResourceDetails = sapMonitorHeaderResource.mapLoanMonitorToSAP(loanMonitor);
                    SAPMonitorHeaderResource sapMonitorHeaderResource = new SAPMonitorHeaderResource();
                    sapMonitorHeaderResource.setSapMonitorHeaderResourceDetails(sapMonitorHeaderResourceDetails);

                    resource = (Object) sapMonitorHeaderResource;
                    serviceUri = monitorServiceUri + "MonitorHeaderSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "Lenders Independent Engineer":

                    log.info("Attempting to Post LIE to SAP AT :" + dateFormat.format(new Date()));
                    lendersIndependentEngineer = lieRepository.getOne(sapIntegrationPointer.getBusinessObjectId());

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLIEResourceDetails saplieResourceDetails = saplieResource.mapToSAP(lendersIndependentEngineer, lastChangedByUser);
                    SAPLIEResource d = new SAPLIEResource();
                    d.setSaplieResourceDetails(saplieResourceDetails);

                    resource = (Object) d;
                    serviceUri = monitorServiceUri + "LendersIndependentEngineerSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(d, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "LIE Report And Fee":

                    LIEReportAndFee lieReportAndFee = new LIEReportAndFee();
                    log.info("Attempting to Post LIE  Report and Fee to SAP AT :" + dateFormat.format(new Date()));
                    Optional<LIEReportAndFee> lieRF = lieReportAndFeeRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                    lieReportAndFee = lieRF.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLIEReportAndFeeResourceDetails saplieReportAndFeeResourceDetails = saplieReportAndFeeResource.mapToSAP(lieReportAndFee, lastChangedByUser);
                    SAPLIEReportAndFeeResource c = new SAPLIEReportAndFeeResource();
                    c.setSaplieReportAndFeeResourceDetails(saplieReportAndFeeResourceDetails);

                    resource = (Object) c;
                    serviceUri = monitorServiceUri + "LIEReportAndFeeSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    if (response != null) {
                        if (lieReportAndFee.getFileReference() != null && lieReportAndFee.getFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    lieReportAndFee.getFileReference(),
                                    lieReportAndFee.getLendersIndependentEngineer().getId(),
                                    "",
                                    "LIE Report & Fee",
                                    lieReportAndFee.getDocumentTitle(), lieReportAndFee.getDocumentType());
                        }
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "Lenders Insurance Advisor":

                    log.info("Attempting to Post LIA to SAP AT :" + dateFormat.format(new Date()));
                    LendersInsuranceAdvisor lendersInsuranceAdvisor = liaRepository.getOne(sapIntegrationPointer.getBusinessObjectId());

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLIAResourceDetails sapliaResourceDetails = sapliaResource.mapToSAP(lendersInsuranceAdvisor, lastChangedByUser);
                    SAPLIAResource e = new SAPLIAResource();
                    e.setSapliaResourceDetails(sapliaResourceDetails);

                    resource = (Object) e;
                    serviceUri = monitorServiceUri + "LendersInsuranceAdvisorSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(e, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "LIA Report And Fee":

                    LIAReportAndFee liaReportAndFee = new LIAReportAndFee();
                    log.info("Attempting to Post LIA  Report and Fee to SAP AT :" + dateFormat.format(new Date()));
                    Optional<LIAReportAndFee> liaRF = liaReportAndFeeRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                    liaReportAndFee = liaRF.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLIAReportAndFeeResourceDetails sapliaReportAndFeeResourceDetails = sapliaReportAndFeeResource.mapToSAP(liaReportAndFee, lastChangedByUser);
                    SAPLIAReportAndFeeResource f = new SAPLIAReportAndFeeResource();
                    f.setSapliaReportAndFeeResourceDetails(sapliaReportAndFeeResourceDetails);

                    resource = (Object) f;
                    serviceUri = monitorServiceUri + "LIAReportAndFeeSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    if (response != null) {
                        if (liaReportAndFee.getFileReference() != null && liaReportAndFee.getFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    liaReportAndFee.getFileReference(),
                                    liaReportAndFee.getId(),
                                    "",
                                    "LIA Report & Fee",
                                    liaReportAndFee.getDocumentTitle(), liaReportAndFee.getDocumentType());
                        }
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Lenders Legal Counsel":

                    log.info("Attempting to Post LLC to SAP AT :" + dateFormat.format(new Date()));
                    LendersLegalCouncil lendersLegalCouncil = llcRepository.getOne(sapIntegrationPointer.getBusinessObjectId());

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLLCResourceDetails sapllcResourceDetails = sapllcResource.mapToSAP(lendersLegalCouncil, lastChangedByUser);
                    SAPLLCResource sapllcResource  = new SAPLLCResource();
                    sapllcResource.setSapllcResourceDetails(sapllcResourceDetails);

                    resource = (Object) sapllcResource;
                    serviceUri = monitorServiceUri + "LendersLegalCounselSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "LLC Report And Fee":

                    LLCReportAndFee llcReportAndFee = new LLCReportAndFee();
                    log.info("Attempting to Post LLC  Report and Fee to SAP AT :" + dateFormat.format(new Date()));
                    Optional<LLCReportAndFee> llcRF = llcReportAndFeeRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                    llcReportAndFee = llcRF.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLLCReportAndFeeResourceDetails sapllcReportAndFeeResourceDetails = sapllcReportAndFeeResource.mapToSAP(llcReportAndFee, lastChangedByUser);
                    SAPLLCReportAndFeeResource sapllcReportAndFeeResource = new SAPLLCReportAndFeeResource();
                    sapllcReportAndFeeResource.setSapllcReportAndFeeResourceDetails(sapllcReportAndFeeResourceDetails);
                    resource = (Object) sapllcReportAndFeeResource;
                    serviceUri = monitorServiceUri + "LLCReportAndFeeSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    if (response != null) {
                        if (llcReportAndFee.getFileReference() != null && llcReportAndFee.getFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    llcReportAndFee.getFileReference(),
                                    llcReportAndFee.getId(),
                                    "",
                                    "LLC Report & Fee",
                                    llcReportAndFee.getDocumentTitle(), llcReportAndFee.getDocumentType());
                        }
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Valuer":

                    log.info("Attempting to Post Valuer to SAP AT :" + dateFormat.format(new Date()));
                    Valuer valuer = valuerRepository.getOne(sapIntegrationPointer.getBusinessObjectId());

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPValuerResourceDetails sapValuerResourceDetails = sapValuerResource.mapToSAP(valuer, lastChangedByUser);
                    SAPValuerResource sapValuerResource1  = new SAPValuerResource();
                    sapValuerResource1.setSapValuerResourceDetails(sapValuerResourceDetails);

                    resource = (Object) sapValuerResource1;
                    serviceUri = monitorServiceUri + "ValuerSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "Valuer Report And Fee":

                    ValuerReportAndFee valuerReportAndFee = new ValuerReportAndFee();
                    log.info("Attempting to Post Valuer Report and Fee to SAP AT :" + dateFormat.format(new Date()));
                    Optional<ValuerReportAndFee> valuerRF = valuerReportAndFeeRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                    valuerReportAndFee = valuerRF.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPValuerReportAndFeeResourceDetails sapllcReportAndFeeResourceDetails1 = sapValuerReportAndFeeResource.mapToSAP(valuerReportAndFee, lastChangedByUser);
                    SAPValuerReportAndFeeResource sapllcReportAndFeeResource1 = new SAPValuerReportAndFeeResource();
                    sapllcReportAndFeeResource1.setSapValuerReportAndFeeResourceDetails(sapllcReportAndFeeResourceDetails1);
                    resource = (Object) sapllcReportAndFeeResource1;
                    serviceUri = monitorServiceUri + "ValuerReportAndFeeSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    if (response != null) {
                        if (valuerReportAndFee.getFileReference() != null && valuerReportAndFee.getFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    valuerReportAndFee.getFileReference(),
                                    valuerReportAndFee.getId(),
                                    "",
                                    "Valuer Report & Fee",
                                    valuerReportAndFee.getDocumentTitle(), valuerReportAndFee.getDocumentType());
                        }
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Lenders Financial Advisor":

                    LendersFinancialAdvisor lendersFinancialAdvisor = new LendersFinancialAdvisor();
                    log.info("Attempting to Post LendersFinancialAdvisor to SAP AT :" + dateFormat.format(new Date()));
                    Optional<LendersFinancialAdvisor> lfa = lfaRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                    lendersFinancialAdvisor = lfa.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLFAResourceDetails saplfaResourceDetails = saplfaResource.mapToSAP(lendersFinancialAdvisor, lastChangedByUser);
                    SAPLFAResource saplfaResource = new SAPLFAResource();
                    saplfaResource.setSaplfaResourceDetails(saplfaResourceDetails);
                    resource = (Object) saplfaResource;
                    serviceUri = monitorServiceUri + "LendersFinancialAdvisorSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "LFA Report and Fee":

                    LFAReportAndFee lfaReportAndFee = new LFAReportAndFee();
                    log.info("Attempting to Post LFA Report and Fee to SAP AT :" + dateFormat.format(new Date()));
                    Optional<LFAReportAndFee> lfaRF = lfaReportAndFeeRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());


                    lfaReportAndFee = lfaRF.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPLFAReportAndFeeResourceDetails saplfaReportAndFeeResourceDetails = saplfaReportAndFeeResource.mapToSAP(lfaReportAndFee, lastChangedByUser);
                    SAPLFAReportAndFeeResource saplfaReportAndFeeResource = new SAPLFAReportAndFeeResource();
                    saplfaReportAndFeeResource.setSaplfaReportAndFeeResourceDetails(saplfaReportAndFeeResourceDetails);

                    resource = (Object) saplfaReportAndFeeResource;
                    serviceUri = monitorServiceUri + "LFAReportAndFeeSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    if (response != null) {
                        if (lfaReportAndFee.getFileReference() != null && lfaReportAndFee.getFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    lfaReportAndFee.getFileReference(),
                                    lfaReportAndFee.getId(),
                                    "",
                                    "LFA Report & Fee",
                                    lfaReportAndFee.getDocumentTitle(),
                                    lfaReportAndFee.getDocumentType());
                        }
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Terms and Conditions":

                    TermsAndConditionsModification termsAndConditionsModification = new TermsAndConditionsModification();
                    log.info("Attempting to Post Terms and Conditions to SAP AT :" + dateFormat.format(new Date()));
                    Optional<TermsAndConditionsModification> traMod = termsAndConditionsRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());


                    termsAndConditionsModification = traMod.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPTermsAndConditionsModificationResourceDetails sapTermsAndConditionsModificationResourceDetails = sapTermsAndConditionsModificationResource.mapToSAP(termsAndConditionsModification);
                    SAPTermsAndConditionsModificationResource sapTermsAndConditionsModificationResource = new SAPTermsAndConditionsModificationResource();
                    sapTermsAndConditionsModificationResource.setSAPTermsAndConditionsModificationDetails(sapTermsAndConditionsModificationResourceDetails);

                    resource = (Object) sapTermsAndConditionsModificationResource;
                    serviceUri = monitorServiceUri + "TermsAndConditionsModificationSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    //Borrower request letter
                    if (response != null) {
                        if (termsAndConditionsModification.getFileReference() != null && termsAndConditionsModification.getFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    termsAndConditionsModification.getFileReference(),
                                    termsAndConditionsModification.getId(),
                                    "1",
                                    "TermsAndConditionsModification",
                                    "Borrower_Request_Letter_" + termsAndConditionsModification.getDocumentTitle(),
                                    termsAndConditionsModification.getDocumentType()
                            );
                        }
                    }
                    //LeadBanker Document Type
                    if (response != null) {
                        if (termsAndConditionsModification.getLeadBankerDocumentFileReference() != null && termsAndConditionsModification.getLeadBankerDocumentFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    termsAndConditionsModification.getLeadBankerDocumentFileReference(),
                                    termsAndConditionsModification.getId(),
                                    "2",
                                    "TermsAndConditionsModification",
                                    "Lead_Banker_Doc_" + termsAndConditionsModification.getLeadBankerDocumentTitle(),
                                    termsAndConditionsModification.getLeadBankerDocumentType()
                            );
                        }
                    }


                    if (response != null) {
                        if (termsAndConditionsModification.getAmendedDocumentFileReference() != null && termsAndConditionsModification.getAmendedDocumentFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    termsAndConditionsModification.getAmendedDocumentFileReference(),
                                    termsAndConditionsModification.getId(),
                                    "3",
                                    "TermsAndConditionsModification",
                                    "T&C_Amended_Document_" + termsAndConditionsModification.getAmendedDocumentTitle(),
                                    termsAndConditionsModification.getAmendedDocumentType());
                        }
                    }

                    // Internal Document Type
                    if (response != null) {
                        if (termsAndConditionsModification.getInternalDocumentFileReference() != null && termsAndConditionsModification.getInternalDocumentFileReference().length() > 0) {
                            response = postDocument(
                                    sapIntegrationPointer.getSubBusinessProcessName(),
                                    termsAndConditionsModification.getInternalDocumentFileReference(),
                                    termsAndConditionsModification.getId(),
                                    "4",
                                    "TermsAndConditionsModification",
                                    "Internal Document_" + termsAndConditionsModification.getAmendedDocumentTitle(),
                                    termsAndConditionsModification.getInternalDocumentType()
                            );
                        }
                    }

            updateSAPIntegrationPointer(response, sapIntegrationPointer);
            break;
            case "Security Compliance":

                SecurityCompliance securityCompliance = new SecurityCompliance();
                log.info("Attempting to Post Security Compliance to SAP AT :" + dateFormat.format(new Date()));
                Optional<SecurityCompliance> secCompl = securityComplianceRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                securityCompliance = secCompl.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPSecurityComplianceResourceDetails sapSecurityComplianceResourceDetails = sapSecurityComplianceResource.mapToSAP(securityCompliance);
                SAPSecurityComplianceResource sapSecurityComplianceResource = new SAPSecurityComplianceResource();
                sapSecurityComplianceResource.setSapSecurityComplianceResourceDetails(sapSecurityComplianceResourceDetails);

                resource = (Object) sapSecurityComplianceResource;
                serviceUri = monitorServiceUri + "SecurityComplianceSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;

            case "Site Visit":
                SiteVisit siteVisit = new SiteVisit();
                log.info("Attempting to Post Site Visit to SAP AT :" + dateFormat.format(new Date()));
                Optional<SiteVisit> sV = siteVisitRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                siteVisit = sV.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPSiteVisitResourceDetails sapSiteVisitResourceDetails = sapSiteVisitResource.mapToSAP(siteVisit);
                SAPSiteVisitResource s = new SAPSiteVisitResource();
                s.setSapSiteVisitResourceDetails(sapSiteVisitResourceDetails);

                resource = (Object) s;
                serviceUri = monitorServiceUri + "SiteVisitSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                if (response != null) {
                    if (siteVisit.getFileReference() != null && siteVisit.getFileReference().length() > 0) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                siteVisit.getFileReference(),
                                siteVisit.getId(),
                                "",
                                "Site Visit",
                                "Document_" + siteVisit.getDocumentTitle(),
                                siteVisit.getDocumentType()
                        );
                    }
                }

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;

            case "Operating Parameter":

                OperatingParameter operatingParameter = new OperatingParameter();

                log.info("Attempting to Post Operating Parameter to SAP AT :" + dateFormat.format(new Date()));
                Optional<OperatingParameter> oP = operatingParameterRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                operatingParameter = oP.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPOperatingParameterResourceDetails sapOperatingParameterResourceDetails = sapOperatingParameterResource.mapToSAP(operatingParameter);
                SAPOperatingParameterResource operatingParameterResource = new SAPOperatingParameterResource();
                operatingParameterResource.setSapOperatingParameterResourceDetails(sapOperatingParameterResourceDetails);

                resource = (Object) operatingParameterResource;
                serviceUri = monitorServiceUri + "OperatingParameterSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                if (response != null) {
                    if (operatingParameter.getFileReference() != null && operatingParameter.getFileReference().length() > 0) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                operatingParameter.getFileReference(),
                                operatingParameter.getId(),
                                "",
                                "Operating Parameter",
                                operatingParameter.getDocumentTitle(),
                                operatingParameter.getDocumentType()
                        );
                    }
                }

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "Operating Parameter PLF":
                OperatingParameterPLF operatingParameterPLF = new OperatingParameterPLF();
                log.info("Attempting to Post Operating Parameter PLF to SAP AT :" + dateFormat.format(new Date()));
                Optional<OperatingParameterPLF> oPPLF = operatingParameterPLFRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                operatingParameterPLF = oPPLF.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPOperatingParameterPLFCUFResourceDetails sapOperatingParameterPLFResourceDetails = sapOperatingParameterPLFCUFResource.mapToSAP(operatingParameterPLF);
                SAPOperatingParameterPLFCUFResource sapOperatingParameterPLFCUFResource = new SAPOperatingParameterPLFCUFResource();
                sapOperatingParameterPLFCUFResource.setSapOperatingParameterPLFCUFResourceDetails(sapOperatingParameterPLFResourceDetails);

                resource = (Object) sapOperatingParameterPLFCUFResource;
                serviceUri = monitorServiceUri + "OperatingParameterPLFCUFSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;

            case "Rate of Interest":
                RateOfInterest rateOfInterest = new RateOfInterest();
                log.info("Attempting to Post Rate of Interest to SAP AT :" + dateFormat.format(new Date()));
                Optional<RateOfInterest> roi = rateOfInterestRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                rateOfInterest = roi.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPInterestRateResourceDetails sapInterestRateResourceDetails = sapInterestRateResource.mapToSAP(rateOfInterest);
                SAPInterestRateResource sapInterestRateResource = new SAPInterestRateResource();
                sapInterestRateResource.setSapInterestRateResourceDetails(sapInterestRateResourceDetails);

                resource = (Object) sapInterestRateResource;
                serviceUri = monitorServiceUri + "InterestRateSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "Borrower Financials":
                BorrowerFinancials borrowerFinancials = new BorrowerFinancials();
                log.info("Attempting to Post Borrower Financials to SAP AT :" + dateFormat.format(new Date()));
                Optional<BorrowerFinancials> bf = borrowerFinancialsRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                borrowerFinancials = bf.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPBorrowerFinancialsResourceDetails sapBorrowerFinancialsResourceDetails = sapBorrowerFinancialsResource.mapToSAP(borrowerFinancials);
                SAPBorrowerFinancialsResource sapBorrowerFinancialsResource = new SAPBorrowerFinancialsResource();
                sapBorrowerFinancialsResource.setSapBorrowerFinancialsResourceDetails(sapBorrowerFinancialsResourceDetails);

                resource = (Object) sapBorrowerFinancialsResource;
                serviceUri = monitorServiceUri + "BorrowerFinancialSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                if (response != null) {
                    if (borrowerFinancials.getRatingFileReference() != null  && borrowerFinancials.getRatingFileReference().length() > 0 ) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                borrowerFinancials.getRatingFileReference(),
                                borrowerFinancials.getId(),
                                "1",
                                "Borrower Financials Rating File",
                                "BorrowerFinancials_Annual_Report_" + borrowerFinancials.getFiscalYear(),
                                "ZPFSLM55");
                    }
                }

                if (response != null) {
                    if (borrowerFinancials.getAnnualReturnFileReference() != null && borrowerFinancials.getAnnualReturnFileReference().length() > 0) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                borrowerFinancials.getAnnualReturnFileReference(),
                                borrowerFinancials.getId(),
                                "2",
                                "Borrower Financials Annual Report File",
                                "BorrowerFinancials_Rating_" + borrowerFinancials.getFiscalYear(),
                                "ZPFSLM56"
                        );
                    }
                }


                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "Promoter Financials":
                PromoterFinancials promoterFinancials = new PromoterFinancials();
                log.info("Attempting to Post Promoter Financials to SAP AT :" + dateFormat.format(new Date()));
                Optional<PromoterFinancials> pf = promoterFinancialsRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                promoterFinancials = pf.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPPromoterFinancialResourceDetails sapPromoterFinancialResourceDetails = sapPromoterFinancialsResource.mapToSAP(promoterFinancials);
                SAPPromoterFinancialsResource sapPromoterFinancialsResource = new SAPPromoterFinancialsResource();
                sapPromoterFinancialsResource.setSapPromoterFinancialResourceDetails(sapPromoterFinancialResourceDetails);

                resource = (Object) sapPromoterFinancialsResource;
                serviceUri = monitorServiceUri + "PromoterFinancialSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                if (response != null) {
                    if (promoterFinancials.getRatingFileReference() != null && promoterFinancials.getRatingFileReference().length() > 0) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                promoterFinancials.getRatingFileReference(),
                                promoterFinancials.getId(),
                                "1",
                                "Promoter Financials Rating File",
                                "PromoterFinancials_Rating_" + promoterFinancials.getFiscalYear(),
                                "ZPFSLM57"
                        );
                    }
                }
                if (response != null) {
                    if (promoterFinancials.getAnnualReturnFileReference() != null && promoterFinancials.getAnnualReturnFileReference().length() > 0) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                promoterFinancials.getAnnualReturnFileReference(),
                                promoterFinancials.getId(),
                                "2",
                                "Promoter Financials Annual Report File",
                                "PromoterFinancials_Annual_Report_" + promoterFinancials.getFiscalYear(),
                                "ZPFSLM58");
                    }
                }


                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;

            case "Financial Covenants":
                FinancialCovenants financialCovenants = new FinancialCovenants();
                log.info("Attempting to Post Financial Covenants  to SAP AT :" + dateFormat.format(new Date()));
                Optional<FinancialCovenants> fc = financialCovenantsRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                financialCovenants = fc.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPFinancialCovenantsResourceDetails sapFinancialCovenantsResourceDetails = sapFinancialCovenantsResource.mapToSAP(financialCovenants);
                SAPFinancialCovenantsResource sapFinancialCovenantsResource = new SAPFinancialCovenantsResource();
                sapFinancialCovenantsResource.setSapFinancialCovenantsResourceDetails(sapFinancialCovenantsResourceDetails);

                resource = (Object) sapFinancialCovenantsResource;
                serviceUri = monitorServiceUri + "FinancialCovenantsSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "Promoter Details":
//                PromoterDetail promoterDetails = new PromoterDetail();
//                log.info("Attempting to Post Promoter Details  to SAP AT :" + dateFormat.format(new Date()));
//                Optional<PromoterDetail> pd = promoterDetailsRepository.findById(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
//
//                promoterDetails = pd.get();
//
//                //Set Status as in progress
//                sapIntegrationPointer.setStatus(1); // In Posting Process
//                sapIntegrationRepository.save(sapIntegrationPointer);
//
//                SAPPromoterDetailsResourceDetails sapPromoterDetailsResourceDetails = sapPromoterDetailsResource.mapToSAP(promoterDetails);
//                SAPPromoterDetailsResource sapPromoterDetailsResource = new SAPPromoterDetailsResource();
//                sapPromoterDetailsResource.setSapPromoterDetailsResourceDetails(sapPromoterDetailsResourceDetails);
//
//                resource = (Object) sapPromoterDetailsResource;
//                serviceUri = monitorServiceUri + " PromoterDetailsSet";
//                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
//
//                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "ProjectMonitoring":
                ProjectMonitoringData projectMonitoringData = new ProjectMonitoringData();
                log.info("Attempting to Post ProjectMonitoring  to SAP AT :" + dateFormat.format(new Date()));
                Optional<ProjectMonitoringData> pmd =
                        projectMonitoringDataRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());
                projectMonitoringData = pmd.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPProjectMonitoringResourceDataDetails sapProjectMonitoringResourceDataDetails
                        = sapProjectMonitoringDataResource.mapToSAP(projectMonitoringData);

                SAPProjectMonitoringDataResource sapProjectMonitoringDataResource = new SAPProjectMonitoringDataResource();
                sapProjectMonitoringDataResource.setSapProjectMonitoringResourceDataDetails(sapProjectMonitoringResourceDataDetails);

                resource = (Object) sapProjectMonitoringDataResource;
                serviceUri = monitorServiceUri + "ProjectMonitoringDataSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "Project Monitoring Item":
                ProjectMonitoringDataItem projectMonitoringDataItem = new ProjectMonitoringDataItem();
                log.info("Attempting to Post ProjectMonitoringDataItem  to SAP AT :" + dateFormat.format(new Date()));
                Optional<ProjectMonitoringDataItem> pmdi =
                        projectMonitoringDataItemRepository.findById(sapIntegrationPointer.getBusinessObjectId());
                projectMonitoringDataItem = pmdi.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPProjectMonitoringResourceDataItemDetails sapProjectMonitoringResourceDataItemDetails
                        = sapProjectMonitoringDataItemResource.mapToSAP(projectMonitoringDataItem, sapIntegrationPointer.getMainEntityId());

                SAPProjectMonitoringDataItemResource sapProjectMonitoringDataItemResource = new SAPProjectMonitoringDataItemResource();
                sapProjectMonitoringDataItemResource.setSapProjectMonitoringResourceDataItemDetails(sapProjectMonitoringResourceDataItemDetails);

                resource = (Object) sapProjectMonitoringDataItemResource;
                serviceUri = monitorServiceUri + "ProjectMonitoringDataItemSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "Project Monitoring History":
                ProjectMonitoringDataItemHistory projectMonitoringDataItemHistory = new ProjectMonitoringDataItemHistory();
                log.info("Attempting to Post ProjectMonitoringDataItemHistory  to SAP AT :" + dateFormat.format(new Date()));
                Optional<ProjectMonitoringDataItemHistory> pmdih =
                        projectMonitoringDataItemHistoryRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());
                projectMonitoringDataItemHistory = pmdih.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPProjectMonitoringHistoryResourceDetails sapProjectMonitoringHistoryResourceDetails
                        = sapProjectMonitoringHistoryResource.mapToSAP(projectMonitoringDataItemHistory, sapIntegrationPointer.getMainEntityId());

                SAPProjectMonitoringHistoryResource sapProjectMonitoringHistoryResource = new SAPProjectMonitoringHistoryResource();
                sapProjectMonitoringHistoryResource.setSapProjectMonitoringHistoryResourceDetails(sapProjectMonitoringHistoryResourceDetails);

                resource = (Object) sapProjectMonitoringHistoryResourceDetails;
                serviceUri = monitorServiceUri + "ProjectMonitoringHistorySet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "TRA Account":
                TrustRetentionAccount trustRetentionAccount = new TrustRetentionAccount();

                log.info("Attempting to Post TrustRetentionAccount to SAP AT :" + dateFormat.format(new Date()));
                Optional<TrustRetentionAccount> tra = traRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                trustRetentionAccount = tra.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPTRAResourceDetails saptraResourceDetails = saptraResource.mapToSAP(trustRetentionAccount);
                SAPTRAResource saptraResource = new SAPTRAResource();
                saptraResource.setSaptraResourceDetails(saptraResourceDetails);

                resource = (Object) saptraResourceDetails;
                serviceUri = monitorServiceUri + "TrustRetentionAccountSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

//                     if (response != null) {
//                         response = postDocument(trustRetentionAccount.getF(), operatingParameter.getId(), "Operating Parameter", operatingParameter.getDocumentTitle());
//                     }

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
            case "TRA Account Statement":
                TrustRetentionAccountStatement trustRetentionAccountStatement = new TrustRetentionAccountStatement();

                log.info("Attempting to Post TrustRetentionAccountStatement to SAP AT :" + dateFormat.format(new Date()));
                Optional<TrustRetentionAccountStatement> tras = traStatementRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                trustRetentionAccountStatement = tras.get();

                //Set Status as in progress
                sapIntegrationPointer.setStatus(1); // In Posting Process
                sapIntegrationRepository.save(sapIntegrationPointer);

                SAPTRAStatementResourceDetails saptraStatementResourceDetails = saptraStatementResource.mapToSAP(trustRetentionAccountStatement);
                SAPTRAStatementResource saptraStatementResource = new SAPTRAStatementResource();
                saptraStatementResource.setSaptraStatementResourceDetails(saptraStatementResourceDetails);

                resource = (Object) saptraStatementResourceDetails;
                serviceUri = monitorServiceUri + "TrustRetentionAccountStatementSet";
                response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                if (response != null) {
                    if (trustRetentionAccountStatement.getFileReference() != null && trustRetentionAccountStatement.getFileReference().length() > 0) {
                        response = postDocument(
                                sapIntegrationPointer.getSubBusinessProcessName(),
                                trustRetentionAccountStatement.getFileReference(),
                                trustRetentionAccountStatement.getId(), "" +
                                        "", "Trust Retention Account Statement",
                                trustRetentionAccountStatement.getDocumentTitle(),
                                trustRetentionAccountStatement.getDocumentType()
                        );
                    }
                }

                updateSAPIntegrationPointer(response, sapIntegrationPointer);
                break;
                case "NPA Detail":
                    NPADetail npaDetail = new NPADetail();

                    log.info("Attempting to Post NPA Detail to SAP AT :" + dateFormat.format(new Date()));
                    Optional<NPADetail> npaDetailOptional = npaDetailRepository.findById(UUID.fromString( sapIntegrationPointer.getBusinessObjectId())) ;

                    npaDetail = npaDetailOptional.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPNPADetailResourceDetails sapnpaDetailResourceDetails = sapnpaDetailResource.mapToSAP(npaDetail);
                    SAPNPADetailResource sapnpaDetailResource1 = new SAPNPADetailResource();
                    sapnpaDetailResource1.setSAPNPADetailResourceDetails(sapnpaDetailResourceDetails);


                    resource = (Object) sapnpaDetailResourceDetails;
                    serviceUri = monitorServiceUri + "NPADetailSet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "NPA":
                    NPA npa = new NPA();

                    log.info("Attempting to Post NPA to SAP AT :" + dateFormat.format(new Date()));
                    Optional<NPA> npaOptional = npaRepository.findById(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));

                    npa = npaOptional.get();

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPNPAResourceDetails sapnpaResourceDetails = sapnpaResource.mapToSAP(npa);
                    SAPNPAResource sapnpaResource1 = new SAPNPAResource();
                    sapnpaResource1.setSAPNPAResourceDetails(sapnpaResourceDetails);


                    resource = (Object) sapnpaResourceDetails;
                    serviceUri = monitorServiceUri + "NPASet";
                    response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
        }
    }

}

    private Object postDocument(String businessProcessName,
                                String fileReference,
                                String entityId, String docSubId,
                                String entityName,
                                String fileName,
                                String documentType) throws IOException {
        String documentServiceUri = monitorDocumentUri;
        switch (businessProcessName) {
            case "Monitoring":
                documentServiceUri = monitorDocumentUri;
                break;
            case "Appraisal":
                documentServiceUri = appraisalDocumentUri;
                break;
        }

        if (fileReference.length() == 0) {
            log.error("File Reference is Empty; Posting to SAP Aborted for Process Name :" + entityName + " entityId : " + entityId);
            return null;
        }
        UUID fileUUID = UUID.fromString(fileReference);
        byte[] file = fileStorage.download(fileUUID);
        FileResource fileResource = fileStorage.getFile(fileUUID);
        Optional<FilePointer> filePointer = fileStorage.findFile(fileUUID);
        FilePointer fp = filePointer.get();

        com.google.common.net.MediaType mediaType = fp.getMediaType().get();
        //MediaType mediaType = (MediaType) mediaTypeOptional.get();

        String mimeType = mediaType.toString();
        String filePath = fileStorage.getFilePath(fileUUID);


        SAPDocumentAttachmentResourceDetails sapDocumentAttachmentResourceDetails = new SAPDocumentAttachmentResourceDetails();
        if (mimeType == "")
            mimeType = "application/pdf";

        sapDocumentAttachmentResourceDetails = sapDocumentAttachmentResource.mapToSAP(
                fileUUID.toString(),
                entityId,
                entityName,
                file.toString(),
                mimeType,
                fileName,
                fileReference);

        sapDocumentAttachmentResource.setSapDocumentAttachmentResourceDetails(sapDocumentAttachmentResourceDetails);
        Object d1 = (Object) sapDocumentAttachmentResource;

        String fileType = new String();
        String[] mimeTypeParts = mimeType.split("\\/");
        mimeType = mimeTypeParts[1];
        fileType = mimeTypeParts[0];

//        String documentUploadUri = monitorDocumentUri + "("
//                + "Id='" + fileUUID.toString() + "',"
//                + "EntityId='" +entityId +  "',"
//                + "EntityName='" +entityName +  "',"
//                + "MimeType='" +mimeType +  "',"
//                + "Filename='" +fileName +  "',"
//                + "FileType='" +fileType +  "',"
//                + ")/$value";

        String documentUploadUri = documentServiceUri + "("
                + "Id='" + entityId + "',"
                + "DocSubId='" + docSubId + "',"
                + "EntityId='" + entityId + "',"
                + "EntityName='" + entityName + "',"
                + "MimeType='" + mimeType + "',"
                + "Filename='" + fileName + "',"
                + "FileType='" + fileType + "',"
                + "DocumentType='" + documentType + "',"
                + "FileReference='" + fileReference + "',"
                + ")/$value";


        Object response = fileUploadIntegrationService.fileUploadToSAP(documentUploadUri, filePath);


        return response;
    }


    private MediaType getMediaType(String mimeType) {

        MediaType mediaType = MediaType.MULTIPART_FORM_DATA;
        switch (mimeType) {
            case "application/pdf":
                mediaType = MediaType.APPLICATION_PDF;
                break;
            case "image/jpg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "text/plain":
                mediaType = MediaType.TEXT_PLAIN;
                break;
            case "image/jpeg":
                mediaType = MediaType.IMAGE_JPEG;
        }

        return mediaType;
    }


    private void updateSAPIntegrationPointer(Object response, SAPIntegrationPointer sapIntegrationPointer) {

        System.out.println("Updating SAP Integration Pointer");

        sapIntegrationPointer.setProcessDate(new Date());
        if (response == null) {
            //Set Status as Failed
            sapIntegrationPointer.setStatus(2); // Posting Failed
            sapIntegrationRepository.save(sapIntegrationPointer);
        } else {
            //Set Status as Posted Successfully
            sapIntegrationPointer.setStatus(3); // Posting Successful
            sapIntegrationRepository.save(sapIntegrationPointer);
        }

    }

}








