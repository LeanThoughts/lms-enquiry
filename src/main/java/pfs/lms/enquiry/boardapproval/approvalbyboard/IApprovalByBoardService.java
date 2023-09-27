package pfs.lms.enquiry.boardapproval.approvalbyboard;

import java.util.UUID;

public interface IApprovalByBoardService {

    ApprovalByBoard create(ApprovalByBoardResource approvalByBoardResource, String username);

    ApprovalByBoard update(ApprovalByBoardResource approvalByBoardResource, String username)
            throws CloneNotSupportedException;

    ApprovalByBoard delete(UUID approvalByBoardId, String username) throws CloneNotSupportedException;
}
