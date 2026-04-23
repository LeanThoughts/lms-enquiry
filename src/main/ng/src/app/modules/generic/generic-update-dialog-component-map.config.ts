import { 
    EMAIL_REGEX, 
    FIFTEEN_COMMA_TWO, 
    FIVE_COMMA_TWO, 
    MOBILE_NUMBER_REGEX, 
    NUMERIC_ONLY_REGEX, 
    PHONE_NUMBER_REGEX, 
    SHARE_HOLDING_PERCENTAGE_REGEX 
} from "../../common/common.regex";
import { BusinessPartnerSearchService } from "../business-partner-search/business-partner-search.service";
import { ProcessEnquiryService } from "../loan-contract-search/functional-stage/process-enquiry/process-enquiry.service";
import { ProjectProposalService } from "../loan-contract-search/functional-stage/process-enquiry/project-proposal/project-proposal.service";
import { IccInprincipleApprovalService } from "../loan-contract-search/functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service";

interface EntityUpdateDialogComponentConfig {
    // routeResolvedData?: string[],
    searchString1ForCreate?: string;
    passSearchString1Via?: string;
    searchString2ForCreate?: string;
    passSearchString2Via?: string;
    createFunction: any;
    updateFunction?: any;
    createDialogTitle: string;
    updateDialogTitle?: string;
    viewDialogTitle?: string;
    createSuccessMessage: string;
    updateSuccessMessage?: string;
    trackObjectAfterCreateAndUpdate?: string;
    fieldsConfig: {
        // title?: string;
        row: number;
        span: number;
        name: string;
        label: string;
        type: string;
        nullOption?: boolean;
        required?: any;
        readOnly?: boolean;
        maxLength?: number;
        pattern?: RegExp;
        options?: any[];
        displayFunction?: any; // Function to display the value of the combobox field or to get options for the dependent select field
        itemTemplate?: string;
        displayKey?: string;
        valueKey?: string;
        defaultValue?: any;
        dependsOn?: string; // Name of the field that the dependent select field depends on
        viewOperationKey?: string; // Key to be used to display the value in the view mode
        maxValue?: any; // Maximum value for numeric or date fields
    }[];
}

