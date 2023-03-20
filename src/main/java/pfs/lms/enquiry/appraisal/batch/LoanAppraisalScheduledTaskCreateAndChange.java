package pfs.lms.enquiry.appraisal.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejectionRepository;
import pfs.lms.enquiry.appraisal.furtherdetail.FurtherDetail;
import pfs.lms.enquiry.appraisal.furtherdetail.FurtherDetailRepository;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomerRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletionRepository;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.appraisal.projectdata.ProjectDataRepository;
import pfs.lms.enquiry.appraisal.proposaldetails.ProposalDetail;
import pfs.lms.enquiry.appraisal.proposaldetails.ProposalDetailRepository;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelay;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelayRepository;
import pfs.lms.enquiry.appraisal.resource.*;
import pfs.lms.enquiry.appraisal.riskrating.CorporateLoanRiskRating;
import pfs.lms.enquiry.appraisal.riskrating.CorporateLoanRiskRatingRepository;
import pfs.lms.enquiry.appraisal.riskrating.TermLoanRiskRating;
import pfs.lms.enquiry.appraisal.riskrating.TermLoanRiskRatingRepository;
import pfs.lms.enquiry.appraisal.syndicateconsortium.SyndicateConsortium;
import pfs.lms.enquiry.appraisal.syndicateconsortium.SyndicateConsortiumRepository;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.monitoring.domain.SiteVisit;
import pfs.lms.enquiry.monitoring.repository.SiteVisitRepository;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.repository.SAPIntegrationRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.FileResource;
import pfs.lms.enquiry.sapintegrationservice.ISAPFileUploadIntegrationService;
import pfs.lms.enquiry.sapintegrationservice.ISAPLoanProcessesIntegrationService;
import pfs.lms.enquiry.service.ISAPIntegrationService;
import pfs.lms.enquiry.vault.FilePointer;
import pfs.lms.enquiry.vault.FileStorage;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sajeev on 08-May-2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanAppraisalScheduledTaskCreateAndChange {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${sap.lieUri}")
    private String lieUri;

    @Value("${sap.lieReportAndFeeUri}")
    private String lieReportAndFeeUri;

    @Value("${sap.appraisalDocumentUri}")
    private String appraisalDocumentUri;

    @Value("${sap.appraisalServiceUri}")
    private String appraisalServiceUri;

    private  final ISAPIntegrationService isapIntegrationService;


    private final FileStorage fileStorage;
    private final UserRepository userRepository;
    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPLoanProcessesIntegrationService sapLoanProcessesIntegrationService;
    private final ISAPFileUploadIntegrationService fileUploadIntegrationService;

    private final LoanAppraisalRepository loanAppraisalRepository;
    private final CustomerRejectionRepository customerRejectionRepository;
    private final FurtherDetailRepository furtherDetailRepository;
    private final ProjectAppraisalCompletionRepository projectAppraisalCompletionRepository;
    private final ProjectDataRepository projectDataRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final SyndicateConsortiumRepository syndicateConsortiumRepository;
    private final ReasonForDelayRepository reasonForDelayRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;
    private final ProposalDetailRepository proposalDetailRepository;
    private final SiteVisitRepository siteVisitRepository;
    private final CorporateLoanRiskRatingRepository corporateLoanRiskRatingRepository;
    private final TermLoanRiskRatingRepository termLoanRiskRatingRepository;

    private final SAPDocumentAttachmentResource sapDocumentAttachmentResource;

    private final SAPLoanAppraisalHeaderResource sapLoanAppraisalHeaderResource;
    private final SAPLoanAppraisalCustomerRejectionResource customerRejectionResource;
    private final SAPLoanAppraisalFurtherDetailResource sapLoanAppraisalFurtherDetailResource;
    private final SAPLoanAppraisalProjectAppraisalCompletionResource sapLoanAppraisalProjectAppraisalCompletionResource;
    private final SAPLoanAppraisalProjectDataResource sapLoanAppraisalProjectDataResource;
    private final SAPLoanAppraisalLoanPartnerResource sapLoanAppraisalLoanPartnerResource;
    private final SAPLoanAppraisalSyndicateConsortiumResource sapLoanAppraisalSyndicateConsortiumResource;
    private final SAPLoanAppraisalProposalDetailsResource sapLoanAppraisalProposalDetailsResource;
    private final SAPLoanAppraisalReasonForDelayResource sapLoanAppraisalReasonForDelayResource;
    private final SAPLoanAppraisalKYCResource sapLoanAppraisalKYCResource;
    private final SAPSiteVisitResource sapSiteVisitResource;
    private final SAPLoanAppraisalExternalRatingTermLoanResource sapLoanAppraisalExternalRatingTermLoanResource;
    private final SAPLoanAppraisalExternalRatingCorpLoanResource sapLoanAppraisalExternalRatingCorpLoanResource;

    @Scheduled(fixedRateString = "${batch.loanAppraisalScheduledTaskCreateAndChange}",initialDelayString = "${batch.initialDelay}")
    public void syncLoanAppraisalsToBackend() throws ParseException, IOException {

        LoanAppraisal loanAppraisal = new LoanAppraisal();
        CustomerRejection customerRejection = new CustomerRejection();
        FurtherDetail furtherDetail = new FurtherDetail();
        SiteVisit siteVisit = new SiteVisit();
        ProjectAppraisalCompletion projectAppraisalCompletion = new ProjectAppraisalCompletion();
        ProjectData projectData = new ProjectData();
        LoanPartner loanPartner = new LoanPartner();
        SyndicateConsortium syndicateConsortium;
        ReasonForDelay reasonForDelay;
        KnowYourCustomer knowYourCustomer;
        ProposalDetail proposalDetail;
        CorporateLoanRiskRating corporateLoanRiskRating;
        TermLoanRiskRating termLoanRiskRating;

         Object response = new Object();
         Object resource;

         //Collect SAPIntegrationPointer with the following  Posting Status = 0
         List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();

         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 0,'C'));
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 2,'C'));
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 0,'U'));
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 2,'U'));

         log.info("---------------Sync. Loan Appraisal to SAP ");

         Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
             public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                 return o1.getCreationDate().compareTo(o2.getCreationDate());
             }
         });

         String serviceUri = new String();

         for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

             switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                 case "Header":
                     loanAppraisal = loanAppraisalRepository.getOne( UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                     if (loanAppraisal.getLoanApplication().getLoanContractId() == null){
                         return;
                     }
                     log.info("Attempting to Post Appraisal Header to SAP AT :" + dateFormat.format(new Date()));
                     log.info("Loan Contract :" + loanAppraisal.getLoanApplication().getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalHeaderResourceDetails sapLoanAppraisalHeaderResourceDetails =
                             sapLoanAppraisalHeaderResource.mapLoanAppraisalHeaderToSAP(loanAppraisal);

                     SAPLoanAppraisalHeaderResource sapLoanAppraisalHeaderResource = new SAPLoanAppraisalHeaderResource();
                     sapLoanAppraisalHeaderResource.setsapMonitorHeaderResourceDetails(sapLoanAppraisalHeaderResourceDetails);

                     resource = (Object) sapLoanAppraisalHeaderResource;
                     serviceUri = appraisalServiceUri + "AppraisalHeaderSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "External Rating Corporate Loan":

                     corporateLoanRiskRating = corporateLoanRiskRatingRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( corporateLoanRiskRating.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal External Rating Corporate Loan to SAP AT :" + dateFormat.format(new Date())+ loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);


                     SAPLoanAppraisalExternalRatingCorpLoanResourceDetails sapLoanAppraisalExternalRatingCorpLoanResourceDetails =
                             sapLoanAppraisalExternalRatingCorpLoanResource.mapToSAP(corporateLoanRiskRating);


                     SAPLoanAppraisalExternalRatingCorpLoanResource sapLoanAppraisalExternalRatingCorpLoanResource = new SAPLoanAppraisalExternalRatingCorpLoanResource();
                     sapLoanAppraisalExternalRatingCorpLoanResource.setSapLoanAppraisalCustomerRejectionResourceDetails(sapLoanAppraisalExternalRatingCorpLoanResourceDetails);

                     resource = (Object)  sapLoanAppraisalExternalRatingCorpLoanResource;
                     serviceUri = appraisalServiceUri + "ExternalRatingCorporateLoanSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "External Rating Term Loan":

                     termLoanRiskRating = termLoanRiskRatingRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( termLoanRiskRating.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal External Rating Term Loan to SAP AT :" + dateFormat.format(new Date())+ loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);


                     SAPLoanAppraisalExternalRatingTermLoanResourceDetails sapLoanAppraisalExternalRatingTermLoanResourceDetails =
                             sapLoanAppraisalExternalRatingTermLoanResource.mapToSAP(termLoanRiskRating);


                     SAPLoanAppraisalExternalRatingTermLoanResource sapLoanAppraisalExternalRatingTermLoanResource = new SAPLoanAppraisalExternalRatingTermLoanResource();
                     sapLoanAppraisalExternalRatingTermLoanResource.setSapLoanAppraisalCustomerRejectionResourceDetails(sapLoanAppraisalExternalRatingTermLoanResourceDetails);

                     resource = (Object)  sapLoanAppraisalExternalRatingTermLoanResource;
                     serviceUri = appraisalServiceUri + "ExternalRatingTermLoanSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;

                 case "Customer Rejection":
                     customerRejection = customerRejectionRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( customerRejection.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Customer Rejection to SAP AT :" + dateFormat.format(new Date())+ loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalCustomerRejectionResourceDetails sapLoanAppraisalCustomerRejectionResourceDetails =
                             customerRejectionResource.mapCustomerRejectionToSAP(customerRejection);

                     SAPLoanAppraisalCustomerRejectionResource sapLoanAppraisalCustomerRejectionResource = new SAPLoanAppraisalCustomerRejectionResource();
                     sapLoanAppraisalCustomerRejectionResource.setSapLoanAppraisalCustomerRejectionResourceDetails(sapLoanAppraisalCustomerRejectionResourceDetails);

                     resource = (Object)  sapLoanAppraisalCustomerRejectionResource;
                     serviceUri = appraisalServiceUri + "CustomerRejectionSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Further Detail":

                     furtherDetail = furtherDetailRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( furtherDetail.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Further Detail to SAP AT :" + dateFormat.format(new Date())+ loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalFurtherDetailResourceDetails sapLoanAppraisalFurtherDetailResourceDetails =
                             sapLoanAppraisalFurtherDetailResource.mapFurtherDetailToSAP(furtherDetail);

                     SAPLoanAppraisalFurtherDetailResource sapLoanAppraisalFurtherDetailResource = new SAPLoanAppraisalFurtherDetailResource();
                     sapLoanAppraisalFurtherDetailResource.setSapLoanAppraisalFurtherDetailResourceDetails(sapLoanAppraisalFurtherDetailResourceDetails);

                     resource = (Object)  sapLoanAppraisalFurtherDetailResource;
                     serviceUri = appraisalServiceUri + "FurtherDetailSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Site Visit":
                     siteVisit = siteVisitRepository.getOne(sapIntegrationPointer.getBusinessObjectId());
                     loanAppraisal = loanAppraisalRepository.getOne(UUID.fromString(siteVisit.getLoanAppraisalId()));

                     log.info("Attempting to Post Appraisal Site Visit to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Pos
                     // ting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPSiteVisitResourceDetails sapSiteVisitResourceDetails = sapSiteVisitResource.mapToSAP(siteVisit);

                     SAPSiteVisitResource sapSiteVisitResource = new SAPSiteVisitResource();
                     sapSiteVisitResource.setSapSiteVisitResourceDetails(sapSiteVisitResourceDetails);

                     resource = (Object)  sapSiteVisitResource;
                     serviceUri = appraisalServiceUri + "SiteVisitSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
                     if (response != null) {
                         response = postDocument(
                                 siteVisit.getFileReference(),
                                 siteVisit.getId(),
                                 "",
                                 "Site Visit",
                                 siteVisit.getDocumentTitle(),
                                 siteVisit.getDocumentType()
                         );
                     }
                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Project Appraisal Completion":

                     projectAppraisalCompletion = projectAppraisalCompletionRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( projectAppraisalCompletion.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Project Appraisal Completion to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalProjectAppraisalCompletionResourceDetails sapLoanAppraisalProjectAppraisalCompletionResourceDetails =
                             sapLoanAppraisalProjectAppraisalCompletionResource.mapProjectAppraisalCompletionToSAP(projectAppraisalCompletion);

                     SAPLoanAppraisalProjectAppraisalCompletionResource sapLoanAppraisalProjectApprisalCompletionResource = new SAPLoanAppraisalProjectAppraisalCompletionResource();
                     sapLoanAppraisalProjectApprisalCompletionResource.setSAPLoanAppraisalProjectAppraisalCompletionResourceDetails(sapLoanAppraisalProjectAppraisalCompletionResourceDetails );

                     resource = (Object)  sapLoanAppraisalProjectApprisalCompletionResource;
                     serviceUri = appraisalServiceUri + "ProjectAppraisalCompletionSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;

                 case "Project Data":

                     projectData = projectDataRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( projectData.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Project Data Completion to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalProjectDataResourceDetails sapLoanAppraisalProjectAppraisalCompletionResourceDetails1 =
                             sapLoanAppraisalProjectDataResource.mapToSAP(projectData);

                     SAPLoanAppraisalProjectDataResource sapLoanAppraisalProjectDataResource = new SAPLoanAppraisalProjectDataResource();
                     sapLoanAppraisalProjectDataResource.setSapLoanAppraisalProjectDataResourceDetails(sapLoanAppraisalProjectAppraisalCompletionResourceDetails1  );

                     resource = (Object)  sapLoanAppraisalProjectAppraisalCompletionResourceDetails1;
                     serviceUri = appraisalServiceUri + "ProjectDataSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Proposal Detail":

                     proposalDetail = proposalDetailRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( proposalDetail.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Proposal Details to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalProposalDetailsResourceDetails sapLoanAppraisalProposalDetailsResourceDetails =
                             sapLoanAppraisalProposalDetailsResource.mapProposalToSAP(proposalDetail);

                     SAPLoanAppraisalProposalDetailsResource sapLoanAppraisalProposalDetailsResource = new SAPLoanAppraisalProposalDetailsResource();
                     sapLoanAppraisalProposalDetailsResource.setsapLoanAppraisalProposalDetailsResourceDetails(sapLoanAppraisalProposalDetailsResourceDetails  );

                     resource = (Object)  sapLoanAppraisalProposalDetailsResourceDetails ;
                     serviceUri = appraisalServiceUri + "ProposalDetailsSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Loan Partner":
                     loanPartner = loanPartnerRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     try {
                         loanAppraisal = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));
                     } catch (Exception ex) {
                         log.error("Error Replicating Loan Partner to SAP : " + loanPartner.getBusinessPartnerId() + " Contract Id :" + loanAppraisal.getLoanContractId());
                     }
                     log.info("Attempting to Post Appraisal Loan Partner to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalLoanPartnerResourceDetails sapLoanAppraisalLoanPartnerResourceDetails =
                             sapLoanAppraisalLoanPartnerResource.mapLoanPartnerToSAP(loanPartner);

                     SAPLoanAppraisalLoanPartnerResource  sapLoanAppraisalLoanPartnerResource = new SAPLoanAppraisalLoanPartnerResource();
                     sapLoanAppraisalLoanPartnerResource.setSapLoanAppraisalLoanPartnerResourceDetails(sapLoanAppraisalLoanPartnerResourceDetails  );

                     resource = (Object)  sapLoanAppraisalLoanPartnerResourceDetails;
                     serviceUri = appraisalServiceUri + "LoanPartnerSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Syndicate Consortium":

                     syndicateConsortium = syndicateConsortiumRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( syndicateConsortium.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Syndicate Consortium to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalSyndicateConsortiumResourceDetails sapLoanAppraisalSyndicateConsortiumResourceDetails =
                             sapLoanAppraisalSyndicateConsortiumResource.mapSyndicateConsortiumToSAP(syndicateConsortium);

                     SAPLoanAppraisalSyndicateConsortiumResource  sapLoanAppraisalSyndicateConsortiumResource = new SAPLoanAppraisalSyndicateConsortiumResource();
                     sapLoanAppraisalSyndicateConsortiumResource.setSAPLoanAppraisalSyndicateConsortiumResourceDetails(sapLoanAppraisalSyndicateConsortiumResourceDetails  );

                     resource = (Object)  sapLoanAppraisalSyndicateConsortiumResourceDetails;
                     serviceUri = appraisalServiceUri + "SyndicateBankSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Know Your Customer":

                     knowYourCustomer = knowYourCustomerRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanPartner =  loanPartnerRepository.getOne(UUID.fromString(knowYourCustomer.getLoanPartnerId())) ;
                     loanAppraisal = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));

                     log.info("Attempting to Post Appraisal Know Your Customer to SAP AT :" + dateFormat.format(new Date()) + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalKYCResourceDetails sapLoanAppraisalKYCResourceDetails =
                             sapLoanAppraisalKYCResource.mapKYCToSAP(knowYourCustomer,loanPartner,loanAppraisal);

                     SAPLoanAppraisalKYCResource  sapLoanAppraisalKYCResource = new SAPLoanAppraisalKYCResource();
                     sapLoanAppraisalKYCResource.setSapLoanAppraisalKYCResourceDetails(sapLoanAppraisalKYCResourceDetails  );

                     resource = (Object)  sapLoanAppraisalKYCResourceDetails;
                     serviceUri = appraisalServiceUri + "KYCSet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     if (response != null) {
                         response = postDocument(
                                 knowYourCustomer.getFileReference(),
                                 knowYourCustomer.getId().toString(),
                                 "",
                                 "Know Your Customer",
                                 knowYourCustomer.getDocumentName(),
                                 knowYourCustomer.getDocumentType());
                     }
                     updateSAPIntegrationPointer(response, sapIntegrationPointer);


                     break;
                 case "Reason For Delay":

                     reasonForDelay = reasonForDelayRepository.getOne(UUID.fromString((sapIntegrationPointer.getBusinessObjectId())));
                     loanAppraisal = loanAppraisalRepository.getOne( reasonForDelay.getLoanAppraisal().getId());

                     log.info("Attempting to Post Appraisal Reason For Delay to SAP AT :" + dateFormat.format(new Date())  + loanAppraisal.getLoanContractId().toString());

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalReasonForDelayResourceDetails sapLoanAppraisalReasonForDelayResourceDetails =
                             sapLoanAppraisalReasonForDelayResource.mapReasonForDelayToSAP(reasonForDelay);

                     SAPLoanAppraisalReasonForDelayResource  sapLoanAppraisalReasonForDelayResource = new SAPLoanAppraisalReasonForDelayResource();
                     sapLoanAppraisalReasonForDelayResource.setSAPReasonforDelay(sapLoanAppraisalReasonForDelayResourceDetails  );

                     resource = (Object)  sapLoanAppraisalReasonForDelayResourceDetails;
                     serviceUri = appraisalServiceUri + "ReasonForDelaySet";
                     response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
             }
         }
     }

    private Object postDocument(String fileReference,
                                String entityId, String docSubId,
                                String entityName,
                                String fileName,
                                String documentType) throws IOException {
        if (fileReference.length() == 0) {
            log.error("File Reference is Empty; Posting to SAP Aborted for Process Name :" +entityName + " entityId : " +entityId);
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
                entityName, file.toString(),
                mimeType,
                fileName );

        sapDocumentAttachmentResource.setSapDocumentAttachmentResourceDetails(sapDocumentAttachmentResourceDetails);
        Object d1 = (Object) sapDocumentAttachmentResource;

        String fileType = new String();
        String [] mimeTypeParts = mimeType.split("\\/") ;
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

                String documentUploadUri = appraisalDocumentUri + "("
                + "Id='" + entityId + "'," + "DocSubId='" + docSubId + "',"
                + "EntityId='" +entityId +  "',"
                + "EntityName='" +entityName +  "',"
                + "MimeType='" +mimeType +  "',"
                + "Filename='" +fileName +  "',"
                + "FileType='" +fileType +  "',"
                + "DocumentType='" +documentType +  "',"
//                + "DocId='" + "',"
//                 + "UploadTime='" + "datetime'2015-07-30T00:00:00Z'',"
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

            sapIntegrationPointer = sapIntegrationRepository.getOne(sapIntegrationPointer.getId());
            sapIntegrationPointer.setProcessDate(new Date());
            if (response == null) {
                //Set Status as Failed
                sapIntegrationPointer.setStatus(2); // Posting Failed
                sapIntegrationRepository.save(sapIntegrationPointer);
            } else {
                //Set Status as Posted Successfully
                sapIntegrationPointer.setStatus(3); // Posting Successful
                sapIntegrationRepository.save(sapIntegrationPointer);
                sapIntegrationRepository.flush();
            }

    }

}








