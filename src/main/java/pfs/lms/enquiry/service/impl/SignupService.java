package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.client.OAuthClient;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.mail.domain.MailObject;
import pfs.lms.enquiry.mail.service.EmailService;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.SignupResource;
import pfs.lms.enquiry.resource.UserResource;
import pfs.lms.enquiry.service.IPartnerService;
import pfs.lms.enquiry.service.ISignupService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService implements ISignupService {

    //private final IPartnerService iPartnerService;
    private final UserRepository userRepository;

    private final OAuthClient oAuthClient;

    private final IPartnerService iPartnerService;


    @Autowired
    EmailService emailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signup(SignupResource signupResource) {

        //Partner partner = new Partner("TR0110", signupResource.getFirstName(), signupResource.getLastName(), signupResource.getEmail(), signupResource.getMobile(), signupResource.getPassword());
        //partner = iPartnerService.save(partner);

        User user = new User(signupResource.getFirstName(), signupResource.getLastName(), signupResource.getEmail(),
                "TR0100", true, signupResource.getEmail(), null, null, false, false);
        user = userRepository.save(user);
        log.info("{} created", user);

        //Create User in OAuth
        ResponseEntity<Boolean> response = oAuthClient.signup(signupResource);

        if (!response.getBody())
            throw new LmsException("Error creating OAuth User", HttpStatus.PRECONDITION_FAILED);

        //Create Partner for the User
        Partner partner = new Partner();
        partner.setEmail(signupResource.getEmail());
        partner.setPartyName1(signupResource.getFirstName());
        partner.setPartyName2(signupResource.getLastName());
        partner.setPartyRole("TR0100");
        iPartnerService.save(partner);

        this.sendSignUpNotificationMail(signupResource.getEmail(),signupResource.getFirstName(),signupResource.getLastName());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signup(UserResource userResource)
    {
        // Create user in the LMS application
        User user = new User(userResource.getFirstName(), userResource.getLastName(), userResource.getEmail(),
                userResource.getRole(), true, userResource.getEmail(), userResource.getSapBPNumber(), userResource.getRiskDepartment(), userResource.getDepartmentHead(), userResource.getRiskPortalDisplayOnlyAccess());
        user = userRepository.save(user);
        log.info("{} created.", user);

        // Create user in the OAuth application
        SignupResource signupResource = new SignupResource(userResource.getFirstName(), userResource.getLastName(),
                userResource.getEmail(), userResource.getMobile(), userResource.getPassword());
        ResponseEntity<Boolean> response = oAuthClient.adminSignUp(signupResource);

        if (!response.getBody())
            throw new LmsException("Error creating OAuth User", HttpStatus.PRECONDITION_FAILED);

        //Create Partner for the User
        Partner partner = new Partner();
        partner.setEmail(userResource.getEmail());
        partner.setPartyName1(userResource.getFirstName());
        partner.setPartyName2(userResource.getLastName());
        partner.setContactNumber(userResource.getMobile());
        partner.setPartyRole(user.getRole());
        iPartnerService.save(partner);

    }

    @Override
    public String sendSignUpNotificationMail(String emailId, String fName, String lName) {
        String line1 = "Dear" + " " + fName + " " + lName + System.lineSeparator();
        String line2 = "   Welcome! You are signed up on PTC India Financial Services Ltd. Portal." + System.lineSeparator();
        String line3 = "     " + System.lineSeparator() ;
        String line4 = "    " + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5  ;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Portal");
        mailObject.setAppObjectId(" ");
        mailObject.setToAddress(emailId);
        mailObject.setSubject("PTC India Financial Services Ltd. Portal Signup ");
        mailObject.setMailContent(content);

        //emailService.sendEmailMessage(mailObject);

//        new Thread(() -> {
//            // code goes here.
//            emailService.sendEmailMessage(mailObject);
//        }).start();
//

        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null;
    }

    @Override
    public ResponseEntity verify(String activation) {
        ResponseEntity response = oAuthClient.verify(activation);
        if (response.getStatusCode() != HttpStatus.OK)
            throw new LmsException("Email Verification Failed", HttpStatus.PRECONDITION_FAILED);
        return ResponseEntity.ok().build();
    }


}
