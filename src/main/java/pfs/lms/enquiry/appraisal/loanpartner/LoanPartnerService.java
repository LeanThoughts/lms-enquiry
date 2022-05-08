package pfs.lms.enquiry.appraisal.loanpartner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomerRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPartnerService implements ILoanPartnerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;

    @Override
    public LoanPartner createLoanPartner(LoanPartnerResource loanPartnerResource) {
        // Save loan partner details
        LoanApplication loanApplication = loanApplicationRepository.findById(loanPartnerResource.getLoanApplicationId())
                .orElseThrow(() -> new EntityNotFoundException(loanPartnerResource.getLoanApplicationId().toString()));
        LoanPartner loanPartner = new LoanPartner();
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
        loanPartner = loanPartnerRepository.save(loanPartner);

        // prepopulate loan partner kyc document list
        if (loanPartner.isKycRequired())
            prePopulateKYCDocumentList(loanPartner);

        return loanPartner;
    }

    private void prePopulateKYCDocumentList(LoanPartner loanPartner) {
            List<KnowYourCustomer> kycs = new ArrayList<>();
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0002", "PAN Card", "", "", null));
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0003", "Passport", "", "", null));
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0005",
                    "MoA and Articles of Association (AoA)", "", "", null));
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0006", "Certification of Incorporation", "", "", null));
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0004", "Address Proof", "", "", null));
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0007", "PAN Card of Company", "", "", null));
            kycs.add(new KnowYourCustomer(loanPartner.getId().toString(), "ZPFSBP0008", "Board Resolution", "", "", null));
            knowYourCustomerRepository.saveAll(kycs);
    }

    @Override
    public LoanPartner updateLoanPartner(LoanPartnerResource loanPartnerResource) {
        return null;
    }
}
