package pfs.lms.enquiry.iccapproval;

import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalResource;

public interface ICCApprovalService {

//    ProjectProposalResource getProjectProposal(UUID enquiryActionId);

    ICCApproval create(ICCApproval iccApproval, String username) throws Exception;

    ICCApproval update(ICCApproval iccApproval, String username) throws Exception;

    ICCApproval processApprovedICC(ICCApproval iccApproval,String username) throws CloneNotSupportedException;

    ICCApproval processRejection(ICCApproval iccApproval,String username) throws CloneNotSupportedException;

}
