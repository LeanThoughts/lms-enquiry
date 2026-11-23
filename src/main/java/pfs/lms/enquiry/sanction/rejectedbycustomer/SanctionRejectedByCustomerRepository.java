package pfs.lms.enquiry.sanction.rejectedbycustomer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.READ;
import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.WRITE;

public interface SanctionRejectedByCustomerRepository extends JpaRepository<SanctionRejectedByCustomer, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<SanctionRejectedByCustomer> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<SanctionRejectedByCustomer> findAll();

    @Override
    @PreAuthorize(READ)
    Page<SanctionRejectedByCustomer> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends SanctionRejectedByCustomer> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(SanctionRejectedByCustomer entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<SanctionRejectedByCustomer> findBySanctionId(UUID sanctionId);
}