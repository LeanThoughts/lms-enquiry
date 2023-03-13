package pfs.lms.enquiry.monitoring.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class FinancialCovenants extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;
    private String financialCovenantType;
    private Integer financialYear;
    private Double debtEquityRatio;
    private Double dscr;
    private Double tolTnw;
    private String remarksForDeviation;
    private String documentType;
    private String documentTitle;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
