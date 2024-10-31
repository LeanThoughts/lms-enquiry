package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.IndustryType;

import java.util.List;

public interface IndustryTypeRepository extends JpaRepository<IndustryType, Long> {

    IndustryType findIndustryTypeByCode(String code);
    IndustryType findIndustryTypeByCodeAndIndustrySystem(String code, IndustrySystem industrySystem);
    List<IndustryType> findByIndustrySystemId(Long industrySystemId);
}
