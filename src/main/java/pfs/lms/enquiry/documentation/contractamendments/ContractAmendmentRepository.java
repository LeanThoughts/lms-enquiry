package pfs.lms.enquiry.documentation.contractamendments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface  ContractAmendmentRepository extends JpaRepository<ContractAmendment, UUID> {

    List<ContractAmendment> findByDocumentationId(UUID documentationId);

    List<ContractAmendment> findByDocumentationIdAndDeleteFlag(UUID documentationId, Boolean deleteFlag);
}