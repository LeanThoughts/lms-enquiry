package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BMCICCFurtherDetailRepository extends JpaRepository<BmcIccFurtherDetail, UUID> {

    List<BmcIccFurtherDetail> findByBmcICCApprovalId(UUID iccApprovalId);
}
