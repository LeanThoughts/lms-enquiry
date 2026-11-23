package pfs.lms.enquiry.sanction.paymentreceiptpostsanction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.READ;
import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.WRITE;

public interface PaymentReceiptPostSanctionRepository extends JpaRepository<PaymentReceiptPostSanction, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<PaymentReceiptPostSanction> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<PaymentReceiptPostSanction> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PaymentReceiptPostSanction> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PaymentReceiptPostSanction> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PaymentReceiptPostSanction entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<PaymentReceiptPostSanction> findBySanctionId(UUID sanctionId);
}