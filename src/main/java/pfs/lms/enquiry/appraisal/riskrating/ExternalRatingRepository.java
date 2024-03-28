package pfs.lms.enquiry.appraisal.riskrating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExternalRatingRepository extends JpaRepository<ExternalRating, UUID> {

    List<ExternalRating> findByLoanAppraisalIdOrderBySerialNumber(UUID loanAppraisalId);
}
