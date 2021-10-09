package pfs.lms.enquiry.mail.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.EnquiryPortalCommonConfig;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.mail.domain.MailObject;
import pfs.lms.enquiry.mail.service.EmailService;
import pfs.lms.enquiry.mail.service.LoanNotificationService;
import pfs.lms.enquiry.repository.EnquiryPortalCommonConfigRepository;

import java.util.concurrent.CompletableFuture;

/**
 * Created by sajeev on 17-Feb-19.
 */
@Component
public class LoanNotificationServiceImpl implements LoanNotificationService {

    @Autowired
    EmailService emailService;

    @Autowired
    EnquiryPortalCommonConfigRepository enquiryPortalCommonConfigRepository;

    @Autowired
    Environment env;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Override
    public String sendSubmissionNotification(User user, LoanApplication loanApplication, Partner partner) {

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    Your loan application with the following details is submitted for further processing by PTC India Financial Services Ltd.." + System.lineSeparator();
        String line3 = "    Loan Application Id  : " +loanApplication.getEnquiryNo() + System.lineSeparator() ;
        String line4 = "    Project Name         : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Enquiry Portal");
        mailObject.setAppObjectId(loanApplication.getEnquiryNo().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Your loan application for the project " +loanApplication.getProjectName() + " is submitted to PTC India Financial Services Ltd.");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
              emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }
    @Override
    public String sendSubmissionNotificationToBDTeam(User user, LoanApplication loanApplication, Partner partner) {

       EnquiryPortalCommonConfig enquiryPortalCommonConfig = new EnquiryPortalCommonConfig("","");

       switch (activeProfile){
           case "oauth":
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("DEV");
               break;
           case "dev":
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("DEV");
               break;
           case "pfsdev" :
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("QA");
               break;
           case "pfsprd" :
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("PRD");
               break;
           case "pfsprddrip" :
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("PRD");
               break;
           case "pfsprdip" :
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("PRD");
               break;
           default:
               enquiryPortalCommonConfig  = enquiryPortalCommonConfigRepository.findBySystemId("");
       }

        String line1 = "Dear PFS BD Team" + System.lineSeparator();
        String line2 = "    A loan enquiry was submitted for review by PTC India Financial Services Ltd.." + System.lineSeparator();
        String line3 = "    Loan Application Id  : " +loanApplication.getEnquiryNo() + System.lineSeparator() ;
        String line4 = "    Project Name         : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd. Portal Admin";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Enquiry Portal");
        mailObject.setAppObjectId(loanApplication.getEnquiryNo().toString());


        System.out.println("Enquiry Portal Common Config : " + enquiryPortalCommonConfig.toString());

        mailObject.setToAddress(enquiryPortalCommonConfig.getBdTeamEmail());

        mailObject.setSubject("Loan enquiry for project: " +loanApplication.getProjectName() + " is submitted for review");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);

        System.out.println(" Email Notification Sent to BD Team @ :" + enquiryPortalCommonConfig.getBdTeamEmail());


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }
    @Override
    public String sendApprovalNotification(User user, LoanApplication loanApplication, Partner partner) {

        String line1 = "Dear" + " " + partner.getPartyName1() + " " + partner.getPartyName1() + System.lineSeparator();
        String line2 = "    Your loan application with the following details is taken up for processing by PTC India Financial Services Ltd.." + System.lineSeparator();
        String line3 = "    Loan Number  : " +loanApplication.getLoanEnquiryId() + System.lineSeparator() ;
        String line4 = "    Project Name : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Enquiry Portal");
        mailObject.setAppObjectId(loanApplication.getEnquiryNo().toString());
        mailObject.setToAddress(partner.getEmail());
        mailObject.setSubject("Your loan application for the project " +loanApplication.getProjectName() + " is being processed by PTC India Financial Services Ltd.");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }

    @Override
    public String sendRejectNotification(User user, LoanApplication loanApplication, Partner partner) {

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    Your loan application with the following details is rejected by PTC India Financial Services Ltd.." + System.lineSeparator();
        String line3 = "    Loan Application Id  : " +loanApplication.getEnquiryNo() + System.lineSeparator() ;
        String line4 = "    Project Name         : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Enquiry Portal");
        mailObject.setAppObjectId(loanApplication.getId().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Your loan application for the project " +loanApplication.getProjectName() + " is rejected to PTC India Financial Services Ltd.");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();


    }

    @Override
    public String sendCancelNotification(User user, LoanApplication loanApplication, Partner partner) {

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    Your loan application with the following details is cancelled" + System.lineSeparator();
        String line3 = "    Loan Application Id  : " +loanApplication.getEnquiryNo() + System.lineSeparator() ;
        String line4 = "    Project Name         : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Enquiry Portal");
        mailObject.setAppObjectId(loanApplication.getId().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Your loan application for the project " +loanApplication.getProjectName() + " is cancelled");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }

    @Override
    public String sendUpdateNotification(User user, LoanApplication loanApplication, Partner partner) {

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    Your loan application with the following details is updated" + System.lineSeparator();
        String line3 = "    Loan Application Id  : " +loanApplication.getEnquiryNo() + System.lineSeparator() ;
        String line4 = "    Project Name         : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Enquiry Portal");
        mailObject.setAppObjectId(loanApplication.getId().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Your loan application for the project " +loanApplication.getProjectName() + " is updated");
        mailObject.setMailContent(content);

         //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();

    }
}
