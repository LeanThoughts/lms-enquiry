package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;

public interface BusinessPartnerIndustryRepository extends JpaRepository<BusinessPartnerIndustry, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerIndustry> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerIndustry> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerIndustry> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerIndustry> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerIndustry entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);


    @PreAuthorize(READ)
    List<BusinessPartnerIndustry> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);

    @PreAuthorize(READ)
    Optional<BusinessPartnerIndustry> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);

    @PreAuthorize(READ)
    List<BusinessPartnerIndustry> findByPartnerIdAndIndustrySystemId(UUID partnerId, Long industrySystemId);
}
