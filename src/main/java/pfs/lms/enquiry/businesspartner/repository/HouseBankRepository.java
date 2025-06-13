package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.HouseBank;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;

public interface HouseBankRepository extends JpaRepository<HouseBank, String> {

   HouseBank findByHouseBankId(String code);
}
