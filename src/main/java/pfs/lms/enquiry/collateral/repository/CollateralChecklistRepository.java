package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;

import java.util.Optional;
import java.util.UUID;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralChecklistRepository extends JpaRepository<CollateralChecklist, UUID> {

    Optional<CollateralChecklist> findByLoanApplication_Id(UUID loanApplicationId);
}
