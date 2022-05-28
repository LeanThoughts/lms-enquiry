package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelay;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalReasonForDelayResource implements Serializable {

    DataConversionUtility dataConversionUtility = new DataConversionUtility();

    public SAPLoanAppraisalReasonForDelayResource() {
        sapLoanAppraisalReasonForDelayResourceDetails = new SAPLoanAppraisalReasonForDelayResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalReasonForDelayResourceDetails  sapLoanAppraisalReasonForDelayResourceDetails;


    public void setSAPReasonforDelay(SAPLoanAppraisalReasonForDelayResourceDetails sapLoanAppraisalReasonForDelayResourceDetails) {
        this.sapLoanAppraisalReasonForDelayResourceDetails = sapLoanAppraisalReasonForDelayResourceDetails;
    }

    public SAPLoanAppraisalReasonForDelayResourceDetails
                                mapReasonForDelayToSAP(ReasonForDelay reasonForDelay) throws ParseException {

        SAPLoanAppraisalReasonForDelayResourceDetails detailsResource = new SAPLoanAppraisalReasonForDelayResourceDetails();
        detailsResource.setId(reasonForDelay.getId().toString());
        detailsResource.setAppraisalId(reasonForDelay.getLoanAppraisal().getId().toString());
        detailsResource.setHeldby(reasonForDelay.getHeldBy());
        detailsResource.setStatusofproposal(reasonForDelay.getStatus());
        if (reasonForDelay.getDate() != null)
            detailsResource.setRsnForDelayDate(dataConversionUtility.convertDateToSAPFormat(reasonForDelay.getDate()));
        else
            detailsResource.setRsnForDelayDate(null);

        return detailsResource;
    }



}
