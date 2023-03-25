package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.DocumentationStatus;
import pfs.lms.enquiry.domain.DocumentationType;

import java.util.UUID;

public interface DocumentationStatusRepository extends JpaRepository<DocumentationStatus, UUID> {

    DocumentationStatus findByCode(String code);
}
