package pfs.lms.enquiry.monitoring.promoterdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;
import java.util.UUID;

public interface PromoterDetailItemRepository extends JpaRepository<PromoterDetailItem, UUID> {

    List<PromoterDetailItem> findByLoanMonitorOrderBySerialNumberDesc(LoanMonitor loanMonitor);
}
