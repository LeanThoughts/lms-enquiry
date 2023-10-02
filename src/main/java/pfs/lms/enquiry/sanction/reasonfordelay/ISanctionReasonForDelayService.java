package pfs.lms.enquiry.sanction.reasonfordelay;

import java.util.UUID;

public interface ISanctionReasonForDelayService {

    SanctionReasonForDelay createReasonForDelay(SanctionReasonForDelayResource sanctionReasonForDelayResource,
                                                String username);

    SanctionReasonForDelay updateReasonForDelay(SanctionReasonForDelayResource sanctionReasonForDelayResource,
                                                String username) throws CloneNotSupportedException;

    SanctionReasonForDelay deleteReasonForDelay(UUID sanctionReasonForDelayId,String username) throws CloneNotSupportedException;
}
