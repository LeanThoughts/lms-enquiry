package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.furtherdetail.FurtherDetail;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalFurtherDetailResource implements Serializable {

    public SAPLoanAppraisalFurtherDetailResource() {
        sapLoanAppraisalFurtherDetailResourceDetails = new SAPLoanAppraisalFurtherDetailResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalFurtherDetailResourceDetails  sapLoanAppraisalFurtherDetailResourceDetails;

    DataConversionUtility dataConversionUtility;

    public void setSapLoanAppraisalFurtherDetailResourceDetails(SAPLoanAppraisalFurtherDetailResourceDetails sapLoanAppraisalFurtherDetailResourceDetails) {
        this.sapLoanAppraisalFurtherDetailResourceDetails = sapLoanAppraisalFurtherDetailResourceDetails;
    }

    public SAPLoanAppraisalFurtherDetailResourceDetails
                                mapFurtherDetailToSAP(FurtherDetail furtherDetail) throws ParseException {

        SAPLoanAppraisalFurtherDetailResourceDetails detailsResource = new SAPLoanAppraisalFurtherDetailResourceDetails();

        detailsResource.setAppraisalId(furtherDetail.getLoanAppraisal().getId().toString());
        detailsResource.setId(furtherDetail.getId().toString());
        detailsResource.setFurtherDetails(furtherDetail.getFurtherDetails());

        if (detailsResource.getDate() != null)
            detailsResource.setDate(dataConversionUtility.convertDateToSAPFormat(furtherDetail.getDate()));
        else
            detailsResource.setDate(null);

        return detailsResource;
    }



}
