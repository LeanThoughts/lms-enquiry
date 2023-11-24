package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICCReasonForDelayRepository extends JpaRepository<ICCReasonForDelay, UUID> {

    ICCReasonForDelay findByIccApprovalId(UUID iccApprovalId);
}
