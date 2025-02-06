package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import java.util.UUID;

public interface IBMCApprovalByICCService {

    BmcApprovalByIcc create(BMCApprovalByICCResource bmcApprovalByICCResource, String username);

    BmcApprovalByIcc update(BMCApprovalByICCResource bmcApprovalByICCResource, String username) throws CloneNotSupportedException;

    BmcApprovalByIcc delete(UUID bmcApprovalByICCId, String username);
}
