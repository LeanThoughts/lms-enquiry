package pfs.lms.enquiry.boardapproval.rejectedbyboard;

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
@EqualsAndHashCode(of = {"boardApproval", "meetingNumber", "meetingDate"}, callSuper = false)
public class RejectedByBoard extends AggregateRoot<RejectedByBoard> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BoardApproval boardApproval;

    private Integer meetingNumber;
    private LocalDate meetingDate;
    private String details;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
