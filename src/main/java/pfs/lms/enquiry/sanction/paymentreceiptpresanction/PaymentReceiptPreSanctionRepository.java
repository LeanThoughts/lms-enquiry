package pfs.lms.enquiry.sanction.paymentreceiptpresanction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.READ;
import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.WRITE;

public interface PaymentReceiptPreSanctionRepository extends JpaRepository<PaymentReceiptPreSanction, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<PaymentReceiptPreSanction> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<PaymentReceiptPreSanction> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PaymentReceiptPreSanction> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PaymentReceiptPreSanction> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PaymentReceiptPreSanction entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<PaymentReceiptPreSanction> findBySanctionId(UUID sanctionId);
}