package pfs.lms.enquiry.appraisal.furtherdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import pfs.lms.enquiry.appraisal.loanappraisalkyc.LoanAppraisalKYC;

import java.util.List;
import java.util.UUID;

public interface FurtherDetailSiteVisitRepository extends JpaRepository<LoanAppraisalKYC, UUID> {

    @RestResource(exported = false)
    List<LoanAppraisalKYC> findByLoanAppraisalId(UUID loanAppraisalId);

    List<LoanAppraisalKYC> findByLoanAppraisalLoanApplicationIdOrderBySerialNumberDesc(UUID loanApplicationId);
}
