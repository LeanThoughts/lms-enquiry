package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface SortKeyRepository extends JpaRepository<SortKey, Long> {
@Override
    @PreAuthorize(READ)
    Optional<SortKey> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<SortKey> findAll();

    @Override
    @PreAuthorize(READ)
    Page<SortKey> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends SortKey> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(SortKey entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    SortKey findById(String id);

}
