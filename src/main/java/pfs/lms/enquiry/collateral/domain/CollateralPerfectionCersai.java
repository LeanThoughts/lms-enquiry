package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Security Perfection: CERSAI of a collateral: a row of SAP table ZCOL_SPFCT_CERSAI (1:N with the collateral).
 * All SAP columns are kept except the client (MANDT) and the SAP audit include. Generated from the SAP field list.
 */
@Entity
@Table(name = "collateral_perfection_cersai")
public class CollateralPerfectionCersai extends CollateralChildRecord<CollateralPerfectionCersai> {

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

    /** CERSAI Filing Date */
    @SapField("CERSAI_FILING_DATE")
    private LocalDate cersaiFilingDate;

    /** CERSAI Acknowledgment Number */
    @SapField("CERSAI_ACKNOWLEDGMENT_NUMBER")
    @Column(length = 60)
    private String cersaiAcknowledgmentNumber;

    /** CERSAI Satisfaction Date */
    @SapField("CERSAI_SATISFACTION_DATE")
    private LocalDate cersaiSatisfactionDate;

    /** Timelines */
    @SapField("ZCOL_TIMELINES_TEXT")
    @Column(length = 200)
    private String timelinesText;

    /** Action Days Prefix */
    @SapField("ZCOL_ACT_DAYS_PRFX")
    @Column(length = 1)
    private String actionDaysPrefix;

    /** Action Days Suffix */
    @SapField("ZCOL_ACT_DAYS_SUFFIX")
    @Column(length = 1)
    private String actionDaysSuffix;

    /** Action Period */
    @SapField("ZCOL_ACT_DAYS")
    private Integer actionPeriod;

    /** Collateral Event */
    @SapField("ZCOL_EVENT")
    @Column(length = 1)
    private String timelineEvent;

    /** Collateral Event Date */
    @SapField("ZCOL_EVENT_DATE")
    private LocalDate timelineEventDate;

    /** Timeline Date */
    @SapField("ZCOL_TIMELINE_DATE")
    private LocalDate timelineDate;

    public CollateralPerfectionCersai() {
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDate getCersaiFilingDate() { return cersaiFilingDate; }
    public void setCersaiFilingDate(LocalDate cersaiFilingDate) { this.cersaiFilingDate = cersaiFilingDate; }
    public String getCersaiAcknowledgmentNumber() { return cersaiAcknowledgmentNumber; }
    public void setCersaiAcknowledgmentNumber(String cersaiAcknowledgmentNumber) { this.cersaiAcknowledgmentNumber = cersaiAcknowledgmentNumber; }
    public LocalDate getCersaiSatisfactionDate() { return cersaiSatisfactionDate; }
    public void setCersaiSatisfactionDate(LocalDate cersaiSatisfactionDate) { this.cersaiSatisfactionDate = cersaiSatisfactionDate; }
    public String getTimelinesText() { return timelinesText; }
    public void setTimelinesText(String timelinesText) { this.timelinesText = timelinesText; }
    public String getActionDaysPrefix() { return actionDaysPrefix; }
    public void setActionDaysPrefix(String actionDaysPrefix) { this.actionDaysPrefix = actionDaysPrefix; }
    public String getActionDaysSuffix() { return actionDaysSuffix; }
    public void setActionDaysSuffix(String actionDaysSuffix) { this.actionDaysSuffix = actionDaysSuffix; }
    public Integer getActionPeriod() { return actionPeriod; }
    public void setActionPeriod(Integer actionPeriod) { this.actionPeriod = actionPeriod; }
    public String getTimelineEvent() { return timelineEvent; }
    public void setTimelineEvent(String timelineEvent) { this.timelineEvent = timelineEvent; }
    public LocalDate getTimelineEventDate() { return timelineEventDate; }
    public void setTimelineEventDate(LocalDate timelineEventDate) { this.timelineEventDate = timelineEventDate; }
    public LocalDate getTimelineDate() { return timelineDate; }
    public void setTimelineDate(LocalDate timelineDate) { this.timelineDate = timelineDate; }
}
