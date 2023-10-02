package pfs.lms.enquiry.sanction.sanctionletter;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SanctionLetterResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private LocalDate sanctionLetterIssueDate;
    private LocalDate borrowerRequestLetterDate;
    private LocalDate sanctionLetterAcceptanceDate;
    private String documentType;
    private String documentTitle;
    private String fileReference;
    private String remarks;

    private String type;
    private LocalDate dateOfAmendment;
    private Double originalSanctionAmount;
    private Double originalInterestRate;
    private Double revisedSanctionAmount;
    private Double revisedInterestRate;
    private LocalDate sanctionLetterValidToDate;
}
