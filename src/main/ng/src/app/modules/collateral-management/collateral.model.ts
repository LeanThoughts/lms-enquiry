/** One collateral (SAP ZPFS_T_LN_CHKLST row). Generated from the backend field list. */
export interface CollateralItem {
    id?: string;
    checklistId?: string;
    createdOn?: string;
    createdAt?: string;
    createdByUserName?: string;
    changedOn?: string;
    changedAt?: string;
    changedByUserName?: string;
    /** Checklist ID No. (generated from the checklist ID number range; SAP CHAR 10) (SAP ZID_NO) */
    checklistIdNo?: number | null;
    /** Applicable (SAP ZAPPLICABLE) */
    applicable?: boolean | null;
    /** Product Type (SAP ZPRODUCT_TYPE) */
    productType?: string | null;
    /** Project Type (SAP ZPROJECT_TYPE) */
    projectType?: string | null;
    /** Condition Group (SAP ZCONDITION_GRP) */
    conditionGroup?: string | null;
    /** Condition Description (SAP ZCONDITION_DESC) */
    conditionDescription?: string | null;
    /** Collateral Object (SAP ZCOLLATERAL_TYPE) */
    collateralObjectType?: string | null;
    /** Coll. Obj. Descr. (SAP ZCOL_SEC_TYPE) */
    collateralObjectDescription?: string | null;
    /** Valid from (SAP VALID_FROM_DATE) */
    validFromDate?: string | null;
    /** Valid to (SAP VALID_TO_DATE) */
    validToDate?: string | null;
    /** Condition Category (SAP ZCOND_CAT) */
    conditionCategory?: string | null;
    /** Master Condition (SAP ZMASTER_COND) */
    masterCondition?: boolean | null;
    /** Standard/Loan-Specific (SAP ZSTND_NON) */
    standardOrLoanSpecific?: string | null;
    /** Applicable for Every Disbursement (SAP ZAPPL_ERY_DISBUR) */
    applicableForEveryDisbursement?: boolean | null;
    /** Percentage of the Disbursement (SAP ZAPPL_PER_DISBUR) */
    percentageOfDisbursement?: number | null;
    /** Compliance Date (SAP ZCOMPLIANCE_DATE) */
    complianceDate?: string | null;
    /** Compliance Status (SAP ZSTATUS) */
    complianceStatus?: string | null;
    /** Compliance Status Text (SAP ZCOMPLIANCE_TEXT) */
    complianceStatusText?: string | null;
    /** Key Approvals / Clearance (SAP ZKEY_APPROVALS) */
    keyApprovalsClearance?: boolean | null;
    /** Competent Authority (SAP KEY_APPR_COMP_AUTHORITY) */
    competentAuthority?: string | null;
    /** Board Committee Name (SAP BOARD_COMMITTEE_NAME) */
    boardCommitteeName?: string | null;
    /** Comp. Authority Remarks (SAP COMP_AUTHORITY_REMARKS) */
    competentAuthorityRemarks?: string | null;
    /** Waiver Date (SAP WAIVER_DATE) */
    waiverDate?: string | null;
    /** Penal Charges Applicable (SAP PENAL_CHG_APPL) */
    penalChargesApplicable?: boolean | null;
    /** Penal Charges % (SAP PENAL_CHG_PCT) */
    penalChargesPercentage?: number | null;
    /** Penal Charges Waiver % (SAP PENAL_CHG_WAIVER_PCT) */
    penalChargesWaiverPercentage?: number | null;
    /** Post Execution Opinion from LLC (SAP POST_EXEC_OPINION) */
    postExecutionOpinion?: string | null;
    /** Document Type (SAP POST_EXEC_DOC_TYPE) */
    postExecutionDocumentType?: string | null;
    /** Document Name (SAP POST_EXEC_DOC_NAME) */
    postExecutionDocumentName?: string | null;
    /** Document Title (SAP POST_EXEC_DOC_TITLE) */
    postExecutionDocumentTitle?: string | null;
    /** Uploaded post execution document (portal file reference) (portal only) */
    postExecutionDocumentReference?: string | null;
    /** Security Creation Date (SAP SEC_CREATE_DATE) */
    securityCreationDate?: string | null;
    /** Security Creation Place (SAP SEC_CREATE_PLACE) */
    securityCreationPlace?: string | null;
    /** Security Creation Remarks (SAP SEC_CRATE_REMARKS) */
    securityCreationRemarks?: string | null;
    /** Security Provider (SAP SEC_PROVIDER) */
    securityProvider?: string | null;
    /** Brief Details of Security (SAP SEC_BRIEF_DETIAL) */
    securityBriefDetails?: string | null;
    /** Validity Date (SAP VALIDITY_DATE) */
    validityDate?: string | null;
    /** Expected Value (SAP ZCOL_EXP_VALUE) */
    expectedValue?: number | null;
    /** Expected Value % Holding (SAP ZCOL_EXP_VALUE_PCT_HOLDING) */
    expectedValuePercentageHolding?: number | null;
    /** Location (SAP ZCOL_LOCATION) */
    location?: string | null;
    /** RoC Filing Date (SAP ROC_FILING_DATE) */
    rocFilingDate?: string | null;
    /** RoC Modification Date (SAP ROC_MODIFICATION_DATE) */
    rocModificationDate?: string | null;
    /** RoC Certificate Charge ID Number (SAP ROC_CERTIFICATE_CHARGE_ID_NUMB) */
    rocCertificateChargeIdNumber?: string | null;
    /** RoC Security Satisfaction Date (SAP ROC_SECURITY_SATISFACTION_DATE) */
    rocSecuritySatisfactionDate?: string | null;
    /** CERSAI Filing Date (SAP CERSAI_FILING_DATE) */
    cersaiFilingDate?: string | null;
    /** CERSAI Acknowledgment Number (SAP CERSAI_ACKNOWLEDGMENT_NUMBER) */
    cersaiAcknowledgmentNumber?: string | null;
    /** CERSAI Satisfaction Date (SAP CERSAI_SATISFACTION_DATE) */
    cersaiSatisfactionDate?: string | null;
    /** NeSL Form Submission Date (SAP NESL_FORM_SUBMISSION_DATE) */
    neslFormSubmissionDate?: string | null;
    /** NeSL Satisfaction Date (SAP NESL_SATISFACTION_DATE) */
    neslSatisfactionDate?: string | null;
    /** NeSL Reference (SAP NESL_REFERENCE) */
    neslReference?: string | null;
    /** Recurring (SAP ZRECURRING) */
    recurring?: boolean | null;
    /** Remarks for Recurring (SAP REMARKS_FOR_RECURRING) */
    remarksForRecurring?: string | null;
    /** Waived Off (SAP WAIVED_OFF) */
    waivedOff?: boolean | null;
    /** Key Approvals (SAP KEY_APPROVALS) */
    keyApprovals?: boolean | null;
    /** Remarks for Key Approvals (SAP REMARKS_FOR_KEY_APPROVALS) */
    remarksForKeyApprovals?: string | null;
    /** Waiver Possible (SAP ZWAI_POSSIBLE) */
    waiverPossible?: boolean | null;
    /** Remarks Waiver Reason (SAP ZWAI_REASON) */
    waiverReason?: string | null;
    /** Frequency of Recurrence (SAP ZFRQ_OF_RECUR) */
    frequencyOfRecurrence?: number | null;
    /** Frequency of Recurrence Unit (SAP ZFRQ_OF_RECUR_UNIT) */
    frequencyOfRecurrenceUnit?: string | null;
    /** Security Trustee (SAP SECURITY_TRUSTEE) */
    securityTrustee?: string | null;
    /** Security Agent (SAP SECURITY_AGENT) */
    securityAgent?: string | null;
    /** Custodian (SAP CUSTODIAN) */
    custodian?: string | null;
    /** Responsible Party (SAP ZRESPON_PARTY) */
    responsibleParty?: string | null;
    /** Responsible Party Description (SAP ZRESPON_PARTY_DESC) */
    responsiblePartyDescription?: string | null;
    /** Sec. Trustee Remarks (SAP SECURITY_TRUSTEE_DESC) */
    securityTrusteeRemarks?: string | null;
    /** Sec. Agent Remarks (SAP SECURITY_AGENT_DESC) */
    securityAgentRemarks?: string | null;
    /** Custodian Remarks (SAP CUSTODIAN_DESC) */
    custodianRemarks?: string | null;
    /** Document Type (SAP ZDOC_TYPE) */
    documentType?: string | null;
    /** Document Title (SAP DOC_TITLE) */
    documentTitle?: string | null;
    /** Document Name (SAP DOC_NAME) */
    documentName?: string | null;
    /** Number of Units (SAP ZSEC_NO_OF_UNITS) */
    securitiesNumberOfUnits?: number | null;
    /** Nominal Value (SAP ZSEC_VALUE) */
    securitiesNominalValue?: number | null;
    /** Nominal Value Currency (SAP ZSEC_VALUE_CURR) */
    securitiesNominalValueCurrency?: string | null;
    /** Percentage Holding (SAP ZSEC_PCT_HOLDING) */
    securitiesPercentageHolding?: number | null;
    /** Securities Type (SAP SECURITIES_TYPE) */
    securitiesType?: string | null;
    /** Timelines (SAP ZCOL_TIMELINES_TEXT) */
    timelinesText?: string | null;
    /** Action Period (SAP ZCOL_ACT_DAYS_PRFX) */
    actionDaysPrefix?: string | null;
    /** Action Days Unit (SAP ZCOL_ACT_DAYS_SUFFIX) */
    actionDaysSuffix?: string | null;
    /** Action Period (number) (SAP ZCOL_ACT_DAYS) */
    actionPeriod?: number | null;
    /** Event (SAP ZCOL_EVENT) */
    timelineEvent?: string | null;
    /** Event Date (SAP ZCOL_EVENT_DATE) */
    timelineEventDate?: string | null;
    /** Timeline Date (SAP ZCOL_TIMELINE_DATE) */
    timelineDate?: string | null;
    /** Validity Period of Collateral (SAP ZCOL_VAL_PERIOD) */
    collateralValidityPeriod?: string | null;
    /** Security Perfection Date (SAP ZCOL_SEC_PER_DATE) */
    securityPerfectionDate?: string | null;
    /** Remarks (SAP ZCOL_REMARKS) */
    remarks?: string | null;
    /** Additional Text (SAP ZCOL_ADDTL_TEXT) */
    additionalText?: string | null;
    /** Total Land Area (SAP ZRE_AREA) */
    landArea?: number | null;
    /** Unit of Measure (SAP ZRE_AREA_UOM) */
    landAreaUnit?: string | null;
    /** Collateral Value (SAP ZCOL_VALUE) */
    collateralValue?: number | null;
    /** Currency (SAP ZCOL_VALUE_CURR) */
    collateralValueCurrency?: string | null;
    /** Start Date (SAP START_DATE) */
    startDate?: string | null;
    /** End Date (SAP END_DATE) */
    endDate?: string | null;
    /** Collateral Agreement Type (SAP ZCMS_COL_AGMT_TYPE) */
    collateralAgreementType?: string | null;
    /** Collateral Agreement Type Description (SAP ZCMS_COL_AGMT_TYPE_DESC) */
    collateralAgreementTypeDescription?: string | null;
    /** Collateral Agreement ID (SAP ZCMS_COL_AGMT_ID) */
    collateralAgreementId?: string | null;
    /** Source of Entry (SAP SOURCEOFENTRY) */
    sourceOfEntry?: string | null;
    /** Security Compliance ID (SAP PORTAL_ID) */
    portalId?: string | null;
    /** Monitoring ID (SAP MONITOR_ID) */
    monitoringId?: string | null;
    /** Security Type (long text) */
    securityTypeText?: string | null;
    /** Stipulated Security as per Loan Agreement (long text) */
    stipulatedSecurity?: string | null;
    /** Compliance Status Remarks (Legal Dept) (long text) */
    complianceRemarksLegal?: string | null;
    /** Action Taken by Borrower / PFS (Monitoring Dept) (long text) */
    actionTaken?: string | null;
}

