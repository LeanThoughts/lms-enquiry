package pfs.lms.enquiry.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalHeaderResource;
import pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalHeaderResourceDetails;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResource;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResourceDetails;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.SAPIntegrationRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.FileResource;
import pfs.lms.enquiry.sapintegrationservice.ISAPFileUploadIntegrationService;
import pfs.lms.enquiry.sapintegrationservice.ISAPLoanProcessesIntegrationService;
import pfs.lms.enquiry.vault.FilePointer;
import pfs.lms.enquiry.vault.FileStorage;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PartnerScheduledTaskCreateAndChange {

    @Value("${sap.businessPartnerDocumentServiceUri}")
    private String businessPartnerDocumentServiceUri;

    @Value("${sap.businessPartnerServiceUri}")
    private String   businessPartnerServiceUri;

    private final SAPDocumentAttachmentResource sapDocumentAttachmentResource;

    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPLoanProcessesIntegrationService sapLoanProcessesIntegrationService;
    private final ISAPFileUploadIntegrationService fileUploadIntegrationService;
    private final PartnerRepository partnerRepository;
    private final  SAPBusinessPartnerBasicDetailsResource sapLoanAppraisalHeaderResourceDetails;

    private final FileStorage fileStorage;
    private final UserRepository userRepository;
    private Partner partner;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    User lastChangedByUser = new User();

    @Scheduled(fixedRateString = "${batch.loanAppraisalScheduledTaskCreateAndChange}",initialDelayString = "${batch.initialDelay}")
    public void syncBusinessPartnerToBackend() throws Exception {

        Object response = new Object();
        Object resource;

        //Collect SAPIntegrationPointer with the following  Posting Status = 0
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();

        sapIntegrationPointers = fetchSAPIntegrationPointers();



        Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
            public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        String serviceUri = new String();

        for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

            switch (sapIntegrationPointer.getSubBusinessProcessName()) {
                case "BasicDetail":
                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    log.info("---------------Sync. Business Partner Master Data: BASIC DATA  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner Header to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1() + partner.getPartyName2());

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerBasicDetailsResourceDetails sapBusinessPartnerBasicDetailsResourceDetails =
                            sapLoanAppraisalHeaderResourceDetails.mapBupaBasicDetails(partner);

                    SAPBusinessPartnerBasicDetailsResource sapBusinessPartnerBasicDetailsResource = new SAPBusinessPartnerBasicDetailsResource();
                    sapBusinessPartnerBasicDetailsResource.setSAPBusinessPartnerBasicDetailsResourceDetails(sapBusinessPartnerBasicDetailsResourceDetails);

                    resource = (Object) sapBusinessPartnerBasicDetailsResource;
                    serviceUri = businessPartnerServiceUri + "BasicDetailSet";

                    switch (sapIntegrationPointer.getMode()){
                        case "C":
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
                        break;
                            case "U":
                            serviceUri = serviceUri + "('" + partner.getPartyNumber() + "')";
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.PUT, MediaType.APPLICATION_JSON);
                        break;
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
            }

        }
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
                entityName,
                file.toString(),
                mimeType,
                fileName,
                fileReference);

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

        String documentUploadUri = businessPartnerDocumentServiceUri + "("
                + "Id='" + entityId + "',"
                + "DocSubId='" + docSubId + "',"
                + "EntityId='" +entityId +  "',"
                + "EntityName='" +entityName +  "',"
                + "MimeType='" +mimeType +  "',"
                + "Filename='" +fileName +  "',"
                + "FileType='" +fileType +  "',"
                + "DocumentType='" +documentType +  "',"
                + "FileReference='" +fileReference +  "',"

//                + "DocId='" + "',"
//                 + "UploadTime='" + "datetime'2015-07-30T00:00:00Z'',"
                + ")/$value";


        Object response =  fileUploadIntegrationService.fileUploadToSAP(documentUploadUri, filePath);



        return  response;
    }


    private List<SAPIntegrationPointer> fetchSAPIntegrationPointers() {
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();

        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BasicDetail", 0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BasicDetail", 2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BasicDetail", 0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BasicDetail", 2, "U"));

        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Identification", 0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Identification", 2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Identification", 0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Identification", 2, "U"));

        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "ContactDetails", 0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "ContactDetails", 2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "ContactDetails", 0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "ContactDetails", 2, "U"));

        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BankDetails", 0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BankDetails", 2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BankDetails", 0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "BankDetails", 2, "U"));

        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Industry", 0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Industry", 2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Industry", 0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("BusinessPartner", "Industry", 2, "U"));

        return sapIntegrationPointers;
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
