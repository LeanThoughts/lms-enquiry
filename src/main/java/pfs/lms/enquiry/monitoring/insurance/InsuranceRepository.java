package pfs.lms.enquiry.monitoring.insurance;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;
import java.util.UUID;

public interface InsuranceRepository extends JpaRepository<Insurance, UUID> {

    List<Insurance> findByLoanMonitor(LoanMonitor loanMonitor);
}