/** Maximum lengths of the text fields (SAP data element lengths). */
export const ITEM_MAX_LENGTH: Record<string, number> = {
    productType: 3,
    projectType: 30,
    conditionGroup: 30,
    conditionDescription: 60,
    collateralObjectType: 6,
    collateralObjectDescription: 200,
    conditionCategory: 3,
    standardOrLoanSpecific: 20,
    complianceStatus: 20,
    complianceStatusText: 60,
    competentAuthority: 1,
    boardCommitteeName: 90,
    competentAuthorityRemarks: 100,
    postExecutionOpinion: 150,
    postExecutionDocumentType: 10,
    postExecutionDocumentName: 100,
    postExecutionDocumentTitle: 100,
    postExecutionDocumentReference: 36,
    securityCreationPlace: 70,
    securityCreationRemarks: 70,
    securityProvider: 70,
    securityBriefDetails: 150,
    location: 90,
    rocCertificateChargeIdNumber: 60,
    cersaiAcknowledgmentNumber: 60,
    neslReference: 60,
    remarksForRecurring: 150,
    remarksForKeyApprovals: 150,
    waiverReason: 60,
    frequencyOfRecurrenceUnit: 6,
    securityTrustee: 10,
    securityAgent: 10,
    custodian: 10,
    responsibleParty: 15,
    responsiblePartyDescription: 60,
    securityTrusteeRemarks: 90,
    securityAgentRemarks: 90,
    custodianRemarks: 90,
    documentType: 10,
    documentTitle: 100,
    documentName: 100,
    securitiesNominalValueCurrency: 5,
    securitiesType: 20,
    timelinesText: 200,
    actionDaysPrefix: 1,
    actionDaysSuffix: 1,
    timelineEvent: 1,
    collateralValidityPeriod: 90,
    remarks: 90,
    additionalText: 90,
    landAreaUnit: 3,
    collateralValueCurrency: 5,
    collateralAgreementType: 6,
    collateralAgreementTypeDescription: 30,
    collateralAgreementId: 40,
    sourceOfEntry: 32,
    portalId: 40,
    monitoringId: 40,
};

