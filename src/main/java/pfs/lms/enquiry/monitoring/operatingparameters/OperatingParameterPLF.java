package pfs.lms.enquiry.monitoring.operatingparameters;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by sajeev on 16-Apr-21.
 */
@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class OperatingParameterPLF extends AbstractEntity implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;
    private Integer year;
    private String designPlfCuf;
    private Double actualYearlyAveragePlfCuf;
    private Double miUnGenerated;

    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
 }
