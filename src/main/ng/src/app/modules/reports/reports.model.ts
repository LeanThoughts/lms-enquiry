/**
 * Reports app: the catalogue of reports, grouped by module. To add a report: add its route to reports.routes.ts
 * (child of 'reports') and an entry here.
 */
export interface ReportDefinition {
    /** Route below /reports */
    path: string;
    title: string;
    description: string;
    /** Where the data comes from, shown in the list */
    source?: string;
}

export interface ReportModule {
    id: string;
    title: string;
    description: string;
    /** SAP icon of the module tile */
    icon: string;
    reports: ReportDefinition[];
}

export const REPORT_MODULES: ReportModule[] = [
    {
        id: 'loans',
        title: 'Loans Management',
        description: 'Loan contracts, disbursements, repayments and monitoring',
        icon: 'loan',
        reports: [
            {
                path: 'icc-approval',
                title: 'ICC Approval Status Report',
                description: 'Investment Credit Committee and Board approval status of loan proposals by fiscal year, quarter and contract',
                source: 'SAP · ZPFS_LMS_REPORTS_SRV'
            },
            {
                path: 'kyc',
                title: 'Loans KYC Report',
                description: 'KYC and Re-KYC status of loan contracts and business partners: compliance dates, CKYC, PAN, constitution documents and risk category',
                source: 'SAP · ZPFS_LMS_REPORTS_SRV'
            },
            {
                path: 'balance-list',
                title: 'Loans : Balance List',
                description: 'Balances of loan contracts at a key date: sanctioned amount, nominal and effective capital, repayments, interest and overdue amounts, with optional totals',
                source: 'SAP · ZPFS_LMS_REPORTS_SRV'
            },
            {
                path: 'disbursement',
                title: 'Loans : Disbursement – Periodwise and Comparison',
                description: 'Loan disbursements by month, quarter or year for a fiscal year, or compared across fiscal years, for all loans or a selection of contracts, partners and project type',
                source: 'SAP · ZPFS_LMS_REPORTS_SRV'
            }
        ]
    },
    {
        id: 'collaterals',
        title: 'Collaterals',
        description: 'Collateral checklists, security creation and perfection',
        icon: 'shield',
        reports: []
    },
    {
        id: 'business-partner',
        title: 'Business Partner',
        description: 'Borrowers, guarantors, trustees and other business partners',
        icon: 'customer',
        reports: []
    },
    {
        id: 'miscellaneous',
        title: 'Miscellaneous',
        description: 'Workflow, administration and cross-module reports',
        icon: 'folder-blank',
        reports: [
            {
                path: 'workflow-status',
                title: 'Workflow Status Report',
                description: 'Release workflow of loan master data and other release objects: initiator, approver, task, status and dates',
                source: 'SAP · ZPFS_LMS_REPORTS_SRV'
            }
        ]
    }
];

/** Total number of reports in the catalogue. */
export function reportCount(modules: ReportModule[] = REPORT_MODULES): number {
    return modules.reduce((n, m) => n + m.reports.length, 0);
}
