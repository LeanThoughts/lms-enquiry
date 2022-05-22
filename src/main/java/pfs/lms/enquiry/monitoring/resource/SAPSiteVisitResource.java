package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.domain.SiteVisit;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPSiteVisitResource {

    @JsonProperty(value = "d")
    private SAPSiteVisitResourceDetails sapSiteVisitResourceDetails;

    public SAPSiteVisitResourceDetails getSapSiteVisitResourceDetails() {
        return sapSiteVisitResourceDetails;
    }

    public void setSapSiteVisitResourceDetails(SAPSiteVisitResourceDetails sapSiteVisitResourceDetails) {
        this.sapSiteVisitResourceDetails = sapSiteVisitResourceDetails;
    }

    public SAPSiteVisitResourceDetails mapToSAP(SiteVisit siteVisit) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPSiteVisitResourceDetails detailedResource = new SAPSiteVisitResourceDetails();

        detailedResource.setId(siteVisit.getId());
        detailedResource.setMonitorId(siteVisit.getLoanMonitoringId());
        detailedResource.setSerialNo(siteVisit.getSerialNumber());

        if (siteVisit.getActualCOD() != null)
             detailedResource.setActualcod(dataConversionUtility.convertDateToSAPFormat(siteVisit.getActualCOD()));
        else
            detailedResource.setActualcod(null);

        if(siteVisit.getDateOfSiteVisit() != null) {
            detailedResource.setDateofsitevisit(dataConversionUtility.convertDateToSAPFormat(siteVisit.getDateOfSiteVisit()));
        } else
            detailedResource.setDateofsitevisit(null);

        if(siteVisit.getDateOfLendersMeet() != null) {
            detailedResource.setDateoflendersmeet(dataConversionUtility.convertDateToSAPFormat(siteVisit.getDateOfLendersMeet()));
        } else
            detailedResource.setDateoflendersmeet(null);

        return detailedResource;


    }

}
