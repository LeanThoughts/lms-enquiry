package pfs.lms.enquiry.monitoring.valuer;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ValuerReportAndFee extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private Valuer valuer;

    private Integer serialNumber;
    private String reportType;
    private LocalDate dateOfReceipt;
    private LocalDate invoiceDate;
    private String invoiceNo;
    private Double feeAmount;
    private String statusOfFeeReceipt;
    private String statusOfFeePaid;
    private String documentTitle;
    private String documentType;
    private LocalDate nextReportDate;
    private String fileReference;
    private LocalDate reportDate;
    private Double percentageCompletion;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}