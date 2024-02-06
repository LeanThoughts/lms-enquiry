package pfs.lms.enquiry.documentation.nodalofficer;

import java.util.UUID;

public interface INodalOfficerService {

    NodalOfficer create(NodalOfficerResource resource, String username);

    NodalOfficer update(NodalOfficerResource resource, String username) throws CloneNotSupportedException;

    NodalOfficer delete(UUID nodalOfficerId, String username) throws CloneNotSupportedException;
}
