package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalCustomerRejectionResource implements Serializable {

    public SAPLoanAppraisalCustomerRejectionResource() {
        sapLoanAppraisalCustomerRejectionResourceDetails = new SAPLoanAppraisalCustomerRejectionResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalCustomerRejectionResourceDetails sapLoanAppraisalCustomerRejectionResourceDetails;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSapLoanAppraisalCustomerRejectionResourceDetails(SAPLoanAppraisalCustomerRejectionResourceDetails sapLoanAppraisalCustomerRejectionResourceDetails) {
        this.sapLoanAppraisalCustomerRejectionResourceDetails = sapLoanAppraisalCustomerRejectionResourceDetails;
    }

    public SAPLoanAppraisalCustomerRejectionResourceDetails
                                mapCustomerRejectionToSAP(CustomerRejection customerRejection) throws ParseException {

        SAPLoanAppraisalCustomerRejectionResourceDetails detailsResource = new SAPLoanAppraisalCustomerRejectionResourceDetails();
        detailsResource.setId(customerRejection.getId().toString());
        detailsResource.setAppraisalId(customerRejection.getLoanAppraisal().getId().toString());
        detailsResource.setCategory(customerRejection.getCategory());
        detailsResource.setDate(customerRejection.getDate().toString());
        detailsResource.setReasonForRejection(customerRejection.getReasonForRejection());


        if (detailsResource.getDate() != null)
            detailsResource.setDate(dataConversionUtility.convertDateToSAPFormat(customerRejection.getDate()));
        else
            detailsResource.setDate(null);
        return detailsResource;
    }



}
