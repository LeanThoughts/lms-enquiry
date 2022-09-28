package pfs.lms.enquiry.action.rejectbycustomer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RejectByCustomerRepository extends JpaRepository<RejectByCustomer, UUID> {

    RejectByCustomer findByEnquiryActionId(UUID enquiryActionId);
}
