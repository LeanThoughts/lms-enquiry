package pfs.lms.enquiry.documentation.nodalofficer;

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
@EqualsAndHashCode(of = {"documentation", "serialNumber", "bpCode"}, callSuper = false)
public class NodalOfficer extends AggregateRoot<NodalOfficer> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Documentation documentation;

    private Integer serialNumber;

    private String bpCode;
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
