package pfs.lms.enquiry.iccapproval.risknotification;

import java.util.UUID;

public interface IRiskNotificationService {

    RiskNotification create(RiskNotificationResource riskNotificationResource, String username);

    RiskNotification update(RiskNotificationResource riskNotificationResource, String username) throws CloneNotSupportedException;
    RiskNotification sendNotification(RiskNotificationResource riskNotificationResource, String username) throws Exception;

    RiskNotification delete(UUID riskNotificationId, String username);
}
