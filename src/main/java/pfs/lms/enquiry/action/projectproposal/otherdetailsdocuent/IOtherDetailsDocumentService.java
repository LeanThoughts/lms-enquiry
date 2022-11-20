package pfs.lms.enquiry.action.projectproposal.otherdetailsdocuent;

public interface IOtherDetailsDocumentService {

    OtherDetailsDocument create(OtherDetailsDocumentResource resource, String username);

    OtherDetailsDocument update(OtherDetailsDocumentResource resource, String username) throws CloneNotSupportedException;
}
