package pfs.lms.enquiry.appraisal.proposaldetails;

public interface IProposalDetailService {

    ProposalDetail createProposalDetail(ProposalDetailResource proposalDetailResource, String username);

    ProposalDetail updateProposalDetail(ProposalDetailResource proposalDetailResource, String username) throws CloneNotSupportedException;
}
