package pfs.lms.enquiry.action;

import pfs.lms.enquiry.iccapproval.ICCApproval;

public interface IEnquiryActionService {


    EnquiryAction processRejection(EnquiryAction enquiryAction,String username) throws CloneNotSupportedException;
}
