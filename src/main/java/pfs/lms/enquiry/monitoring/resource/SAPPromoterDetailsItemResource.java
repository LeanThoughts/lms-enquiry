package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.promoterdetails.PromoterDetailItem;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Component
public class SAPPromoterDetailsItemResource {


    @JsonProperty(value = "d")
    private SAPPromoterDetailsItemsResourceDetails sapPromoterDetailsItemsResourceDetails;

    public SAPPromoterDetailsItemsResourceDetails getSapPromoterDetailsItemsResourceDetails() {
        return sapPromoterDetailsItemsResourceDetails;
    }

    public void setSapPromoterDetailsItemsResourceDetails(SAPPromoterDetailsItemsResourceDetails sapPromoterDetailsItemsResourceDetails) {
        this.sapPromoterDetailsItemsResourceDetails = sapPromoterDetailsItemsResourceDetails;
    }

    public SAPPromoterDetailsItemsResourceDetails mapToSAP(PromoterDetailItem promoterDetailItem) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPPromoterDetailsItemsResourceDetails detailedResource = new SAPPromoterDetailsItemsResourceDetails();
        detailedResource.setId(promoterDetailItem.getId().toString());

        //detailedResource.setMonitorId(promoterDetailsItem.get);


        detailedResource.setSerialNo(promoterDetailItem.getSerialNumber() );
        detailedResource.setShareholdingcompany(promoterDetailItem.getShareHoldingCompany());
        detailedResource.setCurrentpaidupce(promoterDetailItem.getPaidupCapitalEquityCurrent());
        detailedResource.setCurrentequitylinkinstrument(promoterDetailItem.getPaidupCapitalEquityCurrent());
        detailedResource.setSanctionedequitylinkinstrument(promoterDetailItem.getEquityLinkInstrumentSanction());
        detailedResource.setCurrentequitylinkinstrument(promoterDetailItem.getEquityLinkInstrumentCurrent());
        detailedResource.setGroupexposure(promoterDetailItem.getGroupExposure());
        if (promoterDetailItem.getDateOfChange() !=null)
            detailedResource.setDateofchange(dataConversionUtility.convertDateToSAPFormat(promoterDetailItem.getDateOfChange()));
        else
            detailedResource.setDateofchange(null);


        return detailedResource;
    }





}
