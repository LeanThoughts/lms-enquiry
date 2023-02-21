package pfs.lms.enquiry.monitoring.cla;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;

public interface CLARepository extends JpaRepository<CommonLoanAgreement, String> {

    List<CommonLoanAgreement> findByLoanMonitor(LoanMonitor loanMonitor);

    List<CommonLoanAgreement> findByLoanMonitorAndBpCode(LoanMonitor loanMonitor, String bpCode);
}
