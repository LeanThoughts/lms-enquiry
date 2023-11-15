package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.iccapproval.furtherdetail.FurtherDetail;
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

        dataConversionUtility = new DataConversionUtility();
        SAPLoanAppraisalFurtherDetailResourceDetails detailsResource = new SAPLoanAppraisalFurtherDetailResourceDetails();

        detailsResource.setId(furtherDetail.getId().toString());
        detailsResource.setAppraisalId(furtherDetail.getLoanAppraisal().getId().toString());
        detailsResource.setFurtherDetails(furtherDetail.getFurtherDetails());

        if (furtherDetail.getDate() != null)
            detailsResource.setDate(dataConversionUtility.convertDateToSAPFormat(furtherDetail.getDate()));
        else
            detailsResource.setDate(null);

        return detailsResource;
    }



}
