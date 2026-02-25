package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerFinancial;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessPartnerFinancialRepository extends JpaRepository<BusinessPartnerFinancial, UUID> {

    List<BusinessPartnerFinancial> findByPartnerIdOrderByFiscalYearAsc(UUID partnerId);
    BusinessPartnerFinancial findByPartnerIdAndFiscalYear(UUID partnerId, String fiscalYear);
}
