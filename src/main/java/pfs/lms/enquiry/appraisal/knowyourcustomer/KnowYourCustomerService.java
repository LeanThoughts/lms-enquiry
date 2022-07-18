package pfs.lms.enquiry.appraisal.knowyourcustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowYourCustomerService implements IKnowYourCustomerService {

    private final LoanAppraisalRepository loanAppraisalRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public KnowYourCustomer updateKYC(KnowYourCustomerResource knowYourCustomerResource, String username) throws CloneNotSupportedException {
        KnowYourCustomer knowYourCustomer = knowYourCustomerRepository.findById(knowYourCustomerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(knowYourCustomerResource.getId().toString()));

        Object oldKnowYourCustomer = knowYourCustomer.clone();

        knowYourCustomer.setDateOfCompletion(knowYourCustomerResource.getDateOfCompletion());
        knowYourCustomer.setDocumentName(knowYourCustomerResource.getDocumentName());
        knowYourCustomer.setFileReference(knowYourCustomerResource.getFileReference());
        knowYourCustomer.setRemarks(knowYourCustomerResource.getRemarks());
        knowYourCustomer = knowYourCustomerRepository.save(knowYourCustomer);

        updateLoanPartnerKYCStatus(knowYourCustomer.getLoanPartnerId());

        LoanPartner loanPartner =  loanPartnerRepository.getOne(UUID.fromString(knowYourCustomer.getLoanPartnerId())) ;
        LoanAppraisal loanAppraisal = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));


        // Change Documents for KYC
        changeDocumentService.createChangeDocument(
                loanAppraisal.getId(),
                knowYourCustomer.getId().toString(),
                loanAppraisal.getId().toString(),
                loanPartner.getLoanApplication().getLoanContractId(),
                oldKnowYourCustomer,
                knowYourCustomer,
                "Updated",
                username,
                "Appraisal", "Know Your Customer" );


        return knowYourCustomer;
    }

    private void updateLoanPartnerKYCStatus(String loanPartnerId) {
        LoanPartner loanPartner = loanPartnerRepository.findById(UUID.fromString(loanPartnerId)).
                orElseThrow(() -> new EntityNotFoundException(loanPartnerId));
        List<KnowYourCustomer> kycs = knowYourCustomerRepository.findByLoanPartnerId(loanPartnerId);
        int kycStatusCount = 0;
        for (KnowYourCustomer kyc : kycs) {
            if (kyc.getFileReference() != null && !kyc.getFileReference().equals(""))
                kycStatusCount++;
        }

        for (KnowYourCustomer knowYourCustomer : kycs) {

            if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("TR0110")) {

                if (knowYourCustomer.getFileReference() != null) {
                    if (knowYourCustomer.getDocumentType().equals("ZPFSBP0008") ||   //PAN Card of Company
                            knowYourCustomer.getDocumentType().equals("ZPFSBP0006") ||   //"Certification of Incorporation",
                            knowYourCustomer.getDocumentType().equals("ZPFSBP0018") ||   //Board Resolution
                            knowYourCustomer.getDocumentType().equals("ZPFSBP0005")) {   //AOA

                        kycStatusCount++;
                    }
                }

                if (loanPartner.getRoleType().equals("ZLM038")) {

                    if (knowYourCustomer.getFileReference() != null) {
                        if (knowYourCustomer.getDocumentType().equals("ZPFSBP0002") ||   //PAN Card
                                knowYourCustomer.getDocumentType().equals("ZPFSBP0003") ||   //"Passport
                                knowYourCustomer.getDocumentType().equals("ZPFSBP0004")) {   //"Address Proof

                            kycStatusCount++;
                        }
                    }
                }


            }
        }

        if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("TR0110")) {
            if (kycStatusCount > 0 && kycStatusCount < 4)
                loanPartner.setKycStatus("Pending");
            else if (kycStatusCount == 4)
                loanPartner.setKycStatus("Completed");
        } else {
            if (kycStatusCount > 0 && kycStatusCount < 2)
                loanPartner.setKycStatus("Pending");
            else if (kycStatusCount == 2)
                loanPartner.setKycStatus("Completed");
        }

        loanPartnerRepository.save(loanPartner);
    }
}
