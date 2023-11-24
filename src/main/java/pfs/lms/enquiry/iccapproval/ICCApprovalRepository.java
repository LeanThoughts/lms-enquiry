package pfs.lms.enquiry.iccapproval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface ICCApprovalRepository extends JpaRepository<ICCApproval, UUID> {

    @RestResource(exported = false)
    Optional<ICCApproval> findByLoanApplication(LoanApplication loanApplication);

    ICCApproval findByLoanApplicationId(UUID loanApplicationId);
 }
