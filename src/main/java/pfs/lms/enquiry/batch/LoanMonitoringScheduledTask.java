package pfs.lms.enquiry.batch;

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
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.lfa.LFARepository;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.lie.LIERepository;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameter;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLF;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLFRepository;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterRepository;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancialsRepository;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.monitoring.service.ISAPFileUploadIntegrationService;
import pfs.lms.enquiry.monitoring.service.ISAPLoanMonitoringIntegrationService;
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

    private  final ISAPIntegrationService isapIntegrationService;
    private final FileStorage fileStorage;
    private final UserRepository userRepository;
    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPLoanMonitoringIntegrationService sapLoanMonitoringIntegrationService;
    private final ISAPFileUploadIntegrationService fileUploadIntegrationService;
    private final TRARepository traRepository;
    private final TRAStatementRepository traStatementRepository;
    private final LIERepository lieRepository;
    private final LIEReportAndFeeRepository lieReportAndFeeRepository;
    private final LFARepository lfaRepository;
    private final LFAReportAndFeeRepository lfaReportAndFeeRepository;
    private final TermsAndConditionsRepository termsAndConditionsRepository;
    private final BorrowerFinancialsRepository borrowerFinancialsRepository;
    private final PromoterFinancialsRepository promoterFinancialsRepository;
    private final OperatingParameterRepository operatingParameterRepository;
    private final OperatingParameterPLFRepository operatingParameterPLFRepository;

    private final SecurityComplianceRepository securityComplianceRepository;
    private final SiteVisitRepository   siteVisitRepository;
    private final RateOfInterestRepository rateOfInterestRepository;
    private final FinancialCovenantsRepository financialCovenantsRepository;
    private final PromoterDetailsRepository promoterDetailsRepository;
    private final LoanMonitorRepository loanMonitorRepository;

    private final SAPLIEResource saplieResource;
    private final SAPLFAResource saplfaResource;
    private final SAPLFAReportAndFeeResource saplfaReportAndFeeResource;
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
    private final SAPPromoterDetailsResource  sapPromoterDetailsResource;
    private final SAPPromoterDetailsItemResource sapPromoterDetailsItemResource;
    private final SAPTRAResource saptraResource;
    private final SAPTRAStatementResource  saptraStatementResource;


     @Scheduled(fixedRate = 5000)
    public void syncLoanApplicationsToBackend() throws ParseException, IOException {


         LendersIndependentEngineer lendersIndependentEngineer = new LendersIndependentEngineer();

         User lastChangedByUser = new User();
         lastChangedByUser = userRepository.findByEmail(lendersIndependentEngineer.getChangedByUserName());

         Object response = new Object();
         Object resource;

         //Collect SAPIntegrationPointer with the following  Posting Status = 0
         List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatus("Monitoring", 0));
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatus("Monitoring", 2));

         // SAPDocumentAttachmentResource sapDocumentAttachmentResource = new SAPDocumentAttachmentResource();

         String serviceUri = new String();


         for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

             switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                 case "Header":
                     log.info("Attempting to Post MonitorHeader to SAP AT :" + dateFormat.format(new Date()));
                     LoanMonitor loanMonitor = loanMonitorRepository.getOne( UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));

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
                         response = postDocument(lieReportAndFee.getFileReference(), lieReportAndFee.getId(), "LIE Report & Fee", lieReportAndFee.getDocumentTitle());
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
                     break;

                 case "LFA Report and Fee":

                     LFAReportAndFee lfaReportAndFee = new LFAReportAndFee();
                     log.info("Attempting to Post LFS Report and Fee to SAP AT :" + dateFormat.format(new Date()));
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
                         response = postDocument(lfaReportAndFee.getFileReference(), lfaReportAndFee.getId(), "LFE Report & Fee", lfaReportAndFee.getDocumentTitle());
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
                     break;
                 case "Terms and Conditions" :

                     TermsAndConditionsModification termsAndConditionsModification = new TermsAndConditionsModification();
                     log.info("Attempting to Post Terms and Conditions to SAP AT :" + dateFormat.format(new Date()));
                     Optional<TermsAndConditionsModification> traMod = termsAndConditionsRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());


                     termsAndConditionsModification = traMod.get();

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPTermsAndConditionsModificationDetails sapTermsAndConditionsModificationDetails =  sapTermsAndConditionsModificationResource.mapToSAP(termsAndConditionsModification);
                     SAPTermsAndConditionsModificationResource sapTermsAndConditionsModificationResource = new SAPTermsAndConditionsModificationResource();
                     sapTermsAndConditionsModificationResource.setSAPTermsAndConditionsModificationDetails(sapTermsAndConditionsModificationDetails);

                     resource = (Object) sapTermsAndConditionsModificationResource;
                     serviceUri = monitorServiceUri + "TermsAndConditionsModificationSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     if (response != null) {
                         response = postDocument(termsAndConditionsModification.getFileReference(), termsAndConditionsModification.getId(), "TermsAndConditionsModification", termsAndConditionsModification.getDocumentTitle());
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
                     break;
                 case "Security Compliance" :

                     SecurityCompliance securityCompliance = new SecurityCompliance();
                     log.info("Attempting to Post Security Compliance to SAP AT :" + dateFormat.format(new Date()));
                     Optional<SecurityCompliance> secCompl = securityComplianceRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                     securityCompliance = secCompl.get();

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPSecurityComplianceResourceDetails sapSecurityComplianceResourceDetails =  sapSecurityComplianceResource.mapToSAP(securityCompliance);
                     SAPSecurityComplianceResource sapSecurityComplianceResource = new SAPSecurityComplianceResource();
                     sapSecurityComplianceResource.setSapSecurityComplianceResourceDetails(sapSecurityComplianceResourceDetails);

                     resource = (Object) sapSecurityComplianceResource;
                     serviceUri = monitorServiceUri + "SecurityComplianceSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

