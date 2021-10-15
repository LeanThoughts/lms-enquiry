package pfs.lms.enquiry.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.domain.SiteVisit;

import java.util.List;
import java.util.UUID;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, String> {

    List<SiteVisit> findByLoanMonitor(LoanMonitor loanMonitor);

    List<SiteVisit> findByLoanMonitorLoanApplicationIdAndSiteVisitType(UUID loanApplicationId, String siteVisitType);
}
