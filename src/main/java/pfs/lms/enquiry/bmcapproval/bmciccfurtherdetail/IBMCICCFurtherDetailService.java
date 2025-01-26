package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import java.util.UUID;

public interface IBMCICCFurtherDetailService {

    BMCICCFurtherDetail create(BMCICCFurtherDetailResource furtherDetailResource, String username);

    BMCICCFurtherDetail update(BMCICCFurtherDetailResource furtherDetailResource, String username) throws CloneNotSupportedException;

    BMCICCFurtherDetail delete(UUID furtherDetailId, String username);
}
