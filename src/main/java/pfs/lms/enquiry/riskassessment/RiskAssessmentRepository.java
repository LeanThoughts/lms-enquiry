package pfs.lms.enquiry.riskassessment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.Optional;
import java.util.UUID;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, UUID> {

    @RestResource(exported = false)
    Optional<RiskAssessment> findByLoanApplication(LoanApplication loanApplication);

    RiskAssessment findByLoanApplicationId(UUID loanApplicationId);
 }