/** Rows of the SAP child tables of a collateral. Generated from the backend field lists. */

interface CollateralChildBase {
    id?: string;
    itemId?: string;
    checklistIdNo?: number | null;
    sapItemId?: string | null;
    companyCode?: string | null;
    contractNumber?: string | null;
    portalId?: string | null;
    portalDocumentId?: string | null;
    sourceOfEntry?: string | null;
    createdOn?: string;
    createdByUserName?: string;
    changedOn?: string;
    changedByUserName?: string;
}

/** Collateral Coverage (SAP ZCOL_COV) */
export interface Coverage extends CollateralChildBase {
    /** Serial Number (SERIAL_NO) */
    serialNumber?: number | null;
    /** Effective from date (EFFECTIVE_FROM_DATE) */
    effectiveFromDate?: string | null;
    /** Expected Coverage Value (Amt) (EXP_COV_VALUE_AMT) */
    expectedCoverageAmount?: number | null;
    /** Expected Coverage Value (%) (EXP_COV_VALUE_PCT) */
    expectedCoveragePercentage?: number | null;
    /** Coverage Basis (COV_BASIS) */
    coverageBasis?: string | null;
    /** Coverage Basis description (COV_BASIS_DESC) */
    coverageBasisDescription?: string | null;
    /** Basis Amount (BASIS_AMOUNT) */
    basisAmount?: number | null;
    /** Coverage Amount (COVERAGE_AMOUNT) */
    coverageAmount?: number | null;
    /** Remarks (REMARKS) */
    remarks?: string | null;
}

/** Security Perfection: RoC (SAP ZCOL_SPFCT_ROC) */
export interface PerfectionRoc extends CollateralChildBase {
    /** Event (EVENT_TYPE) */
    eventType?: string | null;
    /** Event Desc (EVENT_DESC) */
    eventDescription?: string | null;
    /** Event Date (EVENT_DATE) */
    eventDate?: string | null;
    /** Remarks (REMARKS) */
    remarks?: string | null;
    /** RoC Filing Date (ROC_FILING_DATE) */
    rocFilingDate?: string | null;
    /** RoC Modification Date (ROC_MODIFICATION_DATE) */
    rocModificationDate?: string | null;
    /** RoC Certificate Charge ID Number (ROC_CERTIFICATE_CHARGE_ID_NUMB) */
    rocCertificateChargeIdNumber?: string | null;
    /** RoC Security Satisfaction Date (ROC_SECURITY_SATISFACTION_DATE) */
    rocSecuritySatisfactionDate?: string | null;
    /** Timelines (ZCOL_TIMELINES_TEXT) */
    timelinesText?: string | null;
    /** Action Days Prefix (ZCOL_ACT_DAYS_PRFX) */
    actionDaysPrefix?: string | null;
    /** Action Days Suffix (ZCOL_ACT_DAYS_SUFFIX) */
    actionDaysSuffix?: string | null;
    /** Action Period (ZCOL_ACT_DAYS) */
    actionPeriod?: number | null;
    /** Collateral Event (ZCOL_EVENT) */
    timelineEvent?: string | null;
    /** Collateral Event Date (ZCOL_EVENT_DATE) */
    timelineEventDate?: string | null;
    /** Timeline Date (ZCOL_TIMELINE_DATE) */
    timelineDate?: string | null;
}

/** Security Perfection: CERSAI (SAP ZCOL_SPFCT_CERSAI) */
export interface PerfectionCersai extends CollateralChildBase {
    /** Event (EVENT_TYPE) */
    eventType?: string | null;
    /** Event Desc (EVENT_DESC) */
    eventDescription?: string | null;
    /** Event Date (EVENT_DATE) */
    eventDate?: string | null;
    /** Remarks (REMARKS) */
    remarks?: string | null;
    /** CERSAI Filing Date (CERSAI_FILING_DATE) */
    cersaiFilingDate?: string | null;
    /** CERSAI Acknowledgment Number (CERSAI_ACKNOWLEDGMENT_NUMBER) */
    cersaiAcknowledgmentNumber?: string | null;
    /** CERSAI Satisfaction Date (CERSAI_SATISFACTION_DATE) */
    cersaiSatisfactionDate?: string | null;
    /** Timelines (ZCOL_TIMELINES_TEXT) */
    timelinesText?: string | null;
    /** Action Days Prefix (ZCOL_ACT_DAYS_PRFX) */
    actionDaysPrefix?: string | null;
    /** Action Days Suffix (ZCOL_ACT_DAYS_SUFFIX) */
    actionDaysSuffix?: string | null;
    /** Action Period (ZCOL_ACT_DAYS) */
    actionPeriod?: number | null;
    /** Collateral Event (ZCOL_EVENT) */
    timelineEvent?: string | null;
    /** Collateral Event Date (ZCOL_EVENT_DATE) */
    timelineEventDate?: string | null;
    /** Timeline Date (ZCOL_TIMELINE_DATE) */
    timelineDate?: string | null;
}

