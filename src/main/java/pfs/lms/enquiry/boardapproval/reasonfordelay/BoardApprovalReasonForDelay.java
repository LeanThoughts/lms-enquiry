package pfs.lms.enquiry.boardapproval.reasonfordelay;

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
@EqualsAndHashCode(of = {"boardApproval", "serialNumber", "date"}, callSuper = false)
public class BoardApprovalReasonForDelay extends AggregateRoot<BoardApprovalReasonForDelay> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private BoardApproval boardApproval;

    private Integer serialNumber;
    private LocalDate date;
    private String reason;
    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
