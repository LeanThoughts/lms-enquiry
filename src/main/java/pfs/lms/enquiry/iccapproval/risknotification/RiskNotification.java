package pfs.lms.enquiry.iccapproval.risknotification;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.iccapproval.ICCApproval;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"serialNumber", "notificationDate", "remarks"}, callSuper = false)
public class RiskNotification extends AggregateRoot<RiskNotification> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private Integer serialNumber;
    private LocalDate notificationDate;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
