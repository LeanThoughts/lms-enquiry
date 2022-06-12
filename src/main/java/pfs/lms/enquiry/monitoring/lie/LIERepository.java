package pfs.lms.enquiry.monitoring.lie;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;

public interface LIERepository extends JpaRepository<LendersIndependentEngineer, String> {

    List<LendersIndependentEngineer> findByLoanMonitor(LoanMonitor loanMonitor);

    List<LendersIndependentEngineer> findByLoanMonitorAndBpCode(LoanMonitor loanMonitor, String bpCode);
}
