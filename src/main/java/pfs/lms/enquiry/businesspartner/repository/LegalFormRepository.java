package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface LegalFormRepository extends JpaRepository<LegalForm, String> {
@Override
    @PreAuthorize(READ)
    Optional<LegalForm> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<LegalForm> findAll();

    @Override
    @PreAuthorize(READ)
    Page<LegalForm> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends LegalForm> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(LegalForm entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   LegalForm findByCode(String code);
}
