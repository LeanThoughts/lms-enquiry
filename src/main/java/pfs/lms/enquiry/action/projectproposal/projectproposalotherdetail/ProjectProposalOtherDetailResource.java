package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

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
public class ProjectProposalOtherDetailResource {

    private UUID id;
    private UUID projectProposalId;

    private String sourceAndCashFlow;
    private LocalDate optimumDateOfLoan;
    private String consolidatedGroupLeverage;
    private Double totalDebtTNW;
    private Double tolTNW;
    private Double totalDebtTNWPercentage;
    private Double tolTNWPercentage;
    private String delayInDebtServicing;
}
