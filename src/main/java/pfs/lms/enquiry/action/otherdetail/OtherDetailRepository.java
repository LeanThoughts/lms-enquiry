package pfs.lms.enquiry.action.otherdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OtherDetailRepository extends JpaRepository<OtherDetail, UUID> {

    OtherDetail findByEnquiryActionId(UUID enquiryActionId);
}
