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

    private  String description;

    private String feeAmount;
    private String cGstAmount;
    private String iGstAmount;
    private String sGstAmount;


    private String sapFIDocumentNumberFee;
//    private String sapFIDocumentNumberCGST;
//    private String sapFIDocumentNumberIGST;
//    private String sapFIDocumentNumberSGST;


    private String headerDocumentNumber;

    //0	New
    //1	Entered
    //2	Activated
    //3	Deleted
    //4	Posted
    //5	Reversed
    //6	Completed
    //7	Reset
    private String statusCode;
    private String statusDescription;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
