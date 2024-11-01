package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.resource.SignupResource;
import pfs.lms.enquiry.service.ISignupService;

import java.util.regex.Pattern;

@ApiController
@RequiredArgsConstructor
public class SignupController {

    private final ISignupService iSignupService;

    @GetMapping("/password/strength")
    public ResponseEntity getPasswordStrength(@RequestParam("password") String password){
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z]).*(?=.*[0-9]).*(?=.*[a-z]).{8}$");
        return new ResponseEntity(passwordPattern.matcher(password).matches(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupResource signupResource) throws CloneNotSupportedException {
        iSignupService.signup(signupResource);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/signup/verify/{activation}")
    public ResponseEntity verify(@PathVariable("activation") String activation){
        return iSignupService.verify(activation);
    }
}
