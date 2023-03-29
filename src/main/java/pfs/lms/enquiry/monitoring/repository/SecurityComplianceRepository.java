package pfs.lms.enquiry.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.domain.SecurityCompliance;

import java.util.List;

public interface SecurityComplianceRepository extends JpaRepository<SecurityCompliance, String> {

    List<SecurityCompliance> findByLoanMonitor(LoanMonitor loanMonitor);

    List<SecurityCompliance> findByLoanMonitorOrderBySerialNumberDesc(LoanMonitor loanMonitor);
}
