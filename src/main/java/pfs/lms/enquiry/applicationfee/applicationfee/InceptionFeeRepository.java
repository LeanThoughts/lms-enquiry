package pfs.lms.enquiry.applicationfee.applicationfee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.READ;
import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.WRITE;

public interface InceptionFeeRepository extends JpaRepository<InceptionFee, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<InceptionFee> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<InceptionFee> findAll();

    @Override
    @PreAuthorize(READ)
    Page<InceptionFee> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends InceptionFee> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(InceptionFee entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<InceptionFee> findByApplicationFeeId(UUID applicationFeeId);

    @PreAuthorize(READ)
    List<InceptionFee> findByInvoiceNumber(String invoiceNumber);

    @PreAuthorize(READ)
    List<InceptionFee> findByHeaderDocumentNumber(String headerDocumentNumber);

    @PreAuthorize(READ)
    List<InceptionFee> findBySapFIDocumentNumberFee(String fiDocumentNumber);
}