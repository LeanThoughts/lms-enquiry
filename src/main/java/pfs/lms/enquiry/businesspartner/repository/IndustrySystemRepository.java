package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface IndustrySystemRepository extends JpaRepository<IndustrySystem, Long> {
@Override
    @PreAuthorize(READ)
    Optional<IndustrySystem> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<IndustrySystem> findAll();

    @Override
    @PreAuthorize(READ)
    Page<IndustrySystem> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends IndustrySystem> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(IndustrySystem entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    IndustrySystem findIndustrySystemByCode(String code);

}
