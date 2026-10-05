package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import pfs.lms.enquiry.bmcapproval.BmcIccApproval;
import pfs.lms.enquiry.boardapproval.BoardApproval;

import java.util.UUID;

public interface IBMCApprovalByICCService {

    BmcApprovalByIcc create(BMCApprovalByICCResource bmcApprovalByICCResource, String username);

    BmcApprovalByIcc update(BMCApprovalByICCResource bmcApprovalByICCResource, String username) throws CloneNotSupportedException;

    BmcApprovalByIcc delete(UUID bmcApprovalByICCId, String username);
//
//    BmcApprovalByIcc processApprovedICC(BmcIccApproval BMCICCApproval, String username) throws CloneNotSupportedException;
//
//     BmcApprovalByIcc processRejection(BmcApprovalByIcc bmcApprovalByIcc, String username) throws CloneNotSupportedException;

}
