package pfs.lms.enquiry.documentation.legalcounsel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LegalCounselRepository extends JpaRepository<LegalCounsel, UUID> {

    List<LegalCounsel> findByDocumentationId(UUID documentationId);

    List<LegalCounsel> findByDocumentationIdAndDeleteFlag(UUID documentationId, Boolean deleteFlag);
}
