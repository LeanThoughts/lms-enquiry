package pfs.lms.enquiry.action.projectproposal;

import pfs.lms.enquiry.action.EnquiryAction;

public interface IProjectProposalService {

//    ProjectProposalResource getProjectProposal(UUID enquiryActionId);

    ProjectProposal create(ProjectProposalResource resource, String username) throws Exception;

    ProjectProposal update(ProjectProposalResource resource, String username) throws Exception;

    ProjectProposal processApprovedEnquiry(EnquiryAction enquiryAction,String username) throws CloneNotSupportedException;
}
