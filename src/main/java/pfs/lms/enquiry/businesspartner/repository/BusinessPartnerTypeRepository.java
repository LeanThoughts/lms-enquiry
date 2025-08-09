package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;

public interface BusinessPartnerTypeRepository extends JpaRepository<BusinessPartnerType, String> {

   BusinessPartnerType findByCode(String code);
}
