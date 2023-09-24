package pfs.lms.enquiry.sanction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface SanctionRepository extends JpaRepository<Sanction, UUID> {

    @RestResource(exported = false)
    Optional<Sanction> findByLoanApplication(LoanApplication loanApplication);

    Sanction findByLoanApplicationId(UUID loanApplicationId);
}
