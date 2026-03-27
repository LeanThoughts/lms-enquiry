package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.Country;
import pfs.lms.enquiry.domain.Region;

import java.util.List;
import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {

    Region findById(Integer id);
    List<Region> findByCountryCode(String countryCode);
    Region findByCountryCodeAnAndRegionCode(String countryCode, String regionCode);


}
