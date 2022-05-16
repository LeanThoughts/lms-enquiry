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
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalHeaderResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalHeaderResourceDetails;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancialsRepository;
import pfs.lms.enquiry.monitoring.domain.*;
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
import pfs.lms.enquiry.monitoring.projectmonitoring.*;
import pfs.lms.enquiry.monitoring.promoterdetails.PromoterDetail;
import pfs.lms.enquiry.monitoring.promoterdetails.PromoterDetailRepository;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancialsRepository;
import pfs.lms.enquiry.monitoring.repository.*;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.monitoring.tra.TRARepository;
import pfs.lms.enquiry.monitoring.tra.TRAStatementRepository;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccount;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;
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
public class LoanAppraisalScheduledTask {

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
    private final LoanAppraisalRepository loanAppraisalRepository;

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
    private final LFAReportAndFeeRepository lfaReportAndFeeRepository;

    private final SAPDocumentAttachmentResource sapDocumentAttachmentResource;

    private final SAPLoanAppraisalHeaderResource sapLoanAppraisalHeaderResource;





    @Scheduled(fixedRate = 2000)
    public void syncLoanAppraisalsToBackend() throws ParseException, IOException {


         LendersIndependentEngineer lendersIndependentEngineer = new LendersIndependentEngineer();

         User lastChangedByUser = new User();
         lastChangedByUser = userRepository.findByEmail(lendersIndependentEngineer.getChangedByUserName());

         Object response = new Object();
         Object resource;

         //Collect SAPIntegrationPointer with the following  Posting Status = 0
         List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatus("Appraisal", 0));
         sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatus("Appraisal", 2));

         Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
             public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                 return o1.getCreationDate().compareTo(o2.getCreationDate());
             }
         });



         String serviceUri = new String();


         for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

             switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                 case "Header":
                     log.info("Attempting to Post Appraisal Header to SAP AT :" + dateFormat.format(new Date()));
                     LoanAppraisal loanAppraisal = loanAppraisalRepository.getOne( UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));

                     //Set Status as in progress
                     sapIntegrationPointer.setStatus(1); // In Posting Process
                     sapIntegrationRepository.save(sapIntegrationPointer);

                     SAPLoanAppraisalHeaderResourceDetails sapLoanAppraisalHeaderResourceDetails =
                             sapLoanAppraisalHeaderResource.mapLoanAppraisalHeaderToSAP(loanAppraisal);

                     SAPLoanAppraisalHeaderResource sapLoanAppraisalHeaderResource = new SAPLoanAppraisalHeaderResource();
                     sapLoanAppraisalHeaderResource.setsapMonitorHeaderResourceDetails(sapLoanAppraisalHeaderResourceDetails);

                     resource = (Object) sapLoanAppraisalHeaderResource;
                     serviceUri = monitorServiceUri + "MonitorHeaderSet"; //TODO
                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);

                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
                     break;

                 case "Lenders Independent Engineer":

//                     log.info("Attempting to Post LIE to SAP AT :" + dateFormat.format(new Date()));
//                     lendersIndependentEngineer = lieRepository.getOne(sapIntegrationPointer.getBusinessObjectId());
//
//                     //Set Status as in progress
//                     sapIntegrationPointer.setStatus(1); // In Posting Process
//                     sapIntegrationRepository.save(sapIntegrationPointer);
//
//                     SAPLIEResourceDetails saplieResourceDetails = saplieResource.mapToSAP(lendersIndependentEngineer, lastChangedByUser);
//                     SAPLIEResource d = new SAPLIEResource();
//                     d.setSaplieResourceDetails(saplieResourceDetails);
//
//                     resource = (Object) d;
//                     serviceUri = monitorServiceUri + "LendersIndependentEngineerSet";
//                     response = sapLoanMonitoringIntegrationService.postResourceToSAP(d, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
//
//                     updateSAPIntegrationPointer(response, sapIntegrationPointer);
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
                sapIntegrationPointer.setStatus(2); // Posting Failed
                sapIntegrationRepository.save(sapIntegrationPointer);
            } else {
                //Set Status as Posted Successfully
                sapIntegrationPointer.setStatus(3); // Posting Successful
                sapIntegrationRepository.save(sapIntegrationPointer);
            }

    }

}








