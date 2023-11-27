package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

import java.util.UUID;

public interface IRejectedByCustomerService {

    RejectedByCustomer create(RejectedByCustomerResource rejectedByCustomerResource, String username);

    RejectedByCustomer update(RejectedByCustomerResource rejectedByCustomerResource, String username) throws CloneNotSupportedException;

    RejectedByCustomer delete(UUID rejectedByCustomerId, String username);
}
