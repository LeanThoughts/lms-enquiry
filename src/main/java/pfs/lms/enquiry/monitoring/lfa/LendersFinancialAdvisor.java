package pfs.lms.enquiry.monitoring.lfa;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

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
public class LendersFinancialAdvisor extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private Integer serialNumber;

    private String bpCode;

    private String name;

    private LocalDate dateOfAppointment;

    private LocalDate contractPeriodFrom;

    private LocalDate contractPeriodTo;

    private String contactPerson;

    private String contactNumber;

    private String email;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
