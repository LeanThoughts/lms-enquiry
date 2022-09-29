package pfs.lms.enquiry.action.enquiryactionreasonfordelay;

public interface IEnquiryActionReasonForDelayService {

    EnquiryActionReasonForDelay create(EnquiryActionReasonForDelayResource resource, String username);

    EnquiryActionReasonForDelay update(EnquiryActionReasonForDelayResource resource, String username) throws CloneNotSupportedException;
}
