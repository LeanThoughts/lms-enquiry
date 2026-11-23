package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface BusinessPartnerTypeRepository extends JpaRepository<BusinessPartnerType, String> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerType> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerType> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerType> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerType> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerType entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   BusinessPartnerType findByCode(String code);
}
