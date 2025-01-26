package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import java.util.UUID;

public interface IBMCICCReasonForDelayService {

    BMCICCReasonForDelay create(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username);

    BMCICCReasonForDelay update(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username) throws CloneNotSupportedException;

    BMCICCReasonForDelay delete(UUID furtherDetailId, String username);
}
