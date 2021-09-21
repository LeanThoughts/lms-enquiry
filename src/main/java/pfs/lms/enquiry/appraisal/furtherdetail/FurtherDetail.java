package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@EqualsAndHashCode(of = {"loanAppraisal", "furtherDetails", "date"}, callSuper = false)
@EntityListeners(FurtherDetailEntityListener.class)
public class FurtherDetail extends AggregateRoot<FurtherDetail> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private String furtherDetails;
    private LocalDate date;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
