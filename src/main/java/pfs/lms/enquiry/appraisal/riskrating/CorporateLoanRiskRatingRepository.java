package pfs.lms.enquiry.appraisal.riskrating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CorporateLoanRiskRatingRepository extends JpaRepository<CorporateLoanRiskRating, UUID> {

    List<CorporateLoanRiskRating> findByLoanAppraisalId(UUID loanAppraisalId);
}
