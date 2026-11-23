package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;

public interface BusinessPartnerBankDetailRepository extends JpaRepository<BusinessPartnerBankDetail, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerBankDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerBankDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerBankDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerBankDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerBankDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);


    @PreAuthorize(READ)
    List<BusinessPartnerBankDetail> findByPartnerIdOrderByEntryDateDesc(UUID partnerId);

    @PreAuthorize(READ)
    List<BusinessPartnerBankDetail> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);
    
    @PreAuthorize(READ)
    Optional<BusinessPartnerBankDetail> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);

    @PreAuthorize(READ)
    BusinessPartnerBankDetail findByPartnerIdAndSerialNumber(UUID partnerId, Integer serialNo  );
}
