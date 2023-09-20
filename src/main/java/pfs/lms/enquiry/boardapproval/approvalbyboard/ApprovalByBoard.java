package pfs.lms.enquiry.boardapproval.approvalbyboard;

import lombok.*;
import pfs.lms.enquiry.boardapproval.BoardApproval;
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
@EqualsAndHashCode(of = {"boardApproval", "meetingNumber", "meetingDate"}, callSuper = false)
public class ApprovalByBoard extends AggregateRoot<ApprovalByBoard> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private BoardApproval boardApproval;

    private Integer meetingNumber;
    private LocalDate meetingDate;
    private String details;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
