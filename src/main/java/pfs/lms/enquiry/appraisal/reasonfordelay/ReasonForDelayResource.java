package pfs.lms.enquiry.appraisal.reasonfordelay;

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
public class ReasonForDelayResource {

    private UUID id;
    private UUID loanApplicationId;

    private String statusOfProposal;
    private LocalDate date;
    private String heldBy;
    private String status;
}
