package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.client.OAuthClient;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.mail.service.PasswordResetService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.EmailId;
import pfs.lms.enquiry.resource.LoanNumberResource;
import pfs.lms.enquiry.resource.SignupResource;
import pfs.lms.enquiry.resource.UserResource;
import pfs.lms.enquiry.service.IPartnerService;
import pfs.lms.enquiry.service.ISignupService;
import pfs.lms.enquiry.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@ApiController
@RequiredArgsConstructor


public class UserController {
    private final ISignupService iSignupService;

    private final OAuthClient oAuthClient;

    private final ResourceServerTokenServices defaultTokenServices;

    private final UserRepository userRepository;

    private final PasswordResetService passwordResetService;

    private final LoanApplicationRepository loanApplicationRepository;

    private final IUserService userService;

    private final IPartnerService partnerService;

    @PostMapping("/user")
    public ResponseEntity signup(@RequestBody UserResource userResource) {
        // Create the user.
        iSignupService.signup(userResource);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/user")
    public ResponseEntity update(@RequestBody UserResource userResource, Principal principal) {
        // Update the user.
        User user = userRepository.findByEmail(userResource.getEmail());
        user.setFirstName(userResource.getFirstName());
        user.setLastName(userResource.getLastName());
        user.setSapBPNumber(userResource.getSapBPNumber());
        user.setRiskDepartment(userResource.getRiskDepartment());
        user.setRole(userResource.getRole());
        user.setDepartmentHead(userResource.getDepartmentHead());
        user.setRiskPortalDisplayOnlyAccess(userResource.getRiskPortalDisplayOnlyAccess());
        userRepository.save(user);

        SignupResource signupResource = new SignupResource(userResource.getFirstName(), userResource.getLastName(),
                userResource.getEmail(), "", userResource.getPassword());
        modifyPassword(signupResource, principal);

        //Update Partner for the User
        Partner partner = new Partner();
        partner.setEmail(userResource.getEmail());
        partner.setPartyName1(userResource.getFirstName());
        partner.setPartyName2(userResource.getLastName());
        partner.setContactNumber(userResource.getMobile());
        partner.setPartyRole(user.getRole());
        partnerService.save(partner);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password/modify")
    public ResponseEntity modifyPassword(@RequestBody SignupResource signupResource, Principal principal) {
        // Update user password in oAuth service.
        String token = getAuthorizationBearer(principal);
        oAuthClient.modifyPassword(token, signupResource);
        // Update password reset to false.
        User user = userRepository.findByEmail(signupResource.getEmail());
        user.setPasswordReset(false);
        userRepository.save(user);
        // Post update formalities.
        passwordResetService.sendPasswordChangeNotificationMail(signupResource.getEmail(), user.getFirstName(), user.getLastName());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/user")
    public ResponseEntity  getUserByUserId(@RequestParam("userId") String userId, HttpServletRequest request) {


        User user = userRepository.findByEmail(userId);

        if (user != null) {
            return  ResponseEntity.ok(user);
        } else {
            return ResponseEntity.noContent().build();
        }

    }


    public String getAuthorizationBearer(Principal user) {
        OAuth2Authentication authentication = (OAuth2Authentication) user;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        log.info("Inside Authorization Bearer:  User : ", user.toString());
        log.info("User : ", ((OAuth2Authentication) user).getPrincipal() );
        log.info("OAuth2AuthenticationDetails : ", details.toString());
        log.info("Details Token Value : " , details.getTokenValue() );

        OAuth2AccessToken token = defaultTokenServices.readAccessToken(details.getTokenValue());
        log.info("Access token  = {}", token.toString());
        return "Bearer " + token.toString();
    }

    @PutMapping("/password/reset")
    public ResponseEntity resetPassword(@RequestBody String emailId, Principal principal) {

        User user = userRepository.findByEmail(emailId);
        if (user != null) {
            // Generate the new random password.
            String newPassword = passwordResetService.sendMailWithNewPassword(user.getEmail(),
                    user.getFirstName(),
                    user.getLastName());

            // Update user password by invoking the oAuth service method.
            SignupResource signupResource = new SignupResource(user.getFirstName(), user.getLastName(),
                    user.getEmail(), "", newPassword);
            oAuthClient.resetPassword(signupResource);

            // Update the user with passwordReset status as true.
            user = user.setPasswordReset(true);
            user = userRepository.save(user);

            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.noContent().build();

    }

    @PutMapping("/user/email")
    public ResponseEntity getUserByEmail(@RequestBody EmailId emailId, HttpServletRequest request) {

        User user = userRepository.findByEmail(emailId.getEmailId());

        if (user != null) {
            return  ResponseEntity.ok(user);
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @PutMapping("/loanApp")
     public ResponseEntity getLoanApp(@RequestBody LoanNumberResource loanNumberResource, HttpServletRequest request) {

        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(loanNumberResource.getLoanNumber());
        if (loanApplication != null)
            return ResponseEntity.ok(loanApplication);
        else
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/loanApp/id")
    //public ResponseEntity getLoanApp(@RequestBody EmailId emailId, HttpServletRequest request) {
    public ResponseEntity getLoanAppById(@RequestBody Long id, HttpServletRequest request) {

        LoanApplication loanApplication = loanApplicationRepository.findByLoanEnquiryId(id);
        if (loanApplication != null)
            return ResponseEntity.ok(loanApplication);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("users/queryParams")
    public ResponseEntity getPartnersByQueryParams(@RequestParam("query") String[] queryParams, HttpServletRequest httpServletRequest) {

        List<User> users = userService.searchUsers(queryParams);

        if (users != null){
            return ResponseEntity.ok(users);

        }else{
            return ResponseEntity.noContent().build();
        }

    }

}
