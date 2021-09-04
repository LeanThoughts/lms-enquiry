package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.domain.AuthorizationObject;
import pfs.lms.enquiry.authorization.repository.AuthorizationAccessRepository;
import pfs.lms.enquiry.authorization.repository.AuthorizationObjectRepository;
import pfs.lms.enquiry.domain.UserRole;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationAccessInitializer implements CommandLineRunner {

    @Autowired
    AuthorizationObjectRepository authorizationObjectRepository;
    @Autowired
    AuthorizationAccessRepository authorizationAccessRepository;

    @Override
    public void run(String... args) throws Exception {

        AuthorizationAccess authorizationAccess = new AuthorizationAccess();

        List<AuthorizationObject> authorizationObjectList = authorizationObjectRepository.findAll();

        for (AuthorizationObject authorizationObject: authorizationObjectList) {
            //Role : Admin, PFS IT - Full Authorization
            authorizationAccess = authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject("admin",authorizationObject.getAuthorizationObject());
            if (authorizationAccess == null) {
                createAuthorizationAcess(true,"admin", "admin",authorizationObject);
            }
            authorizationAccess = authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject("ZLM023",authorizationObject.getAuthorizationObject());
            if (authorizationAccess == null) {
                createAuthorizationAcess(true,"ZLM023", "PFS IT",authorizationObject);
            }

            // Execute Monitoring - Only Monitoring Officers ("ZLM028") and Monitoring Head ("ZLM024')
            if (authorizationObject.getAuthorizationObject().equals("Execute Monitoring")) {
                authorizationAccess = authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject("ZLM028",authorizationObject.getAuthorizationObject());
                if (authorizationAccess == null) {
                    createAuthorizationAcess(true,"ZLM028", "Monitoring Head",authorizationObject);
                }
                authorizationAccess = authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject("ZLM024",authorizationObject.getAuthorizationObject());
                if (authorizationAccess == null) {
                    createAuthorizationAcess(true,"ZLM024", "Nodal Officer-Monitoring",authorizationObject);
                }
            }

            // Execute Appraisal - Only Appraisal Officers ("ZLM013") and Appraisal Head ("ZLM010')
            if (authorizationObject.getAuthorizationObject().equals("Execute Appraisal")) {
                authorizationAccess = authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject("ZLM013",authorizationObject.getAuthorizationObject());
                if (authorizationAccess == null) {
                    createAuthorizationAcess(true,"ZLM013", "Appraisal Head",authorizationObject);
                }
                authorizationAccess = authorizationAccessRepository.findByUserRoleCodeAndAuthorizationObject("ZLM010",authorizationObject.getAuthorizationObject());
                if (authorizationAccess == null) {
                    createAuthorizationAcess(true,"ZLM010", " Appraisal Officer",authorizationObject);
                }
            }


        }


    }

    private void createAuthorizationAcess(Boolean accessAllowed, String userRoleCode, String userRoleName, AuthorizationObject authorizationObject) {
        AuthorizationAccess authorizationAccess = new AuthorizationAccess();
        authorizationAccess.setAuthorizationObject(authorizationObject.getAuthorizationObject());
        authorizationAccess.setUserRoleCode(userRoleCode);
        authorizationAccess.setUserRoleName(userRoleName);
        authorizationAccess.setAccessAllowed(accessAllowed);
        authorizationAccess.setCreatedAt(new Date());authorizationAccess.setUpdatedAt(new Date());
        authorizationAccessRepository.save(authorizationAccess);
    }
}
