package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import java.util.UUID;

public interface IBMCICCFurtherDetailService {

    BmcICCFurtherDetail create(BMCICCFurtherDetailResource furtherDetailResource, String username);

    BmcICCFurtherDetail update(BMCICCFurtherDetailResource furtherDetailResource, String username) throws CloneNotSupportedException;

    BmcICCFurtherDetail delete(UUID furtherDetailId, String username);
}
