package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BMCRejectedByICCRepository extends JpaRepository<BmcRejectedByIcc, UUID> {

    BmcRejectedByIcc findByBmcICCApprovalId(UUID iccApprovalId);
}
