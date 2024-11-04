package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.IndustryType;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Long> {

    Title findByCode(String code);

}
