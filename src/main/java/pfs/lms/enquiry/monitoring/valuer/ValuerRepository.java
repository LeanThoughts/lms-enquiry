package pfs.lms.enquiry.monitoring.valuer;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;

public interface ValuerRepository extends JpaRepository<Valuer, String> {

    List<Valuer> findByLoanMonitor(LoanMonitor loanMonitor);

    List<Valuer> findByLoanMonitorAndBpCode(LoanMonitor loanMonitor, String bpCode);
}
