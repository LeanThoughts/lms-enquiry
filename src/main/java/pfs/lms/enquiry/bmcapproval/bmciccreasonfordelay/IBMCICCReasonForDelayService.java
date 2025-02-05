package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import java.util.UUID;

public interface IBMCICCReasonForDelayService {

    BmcIccReasonForDelay create(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username);

    BmcIccReasonForDelay update(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username) throws CloneNotSupportedException;

    BmcIccReasonForDelay delete(UUID furtherDetailId, String username);
}
