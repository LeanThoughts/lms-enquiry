package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

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
@EqualsAndHashCode(of = {"bmcICCApproval", "meetingNumber", "dateOfRejection"}, callSuper = false)
public class BMCRejectedByCustomer extends AggregateRoot<BMCRejectedByCustomer> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BMCICCApproval bmcICCApproval;

    private String meetingNumber;
    private String rejectionCategory;
    private LocalDate dateOfRejection;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
