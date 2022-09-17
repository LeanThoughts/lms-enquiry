package pfs.lms.enquiry.action;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface EnquiryActionRepository extends JpaRepository<EnquiryAction, UUID> {

    @RestResource(exported = false)
    Optional<EnquiryAction> findByLoanApplication(LoanApplication loanApplication);

    EnquiryAction findByLoanApplicationId(UUID loanApplicationId);
}
