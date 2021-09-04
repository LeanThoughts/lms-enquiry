package pfs.lms.enquiry.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;

import java.util.List;

public interface AuthorizationAccessRepository extends JpaRepository<AuthorizationAccess, Long> {

    List<AuthorizationAccess> findByUserRoleCode(String userRole);
    AuthorizationAccess findByUserRoleCodeAndAuthorizationObject(String userRole, String authorizationObject);
}
