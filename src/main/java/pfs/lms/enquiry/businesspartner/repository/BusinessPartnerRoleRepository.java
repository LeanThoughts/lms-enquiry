package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;

@RepositoryRestResource(excerptProjection = BusinessPartnerRoleProjection.class)
public interface BusinessPartnerRoleRepository extends JpaRepository<BusinessPartnerRole, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerRole> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerRole> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerRole> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerRole> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerRole entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BusinessPartnerRole> findByPartnerId(UUID partnerId);
    @PreAuthorize(READ)
    BusinessPartnerRole findByPartnerIdAndRoleTypeId(UUID partnerId, Long roleTypeId);
}
