package pfs.lms.enquiry.monitoring.tra;

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
public class    TrustRetentionAccountStatement extends AbstractEntity  implements Cloneable{

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TrustRetentionAccount trustRetentionAccount;

    private Integer serialNumber;
    private String viewRights;
    private String remarks;
    private String periodQuarter;
    private String periodYear;
    private String documentTitle;
    private String documentType;
    private String fileReference;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
