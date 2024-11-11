package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

 DocumentType findByCode(String code);
 List<DocumentType> findAll();

}
