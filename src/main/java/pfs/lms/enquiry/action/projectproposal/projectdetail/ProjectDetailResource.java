package pfs.lms.enquiry.action.projectproposal.projectdetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectDetailResource {

    private UUID id;
    private UUID projectProposalId;

    private String projectName;
    private String borrowerName;
    private String promoterName;
    private String loanPurpose;
    private Double projectCapacity;
    private String projectCapacityUnit;
    private String state;
    private String district;
    private String productType;
    private String loanClass;
    private String assistanceType; // (Loan Purpose)
    private String financingType;
    private String projectType;
    private String projectCoreSector;
    private String renewableFlag;
    private String policyExposure;
    private String endUseOfFunds;
    private Double fees;
    private Integer tenorYear;
    private Integer tenorMonths;
    private Integer moratoriumPeriod;
    private String moratoriumPeriodUnit;
    private Integer constructionPeriod;
    private String constructionPeriodUnit;
}
