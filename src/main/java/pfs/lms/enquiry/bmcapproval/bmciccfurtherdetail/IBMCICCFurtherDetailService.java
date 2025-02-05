package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import java.util.UUID;

public interface IBMCICCFurtherDetailService {

    BmcIccFurtherDetail create(BMCICCFurtherDetailResource furtherDetailResource, String username);

    BmcIccFurtherDetail update(BMCICCFurtherDetailResource furtherDetailResource, String username) throws CloneNotSupportedException;

    BmcIccFurtherDetail delete(UUID furtherDetailId, String username);
}
