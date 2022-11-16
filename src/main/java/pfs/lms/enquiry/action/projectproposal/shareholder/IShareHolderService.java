package pfs.lms.enquiry.action.projectproposal.shareholder;

public interface IShareHolderService {

    ShareHolder create(ShareHolderResource resource, String username);

    ShareHolder update(ShareHolderResource resource, String username) throws CloneNotSupportedException;
}
