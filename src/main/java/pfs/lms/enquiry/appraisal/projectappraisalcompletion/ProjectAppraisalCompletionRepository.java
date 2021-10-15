package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectAppraisalCompletionRepository extends JpaRepository<ProjectAppraisalCompletion, UUID> {

    ProjectAppraisalCompletion findByLoanAppraisalId(UUID loanAppraisalId);
}
