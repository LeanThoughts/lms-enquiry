package pfs.lms.enquiry.appraisal.knowyourcustomer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KnowYourCustomerRepository extends JpaRepository<KnowYourCustomer, UUID> {

    List<KnowYourCustomer> findByLoanAppraisalId(UUID loanAppraisalId);

    List<KnowYourCustomer> findByLoanAppraisalIdOrderBySerialNumberDesc(UUID loanAppraisalId);
}
