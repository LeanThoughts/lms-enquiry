package pfs.lms.enquiry.action.enquiryactionreasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnquiryActionReasonForDelayRepository extends JpaRepository<EnquiryActionReasonForDelay, UUID> {

    EnquiryActionReasonForDelay findByEnquiryActionId(UUID enquiryActionId);
}
