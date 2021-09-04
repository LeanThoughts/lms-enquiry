package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.authorization.domain.AuthorizationObject;
import pfs.lms.enquiry.authorization.repository.AuthorizationAccessRepository;
import pfs.lms.enquiry.authorization.repository.AuthorizationObjectRepository;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInitializer implements CommandLineRunner {

    @Autowired
    AuthorizationObjectRepository authorizationObjectRepository;
    @Autowired
    AuthorizationAccessRepository authorizationAccessRepository;

    @Override
    public void run(String... args) throws Exception {

        AuthorizationObject authorizationObject = new AuthorizationObject();
        authorizationObject = authorizationObjectRepository.findByAuthorizationObject("Execute Appraisal");
        if (authorizationObject == null) {
            authorizationObject = new AuthorizationObject(); authorizationObject.setCreatedAt(new Date());authorizationObject.setUpdatedAt(new Date());
            authorizationObject.setBusinessProcessName("Appraisal");
            authorizationObject.setAuthorizationObject("Execute Appraisal");
            authorizationObjectRepository.save(authorizationObject);
        }

        authorizationObject = new AuthorizationObject();
        authorizationObject = authorizationObjectRepository.findByAuthorizationObject("Display Appraisal");
        if (authorizationObject == null) {
            authorizationObject = new AuthorizationObject(); authorizationObject.setCreatedAt(new Date());authorizationObject.setUpdatedAt(new Date());
            authorizationObject.setBusinessProcessName("Appraisal");
            authorizationObject.setAuthorizationObject("Display Appraisal");
            authorizationObjectRepository.save(authorizationObject);
        }

        authorizationObject = new AuthorizationObject();
        authorizationObject = authorizationObjectRepository.findByAuthorizationObject("Execute Monitoring");
        if (authorizationObject == null) {
            authorizationObject = new AuthorizationObject(); authorizationObject.setCreatedAt(new Date());authorizationObject.setUpdatedAt(new Date());
            authorizationObject.setBusinessProcessName("Monitoring");
            authorizationObject.setAuthorizationObject("Execute Monitoring");
            authorizationObjectRepository.save(authorizationObject);
        }
        authorizationObject = new AuthorizationObject();
        authorizationObject = authorizationObjectRepository.findByAuthorizationObject("Display Monitoring");
        if (authorizationObject == null) {
            authorizationObject = new AuthorizationObject();authorizationObject.setCreatedAt(new Date());authorizationObject.setUpdatedAt(new Date());
            authorizationObject.setBusinessProcessName("Monitoring");
            authorizationObject.setAuthorizationObject("Display Monitoring");
            authorizationObjectRepository.save(authorizationObject);
        }


    }
}
