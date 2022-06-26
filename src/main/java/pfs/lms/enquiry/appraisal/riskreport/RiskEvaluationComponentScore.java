package pfs.lms.enquiry.appraisal.riskreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RiskEvaluationComponentScore {
    public String RiskEvalId;
    public String ProjectPhase;
    public String ComponentId;
    public String ComponentName;
    public String Score;
}
