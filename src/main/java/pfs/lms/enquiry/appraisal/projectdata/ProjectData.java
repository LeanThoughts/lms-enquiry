package pfs.lms.enquiry.appraisal.projectdata;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectData extends AggregateRoot<ProjectData> implements Cloneable {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private String projectName;
    private String typeOfFunding;
    private String policyApplicable;
    private String technology;
    private Double projectCapacityUnitSize;
    private String projectCapacityUnitMeasure;
    private Integer numberOfUnits;
    private String designPlfCuf;
    private String mainContractor;
    private String epcContractor;
    private String resourceAssessmentAgency;
    private String oandmContractor;

    private Double offtakeVolume;
    private String saleRate;
    private String fuelMix;
    private Double fuelCost;
    private Double stationHeatRate;
    private Double oandmExpenses;
    private Double totalLand;
    private LocalDate projectCOD;
    private Double ppaRate;
    private String offTakerCompany;
    private Double ippPercentage;
    private Double groupCaptivePercentage;
    private Double thirdPartyPercentage;
    private Double marketPercentage;

    private Double epcCost;
    private Double overallProjectCost;
    private String debtEquityRatio;
    private String totalDebt;
    private Double roiPreCod;
    private Double roiPostCod;
    private Integer constructionPeriod;
    private String constructionPeriodUnit;
    private Integer moratoriumPeriod;
    private String moratoriumPeriodUnit;
    private Integer tenorPeriod;
    private String tenorUnit;
    private String repaymentSchedule;
    private Double dscrMinimum;
    private Double dscrAverage;
    private Double levCostTotal;
    private Double levCostFixed;
    private Double levCostVariable;
    private Integer workingCapitalCycle;
    private String workingCapitalUnit;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
