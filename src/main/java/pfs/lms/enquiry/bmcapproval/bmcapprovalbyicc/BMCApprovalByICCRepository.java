package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BMCApprovalByICCRepository extends JpaRepository<BmcApprovalByICC, UUID> {

    BmcApprovalByICC findByBmcICCApprovalId(UUID iccApprovalId);
}
