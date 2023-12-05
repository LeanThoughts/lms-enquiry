package pfs.lms.enquiry.applicationfee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationFeeRepository extends JpaRepository<ApplicationFee, UUID> {

    @RestResource(exported = false)
    Optional<ApplicationFee> findByLoanApplication(LoanApplication loanApplication);

    ApplicationFee findByLoanApplicationId(UUID loanApplicationId);
 }
