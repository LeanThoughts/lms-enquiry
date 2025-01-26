package pfs.lms.enquiry.bmcapproval;

public interface IBMCICCApprovalService {

//    ProjectProposalResource getProjectProposal(UUID enquiryActionId);

    BMCICCApproval create(BMCICCApproval BMCICCApproval, String username) throws Exception;

    BMCICCApproval update(BMCICCApproval BMCICCApproval, String username) throws Exception;

    BMCICCApproval processApprovedICC(BMCICCApproval BMCICCApproval, String username) throws CloneNotSupportedException;

    BMCICCApproval processRejection(BMCICCApproval BMCICCApproval, String username) throws CloneNotSupportedException;

}
