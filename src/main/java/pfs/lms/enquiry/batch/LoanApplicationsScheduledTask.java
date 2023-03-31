package pfs.lms.enquiry.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.LoanApplicationResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationDetailsResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationResource;
import pfs.lms.enquiry.service.ISAPIntegrationService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

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

    @Autowired
    private LoanAppraisalRepository loanAppraisalRepository;

    @Autowired
    private LoanMonitorRepository loanMonitorRepository;

    @Autowired
    private IChangeDocumentService changeDocumentService;

    public LoanApplicationsScheduledTask(LoanApplicationRepository loanApplicationRepository, ISAPIntegrationService isapIntegrationService, PartnerRepository partnerRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.isapIntegrationService = isapIntegrationService;
        this.partnerRepository = partnerRepository;
    }
    @Scheduled(fixedRateString = "${batch.loanApplicationsScheduledTask}",initialDelayString = "${batch.initialDelay}")
    public void syncLoanApplicationsToBackend() throws ParseException {
       // log.info("The time is now {}", dateFormat.format(new Date()));
       //  log.info("The time is now :" + dateFormat.format(new Date()));

        //Collect Loan Application with the following SAP Posting Statuses
        // 0 - Not Posted in SAP
        // 2 - Posting Failed
        // 4 - Approved but not Posted in SAP Yet
        List<LoanApplication> loanApplicationList = loanApplicationRepository.findByTechnicalStatusAndPostedInSAP(4,0);
        loanApplicationList.addAll(loanApplicationRepository.findByTechnicalStatusAndPostedInSAP(4,2));
        loanApplicationList.addAll(loanApplicationRepository.findByTechnicalStatusAndPostedInSAP(4,4));


        for (LoanApplication loanApplication: loanApplicationList) {

            Partner partner = (Partner) partnerRepository.findById(loanApplication.getLoanApplicant()).get();

            
             log.info("-----------------------------------------------------------------------------------------------" );
             log.info("Attempting to Post Loan Application in SAP: Loan Application :" +loanApplication.getEnquiryNo().getId());
             log.info("SAP Business Partner Number :" + partner.getPartyNumber());
             log.info("Product Code : " + loanApplication.getProductCode());
             if (loanApplication.getProductCode() == null) {
                 continue;
             }

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
                 log.info("Loan Contract Id in SAP: " + loanApplication.getLoanContractId());

                // Save Partner with SAP Business partner number
                partner.setPartyNumber(Integer.parseInt(sapLoanApplicationResource.getSapLoanApplicationDetailsResource().getBusPartnerNumber()));
                partner = partnerRepository.save(partner);

                //Update SA{ Business Partner Number to the User of the Loan Applicant
                User user = userRepository.findByEmail(partner.getEmail());
                if (user != null) {
                    user.setSapBPNumber(sapLoanApplicationResource.getSapLoanApplicationDetailsResource().getBusPartnerNumber());
                    userRepository.saveAndFlush(user);
                }
                 log.info("-----------------------------------------------------------------------------------------------" );
                 log.info("Successfully Posted Loan Application in SAP: Loan Contract Id :" +loanApplication.getLoanContractId());
                 log.info("SAP Business Partner Number :" + partner.getPartyNumber());


                 //Create entries for Loan Appraisal And Monitoring

                LoanApplication loanApplication1 = loanApplicationRepository.getOne(loanApplication.getId());

                LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication1)
                        .orElseGet(() -> {
                            LoanAppraisal obj = new LoanAppraisal();
                            obj.setLoanApplication(loanApplication1);
                            obj = loanAppraisalRepository.save(obj);

                            // Change Documents for Appraisal Header
                            changeDocumentService.createChangeDocument(
                                    obj.getId(),
                                    obj.getId().toString(),
                                    obj.getId().toString(),
                                    loanApplication1.getLoanContractId(),
                                    null,
                                    obj,
                                    "Created",
                                    "admin@gmail.com",
                                    "Appraisal", "Header");
                            log.info("Loan Appraisal Created : Loan Contract Id :" +loanApplication1.getLoanContractId());

                             return obj;
                        });


                LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
                if(loanMonitor == null)
                {
                    loanMonitor = new LoanMonitor();
                    loanMonitor.setLoanApplication(loanApplication);
                    loanMonitor.setWorkFlowStatusCode(01); loanMonitor.setWorkFlowStatusDescription("Created");
                    loanMonitor = loanMonitorRepository.save(loanMonitor);

                    // Change Documents for Monitoring Header
                    changeDocumentService.createChangeDocument(
                            loanMonitor.getId(), loanMonitor.getId().toString(), loanMonitor.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            loanMonitor,
                            "Created",
                            "admin@gmail.com",
                            "Monitoring", "Header");

                    log.info("Loan Monitoring Created : Loan Contract Id :" +loanApplication1.getLoanContractId());


                }


            } else {

                // Set SAP Posting Status to "Posting Failed"  - "2"
                loanApplication.setPostedInSAP(2);
                loanApplication = loanApplicationRepository.saveAndFlush(loanApplication);

                 log.info("-----------------------------------------------------------------------------------------------" );
                 log.info("Failed to Post Loan Application in SAP: Loan Application :" +loanApplication.getId());
                 log.info("SAP Business Partner Number :" + partner.getPartyNumber());
            }
        }

    }





}
