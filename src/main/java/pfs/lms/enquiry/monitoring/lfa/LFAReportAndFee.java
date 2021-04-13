package pfs.lms.enquiry.monitoring.lfa;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Blob;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class LFAReportAndFee extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LendersFinancialAdvisor lendersFinancialAdvisor;

    private Integer serialNumber;
    private String reportType;
    private LocalDate quarterOrYear;
    private LocalDate dateOfReceipt;
    private LocalDate invoiceDate;
    private String invoiceNo;
    private Double feeAmount;
    private String statusOfFeeReceipt;
    private String statusOfFeePaid;
    private String documentType;
    private String documentTitle;
    private String fileReference;
    private LocalDate nextReportDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
