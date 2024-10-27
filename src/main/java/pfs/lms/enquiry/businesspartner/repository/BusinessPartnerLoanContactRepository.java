package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;

import java.util.UUID;

public interface BusinessPartnerLoanContactRepository extends JpaRepository<BusinessPartnerLoanContact, UUID> {


}
