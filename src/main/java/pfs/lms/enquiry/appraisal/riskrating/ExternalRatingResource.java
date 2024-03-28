package pfs.lms.enquiry.appraisal.riskrating;

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
public class ExternalRatingResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;

    private LocalDate validityDate;
    private String rating;
    private String ratingAgency;
    private String creditStanding;
    private String creditStandingInstruction;
    private String creditStandingText;
}
