package pfs.lms.enquiry.monitoring.promoterdetails;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

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
@AllArgsConstructor
public class PromoterDetailItem extends AggregateRoot<PromoterDetailItem> implements Cloneable    {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;
    private String  shareHoldingCompany;
    private Double  paidupCapitalEquitySanction;
    private Double  paidupCapitalEquityCurrent;
    private Double  equityLinkInstrumentSanction; // (CCD/ CCPS/ Unsecured loan) - Sanction
    private Double  equityLinkInstrumentCurrent; // (CCD/ CCPS/ Unsecured loan) - Current


    private LocalDate dateOfChange;
    private Double groupExposure;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
