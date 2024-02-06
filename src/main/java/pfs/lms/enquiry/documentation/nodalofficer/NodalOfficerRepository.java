package pfs.lms.enquiry.documentation.nodalofficer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NodalOfficerRepository extends JpaRepository<NodalOfficer, UUID> {

    List<NodalOfficer> findByDocumentationId(UUID documentationId);

    List<NodalOfficer> findByDocumentationIdAndDeleteFlag(UUID documentationId, Boolean deleteFlag);
}
