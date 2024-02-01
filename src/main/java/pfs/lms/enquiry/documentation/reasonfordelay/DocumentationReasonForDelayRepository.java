package pfs.lms.enquiry.documentation.reasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentationReasonForDelayRepository extends JpaRepository<DocumentationReasonForDelay, UUID> {

    List<DocumentationReasonForDelay> findByDocumentationIdAndDeleteFlag(UUID documentationId, Boolean deleteFlag);
}
