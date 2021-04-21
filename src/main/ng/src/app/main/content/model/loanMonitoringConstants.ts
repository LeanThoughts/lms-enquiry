export class LoanMonitoringConstants {

    // reportTypes
    public static reportTypes = [
        {code: "1" , value:"Due Diligence"},
        {code: "2" , value:"Monitoring"},
        {code: "3" , value:"O & M Review"},
        {code: "4" , value:"Other (if any)"}
    ];

    // feeReceiptStatuses
    public static feeReceiptStatuses = [
        {code: "0" , value:"Yes"},
        {code: "1" , value:"No"}
    ];

    // feePaidStatuses
    public static feePaidStatuses = [
        {code: "0" , value:"Yes"},
        {code: "1" , value:"No"}
    ];

    // viewRights
    static viewRights = [
        {code: "0" , value:"Yes"},
        {code: "1" , value:"No"}
    ];

    // periodQuarters
    static periodQuarters = [
        {code: "I" , value:"First Quarter"},
        {code: "II" , value:"Second Quarter"},
        {code: "III" , value:"Third Quarter"},
        {code: "IV" , value:"Fourth Quarter"}
    ];

    // documentTypes
    static documentTypes = [
        {code: "ZPFSLM1", value:"PFS Project Notes"},
        {code: "ZPFSLM10", value:"Loan Agreement"},
        {code: "ZPFSLM11", value:"Lenders Agent Agreement"},
        {code: "ZPFSLM12", value:"Security Trustee Agreement"},
        {code: "ZPFSLM13", value:"Trust and Retention Agreement"},
        {code: "ZPFSLM14", value:"Inter Creditor Agreement"},
        {code: "ZPFSLM15", value:"Confirmation to ICA"},
        {code: "ZPFSLM16", value:"Deed of Hypothecation"},
        {code: "ZPFSLM17", value:"Deed of Pledge"},
        {code: "ZPFSLM18", value:"Power of Attorney"},
        {code: "ZPFSLM19", value:"Indenture of Mortgage"},
        {code: "ZPFSLM2", value:"Project Contract Documents"},
        {code: "ZPFSLM20", value:"Deed of Personal Guarantee"},
        {code: "ZPFSLM21", value:"Deed of Corporate Guarantee"},
        {code: "ZPFSLM22", value:"Affidavit"},
        {code: "ZPFSLM23", value:"Borrowers Undertaking"},
        {code: "ZPFSLM24", value:"Promoters Undertaking"},
        {code: "ZPFSLM25", value:"Other Agreements"},
        {code: "ZPFSLM26", value:"CA / CS/ Auditors Certificates"},
        {code: "ZPFSLM27", value:"Amendment agreements"},
        {code: "ZPFSLM28", value:"Novation / Accession Agreements"},
        {code: "ZPFSLM29", value:"Demand Letters"},
        {code: "ZPFSLM3", value:"Project Clearances/Approvals"},
        {code: "ZPFSLM30", value:"Fee Invoices"},
        {code: "ZPFSLM31", value:"Debit Notes"},
        {code: "ZPFSLM32", value:"No Objection Certificate"},
        {code: "ZPFSLM33", value:"Term Sheet"},
        {code: "ZPFSLM34", value:"Sanction Letter"},
        {code: "ZPFSLM35", value:"Amendment to Sanction Letters"},
        {code: "ZPFSLM36", value:"Disbursement Intimations"},
        {code: "ZPFSLM37", value:"Other Communication (if any)"},
        {code: "ZPFSLM38", value:"Loan Request Application"},
        {code: "ZPFSLM39", value:"Request for Disbursement"},
        {code: "ZPFSLM4", value:"Promoter and Borrower Financials"},
        {code: "ZPFSLM40", value:"Request for Amendment"},
        {code: "ZPFSLM41", value:"Request for Prepayment"},
        {code: "ZPFSLM42", value:"Any Other requests"},
        {code: "ZPFSLM43", value:"Gallery"},
        {code: "ZPFSLM44", value:"Operating_Parameter"},
        {code: "ZPFSLM45", value:"NOC_Prepayment"},
        {code: "ZPFSLM46", value:"NOC_Intt_Reduction"},
        {code: "ZPFSLM47", value:"NOC_Other_Request"},
        {code: "ZPFSLM48", value:"Demand Letters (Old)"},
        {code: "ZPFSLM49", value:"Fee Invoices (Old)"},
        {code: "ZPFSLM5", value:"DPR"},
        {code: "ZPFSLM50", value:"Credit Notes"},
        {code: "ZPFSLM6", value:"Credit Opinion/CIBIL/Analyst Reports"},
        {code: "ZPFSLM7", value:"Financial Model"},
        {code: "ZPFSLM8", value:"Queries/Emails/Imp Communication"},
        {code: "ZPFSLM9", value:"Miscellaneous Documents"},
    ]

    // communications
    static communications = [
        {code: "0" , value:"Original"},
        {code: "1" , value:"Amended"},
        {code: "2" , value:"Others"}
    ]

    // collateralObjectTypes
    static collateralObjectTypes = [
        {code: "Z00007" , value:"PFS:Personal Guarantee"},
        {code: "Z00008" , value:"PFS:Corporate Guarantee"},
        {code: "Z00009" , value:"PFS:Bank Guarantee"},
        {code: "Z30001" , value:"Securities"},
        {code: "ZCL001" , value:"DSRA Account"},
        {code: "ZCL002" , value:"TRA Account"},
        {code: "ZCL003" , value:"Other Current Assets"},
        {code: "ZDE001" , value:"Plant and Machinery"},
        {code: "ZDE002" , value:"Furniture and Fixtures"},
        {code: "ZDE003" , value:"Office Equipment"},
        {code: "ZDE004" , value:"Other Movables"},
        {code: "ZIN002" , value:"Inventory"},
        {code: "ZOT001" , value:"Project Clearances"},
        {code: "ZOT002" , value:"Project Documents"},
        {code: "ZOT003" , value:"Bank Guarantees under project documents"},
        {code: "ZOT004" , value:"Approvals"},
        {code: "ZOT005" , value:"Claims from Insurances"},
        {code: "ZOT006" , value:"Claims from letter of credit"},
        {code: "ZPA001" , value:"Patents"},
        {code: "ZRE001" , value:"Land"},
        {code: "ZRE002" , value:"Building"}
    ];

    // collateralAgreementTypes
    static collateralAgreementTypes = [
        {objectType: "ZRE001", code: "Z00001", value:"Mortgage"},
        {objectType: "ZRE002", code: "Z00002", value:"Mortgage Registerd"},
        {objectType: "Z30001", code: "Z00003", value:"Pledge Of Securities Account"},
        {objectType: "ZPA001", code: "Z00005", value:"Pledge of Patents/Rights"},
        {objectType: "ZCL001", code: "Z00004", value:"Pledge of Accounts"},
        {objectType: "ZCL002", code: "Z00004", value:"Pledge of Accounts"},
        {objectType: "ZCL003", code: "Z00004", value:"Pledge of Accounts"},
        {objectType: "Z00007", code: "Z00007", value:"Personal Guarantee"},
        {objectType: "Z00008", code: "Z00008", value:"Corporate Guarantee"},
        {objectType: "Z00009", code: "Z00009", value:"Bank Guarantee"},
        {objectType: "ZIN002", code: "Z00011", value:"Transfer Of Rights on Inventory"},
        {objectType: "ZOT001", code: "Z00006", value:"Pledge of Other Valuables"},
        {objectType: "ZOT002", code: "Z00006", value:"Pledge of Other Valuables"},
        {objectType: "ZOT003", code: "Z00006", value:"Pledge of Other Valuables"},
        {objectType: "ZOT004", code: "Z00006", value:"Pledge of Other Valuables"},
        {objectType: "ZOT005", code: "Z00006", value:"Pledge of Other Valuables"},
        {objectType: "ZOT006", code: "Z00011", value:"Transfer Of Rights on Inventory"},
        {objectType: "ZDE001", code: "Z00010", value:"Hypothecation"},
        {objectType: "ZDE002", code: "Z00010", value:"Hypothecation"},
        {objectType: "ZDE003", code: "Z00010", value:"Hypothecation"},
        {objectType: "ZDE004", code: "Z00010", value:"Hypothecation"}
    ];    

    // applicability
    static applicability = [
        {code: "0" , value:"Yes"},
        {code: "1" , value:"No"}
    ];

    // actionPeriods
    static actionPeriods = [
        {code: "1" , value:"Days"},
        {code: "2" , value:"Weeks"},
        {code: "3" , value:"Months"},
        {code: "4" , value:"Years"}
    ];

    // actionPeriodSuffixes
    static actionPeriodSuffixes = [
        {code: "1" , value:"Days"},
        {code: "2" , value:"Weeks"},
        {code: "3" , value:"Months"},
        {code: "4" , value:"Years"}
    ];

    // eventTypes
    static eventTypes = [
        {code: "1" , value:"Enquiry Completion Date"},
        {code: "2" , value:"ICC Clearance Date"},
        {code: "3" , value:"Appraisal Completion Date"},
        {code: "4" , value:"Sanction Letter Issue Date"},
        {code: "5" , value:"Contract Date"},
        {code: "6" , value:"1st Disbursement Date"},
        {code: "7" , value:"Scheduled COD"},
        {code: "8" , value:"Actual COD"},
        {code: "9" , value:"Date of Security"},
        {code: "0" , value:"Others"}
    ];

    // unitOfMeasures
    static unitOfMeasures = [
        {code: "ACR" , value:"Acre"},
        {code: "CM2" , value:"Square centimeter"},
        {code: "FT2" , value:"Square foot"},
        {code: "HA" , value:"Hectare"},
        {code: "KM2" , value:"Square kilometer"},
        {code: "LHK" , value:"Liter per 100 km"},
        {code: "M2" , value:"Square meter"},
        {code: "MI2" , value:"Square mile"},
        {code: "MM2" , value:"Square millimeter"},
        {code: "YD2" , value:"Square Yard"}
    ];

    // particulars
    static particulars = [
        {code: "1" , value:"Reference  Base Rate %"},
        {code: "2" , value:"Base Rate %"},
        {code: "3" , value:"Spread Reset"},
        {code: "4" , value:"Spread %"},
        {code: "5" , value:"Effective Rate of Interest %"},
        {code: "6" , value:"Reset period"},
        {code: "7" , value:"Next Reset Date (Month,Year)"}
    ]

    // financialCovenantsType
    static financialCovenantsType = [
        {code: "1" , value:"Permissible Limit"},
        {code: "2" , value:"Financial Year"}
    ];

    static businessPartnerRoles = [
        {
            "RoleCode": "ZLM001",
            "RoleDescription": "Promoter"
        },
        {
            "RoleCode": "ZLM002",
            "RoleDescription": "Lenders Financial Advisor"
        },
        {
            "RoleCode": "ZLM003",
            "RoleDescription": "Lenders Engineer"
        },
        {
            "RoleCode": "ZLM004",
            "RoleDescription": "Lenders Insurance Advisor"
        },
        {
            "RoleCode": "ZLM005",
            "RoleDescription": "Security Trustee"
        },
        {
            "RoleCode": "ZLM006",
            "RoleDescription": "Legal Counsel"
        },
        {
            "RoleCode": "ZLM007",
            "RoleDescription": "Loan underwriter"
        },
        {
            "RoleCode": "ZLM008",
            "RoleDescription": "Syndicate Partner"
        },
        {
            "RoleCode": "ZLM009",
            "RoleDescription": "Co-Security Trustee"
        },
        {
            "RoleCode": "ZLM010",
            "RoleDescription": "Co-Appraisal Officer"
        },
        {
            "RoleCode": "ZLM011",
            "RoleDescription": "TRA Banker"
        },
        {
            "RoleCode": "ZLM012",
            "RoleDescription": "Consultant"
        },
        {
            "RoleCode": "ZLM013",
            "RoleDescription": "Appraisal Officer"
        },
        {
            "RoleCode": "ZLM014",
            "RoleDescription": "PFS Relationship officer"
        },
        {
            "RoleCode": "ZLM015",
            "RoleDescription": "EPC contractor"
        },
        {
            "RoleCode": "ZLM016",
            "RoleDescription": "Co-Lender"
        },
        {
            "RoleCode": "ZLM017",
            "RoleDescription": "Customer-Empl(Supp&3rd Pa"
        },
        {
            "RoleCode": "ZLM018",
            "RoleDescription": "Nodal Officer-Legal"
        },
        {
            "RoleCode": "ZLM019",
            "RoleDescription": "Nodal Officer-Disb&Recov"
        },
        {
            "RoleCode": "ZLM020",
            "RoleDescription": "Lead Bank"
        },
        {
            "RoleCode": "ZLM021",
            "RoleDescription": "Employee(SuppServ&3rd pa)"
        },
        {
            "RoleCode": "ZLM022",
            "RoleDescription": "Loan DocumentationOfficer"
        },
        {
            "RoleCode": "ZLM023",
            "RoleDescription": "PFS IT Team"
        },
        {
            "RoleCode": "ZLM024",
            "RoleDescription": "Nodal Officer-Monitoring"
        },
        {
            "RoleCode": "TR0100",
            "RoleDescription": "Main Loan Partner"
        },
        {
            "RoleCode": "TR0101",
            "RoleDescription": "Co-Borrower"
        },
        {
            "RoleCode": "TR0110",
            "RoleDescription": "Prospect"
        },
        {
            "RoleCode": "TR0113",
            "RoleDescription": "Credit Standing Check"
        },
        {
            "RoleCode": "TR0115",
            "RoleDescription": "Special Arrangement"
        },
        {
            "RoleCode": "TR0120",
            "RoleDescription": "Cust. Authorized Drawer"
        },
        {
            "RoleCode": "TR0121",
            "RoleDescription": "Other Loan Partner"
        },
        {
            "RoleCode": "TR0150",
            "RoleDescription": "Issuer"
        },
        {
            "RoleCode": "TR0151",
            "RoleDescription": "Counterparty"
        },
        {
            "RoleCode": "TR0152",
            "RoleDescription": "Depository Bank"
        },
        {
            "RoleCode": "TR0153",
            "RoleDescription": "Paying Bank"
        },
        {
            "RoleCode": "TR0154",
            "RoleDescription": "Beneficiary"
        },
        {
            "RoleCode": "TR0160",
            "RoleDescription": "Ultimate Borrower"
        },
        {
            "RoleCode": "TR0200",
            "RoleDescription": "Guarantor"
        },
        {
            "RoleCode": "TR0202",
            "RoleDescription": "Alternative Payer"
        },
        {
            "RoleCode": "TR0203",
            "RoleDescription": "Collector"
        },
        {
            "RoleCode": "TR0600",
            "RoleDescription": "Master Tenant w.Cust.Acct"
        },
        {
            "RoleCode": "TR0601",
            "RoleDescription": "Tenant (w/o Cust.Account)"
        },
        {
            "RoleCode": "TR0602",
            "RoleDescription": "Landlord w.Vendor Account"
        },
        {
            "RoleCode": "TR0603",
            "RoleDescription": "Partner w. Customer Acct"
        },
        {
            "RoleCode": "TR0604",
            "RoleDescription": "Partner w. Vendor Account"
        },
        {
            "RoleCode": "TR0605",
            "RoleDescription": "Owner (Customer)"
        },
        {
            "RoleCode": "TR0606",
            "RoleDescription": "Manager w. Vendor Acct"
        },
        {
            "RoleCode": "TR0610",
            "RoleDescription": "Tenant Ext.Compar.Apartmt"
        },
        {
            "RoleCode": "TR0622",
            "RoleDescription": "Subsidizer"
        },
        {
            "RoleCode": "TR0624",
            "RoleDescription": "Subsidizer (w.Cust.Acct)"
        },
        {
            "RoleCode": "TR0630",
            "RoleDescription": "Court"
        },
        {
            "RoleCode": "TR0635",
            "RoleDescription": "State Central Bank"
        },
        {
            "RoleCode": "TR0636",
            "RoleDescription": "Bank for Rent Deposit"
        },
        {
            "RoleCode": "TR0640",
            "RoleDescription": "Applicant"
        },
        {
            "RoleCode": "TR0641",
            "RoleDescription": "Co-Applicant"
        },
        {
            "RoleCode": "TR0645",
            "RoleDescription": "Authorized Occupant"
        },
        {
            "RoleCode": "TR0646",
            "RoleDescription": "Auth. to Suggest Tenant"
        },
        {
            "RoleCode": "TR0655",
            "RoleDescription": "Guarantor"
        },
        {
            "RoleCode": "TR0700",
            "RoleDescription": "Policyholder"
        },
        {
            "RoleCode": "TR0701",
            "RoleDescription": "Insurer"
        },
        {
            "RoleCode": "TR0702",
            "RoleDescription": "Person Insured"
        },
        {
            "RoleCode": "TR0703",
            "RoleDescription": "Bank"
        },
        {
            "RoleCode": "TR0704",
            "RoleDescription": "Insurance Tracking Vendor"
        },
        {
            "RoleCode": "TR0705",
            "RoleDescription": "Payer"
        },
        {
            "RoleCode": "TR0706",
            "RoleDescription": "Insurance Company Agent"
        },
        {
            "RoleCode": "TR0800",
            "RoleDescription": "Owner"
        },
        {
            "RoleCode": "TR0801",
            "RoleDescription": "Broker"
        },
        {
            "RoleCode": "TR0802",
            "RoleDescription": "Notary"
        },
        {
            "RoleCode": "TR0803",
            "RoleDescription": "Property Developer"
        },
        {
            "RoleCode": "TR0804",
            "RoleDescription": "Primary Contractor"
        },
        {
            "RoleCode": "TR0805",
            "RoleDescription": "Primary Receiver"
        },
        {
            "RoleCode": "TR0806",
            "RoleDescription": "Administrator"
        },
        {
            "RoleCode": "TR0807",
            "RoleDescription": "Technology"
        },
        {
            "RoleCode": "TR0808",
            "RoleDescription": "Janitor"
        },
        {
            "RoleCode": "TR0809",
            "RoleDescription": "Architect"
        },
        {
            "RoleCode": "TR0810",
            "RoleDescription": "Land Registry"
        },
        {
            "RoleCode": "TR0811",
            "RoleDescription": "Legal Owner"
        },
        {
            "RoleCode": "TR0812",
            "RoleDescription": "Land Lease Right Holder"
        },
        {
            "RoleCode": "TR0815",
            "RoleDescription": "Appraiser"
        },
        {
            "RoleCode": "TR0820",
            "RoleDescription": "Survey Office"
        },
        {
            "RoleCode": "TR0821",
            "RoleDescription": "Tax Office"
        },
        {
            "RoleCode": "TR0822",
            "RoleDescription": "Creditor"
        },
        {
            "RoleCode": "TR0823",
            "RoleDescription": "Holder"
        },
        {
            "RoleCode": "TR0824",
            "RoleDescription": "Obligated Party"
        },
        {
            "RoleCode": "TR0825",
            "RoleDescription": "Special Role"
        },
        {
            "RoleCode": "TR0850",
            "RoleDescription": "Corresp. Partner (priv.)"
        },
        {
            "RoleCode": "TR0860",
            "RoleDescription": "Corresp. Partner (Bus.)"
        },
        {
            "RoleCode": "TR0990",
            "RoleDescription": "Loan Contract Officer"
        },
        {
            "RoleCode": "TR0991",
            "RoleDescription": "Loan Rollover Officer"
        },
        {
            "RoleCode": "TR0992",
            "RoleDescription": "Loan Accounting Officer"
        },
        {
            "RoleCode": "TR0995",
            "RoleDescription": "Loans Officer"
        },
        {
            "RoleCode": "TR0997",
            "RoleDescription": "SCS Clerk"
        },
        {
            "RoleCode": "TR0998",
            "RoleDescription": "Loans Administrator"
        }
    ]

    // accountTypes
    public static accountTypes = [
        {code: "TRA Account" , value:"TRA Account"}
    ];

    // months
    public static months = [
        {code: "Jan" , value:"Jan"},
        {code: "Feb" , value:"Feb"},
        {code: "Mar" , value:"Mar"},
        {code: "Apr" , value:"Apr"},
        {code: "May" , value:"May"},
        {code: "Jun" , value:"Jun"},
        {code: "Jul" , value:"Jul"},
        {code: "Aug" , value:"Aug"},
        {code: "Sep" , value:"Sep"},
        {code: "Oct" , value:"Oct"},
        {code: "Nov" , value:"Nov"},
        {code: "Dec" , value:"Dec"}
    ];

    public static functionalStatuses = [
        {code: "01", value:"Enquiry Stage"},
        {code: "02", value:"ICC ApprovalStage"},
        {code: "03", value:"Appraisal Stage"},
        {code: "04", value:"Board Approval Stage"},
        {code: "05", value:"Loan Documentation Stage"},
        {code: "06", value:"Loan Disbursement Stage"},
        {code: "07", value:"Approved"},
        {code: "08", value:"Rejected"}
    ]
}
