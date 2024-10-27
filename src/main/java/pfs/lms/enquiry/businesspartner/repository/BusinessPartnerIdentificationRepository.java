package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;

import java.util.UUID;

public interface BusinessPartnerIdentificationRepository extends JpaRepository<BusinessPartnerIdentification, UUID> {


}
