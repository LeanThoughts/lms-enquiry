package pfs.lms.enquiry.monitoring.lia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LIAReportAndFeeRepository extends JpaRepository<LIAReportAndFee, String> {

    List<LIAReportAndFee> findByLendersInsuranceAdvisor(LendersInsuranceAdvisor lendersInsuranceAdvisor);
}
