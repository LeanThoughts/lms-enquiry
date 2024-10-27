package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;

public interface IdentificationCategoryRepository extends JpaRepository<IdentificationCategory, Long> {

    IdentificationCategory findIdentificationCategoryByCode(String code);

}
