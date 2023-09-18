package pfs.lms.enquiry.boardapproval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface BoardApprovalRepository extends JpaRepository<BoardApproval, UUID> {

    @RestResource(exported = false)
    Optional<BoardApproval> findByLoanApplication(LoanApplication loanApplication);

    BoardApproval findByLoanApplicationId(UUID loanApplicationId);
}
