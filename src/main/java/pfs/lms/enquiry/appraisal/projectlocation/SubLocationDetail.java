package pfs.lms.enquiry.appraisal.projectlocation;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"loanAppraisal", "serialNumber"}, callSuper = false)
public class SubLocationDetail extends AggregateRoot<SubLocationDetail> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

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

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
