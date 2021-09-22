package pfs.lms.enquiry.appraisal.customerrejection;

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
@EqualsAndHashCode(of = {"loanAppraisal"}, callSuper = false)
@EntityListeners(CustomerRejectionEntityListener.class)
public class CustomerRejection extends AggregateRoot<CustomerRejection> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private String reasonForRejection;
    private LocalDate date;
    private String category;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
