package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

import java.util.UUID;

public interface IBMCRejectedByCustomerService {

    BmcRejectedByCustomer create(BMCRejectedByCustomerResource bmcRejectedByCustomerResource, String username);

    BmcRejectedByCustomer update(BMCRejectedByCustomerResource bmcRejectedByCustomerResource, String username) throws CloneNotSupportedException;

    BmcRejectedByCustomer delete(UUID bmcRejectedByCustomerId, String username);
}
