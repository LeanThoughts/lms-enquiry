package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessPartnerBankDetailRepository extends JpaRepository<BusinessPartnerBankDetail, UUID> {

    List<BusinessPartnerBankDetail> findByPartnerIdOrderByEntryDateDesc(UUID partnerId);

    List<BusinessPartnerBankDetail> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);
    
    Optional<BusinessPartnerBankDetail> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);
}
