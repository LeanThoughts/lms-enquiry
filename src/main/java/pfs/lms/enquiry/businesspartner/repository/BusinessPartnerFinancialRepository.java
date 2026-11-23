package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerFinancial;
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

public interface BusinessPartnerFinancialRepository extends JpaRepository<BusinessPartnerFinancial, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerFinancial> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerFinancial> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerFinancial> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerFinancial> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerFinancial entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);


    @PreAuthorize(READ)
    List<BusinessPartnerFinancial> findByPartnerIdOrderByFiscalYearAsc(UUID partnerId);
    @PreAuthorize(READ)
    BusinessPartnerFinancial findByPartnerIdAndFiscalYear(UUID partnerId, String fiscalYear);
}
