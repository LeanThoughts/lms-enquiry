package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;

public interface BusinessPartnerIdentificationRepository extends JpaRepository<BusinessPartnerIdentification, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerIdentification> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerIdentification> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerIdentification> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerIdentification> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerIdentification entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);


    @PreAuthorize(READ)
    List<BusinessPartnerIdentification> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);

    @PreAuthorize(READ)
    Optional<BusinessPartnerIdentification> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);

//    List<BusinessPartnerIdentification> findByIdentificationCategoryId(Long identificationCategoryId);
    @PreAuthorize(READ)
    List<BusinessPartnerIdentification> findByIdentificationCategoryCode(String code);

//    List<BusinessPartnerIdentification> findByPartnerIdAndIdentificationCategoryIdAndIdentificationNumber(UUID partnerId, Long identificationCategory, String identificationNUmber);
    @PreAuthorize(READ)
    List<BusinessPartnerIdentification> findByPartnerIdAndIdentificationCategoryCodeAndIdentificationNumber(UUID partnerId, String code, String identificationNUmber);

    @PreAuthorize(READ)
    List<BusinessPartnerIdentification> findByIdentificationCategoryCodeAndIdentificationNumber(String identificationCategoryCode, String idNumber);

}
