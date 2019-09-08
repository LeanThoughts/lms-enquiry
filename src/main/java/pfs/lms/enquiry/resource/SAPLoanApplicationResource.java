package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;

import java.io.Serializable;

@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLoanApplicationResource implements Serializable {

    public SAPLoanApplicationResource() {
        sapLoanApplicationDetailsResource = new SAPLoanApplicationDetailsResource();
    }

    @JsonProperty(value = "d")
    private SAPLoanApplicationDetailsResource sapLoanApplicationDetailsResource;

    public SAPLoanApplicationDetailsResource getSapLoanApplicationDetailsResource() {
        return sapLoanApplicationDetailsResource;
    }

    public void setSapLoanApplicationDetailsResource(SAPLoanApplicationDetailsResource sapLoanApplicationDetailsResource) {
        this.sapLoanApplicationDetailsResource = sapLoanApplicationDetailsResource;
    }


    public SAPLoanApplicationDetailsResource mapLoanApplicationToSAP(LoanApplication loanApplication, Partner partner){

        SAPLoanApplicationDetailsResource detailsResource = new SAPLoanApplicationDetailsResource();

        detailsResource.setLoanApplicationId(Long.toString(loanApplication.getEnquiryNo().getId()));
        detailsResource.setPartnerExternalNumber(" ");
        detailsResource.setPartnerRole("TR0100");
        detailsResource.setName1(partner.getPartyName1());
        detailsResource.setName2(partner.getPartyName2() == null? "": partner.getPartyName2());
        detailsResource.setEmail(partner.getEmail());
        detailsResource.setCity(partner.getCity());
        detailsResource.setRegiogroup(partner.getState());
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
        detailsResource.setProjectType(loanApplication.getProjectType());
        detailsResource.setProjectCapaacity(loanApplication.getProjectCapacity() == null? "0.00":
                String.format("%.2f", loanApplication.getProjectCapacity()));
        detailsResource.setProjectCapacityUnit("MW");

        if(loanApplication.getScheduledCOD() != null) {
            detailsResource.setScheduledCommDate("\\/Date(" + loanApplication.getScheduledCOD().toEpochDay() + ")\\/");
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
        detailsResource.setProjectState(loanApplication.getProjectLocationState());
        detailsResource.setProjectDistrict(loanApplication.getProjectDistrict());

        detailsResource.setContactBranchAddress(loanApplication.getContactBranchAddress());
        detailsResource.setContactDepartment(loanApplication.getContactDepartment());
        detailsResource.setContactDesignation(loanApplication.getContactDesignation());
        detailsResource.setContactEmail(loanApplication.getContactEmail());
        detailsResource.setContactFaxNumber(loanApplication.getContactFaxNumber());
        detailsResource.setContactLandLinePhone(loanApplication.getContactLandLinePhone());
        detailsResource.setContactTelePhone(loanApplication.getContactTelePhone());


        return detailsResource;
    }
}
