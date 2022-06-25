package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;
import java.util.UUID;

@Component
@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalKYCResource implements Serializable {

    DataConversionUtility dataConversionUtility;


    public SAPLoanAppraisalKYCResource() {
        sapLoanAppraisalKYCResourceDetails = new SAPLoanAppraisalKYCResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalKYCResourceDetails sapLoanAppraisalKYCResourceDetails;


    public void setSapLoanAppraisalKYCResourceDetails(SAPLoanAppraisalKYCResourceDetails sapLoanAppraisalKYCResourceDetails
    ) {
        this.sapLoanAppraisalKYCResourceDetails = sapLoanAppraisalKYCResourceDetails;
    }

    public SAPLoanAppraisalKYCResourceDetails
                                mapKYCToSAP(KnowYourCustomer knowYourCustomer,LoanPartner loanPartner, LoanAppraisal loanAppraisal) throws ParseException {

        SAPLoanAppraisalKYCResourceDetails detailsResource = new SAPLoanAppraisalKYCResourceDetails();
        detailsResource.setId(knowYourCustomer.getId().toString());


        detailsResource.setAppraisalId(loanAppraisal.getId().toString());

        detailsResource.setLoanpartnerid(loanPartner.getBusinessPartnerId());
        detailsResource.setDocumentname(knowYourCustomer.getDocumentName());
        detailsResource.setDocumenttype(knowYourCustomer.getDocumentType());
        detailsResource.setFilereference(knowYourCustomer.getFileReference());
        detailsResource.setRemarks(knowYourCustomer.getRemarks());
        if (knowYourCustomer.getDateOfCompletion() != null)
            detailsResource.setDateOfCompletion(dataConversionUtility.convertDateToSAPFormat(knowYourCustomer.getDateOfCompletion()));
        else
            detailsResource.setDateOfCompletion(null);


        return detailsResource;
    }



}
