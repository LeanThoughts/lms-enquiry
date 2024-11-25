package pfs.lms.enquiry.appraisal.knowyourcustomer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KnowYourCustomerRepository extends JpaRepository<KnowYourCustomer, UUID> {

    List<KnowYourCustomer> findByLoanPartnerId(String loanPartnerId);
    KnowYourCustomer findByDocumentType(String documentType);
    KnowYourCustomer findByLoanPartnerIdAndDocumentType(String loanPartnerId, String documentType);
}
