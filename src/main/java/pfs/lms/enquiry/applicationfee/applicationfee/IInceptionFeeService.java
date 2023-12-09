package pfs.lms.enquiry.applicationfee.applicationfee;

import java.util.UUID;

public interface IInceptionFeeService {

    InceptionFee create(InceptionFeeResource inceptionFeeResource, String username);

    InceptionFee update(InceptionFeeResource inceptionFeeResource, String username) throws CloneNotSupportedException;

    InceptionFee delete(UUID inceptionFeeId, String username);
}
