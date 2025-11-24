package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.PaymentTerms;
import pfs.lms.enquiry.businesspartner.domain.SortKey;

public interface PaymentTermsRepository extends JpaRepository<PaymentTerms, Long> {

    PaymentTerms findById(String id);

}
