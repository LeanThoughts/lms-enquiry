package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.client.OAuthClient;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.SignupResource;
import pfs.lms.enquiry.resource.UserResource;
import pfs.lms.enquiry.service.ISignupService;

import java.security.Principal;

@Slf4j
@ApiController
@RequiredArgsConstructor
@Profile({"oauth", "pfsdev"})
public class UserController
{
    private final ISignupService iSignupService;

    private final OAuthClient oAuthClient;

    private final ResourceServerTokenServices defaultTokenServices;

    private final UserRepository userRepository;

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
        user.setRole(userResource.getRole());
        userRepository.save(user);

        SignupResource signupResource = new SignupResource(userResource.getFirstName(), userResource.getLastName(),
                userResource.getEmail(), "", userResource.getPassword());
        modifyPassword(signupResource, principal);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password/modify")
    public ResponseEntity modifyPassword(@RequestBody SignupResource signupResource, Principal principal) {
        String token = getAuthorizationBearer(principal);
        oAuthClient.modifyPassword(token, signupResource);
        return ResponseEntity.ok().build();
    }

    public String getAuthorizationBearer(Principal user) {
        OAuth2Authentication authentication = (OAuth2Authentication) user;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        OAuth2AccessToken token = defaultTokenServices.readAccessToken(details.getTokenValue());
        log.info("Access token  = {}", token.toString());
        return "Bearer " + token.toString();
    }
}