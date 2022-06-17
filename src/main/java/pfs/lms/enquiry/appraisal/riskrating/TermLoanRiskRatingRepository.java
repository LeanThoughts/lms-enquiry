package pfs.lms.enquiry.appraisal.riskrating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TermLoanRiskRatingRepository extends JpaRepository<TermLoanRiskRating, UUID> {

    List<TermLoanRiskRating> findByLoanAppraisalId(UUID loanAppraisalId);
}
