package pfs.lms.enquiry.documentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface DocumentationRepository extends JpaRepository<Documentation, UUID> {

    @RestResource(exported = false)
    Optional<Documentation> findByLoanApplication(LoanApplication loanApplication);

    Documentation findByLoanApplicationId(UUID loanApplicationId);
}
