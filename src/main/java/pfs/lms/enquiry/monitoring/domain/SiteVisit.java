package pfs.lms.enquiry.monitoring.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.domain.LoanApplication;

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
public class SiteVisit extends AbstractEntity implements  Cloneable{

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    private String loanMonitoringId;
    private String loanAppraisalId;

    private Integer serialNumber;

    private String siteVisitType;

    private LocalDate  actualCOD;

    private LocalDate dateOfSiteVisit;

    private LocalDate dateOfLendersMeet;

    private String documentType;
    private String documentTitle;
    private String fileReference;

    private String fiscalYear;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
