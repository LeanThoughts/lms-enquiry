package pfs.lms.enquiry.monitoring.cla;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CLAReportAndFeeRepository extends JpaRepository<CLAReportAndFee, String> {

    List<CLAReportAndFee> findByCommonLoanAgreement(CommonLoanAgreement commonLoanAgreement);
}
