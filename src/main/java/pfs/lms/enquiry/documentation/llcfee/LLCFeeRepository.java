package pfs.lms.enquiry.documentation.llcfee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LLCFeeRepository extends JpaRepository<LLCFee, UUID> {

    List<LLCFee> findByDocumentationId(UUID documentationId);

    List<LLCFee> findByDocumentationIdAndDeleteFlag(UUID documentationId, Boolean deleteFlag);
}
