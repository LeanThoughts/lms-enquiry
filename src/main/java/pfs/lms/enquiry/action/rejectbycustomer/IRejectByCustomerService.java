package pfs.lms.enquiry.action.rejectbycustomer;

public interface IRejectByCustomerService {

    RejectByCustomer create(RejectByCustomerResource resource, String username);

    RejectByCustomer update(RejectByCustomerResource resource, String username) throws CloneNotSupportedException;
}
