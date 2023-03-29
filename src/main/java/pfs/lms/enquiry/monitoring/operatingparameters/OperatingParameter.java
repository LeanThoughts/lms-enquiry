package pfs.lms.enquiry.monitoring.operatingparameters;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

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
public class OperatingParameter extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;
    private String month;
    private Integer year;
    private Double exportNetGeneration; //(Million Units)
    private Double plfCufActual; // (%age)
    private Double applicableTariff; // (Rs / Unit)
    private Double revenue; // (Rs in Lakhs)
    private LocalDate dateOfInvoice;
    private LocalDate dateOfPaymentReceipt;
    private Double carbonDiOxideEmission; // (Tonnes)
    private Double waterSaved;
//    private String remarks;
//    private String actualYearlyAveragePlfCuf;
    private String documentType;
    private String documentTitle;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
