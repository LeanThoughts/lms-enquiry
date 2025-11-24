package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.PaymentMethod;
import pfs.lms.enquiry.businesspartner.domain.SortKey;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    SortKey findById(String id);

}
