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
        apps: []
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
