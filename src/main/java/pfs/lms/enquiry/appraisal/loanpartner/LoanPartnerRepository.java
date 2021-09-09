package pfs.lms.enquiry.appraisal.loanpartner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanPartnerRepository extends JpaRepository<LoanPartner, UUID> {

    List<LoanPartner> findByLoanApplicationIdOrderBySerialNumberDesc(UUID loanApplicationId);
}
