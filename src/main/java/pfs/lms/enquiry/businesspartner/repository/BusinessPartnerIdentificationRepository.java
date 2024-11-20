package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessPartnerIdentificationRepository extends JpaRepository<BusinessPartnerIdentification, UUID> {

    List<BusinessPartnerIdentification> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);

    Optional<BusinessPartnerIdentification> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);

//    List<BusinessPartnerIdentification> findByIdentificationCategoryId(Long identificationCategoryId);
    List<BusinessPartnerIdentification> findByIdentificationCategoryCode(String code);

//    List<BusinessPartnerIdentification> findByPartnerIdAndIdentificationCategoryIdAndIdentificationNumber(UUID partnerId, Long identificationCategory, String identificationNUmber);
    List<BusinessPartnerIdentification> findByPartnerIdAndIdentificationCategoryCodeAndIdentificationNumber(UUID partnerId, String code, String identificationNUmber);


}
