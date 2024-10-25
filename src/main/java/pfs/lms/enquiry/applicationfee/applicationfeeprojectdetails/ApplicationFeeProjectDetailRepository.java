package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationFeeProjectDetailRepository extends JpaRepository<ApplicationFeeProjectDetail, UUID> {

    ApplicationFeeProjectDetail findByApplicationFeeId(UUID applicationFeeId);
}
