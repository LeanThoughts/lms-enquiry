package pfs.lms.enquiry.appraisal.customerrejection;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRejectionRepository extends JpaRepository<CustomerRejection, UUID> {

    CustomerRejection findByLoanAppraisalId(UUID loanAppraisalId);
}
