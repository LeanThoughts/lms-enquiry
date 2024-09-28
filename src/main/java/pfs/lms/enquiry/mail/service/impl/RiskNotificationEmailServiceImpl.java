package pfs.lms.enquiry.mail.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.mail.domain.MailObject;
import pfs.lms.enquiry.mail.service.EmailService;
import pfs.lms.enquiry.mail.service.RiskNotificationEmailService;

import java.util.concurrent.CompletableFuture;

/**
 * Created by sajeev on 17-Feb-19.
 */
@Component
@Slf4j
public class RiskNotificationEmailServiceImpl implements RiskNotificationEmailService {

    @Autowired
    EmailService emailService;

    @Override
    public String sendOfficerAssignmentNotification(User user, LoanApplication loanApplication , String assignmentType) {

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    Your are assigned as the " +assignmentType + " to initiate the risk evaluation for the following project." + System.lineSeparator();
        String line3 = "    Loan Contract Number : " +loanApplication.getLoanContractId() + System.lineSeparator() ;
        String line4 = "    Project Name         : " +loanApplication.getProjectName() + System.lineSeparator();
        String line5 = "Regards," + System.lineSeparator() + "Risk Model System Administrator";
        String content = line1 + line2 + line3 + line4 + line5;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Risk Modeller Portal");
        mailObject.setAppObjectId(loanApplication.getEnquiryNo().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Assignment: Risk Model Evaluation for " +loanApplication.getProjectName() + " ");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
              emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }

    @Override
    public String sendRiskOfficerAssignmentNotification(User user, LoanApplication loanApplication ) {

        log.info ("Sending Notification to Risk Officer " );
        log.info("User Object : " + user.toString());

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    You are requested to assign an officer in the risk department to process the risk model evaluation for the following project" + System.lineSeparator();
        String line3 = "    " + System.lineSeparator();
        String line4 = "    Loan Contract  : " +loanApplication.getLoanContractId() + System.lineSeparator() ;
        String line5 = "    Project Name : " +loanApplication.getProjectName() + System.lineSeparator();
        String line6 = "Regards," + System.lineSeparator() + "PTC India Financial Services Ltd.";
        String content = line1 + line2 + line3 + line4 + line5 + line6;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Risk Modeller Portal");
        mailObject.setAppObjectId(loanApplication.getEnquiryNo().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Request to assign officer for the project: " +loanApplication.getProjectName() + " to review risk evaluation");
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }

    @Override
    public String sendPremlimRiskNotification(User user, LoanApplication loanApplication, String remarks ) {

        log.info ("Sending Premlim Rsik Notification to Risk Department " );
        log.info("User Object : " + user.toString());

        String line1 = "Dear" + " " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator();
        String line2 = "    " + System.lineSeparator();
        String line3 = "ICC In-principle approval is completed for the following loan enquiry" + System.lineSeparator();
        String line4 = "    " + System.lineSeparator();
        String line5 = "    Loan Enquiry Id  : " +loanApplication.getEnquiryNo().getId() + System.lineSeparator() ;
        String line6 = "    Project Name     : " +loanApplication.getProjectName() + System.lineSeparator();
        String line7 = "    Remarks from ICC In-Princple Approval : " + remarks;
        String line8 = "    " + System.lineSeparator();
        String line9 = "You are requested to conduct a Preliminary Risk assessment for the above loan enquiry" + System.lineSeparator();
        String line10 = "    " + System.lineSeparator();
        String line11 = "Regards," + System.lineSeparator() + "PFS Loans Portal";

        String content = line1 + line2 + line3 + line4 + line5 + line6 + line7 + line8 + line9 + line10 + line10 +line11;

        MailObject mailObject = new MailObject();
        mailObject.setSendingApp("PFS Loan Processing Appliction");
        mailObject.setAppObjectId(loanApplication.getEnquiryNo().toString());
        mailObject.setToAddress(user.getEmail());
        mailObject.setSubject("Preliminary Risk Assessment for Loan Enquiry: " +loanApplication.getProjectName()  );
        mailObject.setMailContent(content);

        //mailObject = emailService.sendEmailMessage(mailObject);


        CompletableFuture.runAsync(() -> {
            // method call or code to be asynch.
            emailService.sendEmailMessage(mailObject);

        });

        return null; //mailObject.getId().toString();
    }


}
