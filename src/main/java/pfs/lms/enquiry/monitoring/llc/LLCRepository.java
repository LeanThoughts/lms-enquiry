package pfs.lms.enquiry.monitoring.llc;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;

public interface LLCRepository extends JpaRepository<LendersLegalCouncil, String> {

    List<LendersLegalCouncil> findByLoanMonitor(LoanMonitor loanMonitor);

    List<LendersLegalCouncil> findByLoanMonitorAndBpCode(LoanMonitor loanMonitor, String bpCode);
}
