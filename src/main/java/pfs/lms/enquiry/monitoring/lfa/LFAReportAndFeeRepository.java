package pfs.lms.enquiry.monitoring.lfa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LFAReportAndFeeRepository extends JpaRepository<LFAReportAndFee, String> {

    List<LFAReportAndFee> findByLendersFinancialAdvisor(LendersFinancialAdvisor lendersFinancialAdvisor);

    List<LFAReportAndFee> findByLendersFinancialAdvisorOrderBySerialNumberDesc(LendersFinancialAdvisor lendersFinancialAdvisor);
}
