package pfs.lms.enquiry.bmcapproval;

public interface IBMCICCApprovalService {

//    ProjectProposalResource getProjectProposal(UUID enquiryActionId);

    BmcIccApproval create(BmcIccApproval BMCICCApproval, String username) throws Exception;

    BmcIccApproval update(BmcIccApproval BMCICCApproval, String username) throws Exception;

    BmcIccApproval processApprovedICC(BmcIccApproval BMCICCApproval, String username) throws CloneNotSupportedException;

    BmcIccApproval processRejection(BmcIccApproval BMCICCApproval, String username) throws CloneNotSupportedException;

}
