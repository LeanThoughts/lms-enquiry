// Functional status codes, in the order of the loan life cycle
export const FUNCTIONAL_STAGES: { code: number, label: string }[] = [
    { code: 1, label: 'Enquiry' },
    { code: 2, label: 'ICC In-Principle Approval' },
    { code: 10, label: 'Prelim Risk Assessment' },
    { code: 11, label: 'Application Fee' },
    { code: 3, label: 'Appraisal' },
    { code: 12, label: 'BMC Approval' },
    { code: 4, label: 'Board Approval' },
    { code: 5, label: 'Sanction' },
    { code: 6, label: 'Documentation' },
    { code: 8, label: 'Loan Monitoring' },
    { code: 9, label: 'Recovery' }
];
