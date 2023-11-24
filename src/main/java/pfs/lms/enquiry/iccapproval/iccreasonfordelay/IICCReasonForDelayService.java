package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

import java.util.UUID;

public interface IICCReasonForDelayService {

    ICCReasonForDelay create(ICCReasonForDelayResource furtherDetailResource, String username);

    ICCReasonForDelay update(ICCReasonForDelayResource furtherDetailResource, String username) throws CloneNotSupportedException;

    ICCReasonForDelay delete(UUID furtherDetailId, String username);
}
