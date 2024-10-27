package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;

public interface BusinessPartnerRoleRepository extends JpaRepository<BusinessPartnerRole, Long> {

    BusinessPartnerRole findBusinessPartnerRoleByCode(String code);

}
