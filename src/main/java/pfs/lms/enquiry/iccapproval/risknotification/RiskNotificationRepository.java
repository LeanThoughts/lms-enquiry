package pfs.lms.enquiry.iccapproval.risknotification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RiskNotificationRepository extends JpaRepository<RiskNotification, UUID> {

    List<RiskNotification> findByIccApprovalId(UUID iccApprovalId);
}
