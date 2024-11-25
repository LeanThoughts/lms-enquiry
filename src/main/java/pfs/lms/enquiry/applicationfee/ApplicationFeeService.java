package pfs.lms.enquiry.applicationfee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.applicationfee.invoice.InvoicingDetail;
import pfs.lms.enquiry.applicationfee.invoice.InvoicingDetailRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIdentificationRepository;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationFeeService implements IApplicationFeeService {

    private  final LoanApplicationRepository loanApplicationRepository;
    private final IChangeDocumentService changeDocumentService;
    private final ApplicationFeeRepository applicationFeeRepository;
    private final PartnerRepository partnerRepository;
    private final InvoicingDetailRepository invoicingDetailRepository;
    private final IBusinessPartnerIdentificationService businessPartnerIdentificationService;
    private final BusinessPartnerIdentificationRepository businessPartnerIdentificationRepository;

    @Override
    public ApplicationFee processRejection(ApplicationFee applicationFee, String username) throws CloneNotSupportedException {
        Object oldEnquiryAction = applicationFee.clone();
        applicationFee.setWorkFlowStatusCode(04);
        applicationFee.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                applicationFee.getId(), applicationFee.getId().toString(), null,
                applicationFee.getLoanApplication().getLoanContractId(),
                oldEnquiryAction,
                applicationFee,
                "Updated",
                username,
                "Application Fee", "Header");
        applicationFeeRepository.save(applicationFee);

        return applicationFee;
    }

    @Override
    public ApplicationFee processApprovedApplicationFee(ApplicationFee applicationFee, String username) throws CloneNotSupportedException {

        Partner partner = new Partner();
        Object oldPartner;
        LoanApplication loanApplication = applicationFee.getLoanApplication();
        Object oldLoanApplication;
        oldLoanApplication = loanApplication.clone();

         if (loanApplication.getbusPartnerNumber() != null)
             partner = partnerRepository.findByPartyNumber(Integer.valueOf(loanApplication.getbusPartnerNumber()));
        else
            partner = partnerRepository.getOne(loanApplication.getLoanApplicant());

        oldPartner = partner.clone();

         loanApplication.setPostedInSAP(0);

        loanApplication.setFunctionalStatus(11);
        loanApplication.setFunctionalStatusDescription("Application Fee Stage");

        partnerRepository.save(partner);
        loanApplicationRepository.save(loanApplication);

        // Change Documents for Loan Application
        changeDocumentService.createChangeDocument(
                loanApplication.getId(),
                loanApplication.getId().toString(),
                loanApplication.getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                oldLoanApplication,
                loanApplication,
                "Updated",
                username,
                "LoanApplication", "LoanApplication" );

        InvoicingDetail invoicingDetail = invoicingDetailRepository.findByApplicationFeeId(applicationFee.getId());
        if (invoicingDetail != null) {
            List<BusinessPartnerIdentification> businessPartnerIdentificationList =
                    businessPartnerIdentificationRepository.findByPartnerIdOrderBySerialNumberDesc(invoicingDetail.getPartner().getId());

            for (BusinessPartnerIdentification businessPartnerIdentification : businessPartnerIdentificationList) {
                businessPartnerIdentificationService.updateLoanPartnerKYC(businessPartnerIdentification);
            }
        }
        return applicationFee;
    }

    @Override
    public ApplicationFee create(ApplicationFee applicationFee, String username) throws Exception {
        return null;
    }

    @Override
    public ApplicationFee update(ApplicationFee applicationFee, String username) throws Exception {
        return null;
    }


}
