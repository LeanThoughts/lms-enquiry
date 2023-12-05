package pfs.lms.enquiry.applicationfee.termsheet;

import lombok.*;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
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
@EqualsAndHashCode(of = {"applicationFee", "serialNumber", "status"}, callSuper = false)
public class TermSheet extends AggregateRoot<TermSheet> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ApplicationFee applicationFee;

    private Integer serialNumber;
    private String status;
    private LocalDate issuanceDate;
    private LocalDate acceptanceDate;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
