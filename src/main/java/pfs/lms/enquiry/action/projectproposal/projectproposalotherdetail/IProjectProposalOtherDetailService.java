package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

public interface IProjectProposalOtherDetailService {

    ProjectProposalOtherDetail create(ProjectProposalOtherDetailResource resource, String username);

    ProjectProposalOtherDetail update(ProjectProposalOtherDetailResource resource, String username) throws CloneNotSupportedException;
}
