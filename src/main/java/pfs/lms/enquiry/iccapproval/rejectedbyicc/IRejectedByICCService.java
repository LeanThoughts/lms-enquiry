package pfs.lms.enquiry.iccapproval.rejectedbyicc;

import java.util.UUID;

public interface IRejectedByICCService {

    RejectedByICC create(RejectedByICCResource rejectedByICCResource, String username);

    RejectedByICC update(RejectedByICCResource rejectedByICCResource, String username) throws CloneNotSupportedException;

    RejectedByICC delete(UUID rejectedByICCId, String username);
}
