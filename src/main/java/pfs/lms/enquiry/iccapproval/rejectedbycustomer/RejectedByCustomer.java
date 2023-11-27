package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

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
@EqualsAndHashCode(of = {"iccApproval", "meetingNumber", "dateOfRejection"}, callSuper = false)
public class RejectedByCustomer extends AggregateRoot<RejectedByCustomer> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private String meetingNumber;
    private String rejectionCategory;
    private LocalDate dateOfRejection;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
