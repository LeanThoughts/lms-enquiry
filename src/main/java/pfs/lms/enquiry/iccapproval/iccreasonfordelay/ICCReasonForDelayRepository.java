package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ICCReasonForDelayRepository extends JpaRepository<ICCReasonForDelay, UUID> {

    List<ICCReasonForDelay> findByIccApprovalId(UUID iccApprovalId);
}
