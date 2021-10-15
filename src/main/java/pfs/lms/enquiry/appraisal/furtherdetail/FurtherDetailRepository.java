package pfs.lms.enquiry.appraisal.furtherdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FurtherDetailRepository extends JpaRepository<FurtherDetail, UUID> {

    Optional<FurtherDetail> findByLoanAppraisalId(UUID loanAppraisalId);
}
