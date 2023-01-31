package pfs.lms.enquiry.monitoring.lia;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;

public interface LIARepository extends JpaRepository<LendersInsuranceAdvisor, String> {

    List<LendersInsuranceAdvisor> findByLoanMonitor(LoanMonitor loanMonitor);

    List<LendersInsuranceAdvisor> findByLoanMonitorAndBpCode(LoanMonitor loanMonitor, String bpCode);
}
