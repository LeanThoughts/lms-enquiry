package pfs.lms.enquiry.appraisal.riskreport;

import java.util.ArrayList;
import java.util.List;

public class RiskEvaluationSummary {
    public RiskEvaluationSummary() {
        this.RiskEvaluation_OverallScore_Nav = new ArrayList<RiskEvaluationScore>();
        this.RiskEvaluation_ComponentScore_Nav   = new ArrayList<RiskEvaluationComponentScore>();
    }

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
