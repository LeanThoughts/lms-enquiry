package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;

public interface LegalEntityRepository extends JpaRepository<LegalEntity, String> {

   LegalEntity findByCode(String code);
}
