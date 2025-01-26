package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

import java.util.UUID;

public interface IBMCRejectedByICCService {

    BMCRejectedByICC create(BMCRejectedByICCResource bmcRejectedByICCResource, String username);

    BMCRejectedByICC update(BMCRejectedByICCResource bmcRejectedByICCResource, String username) throws CloneNotSupportedException;

    BMCRejectedByICC delete(UUID rejectedByICCId, String username);
}
