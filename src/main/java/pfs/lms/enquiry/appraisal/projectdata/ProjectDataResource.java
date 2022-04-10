package pfs.lms.enquiry.appraisal.projectdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectDataResource {

    private UUID id;
    private UUID loanApplicationId;

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

}
