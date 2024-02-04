package pfs.lms.enquiry.documentation.legalcouncelreport;

import lombok.*;
import pfs.lms.enquiry.documentation.legalcounsel.LegalCounsel;
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
public class LegalCounselReport extends AggregateRoot<LegalCounselReport> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private LegalCounsel legalCounsel;

    private Integer serialNumber;

    private LocalDate submissionDate;

    private String fiscalYear;
    private String period;
    private String remarks;

    private String documentName;
    private String documentType;
    private String fileReference;

    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
