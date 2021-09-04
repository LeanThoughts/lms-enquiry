package pfs.lms.enquiry.authorization.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.repository.AuthorizationAccessRepository;
import pfs.lms.enquiry.authorization.service.IAuthorizationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    @Autowired
    private AuthorizationAccessRepository authorizationAccessRepository;

    @Override
    public List<AuthorizationAccess> getUserRoleAuthorizations(String userRole) {

        List<AuthorizationAccess> authorizationAccessList = authorizationAccessRepository.findByUserRoleCode(userRole);

        return authorizationAccessList;
    }
}
