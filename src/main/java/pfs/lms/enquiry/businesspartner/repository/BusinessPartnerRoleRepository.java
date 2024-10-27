package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;

public interface BusinessPartnerRoleRepository extends JpaRepository<BusinessPartnerRoleType, Long> {

    BusinessPartnerRoleType findBusinessPartnerRoleByCode(String code);

}
