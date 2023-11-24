package pfs.lms.enquiry.iccapproval.iccfurtherdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ICCFurtherDetailRepository extends JpaRepository<ICCFurtherDetail, UUID> {

    List<ICCFurtherDetail> findByIccApprovalId(UUID iccApprovalId);
}
