package pfs.lms.enquiry.action.enquirycompletion;

public interface IEnquiryCompletionService {

    EnquiryCompletion create(EnquiryCompletionResource resource, String username);

    EnquiryCompletion update(EnquiryCompletionResource resource, String username) throws CloneNotSupportedException;
}
