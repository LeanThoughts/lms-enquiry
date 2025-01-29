package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import java.util.UUID;

public interface IBMCICCReasonForDelayService {

    BmcICCReasonForDelay create(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username);

    BmcICCReasonForDelay update(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username) throws CloneNotSupportedException;

    BmcICCReasonForDelay delete(UUID furtherDetailId, String username);
}
