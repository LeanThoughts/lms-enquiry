package pfs.lms.enquiry.sanction.rejectedbycustomer;

import java.util.UUID;

public interface ISanctionRejectedByCustomerService {

    SanctionRejectedByCustomer create(SanctionRejectedByCustomerResource sanctionRejectedByCustomerResource, String username);

    SanctionRejectedByCustomer update(SanctionRejectedByCustomerResource sanctionRejectedByCustomerResource, String username)
            throws CloneNotSupportedException;

    SanctionRejectedByCustomer delete(UUID rejectedByBoardId,String username) throws CloneNotSupportedException;
}
