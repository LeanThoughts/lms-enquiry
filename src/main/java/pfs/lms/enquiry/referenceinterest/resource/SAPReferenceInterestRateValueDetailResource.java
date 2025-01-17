package pfs.lms.enquiry.referenceinterest.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.batch.SAPBusinessPartnerBankDetailResourceDetail;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class SAPReferenceInterestRateValueDetailResource implements Serializable {

    public SAPReferenceInterestRateValueDetailResource() {
        sapReferenceInterestRateValueResourceDetail = new SAPReferenceInterestRateValueResourceDetail();
    }

    @JsonProperty(value = "d")
    private SAPReferenceInterestRateValueResourceDetail sapReferenceInterestRateValueResourceDetail;
    DataConversionUtility dataConversionUtility = new DataConversionUtility();


    public void setSAPReferenceInterestRateValueResourceDetail(SAPReferenceInterestRateValueResourceDetail sapReferenceInterestRateValueResourceDetail) {
        this.sapReferenceInterestRateValueResourceDetail = sapReferenceInterestRateValueResourceDetail;
    }

    public SAPReferenceInterestRateValueResourceDetail
    mapResourceDetails(ReferenceInterestRateValue referenceInterestRateValue) throws ParseException {

        SAPReferenceInterestRateValueResourceDetail detailsResource = new SAPReferenceInterestRateValueResourceDetail();

        if (referenceInterestRateValue.getReferenceInterestRate().getCode() != null)
            detailsResource.setReferenz(referenceInterestRateValue.getReferenceInterestRate().getCode().toString());
        else
            detailsResource.setReferenz("");

        if(referenceInterestRateValue.getValidFromDate() != null){
            detailsResource.setDatab(dataConversionUtility.convertDateToSAPFormat(referenceInterestRateValue.getValidFromDate()));
        }

        if(referenceInterestRateValue.getInterestRate() != null) {
            detailsResource.setZsoll(referenceInterestRateValue.getInterestRate().toString());
        }

        return detailsResource;
    }


}
