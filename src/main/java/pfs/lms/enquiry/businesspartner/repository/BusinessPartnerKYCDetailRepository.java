package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerFinancial;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerKYCDetail;

import java.util.List;
import java.util.UUID;

public interface BusinessPartnerKYCDetailRepository extends JpaRepository<BusinessPartnerKYCDetail, UUID> {
    List<BusinessPartnerKYCDetail> findByPartnerId(UUID partnerId);
}
