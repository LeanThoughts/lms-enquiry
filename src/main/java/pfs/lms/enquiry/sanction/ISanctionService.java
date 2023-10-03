package pfs.lms.enquiry.sanction;

import pfs.lms.enquiry.boardapproval.BoardApproval;

public interface ISanctionService {

     BoardApproval processApprovedSanction(Sanction sanction, String username);
}
