package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPBusinessPartnerIndustryResource implements Serializable {

    public SAPBusinessPartnerIndustryResource() {
        sapBusinessPartnerIndustryResourceDetail = new SAPBusinessPartnerIndustryResourceDetail();
    }

    @JsonProperty(value = "d")
    private SAPBusinessPartnerIndustryResourceDetail sapBusinessPartnerIndustryResourceDetail;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSapBusinessPartnerIndustryResourceDetail(SAPBusinessPartnerIndustryResourceDetail sapBusinessPartnerIndustryResourceDetail) {
        this.sapBusinessPartnerIndustryResourceDetail = sapBusinessPartnerIndustryResourceDetail;
    }

    public SAPBusinessPartnerIndustryResourceDetail
                                mapResource(BusinessPartnerIndustry businessPartnerIndustry) throws ParseException {

        SAPBusinessPartnerIndustryResourceDetail detailsResource = new SAPBusinessPartnerIndustryResourceDetail();
        if (businessPartnerIndustry.getPartner().getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(businessPartnerIndustry.getPartner().getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");

//        if(businessPartnerIndustry.getIndustrySystem() !=null)
//            detailsResource.setIndustrySystem(businessPartnerIndustry.getIndustrySystem().getValue());
//        else detailsResource.setIndustrySystem("");
//
//        if(businessPartnerIndustry.getIndustryType() !=null)
//            detailsResource.setIndustryType(businessPartnerIndustry.getIndustryType().getValue());
//        else detailsResource.setIndustryType("");


        return detailsResource;
    }



}
