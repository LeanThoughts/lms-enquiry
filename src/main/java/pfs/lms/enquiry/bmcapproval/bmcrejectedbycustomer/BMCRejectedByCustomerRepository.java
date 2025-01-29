package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BMCRejectedByCustomerRepository extends JpaRepository<BmcRejectedByCustomer, UUID> {

    BmcRejectedByCustomer findByBmcICCApprovalId(UUID iccApprovalId);
}
