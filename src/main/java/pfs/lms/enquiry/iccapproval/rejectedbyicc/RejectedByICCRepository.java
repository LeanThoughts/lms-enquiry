package pfs.lms.enquiry.iccapproval.rejectedbyicc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RejectedByICCRepository extends JpaRepository<RejectedByICC, UUID> {

    RejectedByICC findByIccApprovalId(UUID iccApprovalId);
}
