package pfs.lms.enquiry.boardapproval.rejectedbyboard;

import java.util.UUID;

public interface IRejectedByBoardService {

    RejectedByBoard create(RejectedByBoardResource rejectedByBoardResource, String username);

    RejectedByBoard update(RejectedByBoardResource rejectedByBoardResource, String username)
            throws CloneNotSupportedException;

    RejectedByBoard delete(UUID rejectedByBoardId) throws CloneNotSupportedException;
}
