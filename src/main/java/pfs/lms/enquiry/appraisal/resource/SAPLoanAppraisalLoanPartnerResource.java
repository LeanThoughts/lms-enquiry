package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalLoanPartnerResource implements Serializable {

    public SAPLoanAppraisalLoanPartnerResource() {
       this.sapLoanAppraisalLoanPartnerResourceDetails = new SAPLoanAppraisalLoanPartnerResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalLoanPartnerResourceDetails sapLoanAppraisalLoanPartnerResourceDetails;

    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSapLoanAppraisalLoanPartnerResourceDetails(SAPLoanAppraisalLoanPartnerResourceDetails  sapLoanAppraisalLoanPartnerResourceDetails) {
        this.sapLoanAppraisalLoanPartnerResourceDetails = sapLoanAppraisalLoanPartnerResourceDetails;
    }

    public SAPLoanAppraisalLoanPartnerResourceDetails
                                mapLoanPartnerToSAP(LoanPartner loanPartner) throws ParseException {

        SAPLoanAppraisalLoanPartnerResourceDetails detailsResource = new SAPLoanAppraisalLoanPartnerResourceDetails();

        detailsResource.setId(loanPartner.getId().toString());
        detailsResource.setAppraisalId(loanPartner.getLoanAppraisalId());

        detailsResource.setRoleType(loanPartner.getRoleType());
        detailsResource.setSerialNumber(loanPartner.getSerialNumber().toString());
        detailsResource.setBusinessPartnerId(loanPartner.getBusinessPartnerId());
        detailsResource.setBusinessPartnerName(loanPartner.getBusinessPartnerName());
        detailsResource.setRoleDescription(loanPartner.getRoleDescription());
        if (loanPartner.getKycStatus() !=null)
            detailsResource.setKycStatus(loanPartner.getKycStatus());
        else
            detailsResource.setKycStatus("");
        if (loanPartner.kycRequired == true) {
            detailsResource.setKycRequired(("X"));
        }else{
            detailsResource.setKycRequired((" "));
        }

        if ( loanPartner.getStartDate()  != null) {
            detailsResource.setStartDate( dataConversionUtility.convertDateToSAPFormat(loanPartner.getStartDate()) );
        } else {
            detailsResource.setStartDate(null);
        }

        if ( loanPartner.getEndDate()  != null) {
            detailsResource.setEndDate( dataConversionUtility.convertDateToSAPFormat(loanPartner.getEndDate()) );
        } else {
            detailsResource.setEndDate(null);
        }

        return detailsResource;
    }



}
