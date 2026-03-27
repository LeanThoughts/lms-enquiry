package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;

public interface IndustrySystemRepository extends JpaRepository<IndustrySystem, Long> {

    IndustrySystem findIndustrySystemByCode(String code);


}
