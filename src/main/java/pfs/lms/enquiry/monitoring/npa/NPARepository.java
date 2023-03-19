package pfs.lms.enquiry.monitoring.npa;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.UUID;

public interface NPARepository extends JpaRepository<NPA, UUID> {

    NPA findByLoanMonitor(LoanMonitor loanMonitor);
}
