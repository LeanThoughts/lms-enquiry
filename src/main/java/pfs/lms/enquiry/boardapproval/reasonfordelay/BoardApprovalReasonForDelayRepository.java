package pfs.lms.enquiry.boardapproval.reasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardApprovalReasonForDelayRepository extends JpaRepository<BoardApprovalReasonForDelay, UUID> {

    List<BoardApprovalReasonForDelay> findByBoardApprovalId(UUID boardApprovalId);
}
