package pfs.lms.enquiry.iccapproval.approvalbyicc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApprovalByICCRepository extends JpaRepository<ApprovalByICC, UUID> {

    ApprovalByICC findByIccApprovalId(UUID iccApprovalId);
}
