package pfs.lms.enquiry.documentation.reasonfordelay;

import lombok.*;
import pfs.lms.enquiry.documentation.Documentation;
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
@EqualsAndHashCode(of = {"documentation", "serialNumber", "date"}, callSuper = false)
public class DocumentationReasonForDelay extends AggregateRoot<DocumentationReasonForDelay> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Documentation documentation;

    private Integer serialNumber;
    private LocalDate date;
    private String reason;
    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
