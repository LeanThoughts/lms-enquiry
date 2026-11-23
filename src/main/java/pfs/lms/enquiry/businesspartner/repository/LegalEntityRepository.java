package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface LegalEntityRepository extends JpaRepository<LegalEntity, String> {
@Override
    @PreAuthorize(READ)
    Optional<LegalEntity> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<LegalEntity> findAll();

    @Override
    @PreAuthorize(READ)
    Page<LegalEntity> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends LegalEntity> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(LegalEntity entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   LegalEntity findByCode(String code);
}
