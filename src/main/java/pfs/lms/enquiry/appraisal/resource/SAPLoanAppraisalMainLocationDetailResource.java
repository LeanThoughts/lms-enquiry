package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.projectlocation.MainLocationDetail;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalMainLocationDetailResource implements Serializable {

    public SAPLoanAppraisalMainLocationDetailResource() {
        sapLoanAppraisalMainLocationDetailResourceDetails = new SAPLoanAppraisalMainLocationDetailResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalMainLocationDetailResourceDetails sapLoanAppraisalMainLocationDetailResourceDetails;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSapLoanAppraisalMainLocationDetailResourceDetails(SAPLoanAppraisalMainLocationDetailResourceDetails sapLoanAppraisalMainLocationDetailResourceDetails) {
        this.sapLoanAppraisalMainLocationDetailResourceDetails = sapLoanAppraisalMainLocationDetailResourceDetails;
    }

    public SAPLoanAppraisalMainLocationDetailResourceDetails
                                mapMainLocationDetails(MainLocationDetail locationDetail) throws ParseException {

         SAPLoanAppraisalMainLocationDetailResourceDetails detailsResource = new SAPLoanAppraisalMainLocationDetailResourceDetails();

        detailsResource.setLocation(locationDetail.getLocation());
        detailsResource.setAppraisalId(locationDetail.getLoanAppraisal().getId().toString());
        detailsResource.setRegion(locationDetail.getRegion());
        detailsResource.setState(locationDetail.getState());
        detailsResource.setDistrict(locationDetail.getDistrict());

        detailsResource.setNearestVillage(locationDetail.getNearestVillage());
        if(locationDetail.getNearestVillageDistance()!=null)
            detailsResource.setNearestVillageDistance(locationDetail.getNearestVillageDistance().toString());

        detailsResource.setNearestRailwayStation(locationDetail.getNearestRailwayStation());
        if(locationDetail.getNearestRailwayStationDistance() != null)
            detailsResource.setNearestRailwayStationDistance(locationDetail.getNearestRailwayStationDistance().toString());

        detailsResource.setNearestAirport(locationDetail.getNearestAirport());
        if (locationDetail.getNearestAirportDistance()  != null)
            detailsResource.setNearestAirportDistance(locationDetail.getNearestAirportDistance().toString());

        detailsResource.setNearestSeaport(locationDetail.getNearestSeaport());
        if(locationDetail.getNearestSeaportDistance() !=null)
            detailsResource.setNearestSeaportDistance(locationDetail.getNearestSeaportDistance().toString());

        detailsResource.setNearestFunctionalAirport(locationDetail.getNearestFunctionalAirport());
        if(locationDetail.getNearestFunctionalAirportDistance() != null)
            detailsResource.setNearestFunctionalAirportDistance(locationDetail.getNearestFunctionalAirportDistance().toString());


        return detailsResource;
    }



}
