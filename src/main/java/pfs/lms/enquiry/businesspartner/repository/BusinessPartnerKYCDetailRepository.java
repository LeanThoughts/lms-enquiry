package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerKYCDetail;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface BusinessPartnerKYCDetailRepository extends JpaRepository<BusinessPartnerKYCDetail, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerKYCDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerKYCDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerKYCDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerKYCDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerKYCDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BusinessPartnerKYCDetail> findByPartnerId(UUID partnerId);
}
