package pfs.lms.enquiry.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.BoardApprovalRepository;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoard;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoardRepository;
import pfs.lms.enquiry.boardapproval.deferredbyboard.DeferredByBoard;
import pfs.lms.enquiry.boardapproval.deferredbyboard.DeferredByBoardRepository;
import pfs.lms.enquiry.boardapproval.reasonfordelay.BoardApprovalReasonForDelay;
import pfs.lms.enquiry.boardapproval.reasonfordelay.BoardApprovalReasonForDelayRepository;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoard;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoardRepository;
import pfs.lms.enquiry.boardapproval.rejectedbycustomer.BoardApprovalRejectedByCustomer;
import pfs.lms.enquiry.boardapproval.rejectedbycustomer.BoardApprovalRejectedByCustomerRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.SanctionTypeRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.*;
import pfs.lms.enquiry.sanction.sanctionletter.SanctionLetter;
import pfs.lms.enquiry.sanction.sanctionletter.SanctionLetterRepository;
import pfs.lms.enquiry.service.ISAPIntegrationService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private BoardApprovalRepository boardApprovalRepository;

    @Autowired
    private ApprovalByBoardRepository approvalByBoardRepository;

    @Autowired
    private  DeferredByBoardRepository deferredByBoardRepository;

    @Autowired
    private BoardApprovalReasonForDelayRepository boardApprovalReasonForDelayRepository;

    @Autowired
    private RejectedByBoardRepository rejectedByBoardRepository;

    @Autowired
    private BoardApprovalRejectedByCustomerRepository boardApprovalRejectedByCustomerRepository;
    @Autowired
    private SanctionTypeRepository sanctionTypeRepository;
    @Autowired
    private SanctionLetterRepository sanctionLetterRepository;

    public LoanApplicationsScheduledTask(LoanApplicationRepository loanApplicationRepository,
                                         ISAPIntegrationService isapIntegrationService,
                                         PartnerRepository partnerRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.isapIntegrationService = isapIntegrationService;
        this.partnerRepository = partnerRepository;
        this.deferredByBoardRepository = deferredByBoardRepository;
    }
    @Scheduled(fixedRateString = "${batch.loanApplicationsScheduledTask}0",initialDelayString = "${batch.initialDelay}")
    public void syncSanctionLetterToBackend() throws ParseException {

         //Collect Loan Application with the following SAP Posting Statuses
        // 0 - Not Posted in SAP
        // 2 - Posting Failed
        // 4 - Approved but not Posted in SAP Yet
        List<SanctionLetter> sanctionLetterList = sanctionLetterRepository.findByPostedInSAP(0);
        sanctionLetterList.addAll(sanctionLetterRepository.findByPostedInSAP(2));
        sanctionLetterList.addAll(sanctionLetterRepository.findByPostedInSAP(4));


        for(SanctionLetter sanctionLetter: sanctionLetterList) {
            LoanApplication loanApplication = sanctionLetter.getSanction().getLoanApplication();
            log.info("-----------------------------------------------------------------------------------------------");
            log.info("Attempting to Post Sanction Letter in SAP: Loan Application :" + loanApplication.getLoanContractId());


            // Set SAP Posting Status to Attempted to Post - "1"
            sanctionLetter.setPostedInSAP(1);
            sanctionLetterRepository.saveAndFlush(sanctionLetter);

            SAPSanctionLetterResource sapSanctionLetterResource = new SAPSanctionLetterResource();
            SAPSanctionLetterDetailsResource sapSanctionLetterDetailsResource = sapSanctionLetterResource.mapSanctionLetter(sanctionLetter);

            SAPSanctionLetterResource d = new SAPSanctionLetterResource();
            d.setSapSanctionLetterDetailsResource(sapSanctionLetterDetailsResource);

            sapSanctionLetterResource = isapIntegrationService.postSanctionLetter(d);
            if (sapSanctionLetterResource != null) {
                //loanApplication.responseFromSAP(sapLoanApplicationResource);

                // Set SAP Posting Status to "Posting Successfully"  - "3"
                sanctionLetter.setPostedInSAP(3);
                sanctionLetter = sanctionLetterRepository.saveAndFlush(sanctionLetter);
                log.info("Loan Contract Id in SAP: " + loanApplication.getLoanContractId());

                log.info("-----------------------------------------------------------------------------------------------");
                log.info("Successfully Posted Sanction Letter in SAP: Loan Contract Id :" + loanApplication.getLoanContractId());


            }else {

                // Set SAP Posting Status to "Posting Failed"  - "2"
                sanctionLetter.setPostedInSAP(2);
                sanctionLetter = sanctionLetterRepository.saveAndFlush(sanctionLetter);

                log.info("-----------------------------------------------------------------------------------------------" );
                log.info("Failed to Post Sanction Letter in SAP: Loan Application :" +loanApplication.getId());
            }
        }


    }


    @Scheduled(fixedRateString = "${batch.loanApplicationsScheduledTask}0",initialDelayString = "${batch.initialDelay}")
    public void syncLoanApplicationsToBackend() throws ParseException {

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

            //Map Loan Application and Partner
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

                //Update SAP Business Partner Number to the User of the Loan Applicant
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
