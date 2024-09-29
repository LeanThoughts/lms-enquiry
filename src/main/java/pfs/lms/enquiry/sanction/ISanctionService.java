package pfs.lms.enquiry.sanction;

import pfs.lms.enquiry.boardapproval.BoardApproval;

public interface ISanctionService {

     Sanction processApprovedSanction(Sanction sanction, String username) throws CloneNotSupportedException;
     Sanction processRejection(Sanction sanction, String username) throws CloneNotSupportedException;

}
