package pfs.lms.enquiry.documentation.legalcouncelreport;

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
public class LegalCounselReportResource {

    private UUID id;
    private UUID legalCounselId;

    private Integer serialNumber;
    private LocalDate submissionDate;

    private String fiscalYear;
    private String period;
    private String remarks;

    private String documentName;
    private String documentType;
    private String fileReference;

    private Boolean deleteFlag;
}
