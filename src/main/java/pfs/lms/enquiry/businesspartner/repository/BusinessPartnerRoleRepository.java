package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = BusinessPartnerRoleProjection.class)
public interface BusinessPartnerRoleRepository extends JpaRepository<BusinessPartnerRole, UUID> {
    List<BusinessPartnerRole> findByPartnerId(UUID partnerId);
    BusinessPartnerRole findByPartnerIdAndRoleTypeId(UUID partnerId, Long roleTypeId);
}