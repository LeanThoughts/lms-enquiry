package pfs.lms.enquiry.action.rejectbypfs;

public interface IRejectByPfsService {

    RejectByPfs create(RejectByPfsResource resource, String username);

    RejectByPfs update(RejectByPfsResource resource, String username) throws CloneNotSupportedException;
}
