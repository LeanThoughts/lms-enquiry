package pfs.lms.enquiry.usermanagement.service;

import pfs.lms.enquiry.usermanagement.dto.UserManagementAccessDto;

/**
 * Checks whether the signed-in user holds the "Maintain Users" authorization.
 */
public interface IUserManagementAuthorizationService {

    /** Authorization object name used in AuthorizationObject / AuthorizationAccess. */
    String MAINTAIN_USERS = "Maintain Users";

    /** Business process name stored on the AuthorizationObject. */
    String BUSINESS_PROCESS = "User Management";

    UserManagementAccessDto getAccess(String principalName);

    /** Throws an LmsException with HTTP 403 when the user may not maintain users. */
    void checkAccess(String principalName);
}
