package pfs.lms.enquiry.monitoring.loanDocumentation;

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
public class LoanDocumentation extends AggregateRoot<LoanDocumentation> implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
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