/** Security Perfection: NeSL (SAP ZCOL_SPFCT_NESL) */
export interface PerfectionNesl extends CollateralChildBase {
    /** Event (EVENT_TYPE) */
    eventType?: string | null;
    /** Event Desc (EVENT_DESC) */
    eventDescription?: string | null;
    /** Event Date (EVENT_DATE) */
    eventDate?: string | null;
    /** Remarks (REMARKS) */
    remarks?: string | null;
    /** NeSL Form Submission Date (NESL_FORM_SUBMISSION_DATE) */
    neslFormSubmissionDate?: string | null;
    /** NeSL Satisfaction Date (NESL_SATISFACTION_DATE) */
    neslSatisfactionDate?: string | null;
    /** NeSL Reference (NESL_REFERENCE) */
    neslReference?: string | null;
}

/** Collateral Documents (SAP ZCOL_DOC) */
export interface CollateralDocument extends CollateralChildBase {
    /** Serial Number (SERIAL_NO) */
    serialNumber?: number | null;
    /** Document Type (DOC_TYPE) */
    documentType?: string | null;
    /** Document Type description (DOC_TYPE_DESC) */
    documentTypeDescription?: string | null;
    /** Document Stage (DOC_STAGE) */
    documentStage?: string | null;
    /** Document Stage description (DOC_STAGE_DESC) */
    documentStageDescription?: string | null;
    /** Document Title (DOC_TITLE) */
    documentTitle?: string | null;
    /** Remarks (REMARKS) */
    remarks?: string | null;
    /** Business Document Service: Component ID (BDS_DOC_ID) */
    bdsDocumentId?: string | null;
    /** Uploaded document (portal file reference) (portal only) */
    fileReference?: string | null;
    /** File name of the uploaded document (portal only) */
    fileName?: string | null;
}

/** Securities position (SAP ZCOL_SEC_POS) */
export interface SecuritiesPosition extends CollateralChildBase {
    /** Serial Number (SERIAL_NO) */
    serialNumber?: number | null;
    /** Sec. Change Date (SEC_CHANGE_DATE) */
    securitiesChangeDate?: string | null;
    /** Short name of securities (SEC_POS_DESC) */
    securitiesShortName?: string | null;
    /** Number of Units in a Securities Position (ZSEC_NO_OF_UNITS) */
    numberOfUnits?: number | null;
    /** Nominal Value of a Position (ZSEC_VALUE) */
    nominalValue?: number | null;
    /** Currency of nominal value of a position (ZSEC_VALUE_CURR) */
    nominalValueCurrency?: string | null;
    /** Percentage Holding (ZSEC_PCT_HOLDING) */
    holdingPercentage?: number | null;
    /** Securities Type (SECURITIES_TYPE) */
    securitiesType?: string | null;
    /** Remarks (REMARKS) */
    remarks?: string | null;
}

/** Maximum lengths of the text fields of the child tables (SAP lengths). */
export const CHILD_MAX_LENGTH: Record<string, number> = {
    coverageBasis: 1,
    coverageBasisDescription: 60,
    remarks: 60,
    eventType: 1,
    eventDescription: 60,
    rocCertificateChargeIdNumber: 60,
    timelinesText: 200,
    actionDaysPrefix: 1,
    actionDaysSuffix: 1,
    timelineEvent: 1,
    cersaiAcknowledgmentNumber: 60,
    neslReference: 60,
    documentType: 10,
    documentTypeDescription: 40,
    documentStage: 1,
    documentStageDescription: 60,
    documentTitle: 100,
    bdsDocumentId: 255,
    fileReference: 40,
    fileName: 255,
    securitiesShortName: 40,
    nominalValueCurrency: 5,
    securitiesType: 20,
};

/** A dropdown entry. */
export interface ValueEntry {
    code: string;
    description: string;
}

/** Dropdown lists keyed by list name, e.g. CONDITION_GROUP, plus DOCUMENT_TYPE and UNIT_OF_MEASURE. */
export type ValueLists = Record<string, ValueEntry[]>;

export interface CollateralAccess {
    canWrite: boolean;
    userName: string | null;
    role: string | null;
    writeRoles: string[];
}

/** Configuration app "Checklist ID Number Range" (menu Configuration). */
export interface ChecklistIdConfiguration {
    /** Highest ZID_NO used in SAP; portal numbers start above it. */
    sapHighestNumber: number | null;
    /** The Checklist ID No. the next collateral created in the portal gets. */
    nextNumber: number;
    highestNumberInPortal: number | null;
    highestMigratedNumber: number | null;
    startNumber: number;
    maximumNumber: number;
    changedBy: string | null;
    changedAt: string | null;
    canChange: boolean;
    userRole: string | null;
}

export interface LoanSummary {
    loanApplicationId: string;
    loanContractId: string | null;
    enquiryNo: number | null;
    projectName: string | null;
    borrowerName: string | null;
    borrowerNumber: string | null;
    functionalStatusDescription: string | null;
}

export interface CollateralChecklist {
    id: string | null;
    workFlowStatusCode: number | null;
    workFlowStatusDescription: string | null;
    /** Reason of the last rejection. */
    rejectionReason?: string | null;
    loan: LoanSummary;
    items: CollateralItem[];
}

export interface CollateralItemDetail {
    item: CollateralItem;
    coverages?: Coverage[];
    rocEvents?: PerfectionRoc[];
    cersaiEvents?: PerfectionCersai[];
    neslEvents?: PerfectionNesl[];
    /** Names of the partners in Security Trustee, Security Agent and Custodian, keyed by party number. */
    partnerNames?: Record<string, string>;
    loan: LoanSummary;
    workFlowStatusCode: number | null;
    workFlowStatusDescription: string | null;
}

/** Kind of input a field uses. */
export type FieldType = 'text' | 'number' | 'integer' | 'date' | 'select' | 'checkbox' | 'longtext' | 'readonly' | 'separator'
    /** Party number of a business partner, chosen with the partner search. */
    | 'partner';

