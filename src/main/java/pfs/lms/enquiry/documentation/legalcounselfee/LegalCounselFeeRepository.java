package pfs.lms.enquiry.documentation.legalcounselfee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LegalCounselFeeRepository extends JpaRepository<LegalCounselFee, UUID> {

    List<LegalCounselFee> findByDocumentationId(UUID documentationId);

    List<LegalCounselFee> findByDocumentationIdAndDeleteFlag(UUID documentationId, Boolean deleteFlag);
}