// Entity-Component configuration
export const entityComponentConfigs: { [key: string]: EntityUpdateDialogComponentConfig } = 
{
    // Business Partner - Financials
    businessPartnerFinancials: {
        searchString1ForCreate: 'businessPartnerId',
        passSearchString1Via: 'Query',
        createDialogTitle: 'Add New Financial Details',
        createFunction: BusinessPartnerSearchService.prototype.createBusinessPartnerFinancials,
        createSuccessMessage: 'Financial details created successfully',
        updateDialogTitle: 'Update Financial Details',
        updateFunction: BusinessPartnerSearchService.prototype.updateBusinessPartnerFinancials,
        updateSuccessMessage: 'Financial details updated successfully',
        viewDialogTitle: 'View Financial Details',
        trackObjectAfterCreateAndUpdate: 'partner',
        fieldsConfig: [
            { row: 1, span: 3, name: 'fiscalYear', label: 'Fiscal Year', type: 'text', required: true, maxLength: 4 },

            { row: 2, span: 12, name: 'header1', type: 'header', label: 'Profit & Loss' },
            { row: 3, span: 3, name: 'revenue', label: 'Revenue', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'netCashAccruals', label: 'Net Cash Accruals', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'depreciation', label: 'Depreciation', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'ebitda', label: 'EBITDA', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'pbt', label: 'Profit Before Tax', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'pat', label: 'Profit After Tax', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'interestExpenses', label: 'Interest Expenses', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },

            { row: 5, span: 12, name: 'header2', type: 'header', label: 'Balance Sheet' },
            { row: 6, span: 3, name: 'wcstDebt', label: 'WC/ST Debt', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 6, span: 3, name: 'ltDebt', label: 'LT Debt (excl CPLTD)', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 6, span: 3, name: 'totalOutstandingLiabilities', label: 'Total Outstanding Liabilities', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 6, span: 3, name: 'reservesAndSurplus', label: 'Reserves and Surplus', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'adjTangibleNetWorth', label: 'Adjusted Tangible Net Worth', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'currentAssets', label: 'Current Assets', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'invInSubAsso', label: 'Inv. in Sub/Asso.', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'fccbQuasiEquity', label: 'FCCB/Quasi Equity', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'cpltd', label: 'CPLTD', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'totalDebt', label: 'Total Debt', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'shareCapital', label: 'Share Capital', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'tangibleNetWorth', label: 'Tangible Net Worth', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 9, span: 3, name: 'cashAndBankBalance', label: 'Cash and Bank Balance', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 9, span: 3, name: 'currentLiabilities', label: 'Current Liabilities', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 9, span: 3, name: 'netFixedAssets', label: 'Net Fixed Assets', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },

            { row: 10, span: 12, name: 'header2', type: 'header', label: 'Financial Ratios' },
            { row: 11, span: 3, name: 'ebitdaMarginPercentage', label: 'EBITDA Margin %', type: 'number', maxLength: 5, pattern: FIVE_COMMA_TWO },
            { row: 11, span: 3, name: 'ebitdaInterest', label: 'EBITDA/ Interest', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 11, span: 3, name: 'cashDSCR', label: 'Cash DSCR', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 11, span: 3, name: 'totalDebtEbitda', label: 'Total Debt/ EBITDA', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 12, span: 3, name: 'termDebtEbitda', label: 'Term Debt/ EBITDA', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 12, span: 3, name: 'dscr', label: 'DSCR', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 12, span: 3, name: 'totalDebtTnw', label: 'Total Debt/ TNW', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 12, span: 3, name: 'tolTnw', label: 'TOL/TNW', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
        ],
    },

    // Business Partner - Roles
    businessPartnerRoles: {
        searchString1ForCreate: 'businessPartnerId',
        passSearchString1Via: 'Object',
        createDialogTitle: 'Add New Role to Business Partner',
        createFunction: BusinessPartnerSearchService.prototype.createBusinessPartnerRole,
        createSuccessMessage: 'Added role to business partner successfully',
        trackObjectAfterCreateAndUpdate: 'partner',
        fieldsConfig: [
            { row: 1, span: 12, name: 'roleTypeId', label: 'Select Business Partner Role', type: 'select', required: true, displayKey: 'value', valueKey: 'id' }
        ],

    },

    // Business Partner Loan Contacts
    businessPartnerLoanContacts: {
        searchString1ForCreate: 'businessPartnerId',
        passSearchString1Via: 'Query', // Pass businessPartnerId as a query parameter to the create function
        createFunction: BusinessPartnerSearchService.prototype.createBusinessPartnerContactDetails,
        updateFunction: BusinessPartnerSearchService.prototype.updateBusinessPartnerContactDetails,
        createDialogTitle: 'Add New Contact',
        updateDialogTitle: 'Update Contact',
        viewDialogTitle: 'View Contact',
        createSuccessMessage: 'Contact details created successfully',
        updateSuccessMessage: 'Contact details updated successfully',
        trackObjectAfterCreateAndUpdate: 'partner',
        fieldsConfig: [
            { row: 1, span: 6, name: 'serialNumber', label: 'Serial Number', type: 'number', readOnly: true },
            { row: 1, span: 6, name: 'loanNumber', label: 'Loan Number', type: 'text', maxLength: 10},
            { row: 2, span: 12, name: 'name', label: 'Contact Person Name', type: 'text', required: true, maxLength: 40},
            { row: 3, span: 6, name: 'branchAddress', label: 'Branch Address', type: 'text', maxLength: 80},
            { row: 3, span: 6, name: 'designation', label: 'Designation', type: 'text', maxLength: 40},
            { row: 4, span: 6, name: 'department', label: 'Department', type: 'text', maxLength: 40},
            { row: 4, span: 6, name: 'telephoneNumber', label: 'Mobile Number', type: 'text', maxLength: 20, pattern: MOBILE_NUMBER_REGEX},
            { row: 5, span: 6, name: 'landLineNumber', label: 'Landline/ Contact Number', type: 'text', maxLength: 20, pattern: PHONE_NUMBER_REGEX},
            { row: 5, span: 6, name: 'faxNumber', label: 'Fax Number', type: 'text', maxLength: 20, pattern: PHONE_NUMBER_REGEX},
            { row: 6, span: 6, name: 'email', label: 'Email', type: 'text', maxLength: 240, pattern: EMAIL_REGEX},
        ]
    },

    // Business Partner - Bank Details
    // businessPartnerBankDetails: {
    //     searchString1ForCreate: 'businessPartnerId',
    //     passSearchString1Via: 'Query',
    //     createDialogTitle: 'Add New Bank Details',
    //     updateDialogTitle: 'Update Bank Details',
    //     viewDialogTitle: 'View Bank Details',
    //     createFunction: BusinessPartnerSearchService.prototype.createBusinessPartnerBankDetails,
    //     updateFunction: BusinessPartnerSearchService.prototype.updateBusinessPartnerBankDetails,
    //     createSuccessMessage: 'Bank details created successfully',
    //     updateSuccessMessage: 'Bank details updated successfully',
    //     trackObjectAfterCreateAndUpdate: 'partner',
    //     fieldsConfig: [
    //         { row: 1, span: 6, name: 'bankKey', label: 'Select Bank', type: 'combobox', required: true,
    //             displayFunction: (item: any): string => item?.bankKey ?? '' },
    //         { row: 1, span: 6, name: 'bankName', label: 'Bank Name', type: 'text', maxLength: 40, required: true },
    //         { row: 1, span: 6, name: 'ifscCode', label: 'IFSC Code', type: 'text', maxLength: 15 },
    //         { row: 1, span: 6, name: 'accountNumber', label: 'Account Number', type: 'text', required: true, maxLength: 18 },
    //         { row: 1, span: 6, name: 'entryDate', label: 'Entry Date', type: 'date' },
    //         { row: 1, span: 6, name: 'validFromDate', label: 'Valid From', type: 'date' },
    //         { row: 1, span: 6, name: 'validToDate', label: 'Valid To', type: 'date' },
    //         { row: 1, span: 6, name: 'bankCountry', label: 'Bank Country', type: 'select', displayKey: 'value', valueKey: 'code', defaultValue: 'India', 
    //             required: true },
    //         { row: 1, span: 6, name: 'referenceNumber', label: 'Reference Details', type: 'text', maxLength: 20 },
    //         { row: 1, span: 6, name: 'accountHolderName', label: 'Account Holder Name', type: 'text', required: true, maxLength: 60 },
    //         { row: 1, span: 6, name: 'bankAccountName', label: 'Account Name', type: 'text', maxLength: 40 },
    //     ]
    // },

    // Business Partner - Industry Details
    businessPartnerIndustryDetails: {
        searchString1ForCreate: 'businessPartnerId',
        passSearchString1Via: 'Query',
        createDialogTitle: 'Add New Industry Details',
        updateDialogTitle: 'Update Industry Details',
        viewDialogTitle: 'View Industry Details',
        createFunction: BusinessPartnerSearchService.prototype.createBusinessPartnerIndustryDetails,
        updateFunction: BusinessPartnerSearchService.prototype.updateBusinessPartnerIndustryDetails,
        createSuccessMessage: 'Industry details created successfully',
        updateSuccessMessage: 'Industry details updated successfully',
        trackObjectAfterCreateAndUpdate: 'partner',
        fieldsConfig: [
            {row: 1, span: 12, name: 'industrySystemId', label: 'Industry System', type: 'select', required: true, displayKey: 'value', valueKey: 'id', 
                viewOperationKey: 'industrySystem' },
            {row: 2, span: 12, name: 'industryTypeId', label: 'Industry', type: 'select', required: true, displayKey: 'value', valueKey: 'id', 
                dependsOn: 'industrySystemId', displayFunction: BusinessPartnerSearchService.prototype.getIndustryTypes, viewOperationKey: 'industryType' },
        ]
    },

    // Process Enquiry - Rejected By PFS
    processEnquiryRejectedByPFS: {
        searchString1ForCreate: 'enquiryActionId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Rejected By PFS Details',
        updateDialogTitle: 'Update Rejected By PFS Details',
        viewDialogTitle: 'View Rejected By PFS Details',
        createFunction: ProcessEnquiryService.prototype.createRejectedByPFS,
        updateFunction: ProcessEnquiryService.prototype.updateRejectedByPFS,
        createSuccessMessage: 'Rejected by PFS details created successfully',
        updateSuccessMessage: 'Rejected by PFS details updated successfully',
        trackObjectAfterCreateAndUpdate: 'enquiryAction',
        fieldsConfig: [
            {row: 1, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'select', required: true, displayKey: 'value', valueKey: 'code',
                viewOperationKey: 'categoryDescription' },
            {row: 1, span: 12, name: 'rejectionReason', label: 'Rejection Reason By PFS', type: 'text', required: true, maxLength: 200},
            {row: 1, span: 12, name: 'rejectionDate', label: 'Rejection Date', type: 'date', required: true},
        ]
    },

    // Process Enquiry - Other Details
    processEnquiryOtherDetails: {
        searchString1ForCreate: 'enquiryActionId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Other Details',
        updateDialogTitle: 'Update Other Details',
        viewDialogTitle: 'View Other Details',
        createFunction: ProcessEnquiryService.prototype.createOtherDetails,
        updateFunction: ProcessEnquiryService.prototype.updateOtherDetails,
        createSuccessMessage: 'Other details created successfully',
        updateSuccessMessage: 'Other details updated successfully',
        trackObjectAfterCreateAndUpdate: 'enquiryAction',
        fieldsConfig: [
            {row: 1, span: 6, name: 'nameOfSourcingCompany', label: 'Name of Sourcing Company', type: 'text', required: true, maxLength: 100},
            {row: 1, span: 6, name: 'rating', label: 'Rating', type: 'select', displayKey: 'value', valueKey: 'code'},
            {row: 2, span: 6, name: 'contactPersonName', label: 'Contact Person', type: 'text', maxLength: 100},
            {row: 2, span: 6, name: 'creditStanding', label: 'Credit Standing', type: 'select', displayKey: 'value', valueKey: 'code'},
            {row: 3, span: 6, name: 'contactNumber', label: 'Contact Number', type: 'text', maxLength: 20},
            {row: 3, span: 6, name: 'creditStandingInstruction', label: 'Credit Standing Instruction', type: 'text', maxLength: 100},
            {row: 4, span: 6, name: 'email', label: 'Email', type: 'text', maxLength: 10},
            {row: 4, span: 6, name: 'creditStandingText', label: 'Credit Standing Text', type: 'text', maxLength: 100},
            {row: 5, span: 6, name: 'enquiryDate', label: 'Enquiry Date', type: 'date'},
            {row: 5, span: 6, name: 'ratingDate', label: 'Rating Date', type: 'date'},
        ]
    },

    // Process Enquiry - Reason For Delay
    processEnquiryReasonForDelay: {
        searchString1ForCreate: 'enquiryActionId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Reason For Delay',
        updateDialogTitle: 'Update Reason For Delay',
        viewDialogTitle: 'View Reason For Delay',
        createFunction: ProcessEnquiryService.prototype.createReasonForDelay,
        updateFunction: ProcessEnquiryService.prototype.updateReasonForDelay,
        createSuccessMessage: 'Reason for delay created successfully',
        updateSuccessMessage: 'Reason for delay updated successfully',
        trackObjectAfterCreateAndUpdate: 'enquiryAction',
        fieldsConfig: [
            {row: 1, span: 12, name: 'reason', label: 'Reason For Delay', type: 'text', required: true, maxLength: 200},
            {row: 1, span: 12, name: 'date', label: 'Date', type: 'date', required: true},
        ],
    },    

    // Process Enquiry - Enquiry Completion Details
    processEnquiryEnquiryCompletion: {
        searchString1ForCreate: 'enquiryActionId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Enquiry Completion Details',
        updateDialogTitle: 'Update Enquiry Completion Details',
        viewDialogTitle: 'View Enquiry Completion Details',
        createFunction: ProcessEnquiryService.prototype.createEnquiryCompletionDetails,
        updateFunction: ProcessEnquiryService.prototype.updateEnquiryCompletionDetails,
        createSuccessMessage: 'Enquiry completion details created successfully',
        updateSuccessMessage: 'Enquiry completion details updated successfully',
        trackObjectAfterCreateAndUpdate: 'enquiryAction',
        fieldsConfig: [
            {row: 1, span: 12, name: 'productType', label: 'Product Type', type: 'select', required: true, displayKey: 'name', valueKey: 'code',
                viewOperationKey: 'productTypeDescription' },
            {row: 1, span: 12, name: 'term', label: 'Term', type: 'select', required: true, displayKey: 'value', valueKey: 'code', 
                viewOperationKey: 'termDescription' },
            {row: 1, span: 12, name: 'date', label: 'Enquiry Completion Date', type: 'date', required: true},
            {row: 1, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200},
        ]
    },

    // Process Enquiry - Rejected By Customer
    processEnquiryRejectedByCustomer: {
        searchString1ForCreate: 'enquiryActionId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Rejected By Customer Details',
        updateDialogTitle: 'Update Rejected By Customer Details',
        viewDialogTitle: 'View Rejected By Customer Details',
        createFunction: ProcessEnquiryService.prototype.createRejectedByCustomer,
        updateFunction: ProcessEnquiryService.prototype.updateRejectedByCustomer,
        createSuccessMessage: 'Rejected by customer details created successfully',
        updateSuccessMessage: 'Rejected by customer details updated successfully',
        trackObjectAfterCreateAndUpdate: 'enquiryAction',
        fieldsConfig: [
            {row: 1, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'select', required: true, displayKey: 'value', valueKey: 'code',
                viewOperationKey: 'categoryDescription' },
            {row: 1, span: 12, name: 'rejectionReason', label: 'Rejection Reason By Customer', type: 'text', required: true, maxLength: 200},
            {row: 1, span: 12, name: 'rejectionDate', label: 'Rejection Date', type: 'date', required: true},
        ]
    },

    // Project Proposal - Credit Rating
    projectProposalCreditRating: {
        searchString1ForCreate: 'projectProposalId',
        passSearchString1Via: 'Object',
        createDialogTitle: 'Add Credit Rating Details',
        updateDialogTitle: 'Update Credit Rating Details',
        viewDialogTitle: 'View Credit Rating Details',
        createFunction: ProjectProposalService.prototype.createProjectProposalCreditRating,
        updateFunction: ProjectProposalService.prototype.updateProjectProposalCreditRating,
        createSuccessMessage: 'Credit rating details created successfully',
        updateSuccessMessage: 'Credit rating details updated successfully',
        trackObjectAfterCreateAndUpdate: 'projectProposal',
        fieldsConfig: [
            {row: 1, span: 12, name: 'creditRating', label: 'Credit Rating', type: 'select', required: true, displayKey: 'value', valueKey: 'code',
                viewOperationKey: 'creditRatingDescription' },
            {row: 1, span: 12, name: 'creditRatingAgency', label: 'Credit Rating Agency', type: 'select', displayKey: 'value', valueKey: 'code',
                viewOperationKey: 'creditRatingAgencyDescription' },
            {row: 1, span: 12, name: 'creditStandingInstruction', label: 'Credit Standing Instruction', type: 'text', maxLength: 100},
            {row: 1, span: 12, name: 'creditStandingText', label: 'Credit Standing Text', type: 'text', maxLength: 100},
        ]
    },

    // Project Proposal - Share Holding
    projectProposalShareHolding: {
        searchString1ForCreate: 'projectProposalId',
        passSearchString1Via: 'Object',
        createDialogTitle: 'Add Share Holding Details',
        updateDialogTitle: 'Update Share Holding Details',
        viewDialogTitle: 'View Share Holding Details',
        createFunction: ProjectProposalService.prototype.createProjectProposalShareHolder,
        updateFunction: ProjectProposalService.prototype.updateProjectProposalShareHolder,
        createSuccessMessage: 'Share holding details created successfully',
        updateSuccessMessage: 'Share holding details updated successfully',
        trackObjectAfterCreateAndUpdate: 'projectProposal',
        fieldsConfig: [
            {row: 1, span: 12, name: 'companyName', label: 'Company Name', type: 'text', maxLength: 200, required: true},
            {row: 2, span: 12, name: 'equityCapital', label: 'Capital (Crores)', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO},
            {row: 3, span: 12, name: 'percentageHolding', label: 'Percentage Holding', type: 'number', maxLength: 6, pattern: SHARE_HOLDING_PERCENTAGE_REGEX},
        ]
    },

    // Project Proposal - Other Loan Details Documents
    projectProposalOtherLoanDetailsDocuments: {
        searchString1ForCreate: 'projectProposalId',
        passSearchString1Via: 'Object',
        createDialogTitle: 'Add Document',
        updateDialogTitle: 'Update Document',
        viewDialogTitle: 'View Document',
        createFunction: ProjectProposalService.prototype.createOtherLoanDetailsDocument,
        updateFunction: ProjectProposalService.prototype.updateOtherLoanDetailsDocument,
        createSuccessMessage: 'Document created successfully',
        updateSuccessMessage: 'Document updated successfully',
        trackObjectAfterCreateAndUpdate: 'projectProposal',
        fieldsConfig: [
            {row: 2, span: 12, name: 'documentType', label: 'Document Type', type: 'select', displayKey: 'description', valueKey: 'code', 
                viewOperationKey: 'documentTypeName', required: true },
            {row: 2, span: 12, name: 'documentName', label: 'Document Name', type: 'text', maxLength: 100, required: true},
            {row: 3, span: 12, name: 'file', label: 'Select file to upload', type: 'file', required: false },
        ]
    },

    // Project Proposal - Promoter Financials
    projectProposalPromoterFinancials: {
        searchString1ForCreate: 'projectProposalId',
        passSearchString1Via: 'Object',
        createDialogTitle: 'Add Promoter Financial Details',
        updateDialogTitle: 'Update Promoter Financial Details',
        viewDialogTitle: 'View Promoter Financial Details',
        createFunction: ProjectProposalService.prototype.createPromoterFinancials,
        updateFunction: ProjectProposalService.prototype.updatePromoterFinancials,
        createSuccessMessage: 'Promoter financial details created successfully',
        updateSuccessMessage: 'Promoter financial details updated successfully',
        trackObjectAfterCreateAndUpdate: 'projectProposal',
        fieldsConfig: [
            // Section: Financials of Previous Fiscal Periods
            { row: 1, span: 12, name: 'header1', type: 'header', label: 'Financials of Previous Fiscal Periods' },
            { row: 2, span: 3, name: 'fiscalPeriod', label: 'Fiscal Period', type: 'text', maxLength: 10, required: true },

            // Section: Profit & Loss
            { row: 3, span: 12, name: 'header2', type: 'header', label: 'Profit & Loss' },
            { row: 3, span: 3, name: 'revenue', label: 'Revenue', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'depreciation', label: 'Depreciation', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'pbt', label: 'PBT (after exceptionals)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'netCashAccruals', label: 'Net Cash Accruals', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'ebitda', label: 'EBITDA', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'interestExpense', label: 'Interest Expense', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 4, span: 3, name: 'pat', label: 'PAT', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },

            // Section: Balance Sheet
            { row: 5, span: 12, name: 'header3', type: 'header', label: 'Balance Sheet' },
            { row: 6, span: 3, name: 'wcDebt', label: 'WC/ST Debt', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 6, span: 3, name: 'totalOutstandingLiabilities', label: 'Total Outstanding Liabilities (TOL)', type: 'text', maxLength: 18, 
                pattern: FIFTEEN_COMMA_TWO },
            { row: 6, span: 3, name: 'adjustedTangibleNetWorth', label: 'Adjusted Tangible Net Worth (ATNW)', type: 'text', maxLength: 18, 
                pattern: FIFTEEN_COMMA_TWO },
            { row: 6, span: 3, name: 'subAsso', label: 'Inv. in Sub/Asso.', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'cpltd', label: 'CPLTD (Current Portion of Long Term Debt)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'shareCapital', label: 'Share Capital', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'cashAndBankBalance', label: 'Cash and Bank Balance', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 7, span: 3, name: 'netFixedAssets', label: 'Net Fixed Assets(inc cwip)', type: 'text', maxLength: 18 },
            { row: 8, span: 3, name: 'ltDebt', label: 'LT Debt (excl CPLTD)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'reservesAndSurplus', label: 'Reserves and Surplus', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'currentAssets', label: 'Current Assets', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 8, span: 3, name: 'quasiEquity', label: 'FCCB/Quasi Equity', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 9, span: 3, name: 'totalDebt', label: 'Total Debt', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 9, span: 3, name: 'tangibleNetWorth', label: 'Tangible Net Worth(TNW)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 9, span: 3, name: 'currentLiabilities', label: 'Current Liabilities (incl. CPLTD)', type: 'text', maxLength: 18, 
                pattern: FIFTEEN_COMMA_TWO },

            // Section: Ratios
            { row: 10, span: 12, name: 'header4', type: 'header', label: 'Ratios' },
            { row: 11, span: 3, name: 'ebitdaMarginPercentage', label: 'EBITDA Margin %', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 11, span: 3, name: 'totalDebtEbitda', label: 'Total Debt/ EBITDA', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 11, span: 3, name: 'totalDebtTnw', label: 'Total Debt/ TNW', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 11, span: 3, name: 'currentRatio', label: 'Current Ratio', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 12, span: 3, name: 'ebitdaInterest', label: 'EBITDA/ Interest', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 12, span: 3, name: 'termDebtEbitda', label: 'Term Debt/ EBITDA', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 12, span: 3, name: 'tnw', label: 'TOL/TNW', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 13, span: 3, name: 'cashDscr', label: 'Cash DSCR', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
            { row: 13, span: 3, name: 'dscr', label: 'DSCR', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO }
        ]
    },

    // Project Proposal - Collateral Details
    projectProposalCollateralDetails: {
        searchString1ForCreate: 'projectProposalId',
        passSearchString1Via: 'Object',
        createDialogTitle: 'Add Collateral Details',
        updateDialogTitle: 'Update Collateral Details',
        viewDialogTitle: 'View Collateral Details',
        createFunction: ProjectProposalService.prototype.createCollateralDetails,
        updateFunction: ProjectProposalService.prototype.updateCollateralDetails,
        createSuccessMessage: 'Collateral details created successfully',
        updateSuccessMessage: 'Collateral details updated successfully',
        trackObjectAfterCreateAndUpdate: 'projectProposal',
        fieldsConfig: [
            { row: 1, span: 12, name: 'collateralType', label: 'Collateral Type', type: 'select', displayKey: 'value', valueKey: 'code', 
                viewOperationKey: 'collateralTypeDescription', required: true },
            { row: 2, span: 12, name: 'details', label: 'Details', type: 'text', maxLength: 200 }
        ]
    },

    // ICC In-principle Approval - Further Details
    iccInprincipleApprovalFurtherDetails: {
        searchString1ForCreate: 'iccInprincipleApprovalId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Further Details',
        updateDialogTitle: 'Update Further Details',
        viewDialogTitle: 'View Further Details',
        createFunction: IccInprincipleApprovalService.prototype.createFurtherDetail,
        updateFunction: IccInprincipleApprovalService.prototype.updateFurtherDetail,
        createSuccessMessage: 'Further details created successfully',
        updateSuccessMessage: 'Further details updated successfully',
        trackObjectAfterCreateAndUpdate: 'iccApproval',
        fieldsConfig: [
            { row: 1, span: 12, name: 'serialNumber', label: 'Serial Number', type: 'text', readOnly: true },
            { row: 2, span: 12, name: 'iccMeetingNumber', label: 'ICC Meeting Number', type: 'text', maxLength: 10, required: true },
            { row: 2, span: 12, name: 'iccMeetingDate', label: 'ICC Meeting Date', type: 'date', required: true },
            { row: 4, span: 12, name: 'detailsRequired', label: 'Details Required', type: 'text', maxLength: 200 },
        ]
    },

    // ICC In-principle Approval - Reasons For Delay
    iccInprincipleApprovalReasonsForDelay: {
        searchString1ForCreate: 'iccInprincipleApprovalId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Reason For Delay',
        updateDialogTitle: 'Update Reason For Delay',
        viewDialogTitle: 'View Reason For Delay',
        createFunction: IccInprincipleApprovalService.prototype.createReasonForDelay,
        updateFunction: IccInprincipleApprovalService.prototype.updateReasonForDelay,
        createSuccessMessage: 'Reason for delay created successfully',
        updateSuccessMessage: 'Reason for delay updated successfully',
        trackObjectAfterCreateAndUpdate: 'iccApproval',
        fieldsConfig: [
            { row: 1, span: 12, name: 'reasonForDelay', label: 'Reason For Delay', type: 'text', required: true, maxLength: 200 },
            { row: 2, span: 12, name: 'date', label: 'Date', type: 'date', required: true },
        ]
    },

    // ICC In-principle Approval - Rejected By ICC
    iccInprincipleApprovalRejectedByIcc: {
        searchString1ForCreate: 'iccInprincipleApprovalId',
        passSearchString1Via: 'Object',
        searchString2ForCreate: 'loanApplicationId',
        passSearchString2Via: 'Object',
        createDialogTitle: 'Add Rejected By ICC Details',
        updateDialogTitle: 'Update Rejected By ICC Details',
        viewDialogTitle: 'View Rejected By ICC Details',
        createFunction: IccInprincipleApprovalService.prototype.createRejectedByIcc,
        updateFunction: IccInprincipleApprovalService.prototype.updateRejectedByIcc,
        createSuccessMessage: 'Rejected by ICC details created successfully',
        updateSuccessMessage: 'Rejected by ICC details updated successfully',
        trackObjectAfterCreateAndUpdate: 'iccApproval',
        fieldsConfig: [
            { row: 1, span: 12, name: 'meetingNumber', label: 'Meeting Number', type: 'text', required: true, maxLength: 10 },
            { row: 2, span: 12, name: 'meetingDate', label: 'Meeting Date', type: 'date', required: true },
            { row: 3, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'text', readOnly: true, 
                defaultValue: 'Rejected By ICC' },
            { row: 3, span: 12, name: 'reasonForRejection', label: 'Reason For Rejection', type: 'text', maxLength: 200, required: true },
        ]
    }
}