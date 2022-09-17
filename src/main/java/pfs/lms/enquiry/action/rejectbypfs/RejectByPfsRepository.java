package pfs.lms.enquiry.action.rejectbypfs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RejectByPfsRepository extends JpaRepository<RejectByPfs, UUID> {

    RejectByPfs findByEnquiryActionId(UUID enquiryActionId);
}
