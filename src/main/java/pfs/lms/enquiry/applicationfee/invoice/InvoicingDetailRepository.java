package pfs.lms.enquiry.applicationfee.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.READ;
import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.WRITE;

public interface InvoicingDetailRepository extends JpaRepository<InvoicingDetail, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<InvoicingDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<InvoicingDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<InvoicingDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends InvoicingDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(InvoicingDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    InvoicingDetail findByApplicationFeeId(UUID applicationFeeId);
}