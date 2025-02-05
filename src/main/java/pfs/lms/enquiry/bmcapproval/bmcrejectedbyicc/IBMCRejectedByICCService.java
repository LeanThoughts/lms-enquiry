package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

import java.util.UUID;

public interface IBMCRejectedByICCService {

    BmcRejectedByIcc create(BMCRejectedByICCResource bmcRejectedByICCResource, String username);

    BmcRejectedByIcc update(BMCRejectedByICCResource bmcRejectedByICCResource, String username) throws CloneNotSupportedException;

    BmcRejectedByIcc delete(UUID rejectedByICCId, String username);
}
