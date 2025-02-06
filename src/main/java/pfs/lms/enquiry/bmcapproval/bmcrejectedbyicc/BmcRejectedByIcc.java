package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

import lombok.*;
import pfs.lms.enquiry.bmcapproval.BmcIccApproval;
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
public class BmcRejectedByIcc extends AggregateRoot<BmcRejectedByIcc> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BmcIccApproval bmcICCApproval;

    private String meetingNumber;
    private String reasonForRejection;
    private LocalDate meetingDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
