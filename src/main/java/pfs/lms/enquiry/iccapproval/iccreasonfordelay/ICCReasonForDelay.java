package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.iccapproval.ICCApproval;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"iccApproval", "reasonForDelay", "date"}, callSuper = false)
public class ICCReasonForDelay extends AggregateRoot<ICCReasonForDelay> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private String reasonForDelay;
    private LocalDate date;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
