package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {
@Override
    @PreAuthorize(READ)
    Optional<CountryCode> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<CountryCode> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CountryCode> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CountryCode> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CountryCode entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    CountryCode findByCode(String code);

}