//                     if (response != null) {
//                         response = postDocument(securityCompliance.getFileReference(), securityCompliance.getId(), "Security Compliance", termsAndConditionsModification.getDocumentTitle());
//                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
//
//                     if (response != null) {
//                         response = postDocument(siteVisit.getF(), securityCompliance.getId(), "Security Compliance", termsAndConditionsModification.getDocumentTitle());
//                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
                         response = postDocument(operatingParameter.getFileReference(), operatingParameter.getId(), "Operating Parameter", operatingParameter.getDocumentTitle());
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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

//                     if (response != null) {
//                         response = postDocument(operatingParameter.getFileReference(), operatingParameter.getId(), "Operating Parameter", operatingParameter.getDocumentTitle());
//                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
                     serviceUri = monitorServiceUri + "RateOfInterestSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

//                     if (response != null) {
//                         response = postDocument(operatingParameter.getFileReference(), operatingParameter.getId(), "Operating Parameter", operatingParameter.getDocumentTitle());
//                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
                     serviceUri = monitorServiceUri + "BorrowerFinancialstSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     if (response != null) {
                         response = postDocument(borrowerFinancials.getRatingFileReference(), borrowerFinancials.getId(), "Borrower Financials Rating File", borrowerFinancials.getId() + borrowerFinancials.getFiscalYear());
                        if (response != null) {
                            response = postDocument(borrowerFinancials.getAnnualReturnFileReference(), borrowerFinancials.getId(), "Borrower Financials Annual Report File", borrowerFinancials.getId() + borrowerFinancials.getFiscalYear());
                        }
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
                     serviceUri = monitorServiceUri + "PromoterFinancialstSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     if (response != null) {
                         response = postDocument(promoterFinancials.getRatingFileReference(), promoterFinancials.getId(), "Promoter Financials Rating File", promoterFinancials.getId() + promoterFinancials.getFiscalYear());
                         if (response != null) {
                             response = postDocument(promoterFinancials.getAnnualReturnFileReference(), promoterFinancials.getId(), "Promoter Financials Annual Report File", promoterFinancials.getId() + promoterFinancials.getFiscalYear());
                         }
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
                     serviceUri = monitorServiceUri + " FinancialCovenantsSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
                     break;
                 case "Promoter Details":
                     PromoterDetails promoterDetails = new PromoterDetails();
                     log.info("Attempting to Post Promoter Details  to SAP AT :" + dateFormat.format(new Date()));
                     Optional<PromoterDetails> pd = promoterDetailsRepository.findById(sapIntegrationPointer.getBusinessObjectId().toString());

                     promoterDetails = pd.get();

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPPromoterDetailsResourceDetails sapPromoterDetailsResourceDetails = sapPromoterDetailsResource.mapToSAP(promoterDetails);
                     SAPPromoterDetailsResource sapPromoterDetailsResource = new SAPPromoterDetailsResource();
                     sapPromoterDetailsResource.setSapPromoterDetailsResourceDetails(sapPromoterDetailsResourceDetails);

                     resource = (Object) sapPromoterDetailsResource;
                     serviceUri = monitorServiceUri + " PromoterDetailsSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
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
                     SAPTRAStatementResource  saptraStatementResource = new SAPTRAStatementResource();
                     saptraStatementResource.setSaptraStatementResourceDetails(saptraStatementResourceDetails);

                     resource = (Object) saptraStatementResourceDetails;
                     serviceUri = monitorServiceUri + "TrustRetentionAccountStatementSet";
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     if (response != null) {
                         response = postDocument(trustRetentionAccountStatement.getFileReference(), trustRetentionAccountStatement.getId(), "Trust Retention Account Statement", "" ); //trustRetentionAccountStatement.getDocumentTitle());
                     }

                     updateSAPIntegrationPointer(response,sapIntegrationPointer);
                     break;
             }
         }
     }

    private Object postDocument(String fileReference,
                                String entityId,
                                String entityName,
                                String fileName) throws IOException {

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
                entityName, file.toString(),
                mimeType,
                fileName );

        sapDocumentAttachmentResource.setSapDocumentAttachmentResourceDetails(sapDocumentAttachmentResourceDetails);
        Object d1 = (Object) sapDocumentAttachmentResource;

        String fileType = new String();
        String [] mimeTypeParts = mimeType.split("\\/") ;
        mimeType = mimeTypeParts[1];
        fileType = mimeTypeParts[0];

        String documentUploadUri = monitorDocumentUri + "("
                + "Id='" + fileUUID.toString() + "',"
                + "EntityId='" +entityId +  "',"
                + "EntityName='" +entityName +  "',"
                + "MimeType='" +mimeType +  "',"
                + "Filename='" +fileName +  "',"
                + "FileType='" +fileType +  "',"
                + ")/$value";


        Object response =  fileUploadIntegrationService.fileUploadToSAP(documentUploadUri, filePath);



        return  response;
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








