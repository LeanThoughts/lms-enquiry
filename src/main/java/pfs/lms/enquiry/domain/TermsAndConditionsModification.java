package pfs.lms.enquiry.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Blob;
import java.time.LocalDate;

@Entity
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TermsAndConditionsModification extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private String documentType;

    private String communication;

    private LocalDate borrowerRequestLetterDate;

    private LocalDate dateofIssueofAmendedSanctionLetter;

    private String remarks;

    private Blob documentContent;
}