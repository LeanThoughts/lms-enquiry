package pfs.lms.enquiry.bmcapproval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface BMCICCApprovalRepository extends JpaRepository<BmcICCApproval, UUID> {

    @RestResource(exported = false)
    Optional<BmcICCApproval> findByLoanApplication(LoanApplication loanApplication);

    BmcICCApproval findByLoanApplicationId(UUID loanApplicationId);
 }
