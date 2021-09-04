package pfs.lms.enquiry.authorization.service;

import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;

import java.util.List;

public interface IAuthorizationService {
    public List<AuthorizationAccess> getUserRoleAuthorizations(String userRole);
}
