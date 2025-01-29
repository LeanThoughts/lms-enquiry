package pfs.lms.enquiry.bmcapproval;

public interface IBMCICCApprovalService {

//    ProjectProposalResource getProjectProposal(UUID enquiryActionId);

    BmcICCApproval create(BmcICCApproval BMCICCApproval, String username) throws Exception;

    BmcICCApproval update(BmcICCApproval BMCICCApproval, String username) throws Exception;

    BmcICCApproval processApprovedICC(BmcICCApproval BMCICCApproval, String username) throws CloneNotSupportedException;

    BmcICCApproval processRejection(BmcICCApproval BMCICCApproval, String username) throws CloneNotSupportedException;

}
