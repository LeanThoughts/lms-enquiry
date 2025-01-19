package pfs.lms.enquiry.referenceinterest.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.batch.*;
import pfs.lms.enquiry.businesspartner.domain.*;
import pfs.lms.enquiry.businesspartner.repository.*;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateValueRepository;
import pfs.lms.enquiry.referenceinterest.resource.SAPReferenceInterestRateValueDetailResource;
import pfs.lms.enquiry.referenceinterest.resource.SAPReferenceInterestRateValueResourceDetail;
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
public class ReferenceInterestRateValueTaskCreateAndChange {

    @Value("${sap.referenceInterestRateValueUri}")
    private String   referenceInterestRateValueUri;

    private final ReferenceInterestRateValueRepository referenceInterestRateValueRepository;
    private final SAPIntegrationRepository sapIntegrationRepository;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final ISAPLoanProcessesIntegrationService sapLoanProcessesIntegrationService;
    User lastChangedByUser = new User();

    @Scheduled(fixedRateString = "${batch.businessPartnerMasterDataScheduledTask}",initialDelayString = "${batch.initialDelay}")
    public void syncReferenceInterestRate() throws Exception {

        ReferenceInterestRateValue referenceInterestRateValue = new ReferenceInterestRateValue();
        Object response = new Object();
        Object resource;

        //Collect SAPIntegrationPointer with the following  Posting Status = 0
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();

        sapIntegrationPointers = fetchSAPIntegrationPointers();
        String businessPartnerID = "";


        Collections.sort(sapIntegrationPointers, new Comparator<SAPIntegrationPointer>() {
            public int compare(SAPIntegrationPointer o1, SAPIntegrationPointer o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        String serviceUri = new String();

        for (SAPIntegrationPointer sapIntegrationPointer : sapIntegrationPointers) {

            switch (sapIntegrationPointer.getBusinessProcessName()) {
                case "ReferenceInterestRateValue":
                    referenceInterestRateValue = referenceInterestRateValueRepository.getOne(UUID.fromString(sapIntegrationPointer.getBusinessObjectId()));
                    log.info("---------------Sync. Reference Interest Value To SAP  : " + referenceInterestRateValue.getReferenceInterestRate().getCode() );

                    log.info("Attempting to Post Reference Interest Value to SAP AT :" + dateFormat.format(new Date())
                            + "Ref. Int. Rate: " +referenceInterestRateValue.getReferenceInterestRate().getCode() );

                    //Set Status as in progressNot found for upload to SAP
                    sapIntegrationPointer.setStatus(1); // In Posting Process
                    sapIntegrationRepository.save(sapIntegrationPointer);



                    SAPReferenceInterestRateValueDetailResource sapReferenceInterestRateValueDetailResource = new SAPReferenceInterestRateValueDetailResource();

                    SAPReferenceInterestRateValueResourceDetail sapReferenceInterestRateValueResourceDetail =
                            sapReferenceInterestRateValueDetailResource.mapResourceDetails(referenceInterestRateValue);
                    log.info(sapReferenceInterestRateValueResourceDetail.toString());

                    sapReferenceInterestRateValueDetailResource.setSAPReferenceInterestRateValueResourceDetail(sapReferenceInterestRateValueResourceDetail);

                     resource = (Object) sapReferenceInterestRateValueDetailResource;
                    serviceUri = referenceInterestRateValueUri ;

                    switch (sapIntegrationPointer.getMode()){
                        case "C":
                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
                        break;
                            case "U":
                                response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.POST, MediaType.APPLICATION_JSON);
//                                serviceUri = serviceUri + "('" + referenceInterestRateValue.getReferenceInterestRate().getCode() + "')";
//                            response = sapLoanProcessesIntegrationService.postResourceToSAP(resource, serviceUri, HttpMethod.PUT, MediaType.APPLICATION_JSON);
                        break;
                    }
                    if (response != null) {
                        ResponseEntity responseEntity = (ResponseEntity) response;
                        LinkedHashMap<String, String> responseKeyValueH = (LinkedHashMap<String, String>) responseEntity.getBody();
                         LinkedHashMap<String, LinkedHashMap<String, String>> responseKeyValueI = (LinkedHashMap<String, LinkedHashMap<String, String>>) responseEntity.getBody();
                         try {
                               String referenceIntRateType = responseKeyValueI.get("d").get("Referenz");
                             log.info("Reference Interest Rate Updated SAP: " + referenceIntRateType);
                         } catch ( Exception ex ){
                             log.info("Exception from Reference Interest Rate Create/Update. HTTP Status Code :" + responseEntity.getStatusCode());
                         }

                    }

                    updateSAPIntegrationPointer(response, sapIntegrationPointer);
                    break;

            }

        }
    }

    private List<SAPIntegrationPointer> fetchSAPIntegrationPointers() {
        List<SAPIntegrationPointer> sapIntegrationPointers = new ArrayList<>();
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("ReferenceInterestRateValue",  0, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("ReferenceInterestRateValue",   2, "C"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("ReferenceInterestRateValue",   0, "U"));
        sapIntegrationPointers.addAll(sapIntegrationRepository.findByBusinessProcessNameAndStatusAndMode("ReferenceInterestRateValue",   2, "U"));

        List<SAPIntegrationPointer> sapIntegrationPointerListFilteredByWorkflowStatus = new ArrayList<>();
        for (SAPIntegrationPointer sapIntegrationPointer:sapIntegrationPointers ) {
            try {
                UUID referenceInterestRateId = UUID.fromString(sapIntegrationPointer.getMainEntityId());
                ReferenceInterestRateValue referenceInterestRateValue = referenceInterestRateValueRepository.findById(referenceInterestRateId).get();
                if (referenceInterestRateValue != null){
                if (referenceInterestRateValue.getWorkFlowStatusCode()!= null) {
                    if (referenceInterestRateValue.getWorkFlowStatusCode() == 3) {
                        sapIntegrationPointerListFilteredByWorkflowStatus.add(sapIntegrationPointer);
                    }
                }
            }

            } catch (Exception ex){
                log.info("ReferenceInterestRate Not Found for ID: " + sapIntegrationPointer.getMainEntityId() );
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
