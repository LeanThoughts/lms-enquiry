package pfs.lms.enquiry.appraisal.reasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReasonForDelayRepository extends JpaRepository<ReasonForDelay, UUID> {

    ReasonForDelay findByLoanAppraisalId(UUID loanAppraisalId);
}
