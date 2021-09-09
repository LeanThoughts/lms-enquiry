package pfs.lms.enquiry.appraisal.loanappraisalkyc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

public interface LoanAppraisalKYCRepository extends JpaRepository<LoanAppraisalKYC, UUID> {

    @RestResource(exported = false)
    List<LoanAppraisalKYC> findByLoanAppraisalId(UUID loanAppraisalId);

    List<LoanAppraisalKYC> findByLoanAppraisalLoanApplicationIdOrderBySerialNumberDesc(UUID loanApplicationId);
}
