package pfs.lms.enquiry.appraisal.knowyourcustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowYourCustomerService implements IKnowYourCustomerService {

    private final LoanPartnerRepository loanPartnerRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;

    @Override
    public KnowYourCustomer updateKYC(KnowYourCustomerResource knowYourCustomerResource) {
        KnowYourCustomer knowYourCustomer = knowYourCustomerRepository.findById(knowYourCustomerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(knowYourCustomerResource.getId().toString()));
        knowYourCustomer.setDateOfCompletion(knowYourCustomerResource.getDateOfCompletion());
        knowYourCustomer.setDocumentName(knowYourCustomerResource.getDocumentName());
        knowYourCustomer.setFileReference(knowYourCustomerResource.getFileReference());
        knowYourCustomer.setRemarks(knowYourCustomerResource.getRemarks());
        knowYourCustomer = knowYourCustomerRepository.save(knowYourCustomer);

        updateLoanPartnerKYCStatus(knowYourCustomer.getLoanPartnerId());

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
        if(kycStatusCount > 0 && kycStatusCount < 7)
            loanPartner.setKycStatus("Pending");
        else if(kycStatusCount == 7)
            loanPartner.setKycStatus("Completed");

        loanPartnerRepository.save(loanPartner);
    }
}
