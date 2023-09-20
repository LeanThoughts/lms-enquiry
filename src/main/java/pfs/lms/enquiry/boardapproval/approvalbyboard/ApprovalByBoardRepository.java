package pfs.lms.enquiry.boardapproval.approvalbyboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApprovalByBoardRepository extends JpaRepository<ApprovalByBoard, UUID> {

    List<ApprovalByBoard> findByBoardApprovalId(UUID boardApprovalId);
}
