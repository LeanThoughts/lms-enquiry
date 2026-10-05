package pfs.lms.enquiry.usermanagement.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.repository.AuthorizationAccessRepository;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.usermanagement.dto.UserManagementAccessDto;
import pfs.lms.enquiry.usermanagement.service.IUserManagementAuthorizationService;

/**
 * Role-based access check for the User Management module, using the existing
 * AuthorizationAccess table. Can be switched off with
 * {@code usermanagement.authorization.enabled=false} (e.g. for local testing).
 */
@Service
public class UserManagementAuthorizationService implements IUserManagementAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(UserManagementAuthorizationService.class);

    private final UserRepository userRepository;
    private final AuthorizationAccessRepository authorizationAccessRepository;
    private final boolean enabled;

    public UserManagementAuthorizationService(UserRepository userRepository,
                                              AuthorizationAccessRepository authorizationAccessRepository,
                                              @Value("${usermanagement.authorization.enabled:true}") boolean enabled) {
        this.userRepository = userRepository;
        this.authorizationAccessRepository = authorizationAccessRepository;
        this.enabled = enabled;
    }

    @Override
    public UserManagementAccessDto getAccess(String principalName) {
        User user = principalName == null ? null : userRepository.findByEmail(principalName);
        String role = user != null ? user.getRole() : null;

        if (!enabled) {
            return new UserManagementAccessDto(true, principalName, role);
        }
        if (user == null || !user.isStatus() || role == null) {
            return new UserManagementAccessDto(false, principalName, role);
        }
        AuthorizationAccess access =
                authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject(role, MAINTAIN_USERS);
        boolean allowed = access != null && Boolean.TRUE.equals(access.getAccessAllowed());
        return new UserManagementAccessDto(allowed, principalName, role);
    }

    @Override
    public void checkAccess(String principalName) {
        if (!getAccess(principalName).isAllowed()) {
            log.warn("User Management access denied for {}", principalName);
            throw new LmsException("You are not authorised to maintain users.", HttpStatus.FORBIDDEN);
        }
    }
}
