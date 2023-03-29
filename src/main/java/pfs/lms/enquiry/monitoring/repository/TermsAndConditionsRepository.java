package pfs.lms.enquiry.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.domain.TermsAndConditionsModification;

import java.util.List;

public interface TermsAndConditionsRepository extends JpaRepository<TermsAndConditionsModification, String> {

    List<TermsAndConditionsModification> findByLoanMonitor(LoanMonitor loanMonitor);

    List<TermsAndConditionsModification> findByLoanMonitorOrderBySerialNumberDesc(LoanMonitor loanMonitor);
}
