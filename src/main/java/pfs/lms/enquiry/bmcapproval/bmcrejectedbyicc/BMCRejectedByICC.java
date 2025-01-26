package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

import lombok.*;
import pfs.lms.enquiry.bmcapproval.BMCICCApproval;
import pfs.lms.enquiry.domain.AggregateRoot;

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
@EqualsAndHashCode(of = {"bmcICCApproval", "meetingNumber", "meetingDate"}, callSuper = false)
public class BMCRejectedByICC extends AggregateRoot<BMCRejectedByICC> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BMCICCApproval bmcICCApproval;

    private String meetingNumber;
    private String reasonForRejection;
    private LocalDate meetingDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
