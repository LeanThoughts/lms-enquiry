package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;

import java.util.List;
import java.util.UUID;

public interface SAPIntegrationRepository extends JpaRepository<SAPIntegrationPointer, Long> {

    List<SAPIntegrationPointer> getByBusinessProcessNameAndStatus(String businessProcessName, Integer status);

    List<SAPIntegrationPointer> getByBusinessObjectIdAndStatus(String businessObejctId, Integer status);

    List<SAPIntegrationPointer> getByBusinessProcessNameAndStatusAndMode(String businessObjectId, Integer status, char mode);

    //SAPIntegrationRepository save(SAPIntegrationPointer sapIntegrationPointer);
}
