package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPSampleResource implements Serializable {

    public SAPSampleResource() {
        sapLoanAppraisalCustomerRejectionResourceDetails = new SAPLoanAppraisalCustomerRejectionResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalCustomerRejectionResourceDetails sapLoanAppraisalCustomerRejectionResourceDetails;


    public void setSapLoanAppraisalCustomerRejectionResourceDetails(SAPLoanAppraisalCustomerRejectionResourceDetails sapLoanAppraisalCustomerRejectionResourceDetails) {
        this.sapLoanAppraisalCustomerRejectionResourceDetails = sapLoanAppraisalCustomerRejectionResourceDetails;
    }

    public SAPLoanAppraisalCustomerRejectionResourceDetails
                                mapCustomerRejectionToSAP(CustomerRejection customerRejection) throws ParseException {

        SAPLoanAppraisalCustomerRejectionResourceDetails detailsResource = new SAPLoanAppraisalCustomerRejectionResourceDetails();



        return detailsResource;
    }



}
