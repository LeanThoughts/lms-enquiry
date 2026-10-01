import { EMAIL_REGEX, FIFTEEN_COMMA_TWO, FIVE_COMMA_TWO, MOBILE_NUMBER_REGEX, PHONE_NUMBER_REGEX } from "../../../common/common.regex";
import { BusinessPartnerSearchService } from "../../business-partner-search/business-partner-search.service";
import { StageConfig } from "./generic-config.model";

// Business Partner - list, update dialog and update component configurations
export const businessPartnerStageConfig: StageConfig = {
    service: BusinessPartnerSearchService,

    list: {
        // Business Partner - KYC Details
        businessPartnerKYCDetails: {
            displayedColumns: [
                {name: 'kycDate', header: 'KYC Date', type: 'date'},
                {name: 'kycRiskCategory', header: 'KYC Risk Category', type: 'text'},
                {name: 'reKYCDate', header: 'Re KYC Date', type: 'date'},
                {name: 'reKYCRiskCategory', header: 'Re KYC Risk Category', type: 'text'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinessPartnerKYCDetails,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            onlyEmitCreateEvent: true,
            onlyEmitUpdateEvent: true,
            onlyEmitViewEvent: true,
        },

        // Business Partner - FI Customer/Vendor
        businessPartnerFICustomerVendor: {
            displayedColumns: [
                {name: 'houseBank', header: 'House Bank', type: 'text'},
                {name: 'planningGroup', header: 'Planning Group', type: 'text'},
                {name: 'reconAccount', header: 'Recon Account', type: 'text'},
                {name: 'sortKey', header: 'Sort Key', type: 'text'},
                {name: 'dunningProcedure', header: 'Dunning Procedure', type: 'text'},
                {name: 'paymentTerms', header: 'Payment Terms', type: 'text'},
                {name: 'paymentMethod', header: 'Payment Method', type: 'text'},
                {name: 'checkDoubleInvoice', header: 'Check Double Invoice', type: 'text'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinesPartner,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            onlyEmitUpdateEvent: true,
            onlyEmitViewEvent: true,
        },

        // Business Partner - Financials
        businessPartnerFinancials: {
            displayedColumns: [
                {name: 'fiscalYear', header: 'Fiscal Year', type: 'text'},
                {name: 'revenue', header: 'Revenue', type: 'number'},
                {name: 'ebitda', header: 'EBITDA', type: 'number'},
                {name: 'pat', header: 'Profit After Tax', type: 'number'},
                {name: 'shareCapital', header: 'Share Capital', type: 'number'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinessPartnerFinancials,
            updateDialogWidth: '75rem',
            viewDialogWidth: '65rem',
            createButton: true,
            updateButton: true,
            viewButton: true
        },

        // Business Partner - Business Partner Roles
        businessPartnerRoles: {
            createButton: true,
            displayedColumns: [
                {name: 'code', header: 'Role Type', type: 'text'},
                {name: 'value', header: 'Description', type: 'text'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinessPartnerRoles,
            updateDialogWidth: '35rem',
            viewDialogWidth: '30rem',
            routeResolvedData: ['roleTypeId'],
        },

        // Business Partner - Bank Details
        businessPartnerBankDetails: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'bankKey', header: 'Bank Key', type: 'text'},
                {name: 'bankName', header: 'Bank Name', type: 'text'},
                {name: 'ifscCode', header: 'IFSC Code', type: 'text'},
                {name: 'bankAccountName', header: 'Account Name', type: 'text'},
                {name: 'accountHolderName', header: 'Account Holder Name', type: 'text'},
                {name: 'accountNumber', header: 'Account Number', type: 'text'},
                {name: 'validFromDate', header: 'Valid From', type: 'date'},
                {name: 'validToDate', header: 'Valid To', type: 'date'},
                {name: 'bankCountry', header: 'Bank Country', type: 'text'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinessPartnerBankDetails,
            createButton: true,
            onlyEmitCreateEvent: true,
            updateButton: true,
            onlyEmitUpdateEvent: true,
            viewButton: true,
            onlyEmitViewEvent: true,
            routeResolvedData: ['bankKey', 'bankCountry'],
        },

        // Business Partner - Identification Details
        businessPartnerIdentificationDetails: { 
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'identificationCategory', header: 'Identification Category', type: 'text'},
                {name: 'identificationNumber', header: 'Identification Number', type: 'text'},
                {name: 'countryName', header: 'Country', type: 'text'},
                {name: 'regionName', header: 'Region', type: 'text'},
                {name: 'idValidFromDate', header: 'Valid From', type: 'date'},
                {name: 'idValidToDate', header: 'Valid To', type: 'date'},
                {name: 'documentName', header: 'Document Name', type: 'text'},
                {name: 'documentTypeName', header: 'Document Type', type: 'text'},
                {name: 'fileReference', header: 'Download', type: 'file'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinessPartnerIdentificationDetails,
            createButton: true,
            onlyEmitCreateEvent: true,
            updateButton: true,
            onlyEmitUpdateEvent: true,
            viewButton: true,
            onlyEmitViewEvent: true,
            routeResolvedData: ['identificationCategoryCode', 'documentType', 'country'],
        },

        // Business Partner - Industry Details
        businessPartnerIndustryDetails: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'industrySystem', header: 'Industry System', type: 'text'},
                {name: 'industryType', header: 'Industry Type', type: 'text'},
            ],
            fetchFunction: BusinessPartnerSearchService.prototype.getBusinessPartnerIndustryDetails,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '35rem',
            viewDialogWidth: '30rem',
            routeResolvedData: ['industrySystemId'],
        }
    },

    updateDialog: {
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
        }
    },

    update: {
        // Business Partner - Basic Information
        businessPartnerBasicInformation: {
            fieldsConfig: [
                {row: 1, span: 12, name: 'header1', type: 'header', label: 'Identification Details' },
                {row: 2, span: 3, name: 'defaultPartnerRole', label: 'Default Partner Role', type: 'select', required: true },
                {row: 2, span: 3, name: 'partnerGroup', label: 'Partner Group', type: 'select', required: true },
                {row: 2, span: 3, name: 'partnerCategory', label: 'Partner Category', type: 'select', required: true },
                {row: 3, span: 3, name: 'partyNumber', label: 'Business Partner Number', type: 'text' },
                {row: 3, span: 3, name: 'title', label: 'Title', type: 'select' },
                {row: 3, span: 3, name: 'partyName1', label: 'Name 1', type: 'text'},
                {row: 3, span: 3, name: 'partyName2', label: 'Name 2', type: 'text'},
                {row: 4, span: 3, name: 'searchTerm1', label: 'Search Term 1', type: 'text', required: true },
                {row: 4, span: 3, name: 'searchTerm2', label: 'Search Term 2', type: 'text' },

                {row: 5, span: 12, name: 'header2', type: 'header', label: 'Address Details' },
                {row: 6, span: 3, name: 'addressLine1', label: 'Street/House No.', type: 'text', required: true },
                {row: 6, span: 3, name: 'addressLine2', label: 'Street2', type: 'text' },
                {row: 6, span: 3, name: 'addressLine3', label: 'Street3', type: 'text' },
                {row: 6, span: 3, name: 'city', label: 'City', type: 'text', required: true },
                {row: 7, span: 3, name: 'postalCode', label: 'Postal Code', type: 'text' },
                {row: 7, span: 3, name: 'state', label: 'State/Region', type: 'select' },
                {row: 7, span: 3, name: 'country', label: 'Country', type: 'select' },
                {row: 7, span: 3, name: 'addressValidFromDate', label: 'Address Valid From Date', type: 'date' },

                {row: 8, span: 12, name: 'header3', type: 'header', label: 'Communication & Other Details' },
                {row: 9, span: 3, name: 'contactNumber', label: 'Telephone', type: 'text' },
                {row: 9, span: 3, name: 'email', label: 'Email', type: 'text', required: true },
                {row: 9, span: 3, name: 'mobileNumber', label: 'Mobile', type: 'text' },
                {row: 9, span: 3, name: 'faxNumber', label: 'Fax', type: 'text' },
                {row: 10, span: 3, name: 'legalForm', label: 'Legal Form', type: 'select' },
                {row: 10, span: 3, name: 'legalEntity', label: 'Legal Entity', type: 'select' },
                {row: 10, span: 3, name: 'houseBank', label: 'House Bank', type: 'select' }
            ]
        },

        // Business Partner - KYC Details
        businessPartnerKYCDetails: {
            fieldsConfig: [
                {row: 1, span: 6, name: 'kycCompletionDate', label: 'KYC Completion Date', type: 'date' },
                {row: 1, span: 3, name: 'kycRiskCategory', label: 'KYC Risk Category', type: 'select' },
                {row: 2, span: 6, name: 'reKycCompletionDate', label: 'Re KYC Completion Date', type: 'date' },
                {row: 2, span: 3, name: 'reKycRiskCategory', label: 'Re KYC Risk Category', type: 'select' },
            ]
        }
    },
};
