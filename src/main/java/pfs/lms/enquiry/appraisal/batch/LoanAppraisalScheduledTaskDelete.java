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
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletionRepository;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.appraisal.projectdata.ProjectDataRepository;
import pfs.lms.enquiry.appraisal.resource.*;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
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
public class LoanAppraisalScheduledTaskDelete {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");



    @Value("${sap.monitorDocumentUri}")
    private String monitorDocumentUri;

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

    private final SAPDocumentAttachmentResource sapDocumentAttachmentResource;

    private final SAPLoanAppraisalHeaderResource sapLoanAppraisalHeaderResource;
    private final SAPLoanAppraisalCustomerRejectionResource customerRejectionResource;
    private final SAPLoanAppraisalFurtherDetailResource sapLoanAppraisalFurtherDetailResource;
    private final SAPLoanAppraisalProjectDataResource sapLoanAppraisalProjectDataResource;



    @Scheduled(fixedRate = 8000)
    public void syncLoanAppraisalsToBackend() throws ParseException, IOException {

        LoanAppraisal loanAppraisal = new LoanAppraisal();
        CustomerRejection customerRejection = new CustomerRejection();
        FurtherDetail furtherDetail = new FurtherDetail();
        ProjectAppraisalCompletion projectAppraisalCompletion = new ProjectAppraisalCompletion();
        ProjectData projectData = new ProjectData();
        String objectId;

         Object response = new Object();
         Object resource;

         //Collect SAPIntegrationPointer with the following  Posting Status = 0
         List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 0,'D'));
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatusAndMode("Appraisal", 2,'D'));

         Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
             public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                 return o1.getCreationDate().compareTo(o2.getCreationDate());
             }
         });

         String serviceUri = new String();

         for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

             switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                 case "KYC":
                     log.info("Attempting to Delete Appraisal KYC from SAP AT :" + dateFormat.format(new Date()));

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                       objectId = sapIntegrationPointer.getBusinessObjectId();
                     serviceUri = appraisalServiceUri + "KycSet";
                     response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri,objectId , MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Loan Partner":
                     log.info("Attempting to Delete Loan Partner from SAP AT :" + dateFormat.format(new Date()));

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                       objectId = sapIntegrationPointer.getBusinessObjectId();
                     serviceUri = appraisalServiceUri + "LoanPartnerSet";
                     response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri,objectId , MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;
                 case "Syndicate Consortium":
                     log.info("Attempting to Delete Syndicate Consortium from SAP AT :" + dateFormat.format(new Date()));

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     objectId = sapIntegrationPointer.getBusinessObjectId();
                     serviceUri = appraisalServiceUri + "SyndicateConsortiumSet";
                     response = sapLoanProcessesIntegrationService.deleteResourceFromSAP(serviceUri,objectId , MediaType.APPLICATION_JSON);

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

                String documentUploadUri = monitorDocumentUri + "("
                + "Id='" + entityId + "'," + "DocSubId='" + docSubId + "',"
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








