package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleTypePartnerGroup;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;

import java.util.List;

public interface BusinessPartnerRoleTypePartnerGroupRepository extends JpaRepository<BusinessPartnerRoleTypePartnerGroup, String> {

   List<BusinessPartnerRoleTypePartnerGroup> findByRoleType(String roleType);
   BusinessPartnerRoleTypePartnerGroup findByRoleTypeAndPartnerGroup(String roleType, String partnerGroup);
}
