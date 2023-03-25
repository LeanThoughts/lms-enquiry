package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.DocumentationType;
import pfs.lms.enquiry.domain.FinancingType;

import java.util.UUID;

public interface DocumentationTypeRepository extends JpaRepository<DocumentationType, UUID> {

    DocumentationType findByCode(String code);
}
