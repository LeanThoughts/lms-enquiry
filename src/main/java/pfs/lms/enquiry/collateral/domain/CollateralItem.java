package pfs.lms.enquiry.collateral.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * One collateral of a loan: a row of the SAP collateral checklist table ZPFS_T_LN_CHKLST.
 * <p>
 * All SAP columns are kept (NFR 17) except the client (MANDT) and the SAP audit include, which are covered by the
 * audit fields of {@link AggregateRoot}. Each field carries its SAP column name in {@link SapField}, which the
 * migration API uses. The four long texts are SAP text-editor objects without a table column; they are stored here
 * as LONGTEXT columns (NFR 16).
 * <p>
 * Generated from the field list of ZPFS_T_LN_CHKLST; lengths follow the SAP data elements.
 */
@Entity
@Table(name = "collateral_item", uniqueConstraints = @UniqueConstraint(name = "uk_collateral_item_checklist_id_no", columnNames = "checklist_id_no"))
public class CollateralItem extends AggregateRoot<CollateralItem> {

    /** The checklist (collateral list) of the loan this collateral belongs to. */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checklist_id", nullable = false)
    private CollateralChecklist checklist;

    /** Checklist ID No. (generated from the checklist ID number range; SAP CHAR 10) */
    @SapField("ZID_NO")
    @Column(name = "checklist_id_no")
    private Long checklistIdNo;

    /** Applicable */
    @SapField("ZAPPLICABLE")
    private Boolean applicable;

    /** Product Type */
    @SapField("ZPRODUCT_TYPE")
    @Column(length = 3)
    private String productType;

    /** Project Type */
    @SapField("ZPROJECT_TYPE")
    @Column(length = 30)
    private String projectType;

    /** Condition Group */
    @SapField("ZCONDITION_GRP")
    @Column(length = 30)
    private String conditionGroup;

    /** Condition Description */
    @SapField("ZCONDITION_DESC")
    @Column(length = 60)
    private String conditionDescription;

    /** Collateral Object */
    @SapField("ZCOLLATERAL_TYPE")
    @Column(length = 6)
    private String collateralObjectType;

    /** Coll. Obj. Descr. */
    @SapField("ZCOL_SEC_TYPE")
    @Column(length = 200)
    private String collateralObjectDescription;

    /** Valid from */
    @SapField("VALID_FROM_DATE")
    private LocalDate validFromDate;

    /** Valid to */
    @SapField("VALID_TO_DATE")
    private LocalDate validToDate;

    /** Condition Category */
    @SapField("ZCOND_CAT")
    @Column(length = 3)
    private String conditionCategory;

    /** Master Condition */
    @SapField("ZMASTER_COND")
    private Boolean masterCondition;

    /** Standard/Loan-Specific */
    @SapField("ZSTND_NON")
    @Column(length = 20)
    private String standardOrLoanSpecific;

    /** Applicable for Every Disbursement */
    @SapField("ZAPPL_ERY_DISBUR")
    private Boolean applicableForEveryDisbursement;

    /** Percentage of the Disbursement */
    @SapField("ZAPPL_PER_DISBUR")
    private Integer percentageOfDisbursement;

    /** Compliance Date */
    @SapField("ZCOMPLIANCE_DATE")
    private LocalDate complianceDate;

    /** Compliance Status */
    @SapField("ZSTATUS")
    @Column(length = 20)
    private String complianceStatus;

    /** Compliance Status Text */
    @SapField("ZCOMPLIANCE_TEXT")
    @Column(length = 60)
    private String complianceStatusText;

    /** Key Approvals / Clearance */
    @SapField("ZKEY_APPROVALS")
    private Boolean keyApprovalsClearance;

    /** Competent Authority */
    @SapField("KEY_APPR_COMP_AUTHORITY")
    @Column(length = 1)
    private String competentAuthority;

    /** Board Committee Name */
    @SapField("BOARD_COMMITTEE_NAME")
    @Column(length = 90)
    private String boardCommitteeName;

