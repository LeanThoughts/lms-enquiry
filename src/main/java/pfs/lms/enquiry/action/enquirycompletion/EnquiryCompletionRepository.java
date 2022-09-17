package pfs.lms.enquiry.action.enquirycompletion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnquiryCompletionRepository extends JpaRepository<EnquiryCompletion, UUID> {

    EnquiryCompletion findByEnquiryActionId(UUID enquiryActionId);
}
