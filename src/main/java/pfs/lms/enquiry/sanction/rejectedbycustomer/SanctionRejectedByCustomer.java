package pfs.lms.enquiry.sanction.rejectedbycustomer;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.sanction.Sanction;

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
@EqualsAndHashCode(of = {"sanction", "approvalByBoardMeetingNumber", "meetingDate"}, callSuper = false)
public class SanctionRejectedByCustomer extends AggregateRoot<SanctionRejectedByCustomer> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Sanction sanction;

    private String approvalByBoardMeetingNumber;

    private LocalDate meetingDate;

    private String rejectionCategory;
    private String details;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
