package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Security Perfection: NeSL of a collateral: a row of SAP table ZCOL_SPFCT_NESL (1:N with the collateral).
 * All SAP columns are kept except the client (MANDT) and the SAP audit include. Generated from the SAP field list.
 */
@Entity
@Table(name = "collateral_perfection_nesl")
public class CollateralPerfectionNesl extends CollateralChildRecord<CollateralPerfectionNesl> {

    /** Event */
    @SapField("EVENT_TYPE")
    @Column(length = 1)
    private String eventType;

    /** Event Desc */
    @SapField("EVENT_DESC")
    @Column(length = 60)
    private String eventDescription;

    /** Event Date */
    @SapField("EVENT_DATE")
    private LocalDate eventDate;

    /** Remarks */
    @SapField("REMARKS")
    @Column(length = 60)
    private String remarks;

    /** NeSL Form Submission Date */
    @SapField("NESL_FORM_SUBMISSION_DATE")
    private LocalDate neslFormSubmissionDate;

    /** NeSL Satisfaction Date */
    @SapField("NESL_SATISFACTION_DATE")
    private LocalDate neslSatisfactionDate;

    /** NeSL Reference */
    @SapField("NESL_REFERENCE")
    @Column(length = 60)
    private String neslReference;

    public CollateralPerfectionNesl() {
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDate getNeslFormSubmissionDate() { return neslFormSubmissionDate; }
    public void setNeslFormSubmissionDate(LocalDate neslFormSubmissionDate) { this.neslFormSubmissionDate = neslFormSubmissionDate; }
    public LocalDate getNeslSatisfactionDate() { return neslSatisfactionDate; }
    public void setNeslSatisfactionDate(LocalDate neslSatisfactionDate) { this.neslSatisfactionDate = neslSatisfactionDate; }
    public String getNeslReference() { return neslReference; }
    public void setNeslReference(String neslReference) { this.neslReference = neslReference; }
}
