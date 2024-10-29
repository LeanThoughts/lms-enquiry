package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessPartnerIndustryRepository extends JpaRepository<BusinessPartnerIndustry, UUID> {

    List<BusinessPartnerIndustry> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);

    Optional<BusinessPartnerIndustry> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);
}
