package pfs.lms.enquiry.boardapproval.rejectedbycustomer;

import lombok.*;
import pfs.lms.enquiry.boardapproval.BoardApproval;
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
@EqualsAndHashCode(of = {"boardApproval", "approvalByBoard", "meetingDate"}, callSuper = false)
public class BoardApprovalRejectedByCustomer extends AggregateRoot<BoardApprovalRejectedByCustomer> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BoardApproval boardApproval;

    private String approvalByBoardMeetingNumber;

    private LocalDate meetingDate;

    private String rejectionCategory;
    private String details;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
