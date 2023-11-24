package pfs.lms.enquiry.iccapproval.iccfurtherdetail;

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
@EqualsAndHashCode(of = {"iccApproval", "iccMeetingNumber", "iccMeetingDate"}, callSuper = false)
public class ICCFurtherDetail extends AggregateRoot<ICCFurtherDetail> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private Integer serialNumber;
    private String iccMeetingNumber;
    private LocalDate iccMeetingDate;
    private String detailsRequired;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
