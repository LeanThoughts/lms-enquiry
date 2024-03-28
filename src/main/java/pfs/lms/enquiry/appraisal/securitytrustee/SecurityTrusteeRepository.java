package pfs.lms.enquiry.appraisal.securitytrustee;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.appraisal.LoanAppraisal;

import java.util.List;

public interface SecurityTrusteeRepository extends JpaRepository<SecurityTrustee, String> {
    List<SecurityTrustee> findByLoanAppraisal(LoanAppraisal loanAppraisal);
}
