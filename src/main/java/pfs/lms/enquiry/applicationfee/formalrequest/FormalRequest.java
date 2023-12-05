package pfs.lms.enquiry.applicationfee.formalrequest;

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
@EqualsAndHashCode(of = {"applicationFee", "serialNumber", "documentName"}, callSuper = false)
public class FormalRequest extends AggregateRoot<FormalRequest> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ApplicationFee applicationFee;

    private Integer serialNumber;
    private String documentName;
    private LocalDate uploadDate;
    private LocalDate documentLetterDate;
    private LocalDate documentReceivedDate;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
