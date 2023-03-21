package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalSubLocationDetailResourceDetails {


    @JsonProperty(value = "SerialNo")
    private String  serialNo;

    @JsonProperty(value = "Location")
    private String  location;

    @JsonProperty(value = "AppraisalId")
    private String  appraisalId;

    @JsonProperty(value = "Region")
    private String  region;

    @JsonProperty(value = "State")
    private String  state;

    @JsonProperty(value = "District")
    private String  district;

    @JsonProperty(value = "NearestVillage")
    private String  nearestVillage;
    @JsonProperty(value = "NearestVillageDistance")
    private String  nearestVillageDistance;

    @JsonProperty(value = "NearestRailwayStation")
    private String  nearestRailwayStation;
    @JsonProperty(value = "NearestRailwayStationDistance")
    private String  nearestRailwayStationDistance;

    @JsonProperty(value = "NearestAirport")
    private String  nearestAirport;
    @JsonProperty(value = "NearestAirportDistance")
    private String   nearestAirportDistance;


    @JsonProperty(value = "NearestSeaport")
    private String   nearestSeaport;
    @JsonProperty(value = "NearestSeaportDistance")
    private String   nearestSeaportDistance;

    @JsonProperty(value = "NearestFunctionalAirport")
    private String  nearestFunctionalAirport;
    @JsonProperty(value = "NearestFunctionalAirportDistance")
    private String   nearestFunctionalAirportDistance;



}
