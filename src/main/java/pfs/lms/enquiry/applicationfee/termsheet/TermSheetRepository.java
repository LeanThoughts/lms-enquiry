package pfs.lms.enquiry.applicationfee.termsheet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.READ;
import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.WRITE;

public interface TermSheetRepository extends JpaRepository<TermSheet, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<TermSheet> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<TermSheet> findAll();

    @Override
    @PreAuthorize(READ)
    Page<TermSheet> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends TermSheet> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(TermSheet entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<TermSheet> findByApplicationFeeIdOrderBySerialNumber(UUID applicationFeeId);

    @PreAuthorize(READ)
    List<TermSheet> findByApplicationFeeIdAndStatus(UUID applicationFeeId, String status);
}