package pfs.lms.enquiry.iccapproval.rejectedbyicc;

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
@EqualsAndHashCode(of = {"iccApproval", "meetingNumber", "meetingDate"}, callSuper = false)
public class RejectedByICC extends AggregateRoot<RejectedByICC> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private String meetingNumber;
    private String reasonForRejection;
    private LocalDate meetingDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
