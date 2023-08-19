package pfs.lms.enquiry.appraisal.loanpartner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomerRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanPartnerService implements ILoanPartnerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public LoanPartner createLoanPartner(LoanPartnerResource loanPartnerResource, String username) throws InterruptedException {

//        log.info("Sleeping in Create Loan Partner");
//        Thread.sleep(1000);

        // Save loan partner details
        LoanApplication loanApplication = loanApplicationRepository.getOne(loanPartnerResource.getLoanApplicationId());

        // If there is no loan contract i.e. project is in enquiry stage, do not create a loan partner
//        if ( loanApplication.getLoanContractId() == null)
//            return null;

        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");

                    return obj;
                });


        LoanPartner loanPartner = new LoanPartner();
        loanPartner = loanPartnerRepository.findByLoanApplicationAndBusinessPartnerIdAndRoleType(loanApplication, loanPartnerResource.getBusinessPartnerId() ,loanPartnerResource.getRoleType());
        if (loanPartner  != null){
            log.info("Loan Partner : " + loanPartner.getBusinessPartnerId() + " For Contract" +loanApplication.getLoanContractId() + " in Role : " + loanPartnerResource.getRoleType() + " already exists. Create Aborted" );
            return loanPartner;
        } else {
            log.info("Loan Partner Service : Creating Loan Partner : " + loanPartnerResource.getBusinessPartnerId()
                    + " For Contract : " +loanApplication.getLoanContractId() + " in Role : " + loanPartnerResource.getRoleType()   );
        }
        loanPartner = new LoanPartner();
        loanPartner.setLoanAppraisalId(loanAppraisal.getId().toString());
        loanPartner.setSerialNumber(loanPartnerRepository.findByLoanApplicationIdOrderBySerialNumberDesc(loanPartnerResource
                .getLoanApplicationId()).size() + 1);
        loanPartner.setLoanApplication(loanApplication);
        loanPartner.setBusinessPartnerId(loanPartnerResource.getBusinessPartnerId());
        loanPartner.setBusinessPartnerName(loanPartnerResource.getBusinessPartnerName());
        loanPartner.setRoleType(loanPartnerResource.getRoleType());
        loanPartner.setRoleDescription(loanPartnerResource.getRoleDescription());
        loanPartner.setStartDate(loanPartnerResource.getStartDate());
        if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("TR0110")|| loanPartner.getRoleType().equals("ZLM038")) {
            loanPartner.setKycRequired(true);
            loanPartner.setKycStatus("Not Started");
        }

        try {
            //loanPartner = loanPartnerRepository.save(loanPartner);
            loanPartnerRepository.save(loanPartner);
        } catch (Exception ex) {
            log.error("Error Saving Loan Partner : " + loanPartner.getBusinessPartnerId() + "for contract :" + loanPartner.getLoanApplication().getLoanContractId());
        }
        // Fetch Loan Appraisal for LoanPartner
        LoanAppraisal loanAppraisalForPartner = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));

        // Change Documents for  Loan Partner
        changeDocumentService.createChangeDocument(
                loanAppraisalForPartner.getId(),
                loanPartner.getId().toString(),
                loanAppraisalForPartner.getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                loanPartner,
                "Created",
                username,
                "Appraisal", "Loan Partner");



        // prepopulate loan partner kyc document list
        if (loanPartner.isKycRequired())
            prePopulateKYCDocumentList(loanPartner);

        return loanPartner;
    }

    @Override
    public LoanPartner updateLoanPartner(LoanPartnerResource loanPartnerResource, String username) throws CloneNotSupportedException {
        LoanPartner loanPartner = loanPartnerRepository.findById(loanPartnerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(loanPartnerResource.getId().toString()));

        Object oldLoanPartner =   loanPartner.clone();

        loanPartner.setBusinessPartnerId(loanPartnerResource.getBusinessPartnerId());
        loanPartner.setBusinessPartnerName(loanPartnerResource.getBusinessPartnerName());
        loanPartner.setRoleType(loanPartnerResource.getRoleType());
        loanPartner.setStartDate(loanPartnerResource.getStartDate());
        // KYC is required for the following Roles:
        //   TR0100	Main Loan Partner
        //   ZLM038	Authorized Signatory
        //   ZLM025	Key Promoter
        if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("ZLM038") || loanPartner.getRoleType().equals("ZLM025")) {
            if (!loanPartner.isKycRequired()) {
                loanPartner.setKycRequired(true);
                loanPartner.setKycStatus("Not Started");
            }
        }
        loanPartner = loanPartnerRepository.save(loanPartner);

        // check if loan partner kyc document list needs to be auto generated
        if (loanPartner.isKycRequired()) {
            if (knowYourCustomerRepository.findByLoanPartnerId(loanPartner.getId().toString()).size() == 0)
                prePopulateKYCDocumentList(loanPartner);
        }
        // Fetch Loan Appraisal for LoanPartner
        LoanAppraisal loanAppraisalForPartner = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));

        // Change Documents for  Loan Partner
        changeDocumentService.createChangeDocument(
                loanAppraisalForPartner.getId(),
                loanPartner.getId().toString(),
                loanAppraisalForPartner.getId().toString(),
                loanPartner.getLoanApplication().getLoanContractId(),
                oldLoanPartner,
                loanPartner,
                "Updated",
                username,
                "Appraisal", "Loan Partner");
        return loanPartner;
    }

    @Override
    public LoanPartner deleteLoanPartner(UUID loanPartnerId, String username) {
        knowYourCustomerRepository.findByLoanPartnerId(loanPartnerId.toString()).forEach(knowYourCustomer -> {
            knowYourCustomerRepository.delete(knowYourCustomer);
        });
        LoanPartner loanPartner = loanPartnerRepository.findById(loanPartnerId)
                .orElseThrow(() -> new EntityNotFoundException(loanPartnerId.toString()));
        UUID loanApplicationId = loanPartner.getLoanApplication().getId();
        loanPartnerRepository.deleteById(loanPartnerId);

        // Fetch Loan Appraisal for LoanPartner
        LoanAppraisal loanAppraisalForPartner = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));

        // Change Documents for  Loan Partner
        changeDocumentService.createChangeDocument(
                loanAppraisalForPartner.getId(),
                loanPartner.getId().toString(),
                loanAppraisalForPartner.getId().toString(),
                loanPartner.getLoanApplication().getLoanContractId(),
                null,
                loanPartner,
                "Deleted",
                username,
                "Appraisal", "Loan Partner");

        updateSerialNumbers(loanApplicationId);
        return loanPartner;
    }

    private void updateSerialNumbers(UUID loanApplicationId) {
        List<LoanPartner> loanPartners = loanPartnerRepository.findByLoanApplicationIdOrderBySerialNumberDesc(loanApplicationId);
        int size = loanPartners.size();
        for(LoanPartner loanPartner : loanPartners) {
            loanPartner.setSerialNumber(size);
            loanPartnerRepository.save(loanPartner);
            size--;
        }
    }

    private void prePopulateKYCDocumentList(LoanPartner loanPartner) {
        List<KnowYourCustomer> kycs = new ArrayList<>();
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0002", "PAN Card", "", "", null));
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0003", "Passport", "", "", null));
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0004", "Address Proof", "", "", null));
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0005",
                "MoA and Articles of Association (AoA)", "", "", null));
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0006", "Certification of Incorporation", "", "", null));
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0008", "PAN Card of Company", "", "", null));
        kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0019", "Board Resolution", "", "", null));
        knowYourCustomerRepository.saveAll(kycs);
    }
}
