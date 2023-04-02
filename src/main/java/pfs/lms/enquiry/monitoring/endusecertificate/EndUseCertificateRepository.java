package pfs.lms.enquiry.monitoring.endusecertificate;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import java.util.List;
import java.util.UUID;

public interface EndUseCertificateRepository extends JpaRepository<EndUseCertificate, UUID> {

    List<EndUseCertificate> findByLoanMonitor(LoanMonitor loanMonitor);

    List<EndUseCertificate> findByLoanMonitorOrderBySerialNumberDesc(LoanMonitor loanMonitor);
}
