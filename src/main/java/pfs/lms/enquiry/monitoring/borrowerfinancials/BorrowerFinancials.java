package pfs.lms.enquiry.monitoring.borrowerfinancials;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BorrowerFinancials extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;
    private Integer fiscalYear;
    private Double turnover;
    private Double pat;
    private Double netWorth;
    private LocalDate dateOfExternalRating;
    private LocalDate nextDueDateOfExternalRating;
    private String overAllRating;
    private String annualReturnFileReference;
    private String ratingFileReference;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
