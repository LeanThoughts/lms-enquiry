package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.ProjectType;

import java.util.UUID;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, UUID> {

    ProjectType findByCode(String code);

    ProjectType findByValue(String value);
}
