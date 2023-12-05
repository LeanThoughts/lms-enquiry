package pfs.lms.enquiry.applicationfee.formalrequest;

import java.util.UUID;

public interface IFormalRequestService {

    FormalRequest create(FormalRequestResource formalRequestResource, String username);

    FormalRequest update(FormalRequestResource formalRequestResource, String username) throws CloneNotSupportedException;

    FormalRequest delete(UUID formalRequestId, String username);
}
