package pfs.lms.enquiry.action.projectproposal.creditrating;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CreditRatingResource {

    private UUID id;
    private UUID projectProposalId;

    private String creditRating;
    private String creditRatingAgency;
    private String creditStandingInstruction;
    private String creditStandingText;
}
