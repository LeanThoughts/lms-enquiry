package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessPartnerLoanContactRepository extends JpaRepository<BusinessPartnerLoanContact, UUID> {

    List<BusinessPartnerLoanContact> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);
    
    Optional<BusinessPartnerLoanContact> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);

    List<BusinessPartnerLoanContact> findByLoanNumber(String loanNumber);
}
