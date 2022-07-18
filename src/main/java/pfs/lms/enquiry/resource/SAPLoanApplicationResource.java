package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.UserRepository;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLoanApplicationResource implements Serializable   {



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


    public SAPLoanApplicationDetailsResource
                    mapLoanApplicationToSAP(LoanApplication loanApplication, Partner partner, User lastProcessedBy) throws ParseException {

        SAPLoanApplicationDetailsResource detailsResource = new SAPLoanApplicationDetailsResource();

        detailsResource.setLoanApplicationId(Long.toString(loanApplication.getEnquiryNo().getId()));
        detailsResource.setPartnerExternalNumber(" ");
        detailsResource.setPartnerRole("TR0100");
        detailsResource.setName1(partner.getPartyName1());
        detailsResource.setName2(partner.getPartyName2() == null? "": partner.getPartyName2());
        if (partner.getPartyCategory() != null)
             detailsResource.setPartnerCategory(partner.getPartyCategory().toString());

        if (partner.getEmail().contains("@") == false){
            partner.setEmail(partner.getEmail() + "@dummy.co.in");
        }

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
        if (partner.getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(partner.getPartyNumber().toString());

        detailsResource.setApplicationDate("\\/Date(" + System.currentTimeMillis() + ")\\/");
        detailsResource.setLoanClass(loanApplication.getLoanClass());
        detailsResource.setFinancingType(loanApplication.getFinancingType());
        detailsResource.setDebtEquityIndicator(loanApplication.getAssistanceType());
        detailsResource.setProjectType(loanApplication.getProjectType());

        detailsResource.setTerm(loanApplication.getTerm());

        detailsResource.setProjectCapaacity(loanApplication.getProjectCapacity() == null? "0.00":
                String.format("%.2f", loanApplication.getProjectCapacity()));
        if (loanApplication.getProjectCapacityUnit() != null){
            detailsResource.setProjectCapacityUnit(loanApplication.getProjectCapacityUnit());
        } else {
            detailsResource.setProjectCapacityUnit("");
        }
        //String myDate = "2014/10/29 18:10:45";
        if (loanApplication.getScheduledCOD() != null) {
            String scheduledCOD = loanApplication.getScheduledCOD().toString();
            scheduledCOD = scheduledCOD + " 01:01:01";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(scheduledCOD);
            long millis = date.getTime();
            detailsResource.setScheduledCommDate ("\\/Date(" + millis + ")\\/");
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

        detailsResource.setTenorYear(loanApplication.getTenorYear().toString());
        detailsResource.setTenorMonth(loanApplication.getTenorMonth().toString());

        detailsResource.setLoanProduct(loanApplication.getProductCode());
        detailsResource.setProjectState(loanApplication.getProjectLocationState());
        detailsResource.setProjectDistrict(loanApplication.getProjectDistrict());

        detailsResource.setContactBranchAddress(loanApplication.getContactBranchAddress());
        detailsResource.setContactDepartment(loanApplication.getContactDepartment());
        detailsResource.setContactDesignation(loanApplication.getContactDesignation());
        if (loanApplication.getContactEmail() != null) {
            if (loanApplication.getContactEmail().contains("@") == false) {
                loanApplication.setContactEmail(loanApplication.getContactEmail() + "@dummy.co.in");
            }
        }
        detailsResource.setContactEmail(loanApplication.getContactEmail());
        detailsResource.setContactFaxNumber(loanApplication.getContactFaxNumber());
        detailsResource.setContactLandLinePhone(loanApplication.getContactLandLinePhone());
        detailsResource.setContactTelePhone(loanApplication.getContactTelePhone());

        // Set Loan Officer
        if (lastProcessedBy != null)
            detailsResource.setLoanOfficer(lastProcessedBy.getSapBPNumber());


        return detailsResource;
    }
}
