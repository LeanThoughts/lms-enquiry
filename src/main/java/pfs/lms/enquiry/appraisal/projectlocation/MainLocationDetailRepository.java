package pfs.lms.enquiry.appraisal.projectlocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MainLocationDetailRepository extends JpaRepository<MainLocationDetail, UUID> {

    Optional<MainLocationDetail> findByLoanAppraisalId(UUID loanAppraisalId);
}
