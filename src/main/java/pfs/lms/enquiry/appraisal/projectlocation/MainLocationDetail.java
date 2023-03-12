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
    private Integer nearestVillageDistance;
    private String nearestRailwayStation;
    private Integer nearestRailwayStationDistance;
    private String nearestAirport;
    private Integer nearestAirportDistance;
    private String nearestSeaport;
    private Integer nearestSeaportDistance;
    private String nearestFunctionalAirport;
    private Integer nearestFunctionalAirportDistance;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
