package pfs.lms.enquiry.appraisal.syndicateconsortium;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SyndicateConsortiumResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;

    private Double sanctionedAmount;
    private Double disbursedAmount;

    private String bankKey;
    private String bankName;
    private String currency;
    private String approvalStatus;
    private String documentStage;
    private String disbursementStatus;

    private boolean leadBank;
}
