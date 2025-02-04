package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import lombok.*;
import pfs.lms.enquiry.bmcapproval.BmcICCApproval;
import pfs.lms.enquiry.domain.AggregateRoot;

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
@EqualsAndHashCode(of = {"bmcICCApproval", "reasonForDelay", "date"}, callSuper = false)
public class BmcICCReasonForDelay extends AggregateRoot<BmcICCReasonForDelay> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private BmcICCApproval bmcICCApproval;

    private Integer serialNumber;
    private String reasonForDelay;
    private LocalDate date;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
