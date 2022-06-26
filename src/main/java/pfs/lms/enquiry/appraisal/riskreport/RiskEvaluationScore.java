package pfs.lms.enquiry.appraisal.riskreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RiskEvaluationScore {
    public String RiskEvalId;
    public String RatingType;
    public String RiskRatingId;
    public String Grade;
    public String Score;
}
