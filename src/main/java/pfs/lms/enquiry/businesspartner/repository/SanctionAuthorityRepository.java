package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface SanctionAuthorityRepository extends JpaRepository<SanctionAuthority, String> {
@Override
    @PreAuthorize(READ)
    Optional<SanctionAuthority> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<SanctionAuthority> findAll();

    @Override
    @PreAuthorize(READ)
    Page<SanctionAuthority> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends SanctionAuthority> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(SanctionAuthority entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   SanctionAuthority findByCode(String code);
}
