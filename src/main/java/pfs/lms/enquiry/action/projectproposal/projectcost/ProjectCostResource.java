package pfs.lms.enquiry.action.projectproposal.projectcost;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectCostResource {

    private UUID id;
    private UUID projectProposalId;

    private Double projectCost;
    private Double debt;
    private Double equity;
    private Double pfsDebtAmount;
    private Double debtEquityRatio;
    private Double grantAmount;
    private Double debtEquityRatioWithGrant;
}
