//package pfs.lms.enquiry;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import pfs.lms.enquiry.mail.service.PasswordResetService;
//
//import static org.springframework.test.util.AssertionErrors.assertTrue;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Slf4j
//public class PasswordTest {
//
//	@Test
//	public void contextLoads() {
//	}
//
//	@Autowired
//    PasswordResetService passwordResetService;
//
//	@Test
//	public void whenPasswordGeneratedUsingPassay_thenSuccessful() {
//
//		//passwordResetService passGen = new RandomPasswordGenerator();
//
//		String password = passwordResetService.generatePassayPassword();
//		System.out.println("Password --------------- :" + password);
//		int specialCharCount = 0;
//		for (char c : password.toCharArray()) {
//			if (c >= 33 || c <= 47) {
//				specialCharCount++;
//			}
//		}
//		assertTrue("Password validation failed in Passay", specialCharCount >= 2);
//	}
//}
