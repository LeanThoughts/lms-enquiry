package pfs.lms.enquiry.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.domain.AuthorizationObject;

import java.util.List;

public interface AuthorizationObjectRepository extends JpaRepository<AuthorizationObject, Long> {

    AuthorizationObject findByAuthorizationObject(String authorizationObject);

}
