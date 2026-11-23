package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface BusinessPartnerRoleTypeRepository extends JpaRepository<BusinessPartnerRoleType, Long> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerRoleType> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerRoleType> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerRoleType> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerRoleType> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerRoleType entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    BusinessPartnerRoleType findBusinessPartnerRoleByCode(String code);

}