    /** Comp. Authority Remarks */
    @SapField("COMP_AUTHORITY_REMARKS")
    @Column(length = 100)
    private String competentAuthorityRemarks;

    /** Waiver Date */
    @SapField("WAIVER_DATE")
    private LocalDate waiverDate;

    /** Penal Charges Applicable */
    @SapField("PENAL_CHG_APPL")
    private Boolean penalChargesApplicable;

    /** Penal Charges % */
    @SapField("PENAL_CHG_PCT")
    @Column(precision = 5, scale = 2)
    private BigDecimal penalChargesPercentage;

    /** Penal Charges Waiver % */
    @SapField("PENAL_CHG_WAIVER_PCT")
    @Column(precision = 5, scale = 2)
    private BigDecimal penalChargesWaiverPercentage;

    /** Post Execution Opinion from LLC */
    @SapField("POST_EXEC_OPINION")
    @Column(length = 150)
    private String postExecutionOpinion;

    /** Document Type */
    @SapField("POST_EXEC_DOC_TYPE")
    @Column(length = 10)
    private String postExecutionDocumentType;

    /** Document Name */
    @SapField("POST_EXEC_DOC_NAME")
    @Column(length = 100)
    private String postExecutionDocumentName;

    /** Document Title */
    @SapField("POST_EXEC_DOC_TITLE")
    @Column(length = 100)
    private String postExecutionDocumentTitle;

    /** Uploaded post execution document (portal file reference) */
    @SapField("")
    @Column(length = 36)
    private String postExecutionDocumentReference;

    /** Security Creation Date */
    @SapField("SEC_CREATE_DATE")
    private LocalDate securityCreationDate;

    /** Security Creation Place */
    @SapField("SEC_CREATE_PLACE")
    @Column(length = 70)
    private String securityCreationPlace;

    /** Security Creation Remarks */
    @SapField("SEC_CRATE_REMARKS")
    @Column(length = 70)
    private String securityCreationRemarks;

    /** Security Provider */
    @SapField("SEC_PROVIDER")
    @Column(length = 70)
    private String securityProvider;

    /** Brief Details of Security */
    @SapField("SEC_BRIEF_DETIAL")
    @Column(length = 150)
    private String securityBriefDetails;

    /** Validity Date */
    @SapField("VALIDITY_DATE")
    private LocalDate validityDate;

    /** Expected Value */
    @SapField("ZCOL_EXP_VALUE")
    @Column(precision = 25, scale = 2)
    private BigDecimal expectedValue;

    /** Expected Value % Holding */
    @SapField("ZCOL_EXP_VALUE_PCT_HOLDING")
    @Column(precision = 25, scale = 3)
    private BigDecimal expectedValuePercentageHolding;

    /** Location */
    @SapField("ZCOL_LOCATION")
    @Column(length = 90)
    private String location;

    /** RoC Filing Date */
    @SapField("ROC_FILING_DATE")
    private LocalDate rocFilingDate;

    /** RoC Modification Date */
    @SapField("ROC_MODIFICATION_DATE")
    private LocalDate rocModificationDate;

    /** RoC Certificate Charge ID Number */
    @SapField("ROC_CERTIFICATE_CHARGE_ID_NUMB")
    @Column(length = 60)
    private String rocCertificateChargeIdNumber;

    /** RoC Security Satisfaction Date */
    @SapField("ROC_SECURITY_SATISFACTION_DATE")
    private LocalDate rocSecuritySatisfactionDate;

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

    /** Recurring */
    @SapField("ZRECURRING")
    private Boolean recurring;

    /** Remarks for Recurring */
    @SapField("REMARKS_FOR_RECURRING")
    @Column(length = 150)
    private String remarksForRecurring;

    /** Waived Off */
    @SapField("WAIVED_OFF")
    private Boolean waivedOff;

    /** Key Approvals */
    @SapField("KEY_APPROVALS")
    private Boolean keyApprovals;

