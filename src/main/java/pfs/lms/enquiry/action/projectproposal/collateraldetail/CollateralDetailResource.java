package pfs.lms.enquiry.action.projectproposal.collateraldetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CollateralDetailResource {

    private UUID id;
    private UUID projectProposalId;

    private String collateralType;
    private String details;
}
