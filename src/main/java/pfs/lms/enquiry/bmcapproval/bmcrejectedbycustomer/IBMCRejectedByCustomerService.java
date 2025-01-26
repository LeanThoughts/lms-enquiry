package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

import java.util.UUID;

public interface IBMCRejectedByCustomerService {

    BMCRejectedByCustomer create(BMCRejectedByCustomerResource bmcRejectedByCustomerResource, String username);

    BMCRejectedByCustomer update(BMCRejectedByCustomerResource bmcRejectedByCustomerResource, String username) throws CloneNotSupportedException;

    BMCRejectedByCustomer delete(UUID bmcRejectedByCustomerId, String username);
}
