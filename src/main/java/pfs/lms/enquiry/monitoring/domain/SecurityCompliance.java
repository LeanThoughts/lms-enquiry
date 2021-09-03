package pfs.lms.enquiry.monitoring.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SecurityCompliance extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;

    private String collateralObjectType;

    private Integer quantity;

    private String applicability;

    private String collateralAgreementType;

    private String collateralAgreementTypeDescription;

    private String timelines;

    private LocalDate dateOfCreation;

    private LocalDate validityDate;

    private Double value; //(if any)

    private LocalDate securityPerfectionDate;

    private String remarks;

    private String actionPeriodPrefix;

    private Integer periodNumber;

    private String actionPeriodSuffix;

    private String eventType;

    private LocalDate eventDate;

    private LocalDate timelineDate;

    private String location;

    private String additionalText;

    private Double realEstateLandArea;

   private String areaUnitOfMeasure;

    private Integer securityNoOfUnits;

    private Double securityFaceValueAmount;

    private Double holdingPercentage;

    private String validityPeriod;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
