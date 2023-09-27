package pfs.lms.enquiry.sanction.paymentreceiptpostsanction;

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
public class PaymentReceiptPostSanctionResource {

    private UUID id;
    private UUID loanApplicationId;

    private String proformaInvoiceNumber;
    private LocalDate proformaInvoiceDate;
    private Double feeInvoice;
    private Double amount;
    private String payee;
    private Double amountReceived;
    private LocalDate dateOfTransfer;
    private String rtgsNeftNumber;
    private String referenceNumber;
}
