package pfs.lms.enquiry.monitoring.lie;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.CascadeType;
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
public class LIEReportAndFee extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LendersIndependentEngineer lendersIndependentEngineer;

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