package pfs.lms.enquiry.sanction.paymentreceiptpresanction;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.sanction.Sanction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"sanction", "proformaInvoiceNumber", "proformaInvoiceDate"}, callSuper = false)
public class PaymentReceiptPreSanction extends AggregateRoot<PaymentReceiptPreSanction> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Sanction sanction;

    private String proformaInvoiceNumber;
    private LocalDate proformaInvoiceDate;
    private Double feeInvoice;
    private Double amount;
    private String payee;
    private Double amountReceived;
    private LocalDate dateOfTransfer;
    private String rtgsNeftNumber;
    private String referenceNumber;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