    /** Remarks for Key Approvals */
    @SapField("REMARKS_FOR_KEY_APPROVALS")
    @Column(length = 150)
    private String remarksForKeyApprovals;

    /** Waiver Possible */
    @SapField("ZWAI_POSSIBLE")
    private Boolean waiverPossible;

    /** Remarks Waiver Reason */
    @SapField("ZWAI_REASON")
    @Column(length = 60)
    private String waiverReason;

    /** Frequency of Recurrence */
    @SapField("ZFRQ_OF_RECUR")
    private Integer frequencyOfRecurrence;

    /** Frequency of Recurrence Unit */
    @SapField("ZFRQ_OF_RECUR_UNIT")
    @Column(length = 6)
    private String frequencyOfRecurrenceUnit;

    /** Security Trustee */
    @SapField("SECURITY_TRUSTEE")
    @Column(length = 10)
    private String securityTrustee;

    /** Security Agent */
    @SapField("SECURITY_AGENT")
    @Column(length = 10)
    private String securityAgent;

    /** Custodian */
    @SapField("CUSTODIAN")
    @Column(length = 10)
    private String custodian;

    /** Responsible Party */
    @SapField("ZRESPON_PARTY")
    @Column(length = 15)
    private String responsibleParty;

    /** Responsible Party Description */
    @SapField("ZRESPON_PARTY_DESC")
    @Column(length = 60)
    private String responsiblePartyDescription;

    /** Sec. Trustee Remarks */
    @SapField("SECURITY_TRUSTEE_DESC")
    @Column(length = 90)
    private String securityTrusteeRemarks;

    /** Sec. Agent Remarks */
    @SapField("SECURITY_AGENT_DESC")
    @Column(length = 90)
    private String securityAgentRemarks;

    /** Custodian Remarks */
    @SapField("CUSTODIAN_DESC")
    @Column(length = 90)
    private String custodianRemarks;

    /** Document Type */
    @SapField("ZDOC_TYPE")
    @Column(length = 10)
    private String documentType;

    /** Document Title */
    @SapField("DOC_TITLE")
    @Column(length = 100)
    private String documentTitle;

    /** Document Name */
    @SapField("DOC_NAME")
    @Column(length = 100)
    private String documentName;

    /** Number of Units */
    @SapField("ZSEC_NO_OF_UNITS")
    @Column(precision = 15, scale = 3)
    private BigDecimal securitiesNumberOfUnits;

    /** Nominal Value */
    @SapField("ZSEC_VALUE")
    @Column(precision = 17, scale = 2)
    private BigDecimal securitiesNominalValue;

    /** Nominal Value Currency */
    @SapField("ZSEC_VALUE_CURR")
    @Column(length = 5)
    private String securitiesNominalValueCurrency;

    /** Percentage Holding */
    @SapField("ZSEC_PCT_HOLDING")
    @Column(precision = 6, scale = 3)
    private BigDecimal securitiesPercentageHolding;

    /** Securities Type */
    @SapField("SECURITIES_TYPE")
    @Column(length = 20)
    private String securitiesType;

    /** Timelines */
    @SapField("ZCOL_TIMELINES_TEXT")
    @Column(length = 200)
    private String timelinesText;

    /** Action Period */
    @SapField("ZCOL_ACT_DAYS_PRFX")
    @Column(length = 1)
    private String actionDaysPrefix;

    /** Action Days Unit */
    @SapField("ZCOL_ACT_DAYS_SUFFIX")
    @Column(length = 1)
    private String actionDaysSuffix;

    /** Action Period (number) */
    @SapField("ZCOL_ACT_DAYS")
    private Integer actionPeriod;

    /** Event */
    @SapField("ZCOL_EVENT")
    @Column(length = 1)
    private String timelineEvent;

    /** Event Date */
    @SapField("ZCOL_EVENT_DATE")
    private LocalDate timelineEventDate;

