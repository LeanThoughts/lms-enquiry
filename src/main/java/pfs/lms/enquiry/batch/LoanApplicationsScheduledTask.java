package pfs.lms.enquiry.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.LoanApplicationResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationDetailsResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationResource;
import pfs.lms.enquiry.service.ISAPIntegrationService;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sajeev on 11-Aug-19.
 */
@Service
@Transactional
public class LoanApplicationsScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(LoanApplicationsScheduledTask.class);

    @Autowired
    private final LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private  final ISAPIntegrationService isapIntegrationService;

    @Autowired
    private final PartnerRepository partnerRepository;

    @Autowired
    private UserRepository userRepository;



    public LoanApplicationsScheduledTask(LoanApplicationRepository loanApplicationRepository, ISAPIntegrationService isapIntegrationService, PartnerRepository partnerRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.isapIntegrationService = isapIntegrationService;
        this.partnerRepository = partnerRepository;
    }

    @Scheduled(fixedRate = 2000)
    public void syncLoanApplicationsToBackend() throws ParseException {
       // log.info("The time is now {}", dateFormat.format(new Date()));
       // System.out.println("The time is now :" + dateFormat.format(new Date()));

        //Collect Loan Application with the following SAP Posting Statuses
        // 0 - Not Posted in SAP
        // 2 - Posting Failed
        // 4 - Approved but not Posted in SAP Yet
        List<LoanApplication> loanApplicationList = loanApplicationRepository.findByTechnicalStatusAndPostedInSAP(4,0);
        loanApplicationList.addAll(loanApplicationRepository.findByTechnicalStatusAndPostedInSAP(4,2));
        loanApplicationList.addAll(loanApplicationRepository.findByTechnicalStatusAndPostedInSAP(4,4));



        for (LoanApplication loanApplication: loanApplicationList) {

            Partner partner = (Partner) partnerRepository.findById(loanApplication.getLoanApplicant()).get();


            System.out.println("-----------------------------------------------------------------------------------------------" );
            System.out.println("Attempting to Post Loan Application in SAP: Loan Application :" +loanApplication.getLoanEnquiryId());
            System.out.println("SAP Business Partner Number :" + partner.getPartyNumber());

            // Set SAP Posting Status to Attempted to Post - "1"
            loanApplication.setPostedInSAP(1);
            loanApplicationRepository.saveAndFlush(loanApplication);

            SAPLoanApplicationResource sapLoanApplicationResource = new SAPLoanApplicationResource();

            LoanApplicationResource loanApplicationResource = new LoanApplicationResource();

            loanApplicationResource.setPartner(partner);
            loanApplicationResource.setLoanApplication(loanApplication);


            // Find Last Changed By User
            User lastChangedByUser = userRepository.findByEmail(loanApplication.getChangedByUserName());

            SAPLoanApplicationDetailsResource detailsResource=
                    sapLoanApplicationResource.mapLoanApplicationToSAP(loanApplication,partner,lastChangedByUser);


            SAPLoanApplicationResource d = new SAPLoanApplicationResource();
            d.setSapLoanApplicationDetailsResource(detailsResource);

            sapLoanApplicationResource =   isapIntegrationService.postLoanApplication(d);

            if (sapLoanApplicationResource != null) {
                loanApplication.responseFromSAP(sapLoanApplicationResource);

                // Set SAP Posting Status to "Posting Successfully"  - "3"
                loanApplication.setPostedInSAP(3);
                loanApplication = loanApplicationRepository.saveAndFlush(loanApplication);
                System.out.println("Loan Contract Id in SAP: " + loanApplication.getLoanContractId());

                // Save Partner with SAP Business partner number
                partner.setPartyNumber(Integer.parseInt(sapLoanApplicationResource.getSapLoanApplicationDetailsResource().getBusPartnerNumber()));
                partner = partnerRepository.save(partner);

                //Update SA{ Business Partner Number to the User of the Loan Applicant
                User user = userRepository.findByEmail(partner.getEmail());
                if (user != null) {
                    user.setSapBPNumber(sapLoanApplicationResource.getSapLoanApplicationDetailsResource().getBusPartnerNumber());
                    userRepository.saveAndFlush(user);
                }
                System.out.println("-----------------------------------------------------------------------------------------------" );
                System.out.println("Successfully Posted Loan Application in SAP: Loan Contract Id :" +loanApplication.getLoanContractId());
                System.out.println("SAP Business Partner Number :" + partner.getPartyNumber());


            } else {

                // Set SAP Posting Status to "Posting Failed"  - "2"
                loanApplication.setPostedInSAP(2);
                loanApplication = loanApplicationRepository.saveAndFlush(loanApplication);

                System.out.println("-----------------------------------------------------------------------------------------------" );
                System.out.println("Failed to Post Loan Application in SAP: Loan Application :" +loanApplication.getId());
                System.out.println("SAP Business Partner Number :" + partner.getPartyNumber());
            }
        }

    }





}
