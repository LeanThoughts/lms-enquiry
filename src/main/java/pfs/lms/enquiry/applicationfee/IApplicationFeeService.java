package pfs.lms.enquiry.applicationfee;

import pfs.lms.enquiry.applicationfee.ApplicationFee;

public interface IApplicationFeeService {

    ApplicationFee create(ApplicationFee applicationFee, String username) throws Exception;

    ApplicationFee update(ApplicationFee applicationFee, String username) throws Exception;

    ApplicationFee processApprovedApplicationFee(ApplicationFee applicationFee,String username) throws CloneNotSupportedException;
}