    /** Timeline Date */
    @SapField("ZCOL_TIMELINE_DATE")
    private LocalDate timelineDate;

    /** Validity Period of Collateral */
    @SapField("ZCOL_VAL_PERIOD")
    @Column(length = 90)
    private String collateralValidityPeriod;

    /** Security Perfection Date */
    @SapField("ZCOL_SEC_PER_DATE")
    private LocalDate securityPerfectionDate;

    /** Remarks */
    @SapField("ZCOL_REMARKS")
    @Column(length = 90)
    private String remarks;

    /** Additional Text */
    @SapField("ZCOL_ADDTL_TEXT")
    @Column(length = 90)
    private String additionalText;

    /** Total Land Area */
    @SapField("ZRE_AREA")
    @Column(precision = 13, scale = 3)
    private BigDecimal landArea;

    /** Unit of Measure */
    @SapField("ZRE_AREA_UOM")
    @Column(length = 3)
    private String landAreaUnit;

    /** Collateral Value */
    @SapField("ZCOL_VALUE")
    @Column(precision = 17, scale = 2)
    private BigDecimal collateralValue;

    /** Currency */
    @SapField("ZCOL_VALUE_CURR")
    @Column(length = 5)
    private String collateralValueCurrency;

    /** Start Date */
    @SapField("START_DATE")
    private LocalDate startDate;

    /** End Date */
    @SapField("END_DATE")
    private LocalDate endDate;

    /** Collateral Agreement Type */
    @SapField("ZCMS_COL_AGMT_TYPE")
    @Column(length = 6)
    private String collateralAgreementType;

    /** Collateral Agreement Type Description */
    @SapField("ZCMS_COL_AGMT_TYPE_DESC")
    @Column(length = 30)
    private String collateralAgreementTypeDescription;

    /** Collateral Agreement ID */
    @SapField("ZCMS_COL_AGMT_ID")
    @Column(length = 40)
    private String collateralAgreementId;

    /** Source of Entry */
    @SapField("SOURCEOFENTRY")
    @Column(length = 32)
    private String sourceOfEntry;

    /** Security Compliance ID */
    @SapField("PORTAL_ID")
    @Column(length = 40)
    private String portalId;

    /** Monitoring ID */
    @SapField("MONITOR_ID")
    @Column(length = 40)
    private String monitoringId;

    /** Security Type */
    @SapField(value = "", longText = true)
    @Column(columnDefinition = "LONGTEXT")
    private String securityTypeText;

    /** Stipulated Security as per Loan Agreement */
    @SapField(value = "", longText = true)
    @Column(columnDefinition = "LONGTEXT")
    private String stipulatedSecurity;

    /** Compliance Status Remarks (Legal Dept) */
    @SapField(value = "", longText = true)
    @Column(columnDefinition = "LONGTEXT")
    private String complianceRemarksLegal;

    /** Action Taken by Borrower / PFS (Monitoring Dept) */
    @SapField(value = "", longText = true)
    @Column(columnDefinition = "LONGTEXT")
    private String actionTaken;

    public CollateralItem() {
    }

    public UUID getChecklistId() {
        return checklist != null ? checklist.getId() : null;
    }

    public CollateralChecklist getChecklist() { return checklist; }
    public void setChecklist(CollateralChecklist checklist) { this.checklist = checklist; }

