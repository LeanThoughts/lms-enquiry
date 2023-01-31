package pfs.lms.enquiry.monitoring.lia;

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
public class LIAReportAndFee extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LendersInsuranceAdvisor lendersInsuranceAdvisor;

    private Integer serialNumber;
    private String reportType;
    private LocalDate dateOfReceipt;
    private LocalDate invoiceDate;
    private String invoiceNo;
    private Double feeAmount;
    private String statusOfFeeReceipt;
    private String statusOfFeePaid;
    private String documentTitle;
    private LocalDate nextReportDate;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}