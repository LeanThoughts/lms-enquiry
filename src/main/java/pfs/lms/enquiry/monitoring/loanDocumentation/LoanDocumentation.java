package pfs.lms.enquiry.monitoring.loanDocumentation;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.npa.NPA;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class LoanDocumentation extends AggregateRoot<LoanDocumentation> implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;
    private String documentationTypeCode;
    private String documentationTypeDescription;
    private LocalDate executionDate;
    private LocalDate approvalDate;
    private String loanDocumentationStatusCode;
    private String  loanDocumentationStatusCodeDescription;
    private String documentType;
    private String documentTitle;
    private String fileReference;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
