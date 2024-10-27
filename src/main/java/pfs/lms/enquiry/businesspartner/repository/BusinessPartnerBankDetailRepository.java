package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;

import java.util.UUID;

public interface BusinessPartnerBankDetailRepository extends JpaRepository<BusinessPartnerBankDetail, UUID> {


}
