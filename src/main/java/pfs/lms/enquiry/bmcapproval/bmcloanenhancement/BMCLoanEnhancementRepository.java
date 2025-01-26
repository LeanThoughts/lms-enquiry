package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BMCLoanEnhancementRepository extends JpaRepository<BMCLoanEnhancement, UUID> {

    List<BMCLoanEnhancement> findByBmcICCApprovalId(UUID iccApprovalId);
}
