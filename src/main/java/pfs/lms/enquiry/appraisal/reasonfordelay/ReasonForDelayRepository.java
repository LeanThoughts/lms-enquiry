package pfs.lms.enquiry.appraisal.reasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReasonForDelayRepository extends JpaRepository<ReasonForDelay, UUID> {

    List<ReasonForDelay> findByLoanAppraisalId(UUID loanAppraisalId);
}
