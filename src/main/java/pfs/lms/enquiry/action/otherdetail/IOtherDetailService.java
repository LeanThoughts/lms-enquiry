package pfs.lms.enquiry.action.otherdetail;

public interface IOtherDetailService {

    OtherDetail create(OtherDetailResource resource, String username);

    OtherDetail update(OtherDetailResource resource, String username) throws CloneNotSupportedException;
}
