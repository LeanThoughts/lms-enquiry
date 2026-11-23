package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface ICCReasonForDelayRepository extends JpaRepository<ICCReasonForDelay, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ICCReasonForDelay> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ICCReasonForDelay> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ICCReasonForDelay> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ICCReasonForDelay> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ICCReasonForDelay entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<ICCReasonForDelay> findByIccApprovalId(UUID iccApprovalId);
}