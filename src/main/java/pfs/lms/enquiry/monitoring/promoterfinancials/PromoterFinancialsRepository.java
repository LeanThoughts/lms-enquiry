package pfs.lms.enquiry.monitoring.promoterfinancials;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;

public interface PromoterFinancialsRepository extends JpaRepository<PromoterFinancials, String> {

    List<PromoterFinancials> findByLoanMonitor(LoanMonitor loanMonitor);
    List<PromoterFinancials> findByLoanMonitorOrderBySerialNumberDesc(LoanMonitor loanMonitor);
}
