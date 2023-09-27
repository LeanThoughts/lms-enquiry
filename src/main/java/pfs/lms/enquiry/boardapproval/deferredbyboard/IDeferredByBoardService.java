package pfs.lms.enquiry.boardapproval.deferredbyboard;

import java.util.UUID;

public interface IDeferredByBoardService {

    DeferredByBoard create(DeferredByBoardResource deferredByBoardResource, String username);

    DeferredByBoard update(DeferredByBoardResource deferredByBoardResource, String username)
            throws CloneNotSupportedException;

    DeferredByBoard delete(UUID deferredByBoardId, String username) throws CloneNotSupportedException;
}
