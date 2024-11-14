package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;

public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {

    CountryCode findByCode(String code);

}
