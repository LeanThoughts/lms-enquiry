package pfs.lms.enquiry.sanction.sanctionletter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.READ;
import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.WRITE;

public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<SanctionLetter> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<SanctionLetter> findAll();

    @Override
    @PreAuthorize(READ)
    Page<SanctionLetter> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends SanctionLetter> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(SanctionLetter entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<SanctionLetter> findBySanctionId(UUID sanctionId);

    @PreAuthorize(READ)
    List<SanctionLetter> findByPostedInSAP(Integer postedInSAP);
}