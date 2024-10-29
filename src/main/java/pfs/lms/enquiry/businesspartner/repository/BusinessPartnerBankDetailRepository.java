package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;

import java.util.List;
import java.util.UUID;

public interface BusinessPartnerBankDetailRepository extends JpaRepository<BusinessPartnerBankDetail, UUID> {

    List<BusinessPartnerBankDetail> findByPartnerId(UUID partnerId);
}
