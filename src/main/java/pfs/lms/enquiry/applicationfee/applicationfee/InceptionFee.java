package pfs.lms.enquiry.applicationfee.applicationfee;

import lombok.*;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.domain.AggregateRoot;

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
@EqualsAndHashCode(of = {"applicationFee", "invoiceNumber"}, callSuper = false)
public class InceptionFee extends AggregateRoot<InceptionFee> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ApplicationFee applicationFee;

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Double amount;
    private Double taxAmount;
    private Double totalAmount;
    private Double amountReceived;
    private String rtgsNumber;
    private String referenceNumber;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
