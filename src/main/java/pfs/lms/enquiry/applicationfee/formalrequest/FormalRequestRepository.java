package pfs.lms.enquiry.applicationfee.formalrequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FormalRequestRepository extends JpaRepository<FormalRequest, UUID> {

    List<FormalRequest> findByApplicationFeeIdOrderBySerialNumber(UUID applicationFeeId);
}
