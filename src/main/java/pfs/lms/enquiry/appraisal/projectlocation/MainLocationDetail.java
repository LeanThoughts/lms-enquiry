package pfs.lms.enquiry.appraisal.projectlocation;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"loanAppraisal", "serialNumber"}, callSuper = false)
public class MainLocationDetail extends AggregateRoot<MainLocationDetail> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

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

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

    public LoanAppraisal getLoanAppraisal() {
        return loanAppraisal;
    }

    public void setLoanAppraisal(LoanAppraisal loanAppraisal) {
        this.loanAppraisal = loanAppraisal;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNearestVillage() {
        return nearestVillage;
    }

    public void setNearestVillage(String nearestVillage) {
        this.nearestVillage = nearestVillage;
    }

    public Double getNearestVillageDistance() {
        return nearestVillageDistance;
    }

    public void setNearestVillageDistance(Double nearestVillageDistance) {
        this.nearestVillageDistance = nearestVillageDistance;
    }

    public String getNearestRailwayStation() {
        return nearestRailwayStation;
    }

    public void setNearestRailwayStation(String nearestRailwayStation) {
        this.nearestRailwayStation = nearestRailwayStation;
    }

    public Double getNearestRailwayStationDistance() {
        return nearestRailwayStationDistance;
    }

    public void setNearestRailwayStationDistance(Double nearestRailwayStationDistance) {
        this.nearestRailwayStationDistance = nearestRailwayStationDistance;
    }

    public String getNearestAirport() {
        return nearestAirport;
    }

    public void setNearestAirport(String nearestAirport) {
        this.nearestAirport = nearestAirport;
    }

    public Double getNearestAirportDistance() {
        return nearestAirportDistance;
    }

    public void setNearestAirportDistance(Double nearestAirportDistance) {
        this.nearestAirportDistance = nearestAirportDistance;
    }

    public String getNearestSeaport() {
        return nearestSeaport;
    }

    public void setNearestSeaport(String nearestSeaport) {
        this.nearestSeaport = nearestSeaport;
    }

    public Double getNearestSeaportDistance() {
        return nearestSeaportDistance;
    }

    public void setNearestSeaportDistance(Double nearestSeaportDistance) {
        this.nearestSeaportDistance = nearestSeaportDistance;
    }

    public String getNearestFunctionalAirport() {
        return nearestFunctionalAirport;
    }

    public void setNearestFunctionalAirport(String nearestFunctionalAirport) {
        this.nearestFunctionalAirport = nearestFunctionalAirport;
    }

    public Double getNearestFunctionalAirportDistance() {
        return nearestFunctionalAirportDistance;
    }

    public void setNearestFunctionalAirportDistance(Double nearestFunctionalAirportDistance) {
        this.nearestFunctionalAirportDistance = nearestFunctionalAirportDistance;
    }
}
