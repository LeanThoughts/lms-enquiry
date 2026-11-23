package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;

public interface IdentificationCategoryRepository extends JpaRepository<IdentificationCategory, Long> {
@Override
    @PreAuthorize(READ)
    Optional<IdentificationCategory> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<IdentificationCategory> findAll();

    @Override
    @PreAuthorize(READ)
    Page<IdentificationCategory> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends IdentificationCategory> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(IdentificationCategory entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    Optional<IdentificationCategory> findByCode(String code);
    //IdentificationCategory findByCode(String code);

}
