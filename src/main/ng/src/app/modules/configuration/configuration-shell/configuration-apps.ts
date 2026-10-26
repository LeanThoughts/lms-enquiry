/**
 * Apps of the Configuration workspace, grouped by module (the tabs of the workspace).
 * To add an app: add its route to configuration.routes.ts (child of 'configuration') and an entry here.
 */
export interface ConfigurationApp {
    /** Route below /configuration (may have several segments, e.g. bp-tables/titles) */
    path: string;
    title: string;
    description: string;
    /** Heading in the app list of the module */
    group?: string;
}

export interface ConfigurationModule {
    /** Route below /configuration that opens the module when it has no apps yet */
    id: string;
    title: string;
    apps: ConfigurationApp[];
}

export const CONFIGURATION_MODULES: ConfigurationModule[] = [
    {
        id: 'business-partner',
        title: 'Business Partner',
        apps: [
            {
                path: 'bupa-field-status',
                title: 'BP Field Status',
                group: 'Field Status',
                description: 'Display only, optional, mandatory or hidden: the fields of the business partner screens per business partner role'
            },
            {
                path: 'bupa-entity-set-field-status',
                title: 'BP Entity Set Field Status',
                group: 'Field Status',
                description: 'Field status, key field and minimum entries of the entity sets (bank details, identifications, …) per business partner role'
            },
            // Tables filled at startup by the CommandLineRunner configs (pfs.lms.enquiry.businesspartner.config);
            // the keys are those of BpTableDefinitions (backend)
            { path: 'bp-tables/business-partner-roles', title: 'Business Partner Roles', group: 'Roles & Partner Groups',
                description: 'Business partner roles (SAP BP role) and their default partner group' },
            { path: 'bp-tables/partner-groups', title: 'Partner Groups', group: 'Roles & Partner Groups',
                description: 'Business partner groupings and their number ranges' },
            { path: 'bp-tables/role-partner-groups', title: 'Role Partner Groups', group: 'Roles & Partner Groups',
                description: 'Partner groups allowed for each business partner role' },
            { path: 'bp-tables/role-customer-field-values', title: 'Role Customer Field Values', group: 'Roles & Partner Groups',
                description: 'Default customer data (reconciliation account, payment, dunning, …) per role and partner group' },
            { path: 'bp-tables/business-partner-types', title: 'Business Partner Types', group: 'General Data',
                description: 'Business partner categories (person, organization, group)' },
            { path: 'bp-tables/titles', title: 'Titles', group: 'General Data',
                description: 'Form of address of business partners' },
            { path: 'bp-tables/legal-forms', title: 'Legal Forms', group: 'General Data',
                description: 'Legal forms of organizations' },
            { path: 'bp-tables/legal-entities', title: 'Legal Entities', group: 'General Data',
                description: 'Legal entity types of business partners' },
            { path: 'bp-tables/country-codes', title: 'Country Codes', group: 'General Data',
                description: 'Countries of addresses and bank details' },
            { path: 'bp-tables/identification-categories', title: 'Identification Categories', group: 'Identification & Documents',
                description: 'Identification types (PAN, TAN, CIN, …) and their checks' },
            { path: 'bp-tables/document-types', title: 'Document Types', group: 'Identification & Documents',
                description: 'Types of documents uploaded for business partners and loans' },
            { path: 'bp-tables/industry-systems', title: 'Industry Systems', group: 'Industry & Rating',
                description: 'Industry classification systems' },
            { path: 'bp-tables/industry-types', title: 'Industry Types', group: 'Industry & Rating',
                description: 'Industries per industry system' },
            { path: 'bp-tables/credit-rating-agencies', title: 'Credit Rating Agencies', group: 'Industry & Rating',
                description: 'Agencies that rate business partners' },
            { path: 'bp-tables/credit-rating-codes', title: 'Credit Rating Codes', group: 'Industry & Rating',
                description: 'Credit ratings' },
            { path: 'bp-tables/sanction-authorities', title: 'Sanction Authorities', group: 'Industry & Rating',
                description: 'Authorities that sanction loans' },
            { path: 'bp-tables/amendment-reasons', title: 'Amendment Reasons', group: 'Industry & Rating',
                description: 'Reasons for amendments of sanctioned loans' },
            { path: 'bp-tables/house-banks', title: 'House Banks', group: 'Payment & Banking',
                description: 'Banks of the company used for payments' },
            { path: 'bp-tables/payment-methods', title: 'Payment Methods', group: 'Payment & Banking',
                description: 'Payment methods of customers and vendors' },
            { path: 'bp-tables/payment-terms', title: 'Payment Terms', group: 'Payment & Banking',
                description: 'Terms of payment' },
            { path: 'bp-tables/dunning-procedures', title: 'Dunning Procedures', group: 'Payment & Banking',
                description: 'Dunning procedures of customers' },
            { path: 'bp-tables/planning-groups', title: 'Planning Groups', group: 'Payment & Banking',
                description: 'Cash management planning groups' },
            { path: 'bp-tables/sort-keys', title: 'Sort Keys', group: 'Payment & Banking',
                description: 'Sort keys (assignment field) of customer accounts' }
        ]
    },
    {
        id: 'loans',
        title: 'Loans',
        apps: [
            // Tables filled at startup by the CommandLineRunners in pfs.lms.enquiry.configInitializer; the keys are
            // those of LoanTableDefinitions (backend)
            { path: 'loan-tables/loan-classes', title: 'Loan Classes', group: 'Loan Classification',
                description: 'Classes (sectors) of loans' },
            { path: 'loan-tables/loan-types', title: 'Loan Types', group: 'Loan Classification',
                description: 'Term loan, non fund based, corporate loan' },
            { path: 'loan-tables/products', title: 'Products', group: 'Loan Classification',
                description: 'Loan products (SAP product type)' },
            { path: 'loan-tables/project-types', title: 'Project Types', group: 'Loan Classification',
                description: 'Types of projects financed' },
            { path: 'loan-tables/project-type-core-sectors', title: 'Project Type Core Sectors', group: 'Loan Classification',
                description: 'Infrastructure or non-infrastructure' },
            { path: 'loan-tables/industry-sectors', title: 'Industry Sectors', group: 'Loan Classification',
                description: 'Industry sectors of the projects' },
            { path: 'loan-tables/purposes-of-loan', title: 'Purposes of Loan', group: 'Loan Classification',
                description: 'Purposes of the loans (term loan, refinancing, LOC, …)' },
            { path: 'loan-tables/financing-types', title: 'Financing Types', group: 'Loan Classification',
                description: 'Sole lending, multiple banking, consortium' },
            { path: 'loan-tables/assistance-types', title: 'Assistance Types', group: 'Loan Classification',
                description: 'Debt, equity, NCD, CCD, sub debt' },
            { path: 'loan-tables/proposal-types', title: 'Proposal Types', group: 'Loan Classification',
                description: 'Lead, sole, consortium' },
            { path: 'loan-tables/units-of-measure', title: 'Units of Measure', group: 'Loan Classification',
                description: 'Units of the project capacity (MW, KM, …)' },
            { path: 'loan-tables/icc-readiness-statuses', title: 'ICC Readiness Statuses', group: 'ICC, Sanction & Rejection',
                description: 'Readiness of a loan for the ICC' },
            { path: 'loan-tables/presented-in-icc', title: 'Presented in ICC', group: 'ICC, Sanction & Rejection',
                description: 'Whether a loan was presented in the ICC' },
            { path: 'loan-tables/icc-statuses', title: 'ICC Statuses', group: 'ICC, Sanction & Rejection',
                description: 'Outcome of the ICC meeting' },
            { path: 'loan-tables/sanction-types', title: 'Sanction Types', group: 'ICC, Sanction & Rejection',
                description: 'Original sanction and its amendments' },
            { path: 'loan-tables/rejection-categories', title: 'Rejection Categories', group: 'ICC, Sanction & Rejection',
                description: 'Who rejected an enquiry' },
            { path: 'loan-tables/customer-rejection-reasons', title: 'Customer Rejection Reasons', group: 'ICC, Sanction & Rejection',
                description: 'Reasons for the rejection by the customer' },
            { path: 'loan-tables/functional-statuses', title: 'Functional Statuses', group: 'ICC, Sanction & Rejection',
                description: 'Functional stages of a loan (enquiry to monitoring)' },
            { path: 'loan-tables/fee-types', title: 'Fee Types', group: 'Fees, Interest & Accounts',
                description: 'Fees charged to the borrower' },
            { path: 'loan-tables/tra-account-types', title: 'TRA Account Types', group: 'Fees, Interest & Accounts',
                description: 'Accounts of the trust and retention account' },
            { path: 'loan-tables/external-rating-types', title: 'External Rating Types', group: 'Fees, Interest & Accounts',
                description: 'Types of external ratings' },
            { path: 'loan-tables/payment-forms', title: 'Payment Forms', group: 'Fees, Interest & Accounts',
                description: 'Payment forms of interest and repayment' },
            { path: 'loan-tables/condition-types', title: 'Condition Types', group: 'Fees, Interest & Accounts',
                description: 'Condition types of the loan conditions' },
            { path: 'loan-tables/interest-calculation-methods', title: 'Interest Calculation Methods', group: 'Fees, Interest & Accounts',
                description: 'Day count methods' },
            { path: 'loan-tables/reference-interest-signs', title: 'Reference Interest Signs', group: 'Fees, Interest & Accounts',
                description: 'Signs of the spread on the reference interest rate' },
            { path: 'loan-tables/documentation-types', title: 'Documentation Types', group: 'Documentation',
                description: 'Types of loan documentation' },
            { path: 'loan-tables/documentation-statuses', title: 'Documentation Statuses', group: 'Documentation',
                description: 'Statuses of the loan documentation' },
            { path: 'loan-tables/departments', title: 'Departments', group: 'Organization & Geography',
                description: 'Departments of PFS' },
            { path: 'loan-tables/countries', title: 'Countries', group: 'Organization & Geography',
                description: 'Countries of projects and addresses' },
            { path: 'loan-tables/regions', title: 'Regions', group: 'Organization & Geography',
                description: 'Regions (states) per country' },
            { path: 'loan-tables/states', title: 'States', group: 'Organization & Geography',
                description: 'States of India' },
            { path: 'loan-tables/user-roles', title: 'User Roles', group: 'Users & Authorizations',
                description: 'Roles of the portal users' },
            { path: 'loan-tables/authorization-objects', title: 'Authorization Objects', group: 'Users & Authorizations',
                description: 'Activities that need an authorization' },
            { path: 'loan-tables/authorization-access', title: 'Authorization Access', group: 'Users & Authorizations',
                description: 'Authorization objects allowed per user role' },
            { path: 'loan-tables/portal-common-config', title: 'Portal Common Configuration', group: 'System',
                description: 'BD team e-mail address per SAP system' }
        ]
    },
    {
        id: 'collaterals',
        title: 'Collaterals',
        apps: [
            {
                path: 'checklist-id-number-range',
                title: 'Checklist ID Number Range',
                description: 'Numbering of the Checklist ID No. (SAP ZID_NO) of new collaterals'
            }
        ]
    },
    {
        id: 'general',
        title: 'General Config',
        apps: []
    }
];
