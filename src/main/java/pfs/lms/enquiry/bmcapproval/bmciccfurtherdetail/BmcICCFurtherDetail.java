package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

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
@EqualsAndHashCode(of = {"bmcICCApproval", "iccMeetingNumber", "iccMeetingDate"}, callSuper = false)
public class BmcICCFurtherDetail extends AggregateRoot<BmcICCFurtherDetail> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private BmcICCApproval bmcICCApproval;

    private Integer serialNumber;
    private String iccMeetingNumber;
    private LocalDate iccMeetingDate;
    private String detailsRequired;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
