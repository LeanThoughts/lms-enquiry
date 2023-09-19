package pfs.lms.enquiry.boardapproval.rejectedbyboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RejectedByBoardRepository extends JpaRepository<RejectedByBoard, UUID> {

    List<RejectedByBoard> findByBoardApprovalId(UUID boardApprovalId);
}
