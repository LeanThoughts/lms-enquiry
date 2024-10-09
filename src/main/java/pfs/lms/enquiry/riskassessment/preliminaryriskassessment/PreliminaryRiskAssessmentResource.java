package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

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
public class PreliminaryRiskAssessmentResource {

    private UUID id;
    private UUID loanApplicationId;

    private LocalDate dateOfAssessment;
    private String remarksByRiskDepartment;
    private LocalDate mdApprovalDate;
    private String remarks;

    private String documentTitle;
    private String documentType;
    private String fileReference;
}
