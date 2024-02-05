package pfs.lms.enquiry.documentation.llcfee;

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
public class LLCFeeResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private LocalDate invoiceDate;
    private String invoiceNumber;
    private String feeName;

    private Double feeAmount;
    private String statusOfFeeReceipt;
    private String remarks;
    private String documentName;
    private String documentType;
    private String fileReference;
}