    public Long getChecklistIdNo() { return checklistIdNo; }
    public void setChecklistIdNo(Long checklistIdNo) { this.checklistIdNo = checklistIdNo; }
    public Boolean getApplicable() { return applicable; }
    public void setApplicable(Boolean applicable) { this.applicable = applicable; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }
    public String getConditionGroup() { return conditionGroup; }
    public void setConditionGroup(String conditionGroup) { this.conditionGroup = conditionGroup; }
    public String getConditionDescription() { return conditionDescription; }
    public void setConditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; }
    public String getCollateralObjectType() { return collateralObjectType; }
    public void setCollateralObjectType(String collateralObjectType) { this.collateralObjectType = collateralObjectType; }
    public String getCollateralObjectDescription() { return collateralObjectDescription; }
    public void setCollateralObjectDescription(String collateralObjectDescription) { this.collateralObjectDescription = collateralObjectDescription; }
    public LocalDate getValidFromDate() { return validFromDate; }
    public void setValidFromDate(LocalDate validFromDate) { this.validFromDate = validFromDate; }
    public LocalDate getValidToDate() { return validToDate; }
    public void setValidToDate(LocalDate validToDate) { this.validToDate = validToDate; }
    public String getConditionCategory() { return conditionCategory; }
    public void setConditionCategory(String conditionCategory) { this.conditionCategory = conditionCategory; }
    public Boolean getMasterCondition() { return masterCondition; }
    public void setMasterCondition(Boolean masterCondition) { this.masterCondition = masterCondition; }
    public String getStandardOrLoanSpecific() { return standardOrLoanSpecific; }
    public void setStandardOrLoanSpecific(String standardOrLoanSpecific) { this.standardOrLoanSpecific = standardOrLoanSpecific; }
    public Boolean getApplicableForEveryDisbursement() { return applicableForEveryDisbursement; }
    public void setApplicableForEveryDisbursement(Boolean applicableForEveryDisbursement) { this.applicableForEveryDisbursement = applicableForEveryDisbursement; }
    public Integer getPercentageOfDisbursement() { return percentageOfDisbursement; }
    public void setPercentageOfDisbursement(Integer percentageOfDisbursement) { this.percentageOfDisbursement = percentageOfDisbursement; }
    public LocalDate getComplianceDate() { return complianceDate; }
    public void setComplianceDate(LocalDate complianceDate) { this.complianceDate = complianceDate; }
    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }
    public String getComplianceStatusText() { return complianceStatusText; }
    public void setComplianceStatusText(String complianceStatusText) { this.complianceStatusText = complianceStatusText; }
    public Boolean getKeyApprovalsClearance() { return keyApprovalsClearance; }
    public void setKeyApprovalsClearance(Boolean keyApprovalsClearance) { this.keyApprovalsClearance = keyApprovalsClearance; }
    public String getCompetentAuthority() { return competentAuthority; }
    public void setCompetentAuthority(String competentAuthority) { this.competentAuthority = competentAuthority; }
    public String getBoardCommitteeName() { return boardCommitteeName; }
    public void setBoardCommitteeName(String boardCommitteeName) { this.boardCommitteeName = boardCommitteeName; }
    public String getCompetentAuthorityRemarks() { return competentAuthorityRemarks; }
    public void setCompetentAuthorityRemarks(String competentAuthorityRemarks) { this.competentAuthorityRemarks = competentAuthorityRemarks; }
    public LocalDate getWaiverDate() { return waiverDate; }
    public void setWaiverDate(LocalDate waiverDate) { this.waiverDate = waiverDate; }
    public Boolean getPenalChargesApplicable() { return penalChargesApplicable; }
    public void setPenalChargesApplicable(Boolean penalChargesApplicable) { this.penalChargesApplicable = penalChargesApplicable; }
    public BigDecimal getPenalChargesPercentage() { return penalChargesPercentage; }
    public void setPenalChargesPercentage(BigDecimal penalChargesPercentage) { this.penalChargesPercentage = penalChargesPercentage; }
    public BigDecimal getPenalChargesWaiverPercentage() { return penalChargesWaiverPercentage; }
    public void setPenalChargesWaiverPercentage(BigDecimal penalChargesWaiverPercentage) { this.penalChargesWaiverPercentage = penalChargesWaiverPercentage; }
    public String getPostExecutionOpinion() { return postExecutionOpinion; }
    public void setPostExecutionOpinion(String postExecutionOpinion) { this.postExecutionOpinion = postExecutionOpinion; }
    public String getPostExecutionDocumentType() { return postExecutionDocumentType; }
    public void setPostExecutionDocumentType(String postExecutionDocumentType) { this.postExecutionDocumentType = postExecutionDocumentType; }
    public String getPostExecutionDocumentName() { return postExecutionDocumentName; }
    public void setPostExecutionDocumentName(String postExecutionDocumentName) { this.postExecutionDocumentName = postExecutionDocumentName; }
    public String getPostExecutionDocumentTitle() { return postExecutionDocumentTitle; }
    public void setPostExecutionDocumentTitle(String postExecutionDocumentTitle) { this.postExecutionDocumentTitle = postExecutionDocumentTitle; }
    public String getPostExecutionDocumentReference() { return postExecutionDocumentReference; }
    public void setPostExecutionDocumentReference(String postExecutionDocumentReference) { this.postExecutionDocumentReference = postExecutionDocumentReference; }
    public LocalDate getSecurityCreationDate() { return securityCreationDate; }
    public void setSecurityCreationDate(LocalDate securityCreationDate) { this.securityCreationDate = securityCreationDate; }
    public String getSecurityCreationPlace() { return securityCreationPlace; }
    public void setSecurityCreationPlace(String securityCreationPlace) { this.securityCreationPlace = securityCreationPlace; }
    public String getSecurityCreationRemarks() { return securityCreationRemarks; }
    public void setSecurityCreationRemarks(String securityCreationRemarks) { this.securityCreationRemarks = securityCreationRemarks; }
    public String getSecurityProvider() { return securityProvider; }
    public void setSecurityProvider(String securityProvider) { this.securityProvider = securityProvider; }
    public String getSecurityBriefDetails() { return securityBriefDetails; }
    public void setSecurityBriefDetails(String securityBriefDetails) { this.securityBriefDetails = securityBriefDetails; }
    public LocalDate getValidityDate() { return validityDate; }
    public void setValidityDate(LocalDate validityDate) { this.validityDate = validityDate; }
    public BigDecimal getExpectedValue() { return expectedValue; }
    public void setExpectedValue(BigDecimal expectedValue) { this.expectedValue = expectedValue; }
    public BigDecimal getExpectedValuePercentageHolding() { return expectedValuePercentageHolding; }
    public void setExpectedValuePercentageHolding(BigDecimal expectedValuePercentageHolding) { this.expectedValuePercentageHolding = expectedValuePercentageHolding; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getRocFilingDate() { return rocFilingDate; }
    public void setRocFilingDate(LocalDate rocFilingDate) { this.rocFilingDate = rocFilingDate; }
    public LocalDate getRocModificationDate() { return rocModificationDate; }
    public void setRocModificationDate(LocalDate rocModificationDate) { this.rocModificationDate = rocModificationDate; }
    public String getRocCertificateChargeIdNumber() { return rocCertificateChargeIdNumber; }
    public void setRocCertificateChargeIdNumber(String rocCertificateChargeIdNumber) { this.rocCertificateChargeIdNumber = rocCertificateChargeIdNumber; }
    public LocalDate getRocSecuritySatisfactionDate() { return rocSecuritySatisfactionDate; }
    public void setRocSecuritySatisfactionDate(LocalDate rocSecuritySatisfactionDate) { this.rocSecuritySatisfactionDate = rocSecuritySatisfactionDate; }
    public LocalDate getCersaiFilingDate() { return cersaiFilingDate; }
    public void setCersaiFilingDate(LocalDate cersaiFilingDate) { this.cersaiFilingDate = cersaiFilingDate; }
    public String getCersaiAcknowledgmentNumber() { return cersaiAcknowledgmentNumber; }
    public void setCersaiAcknowledgmentNumber(String cersaiAcknowledgmentNumber) { this.cersaiAcknowledgmentNumber = cersaiAcknowledgmentNumber; }
    public LocalDate getCersaiSatisfactionDate() { return cersaiSatisfactionDate; }
    public void setCersaiSatisfactionDate(LocalDate cersaiSatisfactionDate) { this.cersaiSatisfactionDate = cersaiSatisfactionDate; }
    public LocalDate getNeslFormSubmissionDate() { return neslFormSubmissionDate; }
    public void setNeslFormSubmissionDate(LocalDate neslFormSubmissionDate) { this.neslFormSubmissionDate = neslFormSubmissionDate; }
    public LocalDate getNeslSatisfactionDate() { return neslSatisfactionDate; }
    public void setNeslSatisfactionDate(LocalDate neslSatisfactionDate) { this.neslSatisfactionDate = neslSatisfactionDate; }
    public String getNeslReference() { return neslReference; }
    public void setNeslReference(String neslReference) { this.neslReference = neslReference; }
    public Boolean getRecurring() { return recurring; }
    public void setRecurring(Boolean recurring) { this.recurring = recurring; }
    public String getRemarksForRecurring() { return remarksForRecurring; }
    public void setRemarksForRecurring(String remarksForRecurring) { this.remarksForRecurring = remarksForRecurring; }
    public Boolean getWaivedOff() { return waivedOff; }
    public void setWaivedOff(Boolean waivedOff) { this.waivedOff = waivedOff; }
    public Boolean getKeyApprovals() { return keyApprovals; }
    public void setKeyApprovals(Boolean keyApprovals) { this.keyApprovals = keyApprovals; }
    public String getRemarksForKeyApprovals() { return remarksForKeyApprovals; }
    public void setRemarksForKeyApprovals(String remarksForKeyApprovals) { this.remarksForKeyApprovals = remarksForKeyApprovals; }
    public Boolean getWaiverPossible() { return waiverPossible; }
    public void setWaiverPossible(Boolean waiverPossible) { this.waiverPossible = waiverPossible; }
    public String getWaiverReason() { return waiverReason; }
    public void setWaiverReason(String waiverReason) { this.waiverReason = waiverReason; }
    public Integer getFrequencyOfRecurrence() { return frequencyOfRecurrence; }
    public void setFrequencyOfRecurrence(Integer frequencyOfRecurrence) { this.frequencyOfRecurrence = frequencyOfRecurrence; }
    public String getFrequencyOfRecurrenceUnit() { return frequencyOfRecurrenceUnit; }
    public void setFrequencyOfRecurrenceUnit(String frequencyOfRecurrenceUnit) { this.frequencyOfRecurrenceUnit = frequencyOfRecurrenceUnit; }
    public String getSecurityTrustee() { return securityTrustee; }
    public void setSecurityTrustee(String securityTrustee) { this.securityTrustee = securityTrustee; }
    public String getSecurityAgent() { return securityAgent; }
    public void setSecurityAgent(String securityAgent) { this.securityAgent = securityAgent; }
    public String getCustodian() { return custodian; }
    public void setCustodian(String custodian) { this.custodian = custodian; }
    public String getResponsibleParty() { return responsibleParty; }
    public void setResponsibleParty(String responsibleParty) { this.responsibleParty = responsibleParty; }
    public String getResponsiblePartyDescription() { return responsiblePartyDescription; }
    public void setResponsiblePartyDescription(String responsiblePartyDescription) { this.responsiblePartyDescription = responsiblePartyDescription; }
    public String getSecurityTrusteeRemarks() { return securityTrusteeRemarks; }
    public void setSecurityTrusteeRemarks(String securityTrusteeRemarks) { this.securityTrusteeRemarks = securityTrusteeRemarks; }
    public String getSecurityAgentRemarks() { return securityAgentRemarks; }
    public void setSecurityAgentRemarks(String securityAgentRemarks) { this.securityAgentRemarks = securityAgentRemarks; }
    public String getCustodianRemarks() { return custodianRemarks; }
    public void setCustodianRemarks(String custodianRemarks) { this.custodianRemarks = custodianRemarks; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentTitle() { return documentTitle; }
    public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }
    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }
    public BigDecimal getSecuritiesNumberOfUnits() { return securitiesNumberOfUnits; }
    public void setSecuritiesNumberOfUnits(BigDecimal securitiesNumberOfUnits) { this.securitiesNumberOfUnits = securitiesNumberOfUnits; }
    public BigDecimal getSecuritiesNominalValue() { return securitiesNominalValue; }
    public void setSecuritiesNominalValue(BigDecimal securitiesNominalValue) { this.securitiesNominalValue = securitiesNominalValue; }
    public String getSecuritiesNominalValueCurrency() { return securitiesNominalValueCurrency; }
    public void setSecuritiesNominalValueCurrency(String securitiesNominalValueCurrency) { this.securitiesNominalValueCurrency = securitiesNominalValueCurrency; }
    public BigDecimal getSecuritiesPercentageHolding() { return securitiesPercentageHolding; }
    public void setSecuritiesPercentageHolding(BigDecimal securitiesPercentageHolding) { this.securitiesPercentageHolding = securitiesPercentageHolding; }
    public String getSecuritiesType() { return securitiesType; }
    public void setSecuritiesType(String securitiesType) { this.securitiesType = securitiesType; }
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
    public String getCollateralValidityPeriod() { return collateralValidityPeriod; }
    public void setCollateralValidityPeriod(String collateralValidityPeriod) { this.collateralValidityPeriod = collateralValidityPeriod; }
    public LocalDate getSecurityPerfectionDate() { return securityPerfectionDate; }
    public void setSecurityPerfectionDate(LocalDate securityPerfectionDate) { this.securityPerfectionDate = securityPerfectionDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getAdditionalText() { return additionalText; }
    public void setAdditionalText(String additionalText) { this.additionalText = additionalText; }
    public BigDecimal getLandArea() { return landArea; }
    public void setLandArea(BigDecimal landArea) { this.landArea = landArea; }
    public String getLandAreaUnit() { return landAreaUnit; }
    public void setLandAreaUnit(String landAreaUnit) { this.landAreaUnit = landAreaUnit; }
    public BigDecimal getCollateralValue() { return collateralValue; }
    public void setCollateralValue(BigDecimal collateralValue) { this.collateralValue = collateralValue; }
    public String getCollateralValueCurrency() { return collateralValueCurrency; }
    public void setCollateralValueCurrency(String collateralValueCurrency) { this.collateralValueCurrency = collateralValueCurrency; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getCollateralAgreementType() { return collateralAgreementType; }
    public void setCollateralAgreementType(String collateralAgreementType) { this.collateralAgreementType = collateralAgreementType; }
    public String getCollateralAgreementTypeDescription() { return collateralAgreementTypeDescription; }
    public void setCollateralAgreementTypeDescription(String collateralAgreementTypeDescription) { this.collateralAgreementTypeDescription = collateralAgreementTypeDescription; }
    public String getCollateralAgreementId() { return collateralAgreementId; }
    public void setCollateralAgreementId(String collateralAgreementId) { this.collateralAgreementId = collateralAgreementId; }
    public String getSourceOfEntry() { return sourceOfEntry; }
    public void setSourceOfEntry(String sourceOfEntry) { this.sourceOfEntry = sourceOfEntry; }
    public String getPortalId() { return portalId; }
    public void setPortalId(String portalId) { this.portalId = portalId; }
    public String getMonitoringId() { return monitoringId; }
    public void setMonitoringId(String monitoringId) { this.monitoringId = monitoringId; }
    public String getSecurityTypeText() { return securityTypeText; }
    public void setSecurityTypeText(String securityTypeText) { this.securityTypeText = securityTypeText; }
    public String getStipulatedSecurity() { return stipulatedSecurity; }
    public void setStipulatedSecurity(String stipulatedSecurity) { this.stipulatedSecurity = stipulatedSecurity; }
    public String getComplianceRemarksLegal() { return complianceRemarksLegal; }
    public void setComplianceRemarksLegal(String complianceRemarksLegal) { this.complianceRemarksLegal = complianceRemarksLegal; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
}
