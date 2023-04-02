package pfs.lms.enquiry.monitoring.endusecertificate;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
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
@AllArgsConstructor
public class EndUseCertificate extends AggregateRoot<EndUseCertificate> implements Cloneable    {

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;

    private LocalDate endUseCertificateDate;
    private LocalDate eventDate;
    private LocalDate endUseCertificateDueDate;
    private String documentType;
    private String documentTitle;
    private String fileReference;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