/** A business partner found by the partner search. */
export interface PartnerSearchResult {
    id: string;
    partyNumber: number;
    partyName1: string | null;
    partyName2: string | null;
    defaultPartnerRole: string | null;
    defaultPartnerRoleText: string | null;
    searchTerm1: string | null;
    searchTerm2: string | null;
    city: string | null;
    state: string | null;
}

/** Criteria of the partner search. */
export interface PartnerSearchCriteria {
    name1?: string | null;
    name2?: string | null;
    defaultPartnerRole?: string | null;
    searchTerm1?: string | null;
    searchTerm2?: string | null;
}

/** Describes one field of the detail page. */
export interface FieldDef {
    /** Property of the record; for type 'separator' only a unique id. */
    key: string;
    label: string;
    type: FieldType;
    /** Value list name for type 'select'. */
    list?: string;
    required?: boolean;
    /** Columns of the 12-column grid the field spans (default 3). */
    span?: number;
    /** Decimal places allowed for type 'number'. */
    decimals?: number;
    /** Upper limit for numbers, e.g. 100 for percentages. */
    max?: number;
    /** Maximum length of text fields (default: SAP length of the collateral field). */
    maxLength?: number;
    /** Text shown instead of "–" when the field has no value (display and read-only fields). */
    emptyText?: string;
}

/** A tab of the detail page. */
export interface TabDef {
    id: string;
    label: string;
    icon: string;
    sections: { title: string; fields: FieldDef[] }[];
    /** Child table shown as a grid below the sections. */
    grid?: GridDef;
}

/** Child tables of a collateral; the value is the URL segment of the backend. */
export type ChildType = 'coverages' | 'roc' | 'cersai' | 'nesl' | 'documents' | 'securities';

/** A column of a child grid. */
export interface GridColumn {
    key: string;
    label: string;
    /** 'file': the uploaded document of the row (link), or a note that the document is kept in SAP. */
    type?: 'text' | 'date' | 'number' | 'code' | 'file';
    /** Value list of type 'code'. */
    list?: string;
    decimals?: number;
}

/** A child table maintained in a grid with a create / change / display dialog. */
export interface GridDef {
    type: ChildType;
    /** Property of the detail response that holds the rows. */
    detailKey: 'coverages' | 'rocEvents' | 'cersaiEvents' | 'neslEvents' | 'documents' | 'securitiesPositions';
    title: string;
    /** Name of one row in messages and the dialog title, e.g. "Coverage". */
    rowLabel: string;
    dialogTitle: string;
    icon: string;
    columns: GridColumn[];
    /** Fields of the dialog; type 'separator' starts a new group. */
    fields: FieldDef[];
    emptyText: string;
    /** The dialog offers Upload Document (fileReference / fileName of the row). */
    upload?: boolean;
    /** Values proposed for a new row, e.g. the currency. */
    defaults?: Record<string, unknown>;
}

/** Fields of the header block, visible above every tab. */
export const HEADER_FIELDS: FieldDef[] = [
    { key: 'conditionGroup', label: 'Condition Group', type: 'select', list: 'CONDITION_GROUP', required: true, span: 3 },
    { key: 'conditionDescription', label: 'Condition Description', type: 'text', span: 3 },
    { key: 'conditionCategory', label: 'Condition Category', type: 'select', list: 'CONDITION_CATEGORY', required: true, span: 4 },
    { key: 'checklistIdNo', label: 'Checklist ID No.', type: 'readonly', span: 2, emptyText: 'Assigned on save' },
    { key: 'collateralObjectType', label: 'Collateral Object', type: 'select', list: 'COLLATERAL_OBJECT_TYPE', required: true, span: 3 },
    { key: 'collateralObjectDescription', label: 'Coll. Obj. Descr.', type: 'text', span: 5 },
    { key: 'validFromDate', label: 'Valid from', type: 'date', span: 2 },
    { key: 'validToDate', label: 'Valid to', type: 'date', span: 2 },
    { key: 'collateralAgreementType', label: 'Collateral Agreement Type', type: 'select', list: 'AGREEMENT_TYPE', span: 3 },
    { key: 'collateralAgreementId', label: 'Collateral Agreement ID', type: 'text', span: 3 },
    { key: 'collateralValue', label: 'Collateral Value', type: 'number', decimals: 2, span: 4 },
    { key: 'collateralValueCurrency', label: 'Currency', type: 'select', list: 'CURRENCY', span: 2 },
    { key: 'remarks', label: 'Remarks', type: 'text', span: 6 },
    { key: 'additionalText', label: 'Additional Text', type: 'text', span: 6 }
];

const TIMELINE_FIELDS: FieldDef[] = [
    { key: 'timelineSeparator', label: 'Timeline', type: 'separator', span: 12 },
    { key: 'timelinesText', label: 'Timelines', type: 'text', span: 12, maxLength: 200 },
    { key: 'actionDaysPrefix', label: 'Action Period', type: 'select', list: 'ACTION_DAYS_PREFIX', span: 5 },
    { key: 'actionPeriod', label: 'Number', type: 'integer', max: 999, span: 3 },
    { key: 'actionDaysSuffix', label: 'Unit', type: 'select', list: 'ACTION_DAYS_SUFFIX', span: 4 },
    { key: 'timelineEvent', label: 'Event', type: 'select', list: 'TIMELINE_EVENT', span: 6 },
    { key: 'timelineEventDate', label: 'Event Date', type: 'date', span: 3 },
    { key: 'timelineDate', label: 'Timeline Date', type: 'date', span: 3 }
];

const EVENT_COLUMNS: GridColumn[] = [
    { key: 'eventType', label: 'Event', type: 'code', list: 'PERFECTION_EVENT_TYPE' },
    { key: 'eventDate', label: 'Event Date', type: 'date' },
    { key: 'remarks', label: 'Remarks' }
];

