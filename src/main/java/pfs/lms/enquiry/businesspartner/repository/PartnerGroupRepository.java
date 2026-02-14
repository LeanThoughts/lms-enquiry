package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.domain.PartnerGroup;

public interface PartnerGroupRepository extends JpaRepository<PartnerGroup, String> {

   PartnerGroup findByCode(String code);
}
