package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;

public interface SanctionAuthorityRepository extends JpaRepository<SanctionAuthority, String> {

   SanctionAuthority findByCode(String code);
}
