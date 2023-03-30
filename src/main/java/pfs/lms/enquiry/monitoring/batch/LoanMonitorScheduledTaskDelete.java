package pfs.lms.enquiry.monitoring.batch;

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
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletionRepository;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.appraisal.projectdata.ProjectDataRepository;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalCustomerRejectionResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalFurtherDetailResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalHeaderResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalProjectDataResource;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResource;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResourceDetails;
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
public class LoanMonitorScheduledTaskDelete {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Value("${sap.monitorDocumentUri}")
    private String monitorDocumentUri;

    @Value("${sap.monitorServiceUri}")
    private String monitorServiceUri;

    @Value("${sap.appraisalServiceUri}")
    private String appraisalServiceUri;

    private final ISAPIntegrationService isapIntegrationService;


    private final FileStorage fileStorage;
    private final UserRepository userRepository;
    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPLoanProcessesIntegrationService sapLoanProcessesIntegrationService;
    private final ISAPFileUploadIntegrationService fileUploadIntegrationService;
    private final LoanAppraisalRepository loanAppraisalRepository;

    private final SAPDocumentAttachmentResource sapDocumentAttachmentResource;


    @Scheduled(fixedRateString = "${batch.loanAppraisalScheduledTaskDelete}", initialDelayString = "${batch.initialDelay}")
    public void syncLoanAppraisalsToBackend() throws ParseException, IOException {


        //log.info("---------------Delete Monitoring Objects Batch Process ");

        LoanMonitor loanMonitor = new LoanMonitor();

        String objectId;

        Object response = new Object();
        Object resource;

        //Collect SAPIntegrationPointer with the following  Posting Status = 0 or 2 for Deleted Items
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Monitoring", 0, 'D'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Monitoring", 2, 'D'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 0, 'D'));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 2, 'D'));

        Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
            public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });

        String serviceUri = new String();

        for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

            switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                case "Lenders Independent Engineer":
                    log.info("Attempting to Delete Lenders Independent Engineer from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LendersIndependentEngineerSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "LIE Report and Fee":
                    log.info("Attempting to Delete LIE Report and Fee from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LIEReportAndFeeSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Lenders Financial Advisor":
                    log.info("Attempting to Delete Lenders Financial Advisor from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LendersFinancialAdvisorSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "LFA Report and Fee":
                    log.info("Attempting to Delete LFA Report and Fee from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LFAReportAndFeeSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Lenders Legal Counsel":
                    log.info("Attempting to Delete Lenders Legal Counsel from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LendersLegalCounselSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "LLC Report and Fee":
                    log.info("Attempting to Delete LLC Report and Fee from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LLCReportAndFeeSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Lenders Insurance Advisor":
                    log.info("Attempting to Delete Lenders Insurance Advisor from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LendersInsuranceAdvisorSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "LIA Report and Fee":
                    log.info("Attempting to Delete LIA Report and Fee from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LIAReportAndFeeSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

                case "Valuer":
                    log.info("Attempting to Delete Valuer from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "ValuerSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Valuer Report and Fee":
                    log.info("Attempting to Delete Valuer Report and Fee from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "ValuerReportAndFeeSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "NPA":
                    log.info("Attempting to Delete NPA  from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "NPASet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "NPA Detail":
                    log.info("Attempting to Delete NPA Detail from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "NPADetailSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Insurance":
                    log.info("Attempting to Delete Insurance from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "InsuranceSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Rate of Interest":
                    log.info("Attempting to Delete Rate of Interest from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "InterestRateSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Terms and Conditions":
                    log.info("Attempting to Terms and Conditions from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "TermsAndConditionsModificationSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Security Compliance":
                    log.info("Attempting to Security Compliance from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "SecurityComplianceSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Site Visit":
                    log.info("Attempting to Site Visit from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "SiteVisitSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Operating Parameter":
                    log.info("Attempting to Operating Parameter from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "OperatingParameterSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Operating Parameter PLF":
                    log.info("Attempting to Operating Parameter PLF from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "OperatingParameterPLFSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Borrower Financials":
                    log.info("Attempting to Borrower Financials from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "BorrowerFinancialsSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Promoter Detail":
                    log.info("Attempting to Promoter Detail from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "PromoterDetailsItemSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Promoter Financials":
                    log.info("Attempting to Promoter Financials from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "PromoterFinancialsSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Financial Covenants":
                    log.info("Attempting to Financial Covenants from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "FinancialCovenantsSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "TRA Account":
                    log.info("Attempting to TRA Account from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "TRAAccountSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "TRA Account Statement":
                    log.info("Attempting to TRA Account Statement from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "TRAAccountStatementSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "Loan Documentation":
                    log.info("Attempting to Loan Documentation from SAP AT :" + dateFormat.format(new Date()));

                    //Set Status as in progress
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    objectId = sapIntegrationPointer.getBusinessObjectId();
                    serviceUri = monitorServiceUri + "LoanDocumentationSet";
                    response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri, objectId, MediaType.APPLICATION_JSON);
                    if (response != null)
                        updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
            }
        }
    }

    private Object postDocument(String fileReference,
                                String entityId, String docSubId,
                                String entityName,
                                String fileName) throws IOException {
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
                entityName, file.toString(),
                mimeType,
                fileName, fileReference);

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

        String documentUploadUri = monitorDocumentUri + "("
                + "Id='" + entityId + "'," + "DocSubId='" + docSubId + "',"
                + "EntityId='" + entityId + "',"
                + "EntityName='" + entityName + "',"
                + "MimeType='" + mimeType + "',"
                + "Filename='" + fileName + "',"
                + "FileType='" + fileType + "',"
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

        sapIntegrationPointer.setProcessDate(new Date());
        if (response == null) {
            //Set Status as Failed
            sapIntegrationPointer.setStatus(2); // Deletion Failed
            sapIntegrationRepository.save(sapIntegrationPointer);
        } else {
            //Set Status as processed Successfully
            sapIntegrationPointer.setStatus(3); // Deletion Successful
            sapIntegrationRepository.save(sapIntegrationPointer);
        }

    }

}








