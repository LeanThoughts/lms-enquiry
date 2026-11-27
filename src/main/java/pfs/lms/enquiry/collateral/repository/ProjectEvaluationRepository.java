package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pfs.lms.enquiry.collateral.domain.ProjectEvaluation;

import java.util.List;
import java.util.UUID;

/**
 * Project (collateral) evaluations of a loan. Not exported by Spring Data REST: access goes through the role-checked
 * {@code ProjectEvaluationController} only.
 */
@RepositoryRestResource(exported = false)
public interface ProjectEvaluationRepository extends JpaRepository<ProjectEvaluation, UUID> {

    /** The evaluations of a loan, by line item number. */
    List<ProjectEvaluation> findByLoanApplication_IdOrderByItemNumberAsc(UUID loanApplicationId);

    long countByLoanApplication_Id(UUID loanApplicationId);
}
