package pfs.lms.enquiry.iccapproval.iccfurtherdetail;

import java.util.UUID;

public interface IICCFurtherDetailService {

    ICCFurtherDetail create(ICCFurtherDetailResource furtherDetailResource, String username);

    ICCFurtherDetail update(ICCFurtherDetailResource furtherDetailResource, String username) throws CloneNotSupportedException;

    ICCFurtherDetail delete(UUID furtherDetailId, String username);
}
