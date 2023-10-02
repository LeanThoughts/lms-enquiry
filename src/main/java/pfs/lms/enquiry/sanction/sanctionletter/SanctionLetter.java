package pfs.lms.enquiry.sanction.sanctionletter;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.sanction.Sanction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SanctionLetter extends AggregateRoot<SanctionLetter> implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private Sanction sanction;

    private Integer serialNumber;
    private LocalDate sanctionLetterIssueDate;
    private LocalDate borrowerRequestLetterDate;
    private LocalDate sanctionLetterAcceptanceDate;
    private String documentType;
    private String documentTitle;
    private String fileReference;
    private String remarks;

    private String type;
    private LocalDate dateOfAmendment;
    private Double originalSanctionAmount;
    private Double originalInterestRate;
    private Double revisedSanctionAmount;
    private Double revisedInterestRate;
    private LocalDate sanctionLetterValidToDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
