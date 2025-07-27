package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.Country;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {

    Country findByCountryCode(String countryCode);
}
