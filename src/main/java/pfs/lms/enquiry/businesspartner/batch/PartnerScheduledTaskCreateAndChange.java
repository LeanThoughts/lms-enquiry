package pfs.lms.enquiry.businesspartner.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.*;
import pfs.lms.enquiry.businesspartner.repository.*;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.domain.User;
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

    private final SAPBusinessPartnerDocumentAttachmentResource sapBusinessPartnerDocumentAttachmentResource;

    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPLoanProcessesIntegrationService sapLoanProcessesIntegrationService;
    private final ISAPFileUploadIntegrationService fileUploadIntegrationService;
    private final PartnerRepository partnerRepository;

    private final SAPBusinessPartnerBasicDetailResource sapBusinessPartnerBasicDetailResource;
    private final SAPBusinessPartnerRoleResource sapBusinessPartnerRoleResource;
    private final SAPBusinessPartnerBankDetailResource sapBusinessPartnerBankDetailResource;
    private final SAPBusinessPartnerIndustryResource sapBusinessPartnerIndustryResource;
    private final SAPBusinessPartnerLoanContactResource sapBusinessPartnerLoanContactResource;
    private final SAPBusinessPartnerIdentificationResource sapBusinessPartnerIdentificationResource;

    private final FileStorage fileStorage;
    private final UserRepository userRepository;
    private Partner partner;
    private BusinessPartnerRole businessPartnerRole;
    private BusinessPartnerLoanContact businessPartnerLoanContact;
    private BusinessPartnerIndustry businessPartnerIndustry;
    private BusinessPartnerBankDetail businessPartnerBankDetail;
    private BusinessPartnerIdentification businessPartnerIdentification;

    private final BusinessPartnerRoleRepository businessPartnerRoleRepository;
    private final BusinessPartnerIdentificationRepository businessPartnerIdentificationRepository;
    private final BusinessPartnerIndustryRepository businessPartnerIndustryRepository;
    private final BusinessPartnerBankDetailRepository businessPartnerBankDetailRepository;
    private final BusinessPartnerLoanContactRepository businessPartnerLoanContactRepository;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final IdentificationCategoryRepository identificationCategoryRepository;

    private final IndustrySystemRepository industrySystemRepository;
    private final IndustryTypeRepository industryTypeRepository;

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
                case "Partner":
                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    log.info("---------------Sync. Business Partner Master Data: BASIC DATA  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner Header to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1() );

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerBasicDetailsResourceDetail sapBusinessPartnerBasicDetailsResourceDetail =
                            sapBusinessPartnerBasicDetailResource.mapBupaBasicDetails(partner);

                    SAPBusinessPartnerBasicDetailResource sapBusinessPartnerBasicDetailResource = new SAPBusinessPartnerBasicDetailResource();
                    sapBusinessPartnerBasicDetailResource.setSAPBusinessPartnerBasicDetailsResourceDetails(sapBusinessPartnerBasicDetailsResourceDetail);

                    resource = (Object) sapBusinessPartnerBasicDetailResource;
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
                    if (response != null) {
                        ResponseEntity responseEntity = (ResponseEntity) response;
                        LinkedHashMap<String, String> responseKeyValueH = (LinkedHashMap<String, String>) responseEntity.getBody();
                         LinkedHashMap<String, LinkedHashMap<String, String>> responseKeyValueI = (LinkedHashMap<String, LinkedHashMap<String, String>>) responseEntity.getBody();

                         String businessPartnerID =  responseKeyValueI.get("d").get("BusPartnerNumber");

                        partner.setPartyNumber(Integer.parseInt(businessPartnerID));
                        partnerRepository.save(partner);
                        log.info("Business Partner Created in SAP: " + businessPartnerID);
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "BusinessPartnerLoanContact":
                    businessPartnerLoanContact = businessPartnerLoanContactRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));

                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getMainEntityId()));
                    if(partner == null) return;
                    if (partner.getPartyNumber() == null) return;
                    log.info("---------------Sync. Business Partner LoanContact  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner LoanContact to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1());

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerLoanContactResourceDetail sapBusinessPartnerLoanContactResourceDetail  =
                            sapBusinessPartnerLoanContactResource.mapResource(businessPartnerLoanContact);

                    SAPBusinessPartnerLoanContactResource sapBusinessPartnerLoanContactResource = new SAPBusinessPartnerLoanContactResource();
                    sapBusinessPartnerLoanContactResource.setSapBusinessPartnerLoanContactResourceDetail(sapBusinessPartnerLoanContactResourceDetail);

                    resource = (Object) sapBusinessPartnerLoanContactResource;
                    serviceUri = businessPartnerServiceUri + "LoanContactSet";

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
                case "BusinessPartnerRole":
                    businessPartnerRole = businessPartnerRoleRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getMainEntityId()));
                    if(partner == null) return;
                    if (partner.getPartyNumber() == null) return;
                    log.info("---------------Sync. Business Partner LoanRole  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner LoanRole to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1());

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerRoleResourceDetail sapBusinessPartnerRoleResourceDetail  =
                            sapBusinessPartnerRoleResource.mapResource(businessPartnerRole);

                    SAPBusinessPartnerRoleResource sapBusinessPartnerLoanContactResource1 = new SAPBusinessPartnerRoleResource();
                    sapBusinessPartnerLoanContactResource1.setSapBusinessPartnerRoleResourceDetail(sapBusinessPartnerRoleResourceDetail);

                    resource = (Object) sapBusinessPartnerLoanContactResource1;
                    serviceUri = businessPartnerServiceUri + "RoleSet";

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
                case "BusinessPartnerBankDetail":
                    businessPartnerBankDetail = businessPartnerBankDetailRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getMainEntityId()));
                    if(partner == null) return;
                    if (partner.getPartyNumber() == null) return;


                    log.info("---------------Sync. Business Partner Bank Detail  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner Bank Detail to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1());

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerBankDetailResourceDetail sapBusinessPartnerBankDetailResourceDetail  =
                            sapBusinessPartnerBankDetailResource.mapResourceDetails(businessPartnerBankDetail);

                    SAPBusinessPartnerBankDetailResource sapBusinessPartnerBankDetailResource1 = new SAPBusinessPartnerBankDetailResource();
                    sapBusinessPartnerBankDetailResource1.setSapBusinessPartnerBankDetailResourceDetail(sapBusinessPartnerBankDetailResourceDetail);

                    resource = (Object) sapBusinessPartnerBankDetailResource1;
                    serviceUri = businessPartnerServiceUri + "BankDetailSet";

                    switch (sapIntegrationPointer.getMode()){
                        case "C":
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
                            break;
                        case "U":
                           // serviceUri = serviceUri + "(" + "Businesspartner='" + partner.getPartyNumber() + "',Bankdetailid=" +  "'" + sapBusinessPartnerBankDetailResourceDetail.getBankDetailId() + "')";
                            serviceUri = serviceUri + "(" + "Businesspartner='" + partner.getPartyNumber() + "'" + ")";
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.PUT, MediaType.APPLICATION_JSON);
                            break;
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "BusinessPartnerIdentification":
                    businessPartnerIdentification = businessPartnerIdentificationRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getMainEntityId()));
                    if(partner == null) return;
                    if (partner.getPartyNumber() == null) return;
                    log.info("---------------Sync. Business Partner Identification  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner Identification to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1());

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerIdentificationResourceDetail sapBusinessPartnerIdentificationResourceDetail  =
                            sapBusinessPartnerIdentificationResource.mapResource(businessPartnerIdentification);

                    if(businessPartnerIdentification.getIdentificationCategoryId() != null){
                        IdentificationCategory identificationCategory =
                                identificationCategoryRepository.findById(businessPartnerIdentification.getIdentificationCategoryId()).get();
                        sapBusinessPartnerIdentificationResourceDetail.setIdentificationCategory(identificationCategory.getCode());
                    }
                    else sapBusinessPartnerIdentificationResourceDetail.setIdentificationCategory("");

                    SAPBusinessPartnerIdentificationResource sapBusinessPartnerIdentificationResource1 = new SAPBusinessPartnerIdentificationResource( );
                    sapBusinessPartnerIdentificationResource1.setSapBusinessPartnerIdentificationResourceDetail(sapBusinessPartnerIdentificationResourceDetail);

                    resource = (Object) sapBusinessPartnerIdentificationResource1;
                    serviceUri = businessPartnerServiceUri + "IdentificationSet";

                    switch (sapIntegrationPointer.getMode()){
                        case "C":
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
                            break;
                        case "U":
                            serviceUri = serviceUri + "('" + partner.getPartyNumber() + "')";
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.PUT, MediaType.APPLICATION_JSON);
                            break;
                    }

                    if (response != null) {
                        if (businessPartnerIdentification.getFileReference() != null && businessPartnerIdentification.getFileReference().length() > 0) {

                            try {
                                response = postDocument(
                                        businessPartnerIdentification.getPartner().getPartyNumber().toString(),
                                        businessPartnerIdentification.getFileReference(),
                                        businessPartnerIdentification.getId().toString(),
                                        businessPartnerIdentification.getId().toString(),
                                        "Business Partner",
                                        "Identification",
                                        businessPartnerIdentification.getDocumentType(),
                                        businessPartnerIdentification.getDocumentName());
                            } catch (Exception ex){
                                log.error("Exception Posting Identification Document : " + businessPartnerIdentification.getPartner().getPartyNumber());
                            }
                        }
                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;
                case "BusinessPartnerIndustry":
                    businessPartnerIndustry = businessPartnerIndustryRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    partner = partnerRepository.getOne(UUID.fromString(sapIntegrationPointer.getMainEntityId()));
                    if(partner == null) return;
                    if (partner.getPartyNumber() == null) return;

                    log.info("---------------Sync. Business Partner Industry  to SAP : " + partner.getPartyNumber() );

                    log.info("Attempting to Post Business Partner Industry to SAP AT :" + dateFormat.format(new Date())
                            + "Partner Name: " + partner.getPartyName1());

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);

                    SAPBusinessPartnerIndustryResourceDetail sapBusinessPartnerIndustryResourceDetail  =
                            sapBusinessPartnerIndustryResource.mapResource(businessPartnerIndustry);

                    IndustrySystem industrySystem = industrySystemRepository.getOne(businessPartnerIndustry.getIndustrySystemId());
                    sapBusinessPartnerIndustryResourceDetail.setIndustrysectorkeysystem(industrySystem.getCode());
                    IndustryType industryType = industryTypeRepository.getOne(businessPartnerIndustry.getIndustryTypeId());
                    sapBusinessPartnerIndustryResourceDetail.setIndustrysector(industryType.getCode());


                    SAPBusinessPartnerIndustryResource sapBusinessPartnerIndustryResource1 = new SAPBusinessPartnerIndustryResource();
                    sapBusinessPartnerIndustryResource1.setSapBusinessPartnerIndustryResourceDetail(sapBusinessPartnerIndustryResourceDetail);

                    resource = (Object) sapBusinessPartnerIndustryResource1;
                    serviceUri = businessPartnerServiceUri + "IndustrySectorSet";

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


    private Object postDocument(String businessPartnerId,
                                String fileReference,
                                String entityId,
                                String docSubId,
                                String entityName,
                                String fileName,
                                String documentType,
                                String documentName) throws IOException {
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


        SAPBusinessPartnerDocumentAttachmentResourceDetails documentAttachmentResourceDetails = new SAPBusinessPartnerDocumentAttachmentResourceDetails();
        if (mimeType == "")
            mimeType = "application/pdf";

        documentAttachmentResourceDetails = sapBusinessPartnerDocumentAttachmentResource.mapToSAP(
                businessPartnerId,
                fileUUID.toString(),
                entityId,
                entityName,
                file.toString(),
                mimeType,
                fileName,
                fileReference,
                documentType);

        sapBusinessPartnerDocumentAttachmentResource.setSapDocumentAttachmentResourceDetails(documentAttachmentResourceDetails);
        Object d1 = (Object) documentAttachmentResourceDetails;

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

                + "Businesspartner='" + businessPartnerId + "',"

                + "Id='" + entityId + "',"
                + "DocSubId='" + "01" + "',"
                + "EntityId='" +entityId +  "',"
                + "EntityName='" +entityName +  "',"
                + "MimeType='" +mimeType +  "',"
                + "Filename='" +fileName +  "',"
                + "FileType='" +fileType +  "',"
                + "DocumentType='" +documentType +  "',"
                + "Filereference='" +fileReference +  "'"

//                + "DocId='" + "',"
//                 + "UploadTime='" + "datetime'2015-07-30T00:00:00Z'',"
                + ")/$value";


        Object response =  fileUploadIntegrationService.fileUploadToSAP(documentUploadUri, filePath);



        return  response;
    }


    private List<SAPIntegrationPointer> fetchSAPIntegrationPointers() {
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("Partner",  0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("Partner",   2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("Partner",   0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("Partner",   2, "U"));

//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Partner", 0, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Partner", 2, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Partner", 0, "U"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Partner", 2, "U"));
//
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BasicDetail", 0, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BasicDetail", 2, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BasicDetail", 0, "U"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BasicDetail", 2, "U"));
//
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Identification", 0, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Identification", 2, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Identification", 0, "U"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Identification", 2, "U"));
//
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "ContactDetails", 0, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "ContactDetails", 2, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "ContactDetails", 0, "U"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "ContactDetails", 2, "U"));
//
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BankDetails", 0, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BankDetails", 2, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BankDetails", 0, "U"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "BankDetails", 2, "U"));
//
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Industry", 0, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Industry", 2, "C"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Industry", 0, "U"));
//        sapIntegrationPointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndSubBusinessProcessNameAndStatusAndMode("Partner", "Industry", 2, "U"));

        List<SAPIntegrationPointer> sapIntegrationPointerListFilteredByWorkflowStatus = new ArrayList<>();
        for (SAPIntegrationPointer sapIntegrationPointer:sapIntegrationPointers ) {
            try {
                UUID partnerId = UUID.fromString(sapIntegrationPointer.getMainEntityId());
                Partner partner1 = partnerRepository.findById(partnerId).get();
                if (partner1 != null){
                if (partner1.getWorkFlowStatusCode()!= null) {
                    if (partner1.getWorkFlowStatusCode() == 3) {
                        sapIntegrationPointerListFilteredByWorkflowStatus.add(sapIntegrationPointer);
                    }
                }
            }

            } catch (Exception ex){
                log.info("Partner Not Found for ID: " + sapIntegrationPointer.getMainEntityId() );
            }

        }
        return sapIntegrationPointerListFilteredByWorkflowStatus;
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