/** Child grids (SAP tables ZCOL_COV, ZCOL_SPFCT_ROC, ZCOL_SPFCT_CERSAI, ZCOL_SPFCT_NESL). */
export const GRIDS: Record<ChildType, GridDef> = {
    coverages: {
        type: 'coverages', detailKey: 'coverages', title: 'Coverage', rowLabel: 'Coverage', dialogTitle: 'Collateral Coverage',
        icon: 'shield',
        columns: [
            { key: 'serialNumber', label: 'Serial No.', type: 'number', decimals: 0 },
            { key: 'effectiveFromDate', label: 'Effective from', type: 'date' },
            { key: 'expectedCoverageAmount', label: 'Exp. Coverage Value (Amt)', type: 'number', decimals: 2 },
            { key: 'expectedCoveragePercentage', label: 'Exp. Coverage Value (%)', type: 'number', decimals: 2 },
            { key: 'coverageBasis', label: 'Coverage Basis', type: 'code', list: 'COVERAGE_BASIS' },
            { key: 'basisAmount', label: 'Basis Amount', type: 'number', decimals: 2 },
            { key: 'coverageAmount', label: 'Coverage Amount', type: 'number', decimals: 2 },
            { key: 'remarks', label: 'Remarks' }
        ],
        fields: [
            { key: 'effectiveFromDate', label: 'Effective from date', type: 'date', required: true, span: 6 },
            { key: 'coverageBasis', label: 'Coverage Basis', type: 'select', list: 'COVERAGE_BASIS', span: 6 },
            { key: 'expectedCoverageAmount', label: 'Exp. Cov. Val (Amt)', type: 'number', decimals: 2, span: 6 },
            { key: 'expectedCoveragePercentage', label: 'Exp. Cov. Val (%)', type: 'number', decimals: 2, max: 100, span: 6 },
            { key: 'basisAmount', label: 'Basis Amount', type: 'number', decimals: 2, span: 6 },
            { key: 'coverageAmount', label: 'Coverage Amount', type: 'number', decimals: 2, span: 6 },
            { key: 'remarks', label: 'Remarks', type: 'text', span: 12, maxLength: 60 }
        ],
        emptyText: 'Add the expected coverage per effective date.'
    },
    roc: {
        type: 'roc', detailKey: 'rocEvents', title: 'Security Perfection with RoC', rowLabel: 'RoC event',
        dialogTitle: 'RoC – Security Perfection', icon: 'official-service',
        columns: [...EVENT_COLUMNS,
            { key: 'rocCertificateChargeIdNumber', label: 'RoC Certificate Charge ID Number' },
            { key: 'rocSecuritySatisfactionDate', label: 'RoC Sec. Satisfy Date', type: 'date' }],
        fields: [
            { key: 'eventType', label: 'Event', type: 'select', list: 'PERFECTION_EVENT_TYPE', required: true, span: 6 },
            { key: 'eventDate', label: 'Event Date', type: 'date', span: 3 },
            { key: 'rocSecuritySatisfactionDate', label: 'RoC Sec. Satisfy Date', type: 'date', span: 3 },
            { key: 'rocCertificateChargeIdNumber', label: 'RoC Certificate Charge ID Number', type: 'text', span: 6, maxLength: 60 },
            { key: 'remarks', label: 'Remarks', type: 'text', span: 6, maxLength: 60 },
            ...TIMELINE_FIELDS
        ],
        emptyText: 'Record filing, perfection, modification and release events with the Registrar of Companies.'
    },
    cersai: {
        type: 'cersai', detailKey: 'cersaiEvents', title: 'Security Perfection with CERSAI', rowLabel: 'CERSAI event',
        dialogTitle: 'CERSAI – Security Perfection', icon: 'chain-link',
        columns: [...EVENT_COLUMNS, { key: 'cersaiAcknowledgmentNumber', label: 'CERSAI Acknowledgment Number' }],
        fields: [
            { key: 'eventType', label: 'Event', type: 'select', list: 'PERFECTION_EVENT_TYPE', required: true, span: 6 },
            { key: 'eventDate', label: 'Event Date', type: 'date', span: 3 },
            { key: 'cersaiAcknowledgmentNumber', label: 'CERSAI Acknowledgment Number', type: 'text', span: 6, maxLength: 60 },
            { key: 'remarks', label: 'Remarks', type: 'text', span: 6, maxLength: 60 },
            ...TIMELINE_FIELDS
        ],
        emptyText: 'Record the events registered with CERSAI.'
    },
    nesl: {
        type: 'nesl', detailKey: 'neslEvents', title: 'Security Perfection with NeSL', rowLabel: 'NeSL event',
        dialogTitle: 'NeSL – Security Perfection', icon: 'course-book',
        columns: [...EVENT_COLUMNS, { key: 'neslReference', label: 'NeSL Reference' }],
        fields: [
            { key: 'eventType', label: 'Event', type: 'select', list: 'PERFECTION_EVENT_TYPE', required: true, span: 6 },
            { key: 'eventDate', label: 'Event Date', type: 'date', span: 3 },
            { key: 'neslReference', label: 'NeSL Reference', type: 'text', span: 6, maxLength: 60 },
            { key: 'remarks', label: 'Remarks', type: 'text', span: 6, maxLength: 60 }
        ],
        emptyText: 'Record the events registered with NeSL.'
    },
    documents: {
        type: 'documents', detailKey: 'documents', title: 'Collateral Documents', rowLabel: 'document',
        dialogTitle: 'Collateral Documents', icon: 'documents', upload: true,
        columns: [
            { key: 'serialNumber', label: 'Serial No.', type: 'number', decimals: 0 },
            { key: 'documentType', label: 'Document Type' },
            { key: 'documentTypeDescription', label: 'Description' },
            { key: 'documentStage', label: 'Document Stage', type: 'code', list: 'DOCUMENT_STAGE' },
            { key: 'documentTitle', label: 'Document Title' },
            { key: 'remarks', label: 'Remarks' },
            { key: 'fileName', label: 'Document', type: 'file' }
        ],
        fields: [
            { key: 'documentStage', label: 'Document Stage', type: 'select', list: 'DOCUMENT_STAGE', required: true, span: 6 },
            { key: 'documentType', label: 'Document Type', type: 'select', list: 'DOCUMENT_TYPE', required: true, span: 6 },
            { key: 'documentTitle', label: 'Document Title', type: 'text', span: 12, maxLength: 100 },
            { key: 'remarks', label: 'Remarks', type: 'text', span: 12, maxLength: 60 }
        ],
        emptyText: 'Add the documents of this collateral and upload them.'
    },
    securities: {
        type: 'securities', detailKey: 'securitiesPositions', title: 'Securities Positions', rowLabel: 'securities position',
        dialogTitle: 'Securities Position', icon: 'pie-chart', defaults: { nominalValueCurrency: 'INR' },
        columns: [
            { key: 'serialNumber', label: 'Serial No.', type: 'number', decimals: 0 },
            { key: 'securitiesChangeDate', label: 'Date', type: 'date' },
            { key: 'securitiesShortName', label: 'Short Name' },
            { key: 'numberOfUnits', label: 'Number of Units', type: 'number', decimals: 5 },
            { key: 'nominalValue', label: 'Nominal Value', type: 'number', decimals: 2 },
            { key: 'nominalValueCurrency', label: 'Currency' },
            { key: 'holdingPercentage', label: 'Holding %', type: 'number', decimals: 3 },
            { key: 'securitiesType', label: 'Securities Type' },
            { key: 'remarks', label: 'Remarks' }
        ],
        fields: [
            { key: 'securitiesType', label: 'Securities Type', type: 'select', list: 'SECURITIES_TYPE', required: true, span: 8 },
            { key: 'securitiesChangeDate', label: 'Date', type: 'date', span: 4 },
            { key: 'numberOfUnits', label: 'No of Units', type: 'number', decimals: 5, span: 4 },
            { key: 'nominalValue', label: 'Nominal Value', type: 'number', decimals: 2, span: 4 },
            { key: 'nominalValueCurrency', label: 'Currency', type: 'select', list: 'CURRENCY', span: 4 },
            { key: 'holdingPercentage', label: 'Holding %', type: 'number', decimals: 3, max: 100, span: 4 },
            { key: 'remarks', label: 'Remarks', type: 'text', span: 8, maxLength: 60 }
        ],
        emptyText: 'Record the securities positions (pledged shares, debentures and other securities).'
    }
};

