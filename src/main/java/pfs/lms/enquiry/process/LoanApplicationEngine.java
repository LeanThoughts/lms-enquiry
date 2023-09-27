package pfs.lms.enquiry.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.LoanApplicationStatus;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.LoanApplicationStatusRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.resource.LoanApplicationResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationDetailsResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationResource;
import pfs.lms.enquiry.service.ISAPIntegrationService;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationEngine {
    
    private  final ISAPIntegrationService integrationService;

    private final LoanApplicationStatusRepository loanApplicationStatusRepository;
    
    private final PartnerRepository partnerRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    @EventListener
    public void onLoanApplicationCreated(LoanApplication.LoanApplicationCreated loanApplicationCreated){
        loanApplicationStatusRepository.save(new LoanApplicationStatus(loanApplicationCreated.getLoanApplication()));
    }

    @EventListener
    public void onLoanApplicationRejected(LoanApplication.LoanApplicationRejected loanApplicationRejected){
        loanApplicationStatusRepository.save(LoanApplicationStatus.reject(loanApplicationRejected.getLoanApplication()));
    }

    @EventListener
    public LoanApplicationResource onLoanApplicationApproved(LoanApplication.LoanApplicationApproved loanApplicationApproved){

        LoanApplication loanApplication = loanApplicationApproved.getLoanApplication();
        Partner partner = partnerRepository.findById(loanApplication.getLoanApplicant()).get();
        SAPLoanApplicationDetailsResource detailsResource = new SAPLoanApplicationDetailsResource();
        detailsResource.setLoanApplicationId(Long.toString(loanApplication.getEnquiryNo().getId()));
        detailsResource.setPartnerExternalNumber("");
        detailsResource.setPartnerRole("TR0100");
        detailsResource.setName1(partner.getPartyName1());
        detailsResource.setName2(partner.getPartyName2() == null? "": partner.getPartyName2());
        detailsResource.setEmail(partner.getEmail());
        detailsResource.setCity(partner.getCity());
        detailsResource.setState(partner.getState());
        detailsResource.setPostalCode(partner.getPostalCode());
        detailsResource.setHouseNo(partner.getAddressLine1());
        detailsResource.setStreet(partner.getStreet());
        detailsResource.setCountry("IN");
        detailsResource.setPanNumber(partner.getPan());
        detailsResource.setContactPerName(partner.getContactPersonName());
        detailsResource.setIndustrySector(partner.getIndustrySector());


        detailsResource.setApplicationDate("\\/Date(" + System.currentTimeMillis() + ")\\/");
        detailsResource.setLoanClass(loanApplication.getLoanClass());
        detailsResource.setFinancingType(loanApplication.getFinancingType());
        detailsResource.setDebtEquityIndicator(loanApplication.getAssistanceType());
        detailsResource.setProjectCapaacity(loanApplication.getProjectCapacity() == null? "0.00":
                String.format("%.2f", loanApplication.getProjectCapacity()));
        detailsResource.setProjectCapacityUnit(loanApplication.getProjectCapacityUnit());

        if(loanApplication.getScheduledCOD() != null) {
             LocalDateTime dateTime = loanApplication.getScheduledCOD().atStartOfDay();
             dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
             detailsResource.setScheduledCommDate("\\/Date(" + dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + ")\\/");

          // detailsResource.setScheduledCommDate("\\/Date(" + System.currentTimeMillis() + ")\\/");

        }
        else {
            detailsResource.setScheduledCommDate(null);
        }

        detailsResource.setProjectCostInCrores(loanApplication.getProjectCost() == null? "0.000":
                String.format("%.3f", loanApplication.getProjectCost()));
        detailsResource.setDebtAmountInCrores(loanApplication.getProjectDebtAmount() == null? "0.000":
                String.format("%.3f", loanApplication.getProjectDebtAmount()));
        detailsResource.setEquityAmountInCrores(loanApplication.getEquity() == null? "0.000":
                String.format("%.3f", loanApplication.getEquity()));
        detailsResource.setCurrency("INR");
        detailsResource.setApplicationCapitalInCrores(loanApplication.getPfsDebtAmount() == null? "0.000":
                String.format("%.3f", loanApplication.getPfsDebtAmount()));
        // detailsResource.setLoanPurpose(loanApplication.getLoanPurpose());
        // Send empty string for loan purpose. Will be handled at SAP.
        detailsResource.setLoanPurpose("");
        detailsResource.setGroupCompanyName(loanApplication.getGroupCompany());
        detailsResource.setPromoterName(loanApplication.getPromoterName());
        detailsResource.setPromoterPATInCrores(loanApplication.getPromoterPATAmount() == null? "0.000":
                String.format("%.3f", loanApplication.getPromoterPATAmount()));
        detailsResource.setPromoterAreaOfBusiness(loanApplication.getPromoterAreaOfBusinessNature());
        detailsResource.setPromoterRating(loanApplication.getRating());
        detailsResource.setPromoterNetWorthInCrores(loanApplication.getPromoterNetWorthAmount() == null? "0.000":
                String.format("%.3f", loanApplication.getPromoterNetWorthAmount()));
        detailsResource.setPromoterKeyDirector(loanApplication.getPromoterKeyDirector());
        detailsResource.setLoanStatus(Integer.toString(loanApplication.getFunctionalStatus()));
        detailsResource.setProjectName(loanApplication.getProjectName());
        detailsResource.setLoanOfficer(loanApplication.getUserBPNumber());
        detailsResource.setLoanProduct(loanApplication.getProductCode());

        detailsResource.setTenorMonth(loanApplication.getTenorMonth().toString());
        detailsResource.setTenorYear(loanApplication.getTenorYear().toString());

        detailsResource.setProjectType(loanApplication.getProjectType());

        detailsResource.setProjectState(loanApplication.getProjectLocationState());
        detailsResource.setProjectDistrict(loanApplication.getProjectDistrict());

        detailsResource.setContactBranchAddress(loanApplication.getContactBranchAddress());
        detailsResource.setContactDepartment(loanApplication.getContactDepartment());
        detailsResource.setContactDesignation(loanApplication.getContactDesignation());
        detailsResource.setContactEmail(loanApplication.getContactEmail());
        detailsResource.setContactFaxNumber(loanApplication.getContactFaxNumber());
        detailsResource.setContactLandLinePhone(loanApplication.getContactLandLinePhone());
        detailsResource.setContactTelePhone(loanApplication.getContactTelePhone());


        SAPLoanApplicationResource d = new SAPLoanApplicationResource();
        d.setSapLoanApplicationDetailsResource(detailsResource);
        SAPLoanApplicationResource sapLoanApplicationResourceResponse = integrationService.postLoanApplication(d);

        loanApplication = loanApplication.responseFromSAP(sapLoanApplicationResourceResponse);

        //Set PostedInSAP to Success
        loanApplication.setPostedInSAP(3);

        loanApplication = loanApplicationRepository.save(loanApplication);

        partner.setPartyNumber(Integer.parseInt(loanApplication.getbusPartnerNumber()));
        partner = partnerRepository.save(partner);


        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();
        loanApplicationResource.setPartner(partner);
        loanApplicationResource.setLoanApplication(loanApplication);

        return loanApplicationResource;
    }
}
