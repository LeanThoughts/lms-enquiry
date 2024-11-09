package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import java.util.UUID;

public interface IApplicationFeeProjectDetailService {

    ApplicationFeeProjectDetail create(ApplicationFeeProjectDetailResource applicationFeeProjectDetailResource, String username) throws CloneNotSupportedException;

    ApplicationFeeProjectDetail update(ApplicationFeeProjectDetailResource applicationFeeProjectDetailResource, String username) throws CloneNotSupportedException;

    ApplicationFeeProjectDetail getApplicationFeeProjectDetail(UUID applicationFeeId);
}
