package pfs.lms.enquiry.action.rejectbypfs;

import lombok.*;
import pfs.lms.enquiry.action.EnquiryAction;
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
@EqualsAndHashCode(of = {"enquiryAction"}, callSuper = false)
public class RejectByPfs extends AggregateRoot<RejectByPfs> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private EnquiryAction enquiryAction;

    private Integer rejectionCategory;
    private String rejectionReason;
    private LocalDate rejectionDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
