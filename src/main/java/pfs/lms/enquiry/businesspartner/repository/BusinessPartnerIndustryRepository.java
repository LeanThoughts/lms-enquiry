package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;

import java.util.UUID;

public interface BusinessPartnerIndustryRepository extends JpaRepository<BusinessPartnerIndustry, UUID> {


}
