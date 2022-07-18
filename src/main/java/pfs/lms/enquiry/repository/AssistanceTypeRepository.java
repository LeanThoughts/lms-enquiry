package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;

import java.util.UUID;

public interface AssistanceTypeRepository extends JpaRepository<AssistanceType, UUID> {

    AssistanceType getAssistanceTypeByCode(String code);
}
