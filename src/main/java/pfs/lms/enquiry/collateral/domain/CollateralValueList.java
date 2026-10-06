package pfs.lms.enquiry.collateral.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Collateral Management value lists (dropdowns) and their values from the requirements deck.
 * The values seed table {@code collateral_value}; entries added or changed there later are kept.
 */
public enum CollateralValueList {

    /** Condition Group (ZCONDITION_GRP), default 04 */
    CONDITION_GROUP("Condition Group (ZCONDITION_GRP), default 04",
            "01", "Pre-Commitment",
            "02", "Pre-Disbursement",
            "03", "Post-Commitment",
            "04", "Collateral Conditions",
            "05", "Other Conditions"),

    /** Condition Category (ZCOND_CAT), default 04 */
    CONDITION_CATEGORY("Condition Category (ZCOND_CAT), default 04",
            "01", "Pre commitment",
            "02", "Pre disbursement",
            "03", "Post commitment",
            "04", "Collateral conditions",
            "05", "Financial covenants",
            "06", "Undertaking by Promoters",
            "07", "Undertaking by Borrowers",
            "08", "Condi. prece. to each disbur.",
            "09", "Other Conditions",
            "10", "Negative Covenants",
            "11", "Credit Rating",
            "12", "Debt Service Reserve Account",
            "13", "Trust & Retention Account",
            "14", "Commitment Fee",
            "15", "Draw-down Schedule",
            "16", "Cash Sweep/Cash Trap",
            "17", "Prepayments",
            "18", "Interest Tax Levies & Duties",
            "19", "Liquidated Damages",
            "20", "Additional Interest"),

    /** Collateral Object Type (ZCOLLATERAL_TYPE) */
    COLLATERAL_OBJECT_TYPE("Collateral Object Type (ZCOLLATERAL_TYPE)",
            "Z00007", "Personal Guarantee",
            "Z00008", "Corporate Guarantee",
            "Z00009", "Bank Guarantee",
            "Z00012", "Letter Of Credit",
            "Z00013", "Post Dated Cheque",
            "Z00014", "Promoters Undertaking",
            "Z00015", "Borrowers Undertaking",
            "Z00016", "Sponsors Undertaking",
            "Z00017", "Interim/Alternate Security",
            "Z30001", "Securities",
            "ZCL001", "DSRA Account",
            "ZCL002", "TRA Account",
            "ZCL003", "Other Current Assets",
            "ZCL004", "Book Debts",
            "ZCL005", "Third Party Pledge",
            "ZCL006", "Unsecured Loan",
            "ZDE001", "Plant and Machinery",
            "ZDE002", "Furniture and Fixtures",
            "ZDE003", "Office Equipment",
            "ZDE004", "Other Movables",
            "ZDE005", "Motor Vehicle (Hypothecation)",
            "ZDE006", "Third Party Movable Assets",
            "ZIN002", "Inventory",
            "ZIN003", "Movable property-Inventory(incl.Recvbls)",
            "ZOT001", "Project Clearances",
            "ZOT002", "Project Documents",
            "ZOT003", "Bank Guarantees under project documents",
            "ZOT004", "Approvals",
            "ZOT005", "Claims from Insurances",
            "ZOT006", "Claims from letter of credit",
            "ZPA001", "Patents",
            "ZPA002", "Intangible \u2013 Goodwill",
            "ZPA003", "Intangibles - Trademarks",
            "ZPA004", "Intangible - Licence",
            "ZPA005", "Intangible - Licence under a Patent",
            "ZPA006", "Intangible - Copyright",
            "ZPA007", "Intangible - Copyright under a Patent",
            "ZPA008", "Intangible - Designs",
            "ZPA009", "Intangible - IPR",
            "ZPA010", "Intangible - Others",
            "ZRE001", "Land",
            "ZRE002", "Building",
            "ZRE003", "Immovable property Residential",
            "ZRE004", "Immovable property Commercial",
            "ZRE005", "Immovable property others",
            "ZRE006", "Property situated outside India",
            "ZRE007", "Third Pary Immovable Collateral",
            "ZSHP01", "Movable property - Ship or any share in"),

    /** Collateral Agreement Type (ZCMS_COL_AGMT_TYPE) */
    AGREEMENT_TYPE("Collateral Agreement Type (ZCMS_COL_AGMT_TYPE)",
            "Z00001", "Mortgage",
            "Z00002", "Mortgage Registered",
            "Z00003", "Pledge of Securities Account",
            "Z00004", "Pledge of Accounts",
            "Z00005", "Pledge of Patents/Rights",
            "Z00006", "Pledge of Other Valuables",
            "Z00007", "Personal Guarantee",
            "Z00008", "Corporate Guarantee",
            "Z00009", "Bank Guarantee",
            "Z00010", "Hypothecation",
            "Z00011", "Transfer of Rights on Inventory",
            "Z00012", "Letter of Credit",
            "Z00013", "Post Dated Cheque",
            "Z00014", "Promoters Undertaking",
            "Z00015", "Borrowers Undertaking",
            "Z00016", "Sponsors Undertaking",
            "Z00017", "Interim/Alternate Security"),

