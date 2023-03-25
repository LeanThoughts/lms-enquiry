package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Component
public class SAPPromoterDetailsResource {


    @JsonProperty(value = "d")
    private SAPPromoterDetailsResourceDetails  sapPromoterDetailsResourceDetails;


    public SAPPromoterDetailsResourceDetails getSapPromoterDetailsResourceDetails() {
        return sapPromoterDetailsResourceDetails;
    }

    public void setSapPromoterDetailsResourceDetails(SAPPromoterDetailsResourceDetails sapPromoterDetailsResourceDetails) {
        this.sapPromoterDetailsResourceDetails = sapPromoterDetailsResourceDetails;
    }

//    public SAPPromoterDetailsResourceDetails mapToSAP(PromoterDetail promoterDetails) throws ParseException {
//
//        DataConversionUtility dataConversionUtility = new DataConversionUtility();
//
//        SAPPromoterDetailsResourceDetails detailedResource = new SAPPromoterDetailsResourceDetails();
//        detailedResource.setId(promoterDetails.getId().toString());
//        detailedResource.setMonitorId(promoterDetails.getLoanMonitor().getId().toString());
//
//        if (promoterDetails.getDateOfChange() != null) {
//            detailedResource.setDateofchange(dataConversionUtility.convertDateToSAPFormat(promoterDetails.getDateOfChange()));
//        } else
//            detailedResource.setDateofchange(null);
//        detailedResource.setGroupexposure(promoterDetails.getGroupExposure());
//
//        for (PromoterDetailItem promoterDetailItem : promoterDetails.getPromoterDetailItemSet()) {
//            SAPPromoterDetailsItemsResourceDetails sapPromoterDetailsItemsResourceDetails = new SAPPromoterDetailsItemsResourceDetails();
//            sapPromoterDetailsItemsResourceDetails.setId(promoterDetailItem.getId().toString());
//            sapPromoterDetailsItemsResourceDetails.setSerialNo(promoterDetailItem.getSerialNumber());
//            sapPromoterDetailsItemsResourceDetails.setPromDtlId(promoterDetails.getId().toString());
//            sapPromoterDetailsItemsResourceDetails.setSanctionedequitylinkinstrument(promoterDetailItem.getEquityLinkInstrumentSanction());
//            sapPromoterDetailsItemsResourceDetails.setCurrentequitylinkinstrument(promoterDetailItem.getEquityLinkInstrumentCurrent());
//            sapPromoterDetailsItemsResourceDetails.setCurrentpaidupce(promoterDetailItem.getPaidupCapitalEquityCurrent());
//            sapPromoterDetailsItemsResourceDetails.setSanctionedpaidupce(promoterDetailItem.getPaidupCapitalEquitySanction());
//            //sapPromoterDetailsItemsResourceDetails.setBupaId(promoterDetailsItem.se);
//            detailedResource.add(sapPromoterDetailsItemsResourceDetails);
//        }
//
//        return detailedResource;
//    }





}
