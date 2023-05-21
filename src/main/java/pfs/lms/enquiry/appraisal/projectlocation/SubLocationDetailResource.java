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
public class SubLocationDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private String location;
    private String state;
    private String district;
    private String region;
    private String nearestVillage;
    private Double nearestVillageDistance;
    private String nearestRailwayStation;
    private Double nearestRailwayStationDistance;
    private String nearestAirport;
    private Double nearestAirportDistance;
    private String nearestSeaport;
    private Double nearestSeaportDistance;
    private String nearestFunctionalAirport;
    private Double nearestFunctionalAirportDistance;
}