    /** Compliance Status (ZSTATUS) */
    COMPLIANCE_STATUS("Compliance Status (ZSTATUS)",
            "1", "Complied",
            "2", "Not Complied",
            "3", "In-Process",
            "4", "Partially Complied",
            "5", "Timeline Available"),

    /** Responsible Party (ZRESPON_PARTY) */
    RESPONSIBLE_PARTY("Responsible Party (ZRESPON_PARTY)",
            "0", "Promoter",
            "1", "Borrower",
            "2", "Third Party",
            "3", "SPC/SPV of Borrower"),

    /** Action Period prefix (ZCOL_ACT_DAYS_PRFX) */
    ACTION_DAYS_PREFIX("Action Period prefix (ZCOL_ACT_DAYS_PRFX)",
            "0", "Equal",
            "1", "Within",
            "2", "Before",
            "3", "Till",
            "4", "After",
            "5", "At the time of",
            "6", "Less Than",
            "7", "Less Than or Equal To",
            "8", "Greater Than",
            "9", "Greater Than or Equal To",
            "A", "Others"),

    /** Action Days suffix (ZCOL_ACT_DAYS_SUFFIX) */
    ACTION_DAYS_SUFFIX("Action Days suffix (ZCOL_ACT_DAYS_SUFFIX)",
            "1", "Days",
            "2", "Weeks",
            "3", "Months",
            "4", "Years"),

    /** Timeline event (ZCOL_EVENT) */
    TIMELINE_EVENT("Timeline event (ZCOL_EVENT)",
            "0", "Others",
            "1", "Enquiry Completion Date",
            "2", "ICC Clearance Date",
            "3", "Appraisal Completion Date",
            "4", "Sanction Letter Issue Date",
            "5", "Contract Date",
            "6", "1st Disbursement Date",
            "7", "Scheduled COD",
            "8", "Actual COD",
            "9", "Date of Security"),

    /** Perfection event (EVENT_TYPE) of RoC, CERSAI and NeSL */
    PERFECTION_EVENT_TYPE("Perfection event (EVENT_TYPE) of RoC, CERSAI and NeSL",
            "1", "Filing/Form Submission",
            "2", "Perfection/ Satisfaction",
            "3", "Modification",
            "4", "Release"),

    /** Competent Authority (KEY_APPR_COMP_AUTHORITY) */
    COMPETENT_AUTHORITY("Competent Authority (KEY_APPR_COMP_AUTHORITY)",
            "0", "Board",
            "1", "BCM",
            "2", "ICC",
            "3", "MD&CEO"),

    /** Document Stage (DOC_STAGE) */
    DOCUMENT_STAGE("Document Stage (DOC_STAGE)",
            "1", "Creation",
            "2", "Perfection",
            "3", "Modification",
            "4", "Release-Pre-Requisites",
            "5", "Release-Final"),

    /** Securities Type (SECURITIES_TYPE) */
    SECURITIES_TYPE("Securities Type (SECURITIES_TYPE)",
            "CALLS_MADE", "Calls made but not paid",
            "CCD", "Pledge CCD (Comp. Convertible Deben.)",
            "CCPS", "Share  Pledge of CCPS's",
            "NCD", "Pledge NCD (Non- Convertible Debentures)",
            "PFS_DUMMY", "Other type of securities",
            "SHARE_PL_EQ_SHARES", "Share Pledge/Equity Shares",
            "UNCALLED_SHARE_CAP", "Uncalled Share Capital"),

    /** Coverage Basis (COV_BASIS) */
    COVERAGE_BASIS("Coverage Basis (COV_BASIS)",
            "1", "Current Contract Capital",
            "2", "Effective Capital",
            "3", "Commitment Capital",
            "4", "Sanction Amount"),

    /** Unit of the frequency of recurrence (ZFRQ_OF_RECUR_UNIT) */
    FREQUENCY_UNIT("Unit of the frequency of recurrence (ZFRQ_OF_RECUR_UNIT)",
            "0", "Days",
            "1", "Week",
            "2", "Month",
            "3", "Year"),

    /** Currency of collateral value (ZCOL_VALUE_CURR) */
    CURRENCY("Currency of collateral value (ZCOL_VALUE_CURR)",
            "INR", "Indian Rupee",
            "USD", "US Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen");

    private final String description;
    private final List<String[]> values;

    CollateralValueList(String description, String... codesAndTexts) {
        this.description = description;
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i + 1 < codesAndTexts.length; i += 2) {
            list.add(new String[]{codesAndTexts[i], codesAndTexts[i + 1]});
        }
        this.values = Collections.unmodifiableList(list);
    }

    public String getDescription() {
        return description;
    }

    /** Seed values as {code, description}, in display order. */
    public List<String[]> getValues() {
        return values;
    }
}
