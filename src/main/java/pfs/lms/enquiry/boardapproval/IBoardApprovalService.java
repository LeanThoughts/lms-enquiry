package pfs.lms.enquiry.boardapproval;

import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoard;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoardResource;

import java.util.UUID;

public interface IBoardApprovalService {

     BoardApproval processApprovedBoardApproval(BoardApproval boardApproval, String username);
}