/** The 14 tabs of the detail page, in the order of the SAP screen. */
export const TABS: TabDef[] = [
    {
        id: 'applicability', label: 'Applicability', icon: 'checklist-item', sections: [
            { title: 'Penal Charges', fields: [
                { key: 'penalChargesApplicable', label: 'Penal Charges Applicable', type: 'checkbox' },
                { key: 'penalChargesPercentage', label: 'Penal Charges %', type: 'number', decimals: 2, max: 100 }
            ] },
            { title: 'Recurrence', fields: [
                { key: 'recurring', label: 'Recurring', type: 'checkbox' },
                { key: 'startDate', label: 'Start Date', type: 'date', span: 2 },
                { key: 'endDate', label: 'End Date', type: 'date', span: 2 },
                { key: 'frequencyOfRecurrence', label: 'Frequency of Recurrence', type: 'integer', max: 99, span: 2 },
                { key: 'frequencyOfRecurrenceUnit', label: 'Frequency Unit', type: 'select', list: 'FREQUENCY_UNIT' }
            ] },
            { title: 'Disbursement', fields: [
                { key: 'applicableForEveryDisbursement', label: 'Applicable for Every Disbursement', type: 'checkbox', span: 4 },
                { key: 'percentageOfDisbursement', label: 'Percentage of the Disbursement', type: 'integer', max: 100 }
            ] }
        ]
    },
    {
        id: 'security-description', label: 'Security Desc', icon: 'document-text', sections: [
            { title: 'Security Description', fields: [
                { key: 'securityTypeText', label: 'Security Type', type: 'longtext', span: 12 },
                { key: 'stipulatedSecurity', label: 'Stipulated Security as per Loan Agreement', type: 'longtext', span: 12 }
            ] }
        ]
    },
    {
        id: 'compliance', label: 'Compl. Status', icon: 'inspection', sections: [
            { title: 'Compliance Status', fields: [
                { key: 'complianceDate', label: 'Compliance Date', type: 'date' },
                { key: 'complianceStatus', label: 'Compliance Status', type: 'select', list: 'COMPLIANCE_STATUS' },
                { key: 'complianceRemarksLegal', label: 'Compliance Status Remarks (Legal Dept)', type: 'longtext', span: 12 },
                { key: 'actionTaken', label: 'Action Taken by Borrower / PFS (Monitoring Dept)', type: 'longtext', span: 12 }
            ] }
        ]
    },
    {
        id: 'timelines', label: 'Timelines', icon: 'appointment-2', sections: [
            { title: 'Timelines', fields: [
                { key: 'timelinesText', label: 'Timelines', type: 'text', span: 12 },
                { key: 'actionDaysPrefix', label: 'Action Period', type: 'select', list: 'ACTION_DAYS_PREFIX' },
                { key: 'actionPeriod', label: 'Number', type: 'integer', max: 999, span: 2 },
                { key: 'actionDaysSuffix', label: 'Unit', type: 'select', list: 'ACTION_DAYS_SUFFIX', span: 2 },
                { key: 'timelineEvent', label: 'Event', type: 'select', list: 'TIMELINE_EVENT', span: 3 },
                { key: 'timelineEventDate', label: 'Event Date', type: 'date', span: 2 },
                { key: 'timelineDate', label: 'Timeline Date', type: 'date', span: 2 }
            ] }
        ]
    },
    {
        id: 'security-creation', label: 'Sec. Creation', icon: 'shield', grid: GRIDS.coverages, sections: [
            { title: 'Security Creation Details', fields: [
                { key: 'securityCreationDate', label: 'Security Creation Date', type: 'date' },
                { key: 'securityCreationPlace', label: 'Security Creation Place', type: 'text', span: 9 },
                { key: 'securityCreationRemarks', label: 'Security Creation Remarks', type: 'text', span: 6 },
                { key: 'securityProvider', label: 'Security Provider', type: 'text', span: 6 },
                { key: 'securityBriefDetails', label: 'Brief Details of Security', type: 'text', span: 12 },
                { key: 'validityDate', label: 'Validity Date', type: 'date' },
                { key: 'expectedValue', label: 'Expected Value', type: 'number', decimals: 2 },
                { key: 'expectedValuePercentageHolding', label: 'Expected Value % Holding', type: 'number', decimals: 3, max: 100 },
                { key: 'location', label: 'Location', type: 'text', span: 12 }
            ] }
        ]
    },
    {
        id: 'responsible-parties', label: 'Resp. Parties', icon: 'group', sections: [
            { title: 'Responsible Party', fields: [
                { key: 'responsibleParty', label: 'Responsible Party', type: 'select', list: 'RESPONSIBLE_PARTY' },
                { key: 'responsiblePartyDescription', label: 'Responsible Party Description', type: 'text', span: 9 }
            ] },
            { title: 'Trustee, Agent and Custodian', fields: [
                { key: 'securityTrustee', label: 'Security Trustee', type: 'partner', span: 4 },
                { key: 'securityTrusteeRemarks', label: 'Sec. Trustee Remarks', type: 'text', span: 8 },
                { key: 'securityAgent', label: 'Security Agent', type: 'partner', span: 4 },
                { key: 'securityAgentRemarks', label: 'Sec. Agent Remarks', type: 'text', span: 8 },
                { key: 'custodian', label: 'Custodian', type: 'partner', span: 4 },
                { key: 'custodianRemarks', label: 'Custodian Remarks', type: 'text', span: 8 }
            ] }
        ]
    },
    { id: 'perfection-roc', label: 'Sec.Perf-ROC', icon: 'official-service', grid: GRIDS.roc, sections: [] },
    { id: 'perfection-cersai', label: 'Sec.Perf-CERSAI', icon: 'chain-link', grid: GRIDS.cersai, sections: [] },
    { id: 'perfection-nesl', label: 'Sec.Perf-NESL', icon: 'course-book', grid: GRIDS.nesl, sections: [] },
    {
        id: 'post-execution', label: 'Post Exec', icon: 'upload', sections: [
            { title: 'Post Execution Opinion', fields: [
                { key: 'postExecutionOpinion', label: 'Post Execution Opinion from LLC', type: 'text', span: 12 },
                { key: 'postExecutionDocumentType', label: 'Document Type', type: 'select', list: 'DOCUMENT_TYPE', span: 4 },
                { key: 'postExecutionDocumentTitle', label: 'Document Title', type: 'text', span: 8 }
            ] }
        ]
    },
    {
        id: 'special-approval', label: 'Spl. Approval', icon: 'workflow-tasks', sections: [
            { title: 'Waiver and Key Approvals', fields: [
                { key: 'waiverPossible', label: 'Waiver Possible', type: 'checkbox' },
                { key: 'keyApprovals', label: 'Key Approvals', type: 'checkbox' },
                { key: 'remarksForKeyApprovals', label: 'Remarks for Key Approvals', type: 'text', span: 12 }
            ] },
            { title: 'Competent Authority', fields: [
                { key: 'waivedOff', label: 'Waived Off', type: 'checkbox', span: 12 },
                { key: 'competentAuthority', label: 'Competent Authority', type: 'select', list: 'COMPETENT_AUTHORITY' },
                { key: 'boardCommitteeName', label: 'Board Committee Name', type: 'text', span: 9 },
                { key: 'competentAuthorityRemarks', label: 'Comp. Authority Remarks', type: 'text', span: 12 },
                { key: 'waiverDate', label: 'Waiver Date', type: 'date' },
                { key: 'penalChargesWaiverPercentage', label: 'Penal Charges Waiver %', type: 'number', decimals: 2, max: 100 },
                { key: 'waiverReason', label: 'Remarks Waiver Reason', type: 'text', span: 6 }
            ] }
        ]
    },
    { id: 'documents', label: 'Documents', icon: 'documents', grid: GRIDS.documents, sections: [] },
    {
        id: 'real-estate', label: 'Real Estate', icon: 'home', sections: [
            { title: 'Real Estate Specific', fields: [
                { key: 'landArea', label: 'Total Land Area', type: 'number', decimals: 3 },
                { key: 'landAreaUnit', label: 'Unit of Measure', type: 'select', list: 'UNIT_OF_MEASURE' }
            ] }
        ]
    },
    { id: 'securities', label: 'Sec. Specific', icon: 'pie-chart', grid: GRIDS.securities, sections: [] }
];

