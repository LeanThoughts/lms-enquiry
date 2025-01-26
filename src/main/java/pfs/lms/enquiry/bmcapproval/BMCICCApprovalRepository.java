package pfs.lms.enquiry.bmcapproval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface BMCICCApprovalRepository extends JpaRepository<BMCICCApproval, UUID> {

    @RestResource(exported = false)
    Optional<BMCICCApproval> findByLoanApplication(LoanApplication loanApplication);

    BMCICCApproval findByLoanApplicationId(UUID loanApplicationId);
 }
