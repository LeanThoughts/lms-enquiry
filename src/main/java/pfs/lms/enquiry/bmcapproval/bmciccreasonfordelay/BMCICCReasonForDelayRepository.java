package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BMCICCReasonForDelayRepository extends JpaRepository<BmcIccReasonForDelay, UUID> {

    List<BmcIccReasonForDelay> findByBmcICCApprovalId(UUID iccApprovalId);
}
