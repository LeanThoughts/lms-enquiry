package pfs.lms.enquiry.monitoring.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.CascadeType;
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
public class TermsAndConditionsModification extends AbstractEntity implements Cloneable{

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    // Borrower Request letter
    private Integer serialNumber;
    private String documentType;
    private String documentTitle;
    private String communication;
    private LocalDate borrowerRequestLetterDate;
    private LocalDate dateOfIssueOfAmendedSanctionLetter;
    private String remarks;
    private String reasonsForAmendment;
    private String fileReference;

    // LeadBanker Document Type
    private String leadBankerDocumentType;
    private String leadBankerDocumentTitle;
    private String leadBankerDocumentFileReference;

    //Amended Document Details
    private String amendedDocumentType;
    private LocalDate dateOfIssueOfAmendedDocument;
    private String amendedDocumentRemarks;
    private String amendedDocumentTitle;
    private String amendedDocumentFileReference;

    // Internal Document Type
    private String internalDocumentType;
    private LocalDate dateOfInternalDocument;
    private String internalDocumentTitle;
    private String internalDocumentRemarks;
    private String internalDocumentFileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
