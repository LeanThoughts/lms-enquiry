package pfs.lms.enquiry.appraisal.projectlocation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MainLocationDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private String location;
    private String state;
    private String district;
    private String region;
    private String nearestVillage;
    private Integer nearestVillageDistance;
    private String nearestRailwayStation;
    private Integer nearestRailwayStationDistance;
    private String nearestAirport;
    private Integer nearestAirportDistance;
    private String nearestSeaport;
    private Integer nearestSeaportDistance;
    private String nearestFunctionalAirport;
    private Integer nearestFunctionalAirportDistance;
}