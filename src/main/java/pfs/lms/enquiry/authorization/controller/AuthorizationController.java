package pfs.lms.enquiry.authorization.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.service.IAuthorizationService;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.BankMaster;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Slf4j
@ApiController
@RequiredArgsConstructor
public class AuthorizationController {

    @Autowired
    private IAuthorizationService authorizationService;

    @RequestMapping(value = "/authorization", method = {RequestMethod.GET})
    public ResponseEntity<List<AuthorizationAccess>> getAuthorizationAccessForUserRole(@RequestParam("userRole") String userRole, HttpServletRequest request) {

        List<AuthorizationAccess> authorizationAccessList =  authorizationService.getUserRoleAuthorizations(userRole);

        return ResponseEntity.ok(authorizationAccessList);

    }
}
