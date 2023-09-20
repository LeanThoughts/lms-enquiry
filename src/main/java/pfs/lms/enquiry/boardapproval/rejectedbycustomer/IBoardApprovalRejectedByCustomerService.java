package pfs.lms.enquiry.boardapproval.rejectedbycustomer;

import java.util.UUID;

public interface IBoardApprovalRejectedByCustomerService {

    BoardApprovalRejectedByCustomer create(BoardApprovalRejectedByCustomerResource boardApprovalRejectedByCustomerResource, String username);

    BoardApprovalRejectedByCustomer update(BoardApprovalRejectedByCustomerResource boardApprovalRejectedByCustomerResource, String username)
            throws CloneNotSupportedException;

    BoardApprovalRejectedByCustomer delete(UUID rejectedByBoardId) throws CloneNotSupportedException;
}
