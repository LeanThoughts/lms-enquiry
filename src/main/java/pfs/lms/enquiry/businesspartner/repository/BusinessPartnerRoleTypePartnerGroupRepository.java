package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleTypePartnerGroup;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface BusinessPartnerRoleTypePartnerGroupRepository extends JpaRepository<BusinessPartnerRoleTypePartnerGroup, String> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerRoleTypePartnerGroup> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerRoleTypePartnerGroup> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerRoleTypePartnerGroup> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerRoleTypePartnerGroup> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerRoleTypePartnerGroup entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   List<BusinessPartnerRoleTypePartnerGroup> findByRoleType(String roleType);
   @PreAuthorize(READ)
   BusinessPartnerRoleTypePartnerGroup findByRoleTypeAndPartnerGroup(String roleType, String partnerGroup);
}
