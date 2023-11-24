package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetail;
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
                                mapFurtherDetailToSAP(ICCFurtherDetail ICCFurtherDetail) throws ParseException {

        dataConversionUtility = new DataConversionUtility();
        SAPLoanAppraisalFurtherDetailResourceDetails detailsResource = new SAPLoanAppraisalFurtherDetailResourceDetails();

        detailsResource.setId(ICCFurtherDetail.getId().toString());
        detailsResource.setIccApprovalId(ICCFurtherDetail.getIccApproval().getId().toString());
//        detailsResource.setFurtherDetails(ICCFurtherDetail.getFurtherDetails());

//        if (ICCFurtherDetail.getDate() != null)
//            detailsResource.setDate(dataConversionUtility.convertDateToSAPFormat(ICCFurtherDetail.getDate()));
//        else
//            detailsResource.setDate(null);

        return detailsResource;
    }



}
