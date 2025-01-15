package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.PaymentForm;

public interface PaymentFormRepository extends JpaRepository<PaymentForm, Integer> {
    public PaymentForm findByCode(String code);
}
