package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.loanpartner.ILoanPartnerService;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerResource;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.LoanApplicationResource;
import pfs.lms.enquiry.service.ILoanApplicationService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationService implements ILoanApplicationService {

    private final PartnerService partnerService;

    private final LoanApplicationRepository loanApplicationRepository;

    private final PartnerRepository partnerRepository;

    private final UserRepository userRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final ILoanPartnerService loanPartnerService;

    private final IChangeDocumentService changeDocumentService;
    private final LoanMonitorRepository loanMonitorRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;

    @Override
    public LoanApplication save(LoanApplicationResource resource, String username) {

        //Set PostedInSAP to "Not Posted" - "0"
        if (resource.getLoanApplication().getPostedInSAP() == null)
            resource.getLoanApplication().setPostedInSAP(0);

        //Set Group Company from Partner Details
        resource.getLoanApplication().setGroupCompany(resource.getPartner().getGroupCompany());

        Partner applicant = new Partner();

        // Check if the Email id in the Loan Application, is already a user
        // If yes, Update the Partner with the details
        Partner existingPartner = null;
        if (resource.getPartner().getPartyNumber() == null) {
            existingPartner = partnerService.getOne(resource.getPartner().getEmail());
        } else  {
            existingPartner = partnerRepository.findByPartyNumber(resource.getPartner().getPartyNumber());
        }


        if (existingPartner != null) {
            existingPartner.setAddressLine1(resource.getPartner().getAddressLine1());
            existingPartner.setAddressLine2(resource.getPartner().getAddressLine2());
            existingPartner.setCity(resource.getPartner().getCity());
            existingPartner.setContactNumber(resource.getPartner().getContactNumber());
            existingPartner.setContactPersonName(resource.getPartner().getContactPersonName());
            existingPartner.setCountry(resource.getPartner().getCountry());
            existingPartner.setGroupCompany(resource.getPartner().getGroupCompany());
            existingPartner.setPan(resource.getPartner().getPan());
            existingPartner.setPartyCategory(resource.getPartner().getPartyCategory());
            existingPartner.setPartyName1(resource.getPartner().getPartyName1());
            existingPartner.setPartyName2(resource.getPartner().getPartyName2());
            existingPartner.setPostalCode(resource.getPartner().getPostalCode());
            existingPartner.setState(resource.getPartner().getState());
            existingPartner.setStreet(resource.getPartner().getStreet());
            existingPartner.setEmail(resource.getPartner().getEmail());
            existingPartner.setIndustrySector(resource.getPartner().getIndustrySector());

            //existingPartner.setPartyNumber(existingPartner.getPartyNumber());


            existingPartner.setChangedOn(LocalDate.now());
            existingPartner.setChangedAt(LocalTime.now());
            existingPartner.setChangedByUserName(username);


            applicant = partnerService.save(existingPartner);

        } else {
            // Create new Partner with the role TR0100
            applicant = new Partner();
            applicant.setUserName(username);
            applicant.setPartyRole("TR0100");
            applicant.setAddressLine1(resource.getPartner().getAddressLine1());
            applicant.setAddressLine2(resource.getPartner().getAddressLine2());
            applicant.setCity(resource.getPartner().getCity());
            applicant.setContactNumber(resource.getPartner().getContactNumber());
            applicant.setContactPersonName(resource.getPartner().getContactPersonName());
            applicant.setCountry(resource.getPartner().getCountry());
            applicant.setGroupCompany(resource.getPartner().getGroupCompany());
            applicant.setPan(resource.getPartner().getPan());
            applicant.setPartyCategory(resource.getPartner().getPartyCategory());
            applicant.setPartyName1(resource.getPartner().getPartyName1());
            applicant.setPartyName2(resource.getPartner().getPartyName2());
            applicant.setPartyNumber(resource.getPartner().getPartyNumber());
            applicant.setPostalCode(resource.getPartner().getPostalCode());
            applicant.setState(resource.getPartner().getState());
            applicant.setStreet(resource.getPartner().getStreet());
            applicant.setEmail(resource.getPartner().getEmail());
            applicant.setIndustrySector(resource.getPartner().getIndustrySector());
            applicant = partnerService.save(applicant);
        }


        // If No, create a new user

        // Check if the user is a Main Loan Partner or a Loan Officer


        // If the user is a Main Loan Partner


        //

        //Get the partner from partner service
//        Partner applicant = partnerService.getOne(resource.getPartner().getEmail());
//        if (applicant == null) {
//            applicant = new Partner();
//            applicant.setUserName(username);
//            applicant.setPartyRole("TR0100");
//        }
//        applicant.setAddressLine1(resource.getPartner().getAddressLine1());
//        applicant.setAddressLine2(resource.getPartner().getAddressLine2());
//        applicant.setCity(resource.getPartner().getCity());
//        applicant.setContactNumber(resource.getPartner().getContactNumber());
//        applicant.setContactPersonName(resource.getPartner().getContactPersonName());
//        applicant.setCountry(resource.getPartner().getCountry());
//        applicant.setGroupCompany(resource.getPartner().getGroupCompany());
//        applicant.setPan(resource.getPartner().getPan());
//        applicant.setPartyCategory(resource.getPartner().getPartyCategory());
//        applicant.setPartyName1(resource.getPartner().getPartyName1());
//        applicant.setPartyName2(resource.getPartner().getPartyName2());
//        applicant.setPartyNumber(resource.getPartner().getPartyNumber());
//        applicant.setPostalCode(resource.getPartner().getPostalCode());
//        applicant.setState(resource.getPartner().getState());
//        applicant.setStreet(resource.getPartner().getStreet());
//        applicant.setEmail(resource.getPartner().getEmail());
//        applicant = partnerService.save(applicant);
//
//        // Partner applicant = partnerService.save(resource.getPartner()); // delete
//        // Partner app = partnerService.getOne(username); // delete

        //Set it to the Loan Application
        LoanApplication loanApplication = resource.getLoanApplication();

        LoanApplication loanApplicationExisting = new LoanApplication();

        // Check if loan application is existing
        if (loanApplication.getId() != null) {
            loanApplicationExisting = loanApplicationRepository.getOne(loanApplication.getId());
        }

        if (loanApplicationExisting.getId() != null) {

            loanApplicationExisting.setPostedInSAP(loanApplication.getPostedInSAP());
            loanApplicationExisting.setFunctionalStatus(loanApplication.getFunctionalStatus());

            if (loanApplication.getTechnicalStatus() != null)
                loanApplicationExisting.setTechnicalStatus(loanApplication.getTechnicalStatus());

            loanApplicationExisting.setAssistanceType(loanApplication.getAssistanceType());
            loanApplicationExisting.setBusPartnerNumber(loanApplication.getbusPartnerNumber());
            loanApplicationExisting.setDecisionDate(loanApplication.getDecisionDate());

            loanApplicationExisting.setGroupCompany(applicant.getGroupCompany());

            loanApplicationExisting.setId(loanApplication.getId());
//            loanApplicationExisting.setEnquiryNo(loanApplication.getEnquiryNo());
            loanApplicationExisting.setEquity(loanApplication.getEquity());

            loanApplicationExisting.setTerm(loanApplication.getTerm());

            loanApplicationExisting.setExpectedInterestRate(loanApplication.getExpectedInterestRate());
            loanApplicationExisting.setExpectedSubDebt(loanApplication.getExpectedSubDebt());
            loanApplicationExisting.setFinalDecisionStatus(loanApplication.getFinalDecisionStatus());
            loanApplicationExisting.setFinancingType(loanApplication.getFinancingType());
            loanApplicationExisting.setAssistanceType(loanApplication.getAssistanceType());

            loanApplicationExisting.setGroupCompany(loanApplication.getGroupCompany());
            loanApplicationExisting.setKeyPromoter(loanApplication.getKeyPromoter());
            loanApplicationExisting.setLeadFILoanAmount(loanApplication.getLeadFILoanAmount());
            loanApplicationExisting.setLeadFIName(loanApplication.getLeadFIName());

            loanApplicationExisting.setLoanEnquiryDate(loanApplication.getLoanEnquiryDate());
            loanApplicationExisting.setLoanClass(loanApplication.getLoanClass());
            loanApplicationExisting.setLoanPurpose(loanApplication.getLoanPurpose());
            loanApplicationExisting.setPfsDebtAmount(loanApplication.getPfsDebtAmount());

            loanApplicationExisting.setLoanContractAmount(loanApplication.getLoanContractAmount());
            loanApplicationExisting.setLoanCurrentContractAmount(loanApplication.getLoanCurrentContractAmount());
            loanApplicationExisting.setLoanDisbursedAmount(loanApplication.getLoanDisbursedAmount());
            loanApplicationExisting.setLoanRevisedSanctionAmount(loanApplication.getLoanRevisedSanctionAmount());

            loanApplicationExisting.setPfsSubDebtAmount(loanApplication.getPfsSubDebtAmount());

            // Keep initiators as is
            // loanApplicationExisting.setMonitoringDepartmentInitiator(loanApplication.)

            loanApplicationExisting.setProductCode(loanApplication.getProductCode());
            loanApplicationExisting.setProjectAmountCurrency(loanApplication.getProjectAmountCurrency());
            loanApplicationExisting.setProjectCapacity(loanApplication.getProjectCapacity());
            loanApplicationExisting.setProjectCapacityUnit(loanApplication.getProjectCapacityUnit());
            loanApplicationExisting.setProjectCost(loanApplication.getProjectCost());
            loanApplicationExisting.setProjectDebtAmount(loanApplication.getProjectDebtAmount());
            loanApplicationExisting.setProjectDistrict(loanApplication.getProjectDistrict());
            loanApplicationExisting.setProjectName(loanApplication.getProjectName());
            loanApplicationExisting.setProjectLocationState(loanApplication.getProjectLocationState());
            loanApplicationExisting.setProjectType(loanApplication.getProjectType());

            loanApplicationExisting.setPromoterAreaOfBusinessNature(loanApplication.getPromoterAreaOfBusinessNature());
            loanApplicationExisting.setPromoterKeyDirector(loanApplication.getPromoterKeyDirector());
            loanApplicationExisting.setPromoterName(loanApplication.getPromoterName());
            loanApplicationExisting.setPromoterNetWorthAmount(loanApplication.getPromoterNetWorthAmount());
            loanApplicationExisting.setPromoterPATAmount(loanApplication.getPromoterPATAmount());

            loanApplicationExisting.setRating(loanApplication.getRating());

            if(loanApplication.getRejectionCategory() != null) {
                loanApplicationExisting.setRejectionCategory(loanApplication.getRejectionCategory());
            }
            if (loanApplication.getRejectionDate() != null)
                loanApplicationExisting.setRejectionDate(loanApplication.getRejectionDate());
            if (loanApplication.getRejectionReason() != null)
                loanApplicationExisting.setRejectionReason(loanApplication.getRejectionReason());
            if (loanApplication.getScheduledCOD() != null)
                loanApplicationExisting.setScheduledCOD(loanApplication.getScheduledCOD());

            if (loanApplication.getUserBPNumber() != null)
                loanApplicationExisting.setUserBPNumber(loanApplicationExisting.getUserBPNumber());

            if (loanApplication.getProjectDepartmentInitiator() != null)
                loanApplicationExisting.setProjectDepartmentInitiator(loanApplication.getProjectDepartmentInitiator());
            if (loanApplication.getMonitoringDepartmentInitiator() != null)
                loanApplicationExisting.setMonitoringDepartmentInitiator(loanApplication.getMonitoringDepartmentInitiator());
            if (loanApplicationExisting.getRiskDepartmentInitiator() != null)
                loanApplicationExisting.setRiskDepartmentInitiator(loanApplication.getRiskDepartmentInitiator());


            loanApplicationExisting.setContactBranchAddress(loanApplication.getContactBranchAddress());
            loanApplicationExisting.setContactDepartment(loanApplication.getContactDepartment());
            loanApplicationExisting.setContactDesignation(loanApplication.getContactDesignation());
            loanApplicationExisting.setContactEmail(loanApplication.getContactEmail());
            loanApplicationExisting.setContactFaxNumber(loanApplication.getContactFaxNumber());
            loanApplicationExisting.setContactLandLinePhone(loanApplication.getContactLandLinePhone());
            loanApplicationExisting.setContactTelePhone(loanApplication.getContactTelePhone());

            loanApplicationExisting.setTenorYear(loanApplication.getTenorYear());
            loanApplicationExisting.setTenorMonth(loanApplication.getTenorMonth());

            if (loanApplicationExisting != null)
                loanApplicationExisting.setLoanEnquiryDate(LocalDate.now());

            loanApplicationExisting.setChangedOn(LocalDate.now());
            loanApplicationExisting.setChangedAt(LocalTime.now());
            loanApplicationExisting.setChangedByUserName(username);


            loanApplicationExisting.setLoanApplicant(applicant.getId());
            loanApplicationExisting.applicant(applicant);
            //loanApplicationExisting.created(applicant,username);

            loanApplicationExisting.setLoanApplicant(applicant.getId());


            //Save and return the Loan Application
            loanApplication = loanApplicationRepository.save(loanApplicationExisting);

        } else {

            loanApplication.setLoanEnquiryDate(LocalDate.now());
            loanApplication.applicant(applicant);
            loanApplication.created(applicant,username);

            // Set Technical Status as Created
            loanApplication.setTechnicalStatus(1);
            loanApplication.setLoanApplicant(applicant.getId());

            // Set Temporary ID for Contract
            //loanApplicationRepository.count();
//            loanApplication.setLoanContractId("TEMPID-" + loanApplicationRepository.count() + 100);

            //Create and return the Loan Application
            loanApplication = loanApplicationRepository.save(loanApplication);
        }

        // Check if the LoanPartner entry is present for the main loan partner - If else, create it

        LoanPartner loanPartner = loanPartnerRepository.findByLoanApplicationAndBusinessPartnerId(loanApplication,loanApplication.getbusPartnerNumber().toString());
        if (loanPartner == null) {
            loanPartner = new LoanPartner();

            LoanPartnerResource loanPartnerResource = new LoanPartnerResource();
            loanPartnerResource.setLoanApplicationId(loanApplication.getId());
            loanPartnerResource.setRoleType("TR0100");
            loanPartnerResource.setBusinessPartnerId(loanApplication.getbusPartnerNumber().toString());
            loanPartnerResource.setStartDate(loanApplication.getCreatedOn());
            loanPartnerResource.setRoleDescription("Main Loan Partner");
            loanPartnerResource.setBusinessPartnerName(applicant.getPartyName1() + " " + applicant.getPartyName2());
            loanPartnerResource.setSerialNumber(1);
            loanPartnerResource.setKycStatus("Not Done");
            loanPartnerResource.setKycRequired(true);
            loanPartnerService.createLoanPartner(loanPartnerResource,username);
        }


        System.out.println("Loan Application :" + loanApplication);
        return loanApplication;
    }

    @Override
    public LoanApplication migrate(LoanApplicationResource resource, String username) {


        System.out.println("-------------- Migrating Loan number : " + resource.getLoanApplication().getLoanContractId() + "-----------------------------------------------------------");
        System.out.println("-------------- BusinessPartner ID    : " + resource.getPartner().getPartyNumber() + "-----------------------------------------------------------");

        System.out.println("LOAN Application --------------");
        System.out.println(resource.getLoanApplication());
        System.out.println("LOAN Application --------------");


        System.out.println("PFS debt Amount --------------:" + resource.getLoanApplication().getPfsDebtAmount());
        System.out.println("Sanction Amount --------------:" + resource.getLoanApplication().getLoanRevisedSanctionAmount());
        System.out.println("Loan Contract Amount ---------:" + resource.getLoanApplication().getLoanContractAmount());
        System.out.println("Loan Current Contract Amt.----:" + resource.getLoanApplication().getLoanCurrentContractAmount());
        System.out.println("Loan Disbursed Amt.-----------:" + resource.getLoanApplication().getLoanDisbursedAmount());


        Partner partner = new Partner();
        //resource.getPartner();

        // Temporary solution to get data migration done for BusinessPartners with empty email id
        if (resource.getPartner().getEmail() == null || resource.getPartner().getEmail().equals("")) {
            partner.setUserName(resource.getLoanApplication().getLoanContractId() + resource.getPartner().getPartyName1());
            partner.setEmail(resource.getLoanApplication().getLoanContractId() + resource.getPartner().getPartyName1() +"@dummy.co.in");
        } else {
            partner.setUserName(resource.getPartner().getEmail());
            partner.setEmail(resource.getPartner().getEmail());
        }

        partner.setPartyRole("TR0100");
        partner.setAddressLine1(resource.getPartner().getAddressLine1());
        partner.setAddressLine2(resource.getPartner().getAddressLine2());
        partner.setCity(resource.getPartner().getCity());
        partner.setContactNumber(resource.getPartner().getContactNumber());
        partner.setContactPersonName(resource.getPartner().getContactPersonName());
        partner.setCountry(resource.getPartner().getCountry());
        partner.setGroupCompany(resource.getPartner().getGroupCompany());
        partner.setPan(resource.getPartner().getPan());
        partner.setPartyCategory(resource.getPartner().getPartyCategory());
        partner.setPartyName1(resource.getPartner().getPartyName1());
        partner.setPartyName2(resource.getPartner().getPartyName2());
        partner.setPartyNumber(resource.getPartner().getPartyNumber());
        partner.setPostalCode(resource.getPartner().getPostalCode());
        partner.setState(resource.getPartner().getState());
        partner.setStreet(resource.getPartner().getStreet());
        partner = partnerService.migrate(partner);

        //Set it to the Loan Application

        LoanApplication loanApplication = resource.getLoanApplication();


        loanApplication.applicant(partner);
        loanApplication.created(partner, username);

        //Save and return the Loan Application
        loanApplication = this.migrateUpdate(loanApplication, partner, username);

        System.out.println("-------------Finished Migrating Loan number : " + resource.getLoanApplication().getLoanContractId() + "-----------------------------------------------------------");

        // Add entries for partners associated with the loan


        //Create entries for Loan Appraisal and Monitoring

        LoanApplication loanApplication1 = loanApplicationRepository.getOne(loanApplication.getId());

        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication1)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication1);
                    obj.setLoanContractId(loanApplication1.getLoanContractId());
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

        return loanApplication;
    }


    @Override
    public LoanApplication migrateUpdate(LoanApplication loanApplication, Partner partner, String username) {


        LoanApplication loanApplicationExisting = new LoanApplication();

        loanApplication.setLoanEnquiryDate(LocalDate.now());

        // Check if loan application is existing
        if (loanApplication.getLoanContractId() != null) {
            loanApplicationExisting = loanApplicationRepository.findByLoanContractId(loanApplication.getLoanContractId());
        }

        if (loanApplicationExisting != null) {

            loanApplicationExisting.setFunctionalStatus(loanApplication.getFunctionalStatus());

            if (loanApplication.getTechnicalStatus() != null)
                loanApplicationExisting.setTechnicalStatus(loanApplication.getTechnicalStatus());
            else
                loanApplicationExisting.setTechnicalStatus(4);

            if (loanApplication.getAssistanceType() != null)
                loanApplicationExisting.setAssistanceType(loanApplication.getAssistanceType());

            loanApplicationExisting.setBusPartnerNumber(loanApplication.getbusPartnerNumber());

            if (loanApplication.getDecisionDate() != null)
                loanApplicationExisting.setDecisionDate(loanApplication.getDecisionDate());

//            loanApplicationExisting.setId(loanApplication.getId());
            loanApplicationExisting.setEnquiryNo(loanApplication.getEnquiryNo());

            if (loanApplication.getEquity() != null)
                loanApplicationExisting.setEquity(loanApplication.getEquity());

            if (loanApplication.getExpectedInterestRate() != null)
                loanApplicationExisting.setExpectedInterestRate(loanApplication.getExpectedInterestRate());
            if (loanApplication.getExpectedSubDebt() != null)
                loanApplicationExisting.setExpectedSubDebt(loanApplication.getExpectedSubDebt());

            if (loanApplication.getFinalDecisionStatus() != null)
                loanApplicationExisting.setFinalDecisionStatus(loanApplication.getFinalDecisionStatus());

            if (loanApplication.getFinancingType() != null)
                loanApplicationExisting.setFinancingType(loanApplication.getFinancingType());

            if (loanApplication.getAssistanceType() != null)
                loanApplicationExisting.setAssistanceType(loanApplication.getAssistanceType());

            if (loanApplication.getGroupCompany() != null)
                loanApplicationExisting.setGroupCompany(loanApplication.getGroupCompany());

            if (loanApplication.getKeyPromoter() != null)
                loanApplicationExisting.setKeyPromoter(loanApplication.getKeyPromoter());

            if (loanApplication.getLeadFILoanAmount() != null)
                loanApplicationExisting.setLeadFILoanAmount(loanApplication.getLeadFILoanAmount());

            if (loanApplication.getLeadFIName() != null)
                loanApplicationExisting.setLeadFIName(loanApplication.getLeadFIName());

            if (loanApplication.getLoanEnquiryDate() != null)
                loanApplicationExisting.setLoanEnquiryDate(loanApplication.getLoanEnquiryDate());

            if (loanApplication.getLoanClass() != null)
                loanApplicationExisting.setLoanClass(loanApplication.getLoanClass());

            if (loanApplication.getLoanPurpose() != null)
                loanApplicationExisting.setLoanPurpose(loanApplication.getLoanPurpose());
            if (loanApplication.getPfsDebtAmount() != null)
                loanApplicationExisting.setPfsDebtAmount(loanApplication.getPfsDebtAmount());

            if (loanApplication.getLoanContractAmount() != null)
                loanApplicationExisting.setLoanContractAmount(loanApplication.getLoanContractAmount());
            if (loanApplication.getLoanCurrentContractAmount() != null)
                loanApplicationExisting.setLoanCurrentContractAmount(loanApplication.getLoanCurrentContractAmount());
            if (loanApplication.getLoanDisbursedAmount() != null)
                loanApplicationExisting.setLoanDisbursedAmount(loanApplication.getLoanDisbursedAmount());
            if (loanApplication.getLoanRevisedSanctionAmount() != null)
                loanApplicationExisting.setLoanRevisedSanctionAmount(loanApplication.getLoanRevisedSanctionAmount());

            if (loanApplication.getPfsSubDebtAmount() != null)
                loanApplicationExisting.setPfsSubDebtAmount(loanApplication.getPfsSubDebtAmount());

            // Keep initiators as is
            // loanApplicationExisting.setMonitoringDepartmentInitiator(loanApplication.)
            if (loanApplication.getProductCode() != null)
                loanApplicationExisting.setProductCode(loanApplication.getProductCode());
            if (loanApplication.getProjectAmountCurrency() != null)
                loanApplicationExisting.setProjectAmountCurrency(loanApplication.getProjectAmountCurrency());

            if (loanApplication.getProjectCapacity() != null)
                loanApplicationExisting.setProjectCapacity(loanApplication.getProjectCapacity());

            if (loanApplication.getProjectCapacityUnit() != null)
                loanApplicationExisting.setProjectCapacityUnit(loanApplication.getProjectCapacityUnit());

            if (loanApplication.getProjectCost() != null)
                loanApplicationExisting.setProjectCost(loanApplication.getProjectCost());

            if (loanApplication.getProjectDebtAmount() != null)
                loanApplicationExisting.setProjectDebtAmount(loanApplication.getProjectDebtAmount());

            if (loanApplication.getProjectDistrict() != null)
                loanApplicationExisting.setProjectDistrict(loanApplication.getProjectDistrict());

            if (loanApplication.getProjectName() != null)
                loanApplicationExisting.setProjectName(loanApplication.getProjectName());

            if (loanApplication.getProjectLocationState() != null)
                loanApplicationExisting.setProjectLocationState(loanApplication.getProjectLocationState());

            if (loanApplication.getProjectType() != null)
                loanApplicationExisting.setProjectType(loanApplication.getProjectType());

            if (loanApplication.getPromoterAreaOfBusinessNature() != null)
                loanApplicationExisting.setPromoterAreaOfBusinessNature(loanApplication.getPromoterAreaOfBusinessNature());

            if (loanApplicationExisting.getPromoterKeyDirector() != null)
                loanApplicationExisting.setPromoterKeyDirector(loanApplication.getPromoterKeyDirector());

            if (loanApplication.getPromoterName() != null)
                loanApplicationExisting.setPromoterName(loanApplication.getPromoterName());

            if (loanApplication.getPromoterNetWorthAmount() != null)
                loanApplicationExisting.setPromoterNetWorthAmount(loanApplication.getPromoterNetWorthAmount());
            if (loanApplication.getPromoterPATAmount() != null)
                loanApplicationExisting.setPromoterPATAmount(loanApplication.getPromoterPATAmount());

            if (loanApplication.getRating() != null)
                loanApplicationExisting.setRating(loanApplication.getRating());

            if(loanApplication.getTenorYear() != null)
                loanApplicationExisting.setTenorYear(loanApplication.getTenorYear());

            if(loanApplication.getTenorMonth() != null)
                loanApplicationExisting.setTenorMonth(loanApplication.getTenorMonth());


            if (loanApplication.getRejectionDate() != null)
                loanApplicationExisting.setRejectionDate(loanApplication.getRejectionDate());
            if (loanApplication.getRejectionReason() != null)
                loanApplicationExisting.setRejectionReason(loanApplication.getRejectionReason());
            if (loanApplication.getScheduledCOD() != null)
                loanApplicationExisting.setScheduledCOD(loanApplication.getScheduledCOD());

            if (loanApplication.getUserBPNumber() != null)
                loanApplicationExisting.setUserBPNumber(loanApplicationExisting.getUserBPNumber());

            if (loanApplication.getProjectDepartmentInitiator() != null)
                loanApplicationExisting.setProjectDepartmentInitiator(loanApplication.getProjectDepartmentInitiator());
            if (loanApplication.getMonitoringDepartmentInitiator() != null)
                loanApplicationExisting.setMonitoringDepartmentInitiator(loanApplication.getMonitoringDepartmentInitiator());
            if (loanApplicationExisting.getRiskDepartmentInitiator() != null)
                loanApplicationExisting.setRiskDepartmentInitiator(loanApplication.getRiskDepartmentInitiator());

            loanApplicationExisting.created(partner,username);
            loanApplicationExisting.applicant(partner);

            //Save and return the Loan Application
            loanApplication = loanApplicationRepository.save(loanApplicationExisting);

        } else {

//            loanApplication.applicant(applicant);
//            loanApplication.created(applicant);

            //Save and return the Loan Application
            loanApplication = loanApplicationRepository.save(loanApplication);
        }

        System.out.println("Loan Application :" + loanApplication);
        return loanApplication;
    }

    /*
         If Partner to the User Belongs to the role TR0100, return only the loans associcated with that partner.
         Else
         Return all Loan Applications
     */

    @Override
    public List<LoanApplication> searchLoans(HttpServletRequest request, Pageable pageable) {

        String userName = request.getUserPrincipal().getName();

        //Get User details of User
        User user= userRepository.findByEmail(userName);

        //In case of Admin User or Other PFS User Roles - Partner does not exist
        if (user == null)
            return loanApplicationRepository.findAll(pageable).getContent();

        List<LoanApplication> loanApplications = new ArrayList<>();

        if (user.getRole().equals("TR0100")) {
            List<LoanApplication> loanApplicationsForPartner = new ArrayList<>();
            List<Partner> partners = partnerRepository.findByEmail(user.getEmail());
                for (Partner partner : partners) {
                    loanApplicationsForPartner = loanApplicationRepository.findByLoanApplicant(partner.getId(), pageable).getContent();
                    loanApplications.addAll(loanApplicationsForPartner);
                }
            }
         else {
            loanApplications =loanApplicationRepository.findAll(pageable).getContent();
        }


        return loanApplications;
    }
}
