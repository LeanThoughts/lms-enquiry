package pfs.lms.enquiry.sanction.paymentreceiptpostsanction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentReceiptPostSanctionRepository extends JpaRepository<PaymentReceiptPostSanction, UUID> {

    List<PaymentReceiptPostSanction> findBySanctionId(UUID sanctionId);
}
