package pfs.lms.enquiry.monitoring.loanDocumentation;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPADetail;

import java.util.List;
import java.util.UUID;

public interface LoanDocumentationRepository extends JpaRepository<LoanDocumentation, UUID> {

    List<LoanDocumentation>  findByLoanMonitor(LoanMonitor loanMonitor);
}
