package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import java.util.UUID;

public interface IBMCApprovalByICCService {

    BmcApprovalByICC create(BMCApprovalByICCResource bmcApprovalByICCResource, String username);

    BmcApprovalByICC update(BMCApprovalByICCResource bmcApprovalByICCResource, String username) throws CloneNotSupportedException;

    BmcApprovalByICC delete(UUID bmcApprovalByICCId, String username);
}
