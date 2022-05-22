package pfs.lms.enquiry.service.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.repository.SAPIntegrationRepository;
import pfs.lms.enquiry.service.ISAPIntegrationPointerService;
import pfs.lms.enquiry.service.ISAPIntegrationService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sajeev on 13-Jun-21.
 */
@Service
public class SAPIntegrationPointerService implements ISAPIntegrationPointerService{

    @Autowired
    SAPIntegrationRepository sapIntegrationRepository;

    @Override
    public SAPIntegrationPointer save(SAPIntegrationPointer sapIntegrationPointer) {

        return sapIntegrationRepository.save(sapIntegrationPointer);

    }

    @Override
    public SAPIntegrationPointer saveForObject(String businessProcessName, String subBusinessProcessName, String entityId, String mainEntityId,char mode) {

        // Check if an entry already exists with the status - Not Posted in SAP for the Object
        // If yes, do nothing
        List<SAPIntegrationPointer> sapIntegrationPointers = sapIntegrationRepository.getByBusinessObjectIdAndStatus(entityId,0);
        if (sapIntegrationPointers.size() > 0 ) {
            return  null;
        }


        //Default Status = 0 (Not Posted in SAP)
        Integer status = 0;

        SAPIntegrationPointer sapIntegrationPointer = new SAPIntegrationPointer();
        sapIntegrationPointer.setBusinessProcessName(businessProcessName);
        sapIntegrationPointer.setSubBusinessProcessName(subBusinessProcessName);
        sapIntegrationPointer.setCreationDate(new Date());
        sapIntegrationPointer.setBusinessObjectId(entityId);
        sapIntegrationPointer.setStatus(status);
        sapIntegrationPointer.setMainEntityId(mainEntityId);
        sapIntegrationPointer.setMode(mode);
        return this.save(sapIntegrationPointer);
     }

    @Override
    public SAPIntegrationPointer updateStatus(Long id, Integer status) {

        SAPIntegrationPointer sapIntegrationPointer = sapIntegrationRepository.getOne(id);
        if (sapIntegrationPointer != null) {
            sapIntegrationPointer.setStatus(status);
            return this.save(sapIntegrationPointer);

        }
        return null;
    }
}
