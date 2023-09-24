package pfs.lms.enquiry.sanction.reasonfordelay;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.sanction.Sanction;

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
@EqualsAndHashCode(of = {"sanction", "serialNumber", "date"}, callSuper = false)
public class SanctionReasonForDelay extends AggregateRoot<SanctionReasonForDelay> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Sanction sanction;

    private Integer serialNumber;
    private LocalDate date;
    private String reason;
    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
