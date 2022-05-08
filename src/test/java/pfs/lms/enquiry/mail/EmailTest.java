package pfs.lms.enquiry.mail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pfs.lms.enquiry.AbstractTest;
import pfs.lms.enquiry.mail.domain.MailObject;
import pfs.lms.enquiry.mail.service.EmailService;
//
//import pfs.lms.enquiry.mail.domain.MailObject;

import static org.junit.Assert.assertEquals;

/**
 * Created by sajeev on 16-Feb-19.
 */
public class EmailTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }


    @Autowired
    EmailService emailService;

   // @Test
    public void sendEmailServiceTest() throws Exception {


        MailObject mailObject = new MailObject();

        mailObject.setSendingApp("Test Application");
        mailObject.setAppObjectId("TEST-OBJECT-001");
        mailObject.setToAddress("sajeev@leanthoughts.com");
        mailObject.setSubject("Test Email from Email Service");
        mailObject.setMailContent("This is test email content...............................");

        mailObject = emailService.sendEmailMessage(mailObject);

        System.out.println("EMAIL OBJECT: " + mailObject);

    }

    @Test
    public void sendEmailTest() throws Exception {

        String uri = "/api/mail";

        MailObject mailObject = new MailObject();

        mailObject.setSendingApp("Test Application");
        mailObject.setAppObjectId("TEST-OBJECT-001");
        mailObject.setToAddress("sajeev@leanthoughts.com");
        mailObject.setSubject("Test Email from Email Service");
        mailObject.setMailContent("This is test email content...............................");


        String inputJson = super.mapToJson(mailObject);
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        System.out.println(" ------Email Testing Completed ");

    }

}