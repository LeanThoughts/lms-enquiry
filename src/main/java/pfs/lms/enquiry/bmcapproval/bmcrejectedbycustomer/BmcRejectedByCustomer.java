package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

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
@EqualsAndHashCode(of = {"bmcICCApproval", "meetingNumber", "dateOfRejection"}, callSuper = false)
public class BmcRejectedByCustomer extends AggregateRoot<BmcRejectedByCustomer> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BmcIccApproval bmcICCApproval;

    private String meetingNumber;
    private String rejectionCategory;
    private LocalDate dateOfRejection;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
