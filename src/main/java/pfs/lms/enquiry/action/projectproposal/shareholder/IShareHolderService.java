package pfs.lms.enquiry.action.projectproposal.shareholder;

import java.util.UUID;

public interface IShareHolderService {

    ShareHolder create(ShareHolderResource resource, String username);

    ShareHolder update(ShareHolderResource resource, String username) throws CloneNotSupportedException;

    ShareHolder delete(UUID shareHolderId, String username);
}
