package pfs.lms.enquiry.appraisal.riskreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RiskEvaluationSummary {
    public String RiskEvalId;
    public String LoanContractId;
    public String RiskPrjType;
    public String RiskPrjTypeT;
    public String ProjectName;
    public String RiskPrjPhase;
    public String RatingDate;
    public String CurrDepartment;
    public String InitiatedBy;
    public String FirstLvlApprover;
    public String SecondLvlApprover;
    public String ThirdLvlApprover;
    public String LatestReviewer;
    public String WfStatusDesc;
    public String FinalGrade;

    public List<RiskEvaluationScore> RiskEvaluation_OverallScore_Nav;

    public List<RiskEvaluationComponentScore> RiskEvaluation_ComponentScore_Nav;
}
