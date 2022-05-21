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

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPartnerService implements ILoanPartnerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;

    @Override
    public LoanPartner createLoanPartner(LoanPartnerResource loanPartnerResource) {
        // Save loan partner details
        LoanApplication loanApplication = loanApplicationRepository.getOne(loanPartnerResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });

        LoanPartner loanPartner = new LoanPartner();
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
        loanPartner = loanPartnerRepository.save(loanPartner);

        // prepopulate loan partner kyc document list
        if (loanPartner.isKycRequired())
            prePopulateKYCDocumentList(loanPartner);

        return loanPartner;
    }

    @Override
    public LoanPartner updateLoanPartner(LoanPartnerResource loanPartnerResource) {
        LoanPartner loanPartner = loanPartnerRepository.findById(loanPartnerResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(loanPartnerResource.getId().toString()));
        loanPartner.setBusinessPartnerId(loanPartnerResource.getBusinessPartnerId());
        loanPartner.setBusinessPartnerName(loanPartnerResource.getBusinessPartnerName());
        loanPartner.setRoleType(loanPartnerResource.getRoleType());
        loanPartner.setStartDate(loanPartnerResource.getStartDate());
        if (loanPartner.getRoleType().equals("TR0100") || loanPartner.getRoleType().equals("ZLM038")) {
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

        return loanPartner;
    }

    @Override
    public LoanPartner deleteLoanPartner(UUID loanPartnerId) {
        knowYourCustomerRepository.findByLoanPartnerId(loanPartnerId.toString()).forEach(knowYourCustomer -> {
            knowYourCustomerRepository.delete(knowYourCustomer);
        });
        LoanPartner loanPartner = loanPartnerRepository.findById(loanPartnerId)
                .orElseThrow(() -> new EntityNotFoundException(loanPartnerId.toString()));
        loanPartnerRepository.deleteById(loanPartnerId);
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
}
