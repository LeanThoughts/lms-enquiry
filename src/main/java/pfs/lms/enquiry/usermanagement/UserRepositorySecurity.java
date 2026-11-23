package pfs.lms.enquiry.usermanagement;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on {@link pfs.lms.enquiry.repository.UserRepository}.
 */
public final class UserRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('UserManagement.Display', 'UserManagement.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('UserManagement.Edit')" + OR_INTERNAL;

    private UserRepositorySecurity() {
    }
}