package pfs.lms.enquiry.action.projectproposal.shareholder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ShareHolderResource {

    private UUID id;
    private UUID projectProposalId;

    private String companyName;
    private Double equityCapital;
    private Double percentageHolding;
}
