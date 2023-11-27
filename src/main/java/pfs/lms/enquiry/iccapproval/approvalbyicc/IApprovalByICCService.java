package pfs.lms.enquiry.iccapproval.approvalbyicc;

import java.util.UUID;

public interface IApprovalByICCService {

    ApprovalByICC create(ApprovalByICCResource approvalByICCResource, String username);

    ApprovalByICC update(ApprovalByICCResource approvalByICCResource, String username) throws CloneNotSupportedException;

    ApprovalByICC delete(UUID approvalByICCId, String username);
}
