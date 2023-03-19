package pfs.lms.enquiry.monitoring.npa;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.promoterdetails.PromoterDetail;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class NPA extends AggregateRoot<PromoterDetail> implements Cloneable {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private String assetClass;
    private LocalDate npaDeclarationDate;
    private Double totalLoanAsset;
    private Double securedLoanAsset;
    private Double unSecuredLoanAsset;
    private String restructuringType;
    private String smaCategory;
    private LocalDate fraudDate;
    private Double impairmentReserve;
    private Double provisionAmount;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
