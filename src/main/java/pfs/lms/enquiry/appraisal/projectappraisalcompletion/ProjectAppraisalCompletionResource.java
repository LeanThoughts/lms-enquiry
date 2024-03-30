package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

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
public class ProjectAppraisalCompletionResource {

    private UUID id;
    private UUID loanApplicationId;

    private LocalDate dateOfProjectAppraisalCompletion;

    private LocalDate agendaNoteApprovalByDirA;
    private LocalDate agendaNoteApprovalByDirB;
    private LocalDate agendaNoteApprovalByMDAndCEO;
    //private LocalDate agendaNoteSubmissionToCoSecy;

    private String directorA;
    private String directorB;
    private String mdAndCEO;

    private String remarks;
    private String fileReference;
    private String documentType;
    private String documentTitle;
}
