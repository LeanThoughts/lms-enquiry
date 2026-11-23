package pfs.lms.enquiry.sanction.reasonfordelay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.READ;
import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.WRITE;

public interface SanctionReasonForDelayRepository extends JpaRepository<SanctionReasonForDelay, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<SanctionReasonForDelay> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<SanctionReasonForDelay> findAll();

    @Override
    @PreAuthorize(READ)
    Page<SanctionReasonForDelay> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends SanctionReasonForDelay> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(SanctionReasonForDelay entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<SanctionReasonForDelay> findBySanctionId(UUID sanctionId);
}