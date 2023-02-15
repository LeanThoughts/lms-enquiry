package pfs.lms.enquiry.monitoring.llc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LLCReportAndFeeRepository extends JpaRepository<LLCReportAndFee, String> {

    List<LLCReportAndFee> findByLendersLegalCouncil(LendersLegalCouncil lendersLegalCouncil);
}
