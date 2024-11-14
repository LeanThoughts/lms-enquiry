package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;

import java.util.Optional;

public interface IdentificationCategoryRepository extends JpaRepository<IdentificationCategory, Long> {

    Optional<IdentificationCategory> findIdentificationCategoryByCode(String code);

}
