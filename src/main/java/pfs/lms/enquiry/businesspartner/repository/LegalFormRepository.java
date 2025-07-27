package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;

public interface LegalFormRepository extends JpaRepository<LegalForm, String> {

   LegalForm findByCode(String code);
}
