package pfs.lms.enquiry.iccapproval.loanenhancement;

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
public class LoanEnhancementResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private String iccMeetingNumber;
    private LocalDate iccClearanceDate;
    private Double revisedProjectCost;
    private Double revisedEquity;
    private Double revisedContractAmount;
    private LocalDate revisedCommercialOperationsDate;
    private LocalDate reviseRepaymentStartDate;
    private String remarks;
}
