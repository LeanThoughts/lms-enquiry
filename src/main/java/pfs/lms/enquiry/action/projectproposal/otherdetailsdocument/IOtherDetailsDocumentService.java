package pfs.lms.enquiry.action.projectproposal.otherdetailsdocument;

import java.util.UUID;

public interface IOtherDetailsDocumentService {

    OtherDetailsDocument create(OtherDetailsDocumentResource resource, String username);

    OtherDetailsDocument update(OtherDetailsDocumentResource resource, String username) throws CloneNotSupportedException;

    OtherDetailsDocument delete(UUID otherDetailsDocumentId, String username);
}
