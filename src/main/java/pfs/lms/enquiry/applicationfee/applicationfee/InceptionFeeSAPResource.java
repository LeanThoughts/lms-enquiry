package pfs.lms.enquiry.applicationfee.applicationfee;

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
public class InceptionFeeSAPResource {

    private UUID id;
    private UUID loanApplicationId;
    private String loanContractId;

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Double amount;
    private Double taxAmount;
    private Double totalAmount;
    private Double amountReceived;
    private String rtgsNumber;
    private String referenceNumber;
    private String remarks;
}
