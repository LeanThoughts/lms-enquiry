package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import java.util.UUID;

public interface IBMCApprovalByICCService {

    BMCApprovalByICC create(BMCApprovalByICCResource bmcApprovalByICCResource, String username);

    BMCApprovalByICC update(BMCApprovalByICCResource bmcApprovalByICCResource, String username) throws CloneNotSupportedException;

    BMCApprovalByICC delete(UUID bmcApprovalByICCId, String username);
}
