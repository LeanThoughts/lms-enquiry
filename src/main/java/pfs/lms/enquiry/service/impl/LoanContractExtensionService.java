package pfs.lms.enquiry.service.impl;

import com.google.common.base.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.loanpartner.ILoanPartnerService;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerResource;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.LoanContractExtension;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.LoanContractExtensionRepository;
import pfs.lms.enquiry.resource.LoanContractExtensionResource;
import pfs.lms.enquiry.service.ILoanContractExtensionService;
import pfs.lms.enquiry.service.IPartnerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class LoanContractExtensionService implements ILoanContractExtensionService {

    private final LoanContractExtensionRepository loanContractExtensionRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanPartnerRepository loanPartnerRepository;


    @Autowired
    private ILoanPartnerService loanPartnerService;
//    @Autowired
//    private IPartnerService partnerService;

    @Override
    public LoanContractExtension save(LoanContractExtensionResource resource, String username) throws InterruptedException, CloneNotSupportedException {

        log.info(" Saving NEW Loan Contract Extension : Loan Application Id: " + resource.getLoanApplicationId());

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        log.info(" Continuing to SAVE NEW Loan Contract Extension :" + resource.getLoanApplicationId());

        LoanContractExtension loanContractExtension = resource.getLoanContractExtension();
        loanContractExtension.setLoanApplication(loanApplication);
        loanContractExtension.setLoanNumber(loanApplication.getLoanContractId());
        loanContractExtension.setBoardApprovalDate(resource.getLoanContractExtension().getBoardApprovalDate());
        loanContractExtension.setBoardMeetingNumber(resource.getLoanContractExtension().getBoardMeetingNumber());
        //loanContractExtension.setLoanNumber(resource.getLoanContractExtension().getLoanNumber());
        loanContractExtension.setSanctionLetterDate(resource.getLoanContractExtension().getSanctionLetterDate());
        loanContractExtension.setLoanDocumentationDate(resource.getLoanContractExtension().getLoanDocumentationDate());
        loanContractExtension.setFirstDisbursementDate(resource.getLoanContractExtension().getFirstDisbursementDate());
        loanContractExtension.setSanctionAmount(resource.getLoanContractExtension().getSanctionAmount());
        loanContractExtension.setDisbursementStatus(resource.getLoanContractExtension().getDisbursementStatus());
        loanContractExtension.setScheduledCOD(resource.getLoanContractExtension().getScheduledCOD());
        loanContractExtension = loanContractExtensionRepository.save(loanContractExtension);

        List<LoanPartner> loanPartnerList = this.updateLoanPartners(loanApplication, resource.getLoanPartners(),username);

        return loanContractExtension;

        }



    @Override
    public LoanContractExtension update(LoanContractExtensionResource resource, String username) throws InterruptedException, CloneNotSupportedException {

        log.info(" Updating Loan Contract Extension : " + resource.getLoanContractExtension().getId());

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanContractExtension existingLoanContractExtension
                = loanContractExtensionRepository.getOne(resource.getLoanContractExtension().getId());


        existingLoanContractExtension.setLoanApplication(resource.getLoanContractExtension().getLoanApplication());
        existingLoanContractExtension.setBoardApprovalDate(resource.getLoanContractExtension().getBoardApprovalDate());
        existingLoanContractExtension.setBoardMeetingNumber(resource.getLoanContractExtension().getBoardMeetingNumber());
        existingLoanContractExtension.setLoanNumber(resource.getLoanContractExtension().getLoanNumber());
        existingLoanContractExtension.setSanctionLetterDate(resource.getLoanContractExtension().getSanctionLetterDate());
        existingLoanContractExtension.setLoanDocumentationDate(resource.getLoanContractExtension().getLoanDocumentationDate());
        existingLoanContractExtension.setFirstDisbursementDate(resource.getLoanContractExtension().getFirstDisbursementDate());
        existingLoanContractExtension.setSanctionAmount(resource.getLoanContractExtension().getSanctionAmount());
        existingLoanContractExtension.setDisbursementStatus(resource.getLoanContractExtension().getDisbursementStatus());
        existingLoanContractExtension.setScheduledCOD(resource.getLoanContractExtension().getScheduledCOD());
        existingLoanContractExtension = loanContractExtensionRepository.save(existingLoanContractExtension);

        log.info("Done Updating Loan Contract Extension : " + resource.getLoanContractExtension().getId());

        List<LoanPartner> loanPartnerList = this.updateLoanPartners(loanApplication, resource.getLoanPartners(),username);

        return existingLoanContractExtension;

    }


    private List<LoanPartner> updateLoanPartners(LoanApplication loanApplication, List<LoanPartner> loanPartners, String username) throws InterruptedException, CloneNotSupportedException {


        //Existing Loan Partner List
        List<LoanPartner> loanPartnerList = new ArrayList<>();
        loanPartnerList = loanPartnerRepository.findByLoanApplicationIdOrderBySerialNumberDesc(loanApplication.getId());

        // Create/Update Loan Partners
        for (LoanPartner loanPartner: loanPartners) {
            log.info(" Saving Loan Contract Extension LOAN PARTNERS: Bupa Id: " + loanPartner.getBusinessPartnerId());

            // Check if the entry exists for the party number
            LoanPartner loanPartnerExisting =
                    loanPartnerRepository.findByLoanApplicationAndBusinessPartnerId(loanApplication, loanPartner.getBusinessPartnerId().toString());
            // create a new loan partner link
            if (loanPartnerExisting == null) {
                 log.info("Creating  Loan Partner  while migrating Loan Contract Extension :" + loanApplication.getLoanContractId());

                LoanPartnerResource loanPartnerResource = new LoanPartnerResource();
                loanPartnerResource.setLoanApplicationId(loanApplication.getId());
                loanPartnerResource.setRoleType(loanPartner.getRoleType());
                loanPartnerResource.setBusinessPartnerId(loanApplication.getbusPartnerNumber().toString());
                loanPartnerResource.setStartDate(loanApplication.getCreatedOn());
                loanPartnerResource.setRoleDescription(loanPartner.getRoleDescription());
                loanPartnerResource.setBusinessPartnerName(loanPartner.getBusinessPartnerName());
                loanPartnerResource.setSerialNumber(1);
                loanPartnerResource.setKycStatus("Not Done");
                if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("TR0110")|| loanPartner.getRoleType().equals("ZLM038")) {
                    loanPartnerResource.setKycRequired(true);
                    loanPartnerResource.setKycStatus("Not Started");
                }
                loanPartnerService.createLoanPartner(loanPartnerResource,username);

            } else {  //update loan partner link

                log.info("Updating Main Loan Partner  while migrating Loan Contract Extension :" + loanApplication.getLoanContractId());

                LoanPartnerResource loanPartnerResource = new LoanPartnerResource();
                loanPartnerResource.setId(loanPartnerExisting.getId());
                loanPartnerResource.setLoanApplicationId(loanApplication.getId());
                loanPartnerResource.setRoleType(loanPartnerExisting.getRoleType());
                loanPartnerResource.setBusinessPartnerId(loanApplication.getbusPartnerNumber().toString());
                loanPartnerResource.setStartDate(loanApplication.getCreatedOn());
                loanPartnerResource.setRoleDescription(loanPartner.getRoleDescription());
                loanPartnerResource.setBusinessPartnerName(loanPartner.getBusinessPartnerName());
                loanPartnerResource.setSerialNumber(1);
                loanPartnerResource.setKycStatus("Not Done");
                if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("TR0110")|| loanPartner.getRoleType().equals("ZLM038")) {
                    loanPartnerResource.setKycRequired(true);
                    loanPartnerResource.setKycStatus("Not Started");
                }
                loanPartnerService.updateLoanPartner(loanPartnerResource,username);

            }
        }

        // Compare the existing LoanPartner list with the LoanPartnersList in the resource
        // Delete the LoanPartners that are not in the resource
        for(LoanPartner loanPartner : loanPartnerList) {
            Optional<LoanPartner>  existingLoanPartner = loanPartners.stream()
                    .filter(item -> item.getBusinessPartnerId().equals(loanPartner.getBusinessPartnerId()))
                    .findFirst();

            Boolean partnerFound = existingLoanPartner.isPresent();

            if ( partnerFound == false ) {
                log.info("  Loan Contract Extension DELETING REMOVED  LOAN PARTNERS: Bupa ID : " + loanPartner.getBusinessPartnerId());
                // Delete Loan Partner
                loanPartnerService.deleteLoanPartner(loanPartner.getId(), username);
             }
        }


        return  loanPartners;
    }
}

