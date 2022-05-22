package pfs.lms.enquiry.service;

import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.domain.User;

import java.util.List;
import java.util.UUID;

public interface ISAPIntegrationPointerService {

    public  SAPIntegrationPointer save(SAPIntegrationPointer sapIntegrationPointer);
    public  SAPIntegrationPointer saveForObject(String businessProcessName, String subBusinessProcessName, String entityId, String mainEntityId, char mode);
    public  SAPIntegrationPointer updateStatus(Long id, Integer status);


}
