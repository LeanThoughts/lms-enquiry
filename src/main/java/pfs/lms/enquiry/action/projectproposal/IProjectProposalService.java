package pfs.lms.enquiry.action.projectproposal;

public interface IProjectProposalService {

//    ProjectProposalResource getProjectProposal(UUID enquiryActionId);

    ProjectProposal create(ProjectProposalResource resource, String username);

    ProjectProposal update(ProjectProposalResource resource, String username) throws CloneNotSupportedException;
}
