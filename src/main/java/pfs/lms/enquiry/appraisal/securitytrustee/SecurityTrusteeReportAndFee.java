package pfs.lms.enquiry.appraisal.securitytrustee;

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
public class SecurityTrusteeReportAndFee extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private SecurityTrustee securityTrustee;

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
    private LocalDate reportDate;
    private Double percentageCompletion;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
