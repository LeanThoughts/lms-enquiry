package pfs.lms.enquiry.sanction.paymentreceiptpresanction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentReceiptPreSanctionRepository extends JpaRepository<PaymentReceiptPreSanction, UUID> {

    List<PaymentReceiptPreSanction> findBySanctionId(UUID sanctionId);
}