/** All fields of the detail page. */
export const ALL_FIELDS: FieldDef[] = [...HEADER_FIELDS, ...TABS.flatMap(tab => tab.sections.flatMap(section => section.fields))];

/** Compliance status colours, keyed by status code. */
export const COMPLIANCE_STATUS_STATE: Record<string, 'positive' | 'negative' | 'critical' | 'informative'> = {
    '1': 'positive',
    '2': 'negative',
    '3': 'critical',
    '4': 'critical',
    '5': 'informative'
};

/** Workflow status codes of the checklist (portal convention). */
export const WORKFLOW_STATUS = {
    NOT_SENT: 0,
    SENT_FOR_APPROVAL: 2,
    APPROVED: 3,
    REJECTED: 4
} as const;

/** Object status colour of a workflow status code. */
export function workflowState(code: number | null | undefined): 'positive' | 'negative' | 'critical' | 'informative' {
    switch (code) {
        case WORKFLOW_STATUS.APPROVED:
            return 'positive';
        case WORKFLOW_STATUS.REJECTED:
            return 'negative';
        case WORKFLOW_STATUS.SENT_FOR_APPROVAL:
            return 'critical';
        default:
            return 'informative';
    }
}

/** dd.MM.yyyy for an ISO date ("2021-06-01") or a Jackson date array ([2021, 6, 1]). */
export function formatDate(value: unknown): string {
    const iso = toIsoDate(value);
    if (!iso) {
        return '';
    }
    const [year, month, day] = iso.split('-');
    return `${day}.${month}.${year}`;
}

/** "yyyy-MM-dd" for an ISO date string or a [year, month, day] array; null otherwise. */
export function toIsoDate(value: unknown): string | null {
    if (Array.isArray(value) && value.length >= 3) {
        const [year, month, day] = value as number[];
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    }
    if (typeof value === 'string') {
        const match = /^(\d{4})-(\d{2})-(\d{2})/.exec(value);
        return match ? `${match[1]}-${match[2]}-${match[3]}` : null;
    }
    return null;
}

/** "code description" of a dropdown value, or the code alone when it is not in the list. */
export function valueText(lists: ValueLists | null | undefined, list: string | undefined, code: unknown): string {
    if (code === null || code === undefined || code === '') {
        return '';
    }
    const entry = list ? lists?.[list]?.find(value => value.code === code) : undefined;
    return entry ? `${entry.code} ${entry.description}` : String(code);
}

/** Description of a dropdown value, or the code when it is not in the list. */
export function valueDescription(lists: ValueLists | null | undefined, list: string, code: unknown): string {
    if (code === null || code === undefined || code === '') {
        return '';
    }
    return lists?.[list]?.find(value => value.code === code)?.description ?? String(code);
}
