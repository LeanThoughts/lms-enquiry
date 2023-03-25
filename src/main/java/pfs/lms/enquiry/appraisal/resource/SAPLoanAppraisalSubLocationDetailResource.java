package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.projectlocation.MainLocationDetail;
import pfs.lms.enquiry.appraisal.projectlocation.SubLocationDetail;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalSubLocationDetailResource implements Serializable {

    public SAPLoanAppraisalSubLocationDetailResource() {
        sapLoanAppraisalSubLocationDetailResourceDetails = new SAPLoanAppraisalSubLocationDetailResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalSubLocationDetailResourceDetails sapLoanAppraisalSubLocationDetailResourceDetails;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSapLoanAppraisalSubLocationDetailResourceDetails(SAPLoanAppraisalSubLocationDetailResourceDetails sapLoanAppraisalSubLocationDetailResourceDetails) {
        this.sapLoanAppraisalSubLocationDetailResourceDetails = sapLoanAppraisalSubLocationDetailResourceDetails;
    }

    public SAPLoanAppraisalSubLocationDetailResourceDetails
                                mapSubLocationDetails(SubLocationDetail locationDetail) throws ParseException {

        SAPLoanAppraisalSubLocationDetailResourceDetails detailsResource = new SAPLoanAppraisalSubLocationDetailResourceDetails();

        detailsResource.setSerialNo(locationDetail.getSerialNumber().toString());

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
