package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RejectedByCustomerRepository extends JpaRepository<RejectedByCustomer, UUID> {

    RejectedByCustomer findByIccApprovalId(UUID iccApprovalId);
}
