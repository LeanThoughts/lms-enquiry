package pfs.lms.enquiry.boardapproval.reasonfordelay;

import java.util.UUID;

public interface IBoardApprovalReasonForDelayService {

    BoardApprovalReasonForDelay createReasonForDelay(BoardApprovalReasonForDelayResource boardApprovalReasonForDelayResource,
                                                     String username);

    BoardApprovalReasonForDelay updateReasonForDelay(BoardApprovalReasonForDelayResource boardApprovalReasonForDelayResource,
                                                     String username) throws CloneNotSupportedException;

    BoardApprovalReasonForDelay deleteReasonForDelay(UUID boardApprovalReasonForDelayId) throws CloneNotSupportedException;
}
