package pfs.lms.enquiry.usermanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.domain.AuthorizationObject;
import pfs.lms.enquiry.authorization.repository.AuthorizationAccessRepository;
import pfs.lms.enquiry.authorization.repository.AuthorizationObjectRepository;
import pfs.lms.enquiry.usermanagement.service.IUserManagementAuthorizationService;

import java.util.Date;

/**
 * Seeds the "Maintain Users" authorization object and grants it to the administrator roles.
 * Insert-only and idempotent: existing rows are never changed, so access can be adjusted in the
 * database afterwards without being overwritten on restart.
 */
@Component
public class UserManagementAuthorizationInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UserManagementAuthorizationInitializer.class);

    /** Role code -> role name granted "Maintain Users". */
    private static final String[][] GRANTS = {
            {"admin", "admin"},
            {"ZLM023", "Administrator"}
    };

    private final AuthorizationObjectRepository authorizationObjectRepository;
    private final AuthorizationAccessRepository authorizationAccessRepository;

    public UserManagementAuthorizationInitializer(AuthorizationObjectRepository authorizationObjectRepository,
                                                  AuthorizationAccessRepository authorizationAccessRepository) {
        this.authorizationObjectRepository = authorizationObjectRepository;
        this.authorizationAccessRepository = authorizationAccessRepository;
    }

    @Override
    public void run(String... args) {
        String objectName = IUserManagementAuthorizationService.MAINTAIN_USERS;

        if (authorizationObjectRepository.findByAuthorizationObject(objectName) == null) {
            AuthorizationObject authorizationObject = new AuthorizationObject();
            authorizationObject.setBusinessProcessName(IUserManagementAuthorizationService.BUSINESS_PROCESS);
            authorizationObject.setAuthorizationObject(objectName);
            authorizationObject.setCreatedAt(new Date());
            authorizationObject.setUpdatedAt(new Date());
            authorizationObjectRepository.save(authorizationObject);
            log.info("User Management: created authorization object '{}'", objectName);
        }

        for (String[] grant : GRANTS) {
            if (authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject(grant[0], objectName) == null) {
                AuthorizationAccess access = new AuthorizationAccess();
                access.setAuthorizationObject(objectName);
                access.setUserRoleCode(grant[0]);
                access.setUserRoleName(grant[1]);
                access.setAccessAllowed(true);
                access.setCreatedAt(new Date());
                access.setUpdatedAt(new Date());
                authorizationAccessRepository.save(access);
                log.info("User Management: granted '{}' to role {}", objectName, grant[0]);
            }
        }
    }
}
