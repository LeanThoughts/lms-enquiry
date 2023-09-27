package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.ProjectTypeCoreSector;

import java.util.UUID;

public interface ProjectTypeCoreSectorRepository extends JpaRepository<ProjectTypeCoreSector, UUID> {

    ProjectTypeCoreSector getProjectTypeCoreSectorByCode(String code);
}
