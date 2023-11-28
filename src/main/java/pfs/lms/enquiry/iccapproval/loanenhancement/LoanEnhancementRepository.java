package pfs.lms.enquiry.iccapproval.loanenhancement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanEnhancementRepository extends JpaRepository<LoanEnhancement, UUID> {

    List<LoanEnhancement> findByIccApprovalId(UUID iccApprovalId);
}
