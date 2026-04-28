import { BusinessPartnerSearchService } from "../business-partner-search/business-partner-search.service";
import { IccInprincipleApprovalService } from "../loan-contract-search/functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service";
import { ProcessEnquiryService } from "../loan-contract-search/functional-stage/process-enquiry/process-enquiry.service";
import { ProjectProposalService } from "../loan-contract-search/functional-stage/process-enquiry/project-proposal/project-proposal.service";

interface EntityUpdateDialogComponentConfig {
    // Required for list components
    header?: string;
    displayedColumns?: { name: string; header: string; type: string }[];
    fetchFunction: any;
    createButton?: boolean;
    updateButton?: boolean;
    deleteButton?: boolean;
    viewButton?: boolean;
    disableCreateButtonAfterCreate?: boolean;
    onlyEmitCreateEvent?: boolean;
    onlyEmitUpdateEvent?: boolean;
    onlyEmitViewEvent?: boolean;
    emitOnCreateSuccess?: boolean;
    emitOnUpdateSuccess?: boolean;
    updateDialogWidth?: string;
    viewDialogWidth?: string;
    routeResolvedData?: string[],
}

// Entity - Component configuration
export const entityComponentConfigs: { [key: string]: EntityUpdateDialogComponentConfig } = 
{
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
    },

    // Process Enquiry - Project Proposals
    processEnquiryProjectProposal: {
        displayedColumns: [
            {name: 'serialNumber', header: 'Serial Number', type: 'text'},
            {name: 'proposalFormSharingDate', header: 'Proposal Date', type: 'date'},
            {name: 'proposalStatus', header: 'Status', type: 'text'},
        ],
        fetchFunction: ProcessEnquiryService.prototype.getProjectProposals,
        createButton: true,
        updateButton: true,
        onlyEmitUpdateEvent: true,
        onlyEmitViewEvent: true,
        viewButton: true,
        disableCreateButtonAfterCreate: false,
        routeResolvedData: [],
        // Undefined values and not required for this component
        updateDialogWidth: "",
        viewDialogWidth: "",
    },

    // Process Enquiry - Rejected By PFS
    processEnquiryRejectedByPFS: {
        displayedColumns: [
            {name: 'categoryDescription', header: 'Category', type: 'select'},
            {name: 'rejectionReason', header: 'Reason For Rejection', type: 'text'},
            {name: 'rejectionDate', header: 'Rejection Date', type: 'date'},
        ],
        fetchFunction: ProcessEnquiryService.prototype.getRejectedByPFS,
        createButton: true,
        updateButton: true,
        viewButton: true,
        disableCreateButtonAfterCreate: true,
        updateDialogWidth: '35rem',
        viewDialogWidth: '30rem',
        routeResolvedData: ['rejectionCategory']
    },
    
    // Process Enquiry - Other Details
    processEnquiryOtherDetails: {
        displayedColumns: [
            {name: 'nameOfSourcingCompany', header: 'Name of Sourcing Company', type: 'text'},
            {name: 'contactPersonName', header: 'Contact Person', type: 'text'},
            {name: 'contactNumber', header: 'Contact Number', type: 'text'},
            {name: 'email', header: 'Email', type: 'text'},
            {name: 'enquiryDate', header: 'Enquiry Date', type: 'date'},
            {name: 'rating', header: 'Rating', type: 'select'},
            {name: 'creditStanding', header: 'Credit Standing', type: 'select'},
            // {name: 'creditStandingInstruction', header: 'Credit Standing Instruction', type: 'text'},
            // {name: 'creditStandingText', header: 'Credit Standing Text', type: 'text'},
            {name: 'ratingDate', header: 'Rating Date', type: 'date'},
        ],
        fetchFunction: ProcessEnquiryService.prototype.getOtherDetails,
        createButton: true,
        updateButton: true,
        viewButton: true,
        disableCreateButtonAfterCreate: true,
        updateDialogWidth: '55rem',
        viewDialogWidth: '45rem',
        routeResolvedData: ['rating', 'creditStanding'],
    },

    // Process Enquiry - Reason For Delay
    processEnquiryReasonForDelay: {
        displayedColumns: [
            {name: 'reason', header: 'Reason For Delay', type: 'text'},
            {name: 'date', header: 'Date', type: 'date'},
        ],
        fetchFunction: ProcessEnquiryService.prototype.getReasonForDelay,
        createButton: true,
        updateButton: true,
        deleteButton: false,
        viewButton: true,
        disableCreateButtonAfterCreate: true,
        updateDialogWidth: '35rem',
        viewDialogWidth: '30rem',
        routeResolvedData: [],
    },

    // Process Enquiry - Enquiry Completion Details
    processEnquiryEnquiryCompletion: {
        displayedColumns: [
            {name: 'productTypeDescription', header: 'Product Type', type: 'select'},
            {name: 'termDescription', header: 'Term', type: 'select'},
            {name: 'date', header: 'Enquiry Completion Date', type: 'date'},
            {name: 'remarks', header: 'Remarks', type: 'text'},
        ],
        fetchFunction: ProcessEnquiryService.prototype.getEnquiryCompletionDetails,
        createButton: true,
        updateButton: true,
        viewButton: true,
        disableCreateButtonAfterCreate: true,
        updateDialogWidth: '35rem',
        viewDialogWidth: '30rem',
        routeResolvedData: ['productType', 'term'],
    },

    // Process Enquiry - Rejected By Customer
    processEnquiryRejectedByCustomer: {
        displayedColumns: [
            {name: 'categoryDescription', header: 'Category', type: 'select'},
            {name: 'rejectionReason', header: 'Reason For Rejection', type: 'text'},
            {name: 'rejectionDate', header: 'Rejection Date', type: 'date'},
        ],
        fetchFunction: ProcessEnquiryService.prototype.getRejectedByCustomer,
        createButton: true,
        updateButton: true,
        deleteButton: false,
        viewButton: true,
        disableCreateButtonAfterCreate: true,
        updateDialogWidth: '35rem',
        viewDialogWidth: '30rem',
        routeResolvedData: ['rejectionCategory'],
    },

    // Project Proposal - Credit Rating
    projectProposalCreditRating: {
        header: 'Credit Rating Details',
        displayedColumns: [
            {name: 'creditRating', header: 'Credit Rating', type: 'select'},
            {name: 'creditRatingAgency', header: 'Credit Rating Agency', type: 'select'},
            {name: 'creditStandingInstruction', header: 'Credit Standing Instruction', type: 'text'},
            {name: 'creditStandingText', header: 'Credit Standing Text', type: 'text'},
        ],
        fetchFunction: ProjectProposalService.prototype.getProjectProposalCreditRatings,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '40rem',
        viewDialogWidth: '35rem',
        routeResolvedData: ['creditRating', 'creditRatingAgency'],
        emitOnCreateSuccess: true,
        emitOnUpdateSuccess: true,
    },

    // Project Proposal - Share Holding
    projectProposalShareHolding: {
        header: 'Share Holding Structure Of The Borrower',
        displayedColumns: [
            {name: 'companyName', header: 'Company Name', type: 'text'},
            {name: 'equityCapital', header: 'Capital (Crores)', type: 'number'},
            {name: 'percentageHolding', header: 'Percentage Holding', type: 'number'},
        ],
        fetchFunction: ProjectProposalService.prototype.getProjectProposalShareHolders,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '40rem',
        viewDialogWidth: '35rem',
        emitOnCreateSuccess: true,
        emitOnUpdateSuccess: true,
    },

    // Project Proposal - Other Loan Details Documents
    projectProposalOtherLoanDetailsDocuments: {
        header: 'Documents',
        displayedColumns: [
            {name: 'documentType', header: 'Document Type', type: 'text'},
            {name: 'documentTypeName', header: 'Document Type Description', type: 'text'},
            {name: 'documentName', header: 'Document Name', type: 'text'},
            {name: 'fileReference', header: 'File Reference', type: 'file'},
        ],
        fetchFunction: ProjectProposalService.prototype.getOtherLoanDetailsDocuments,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '40rem',
        viewDialogWidth: '35rem',
        routeResolvedData: ['documentType'],
        emitOnCreateSuccess: true,
        emitOnUpdateSuccess: true,
    },

    // Project Proposal - Promoter Financials
    projectProposalPromoterFinancials: {
        displayedColumns: [
            {name: 'fiscalPeriod', header: 'Fiscal Period', type: 'text'},
        ],
        fetchFunction: ProjectProposalService.prototype.getPromoterFinancials,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '80rem',
        viewDialogWidth: '70rem',
        emitOnCreateSuccess: true,
        emitOnUpdateSuccess: true,
    },

    // Project Proposal - Collateral Details
    projectProposalCollateralDetails: {
        displayedColumns: [
            {name: 'collateralType', header: 'Collateral Type', type: 'text'},
            {name: 'collateralTypeDescription', header: 'Collateral Type Description', type: 'text'},
            {name: 'details', header: 'Details', type: 'text'},            
        ],
        fetchFunction: ProjectProposalService.prototype.getCollateralDetails,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '35rem',
        viewDialogWidth: '30rem',
        routeResolvedData: ['collateralType'],
        emitOnCreateSuccess: true,
        emitOnUpdateSuccess: true,
    },

    // ICC In-principle Approval - ICC Further Details
    iccInprincipleApprovalFurtherDetails: {
        displayedColumns: [
            {name: 'serialNumber', header: 'Serial Number', type: 'text'},
            {name: 'iccMeetingNumber', header: 'ICC Meeting Number', type: 'text'},
            {name: 'iccMeetingDate', header: 'ICC Meeting Date', type: 'date'},
            {name: 'detailsRequired', header: 'Details Required', type: 'text'},            
        ],
        fetchFunction: IccInprincipleApprovalService.prototype.getIccFurtherDetails,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '40rem',
        viewDialogWidth: '35rem'
    },

    // ICC In-principle Approval - Reasons For Delay
    iccInprincipleApprovalReasonsForDelay: {
        displayedColumns: [
            {name: 'reasonForDelay', header: 'Reason For Delay', type: 'text'},
            {name: 'date', header: 'Date', type: 'date'},
        ],
        fetchFunction: IccInprincipleApprovalService.prototype.getReasonsForDelay,
        createButton: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '35rem',
        viewDialogWidth: '30rem',
    },

    // ICC In-principle Approval - Rejected By ICC
    iccInprincipleApprovalRejectedByIcc: {
        displayedColumns: [
            {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
            {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
            {name: 'rejectionCategory', header: 'Rejection Category', type: 'text'},
            {name: 'reasonForRejection', header: 'Reason For Rejection', type: 'text'},
        ],
        fetchFunction: IccInprincipleApprovalService.prototype.getRejectedByIcc,
        createButton: true,
        disableCreateButtonAfterCreate: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '40rem',
        viewDialogWidth: '35rem',
    },

    // ICC In-principle Approval - Approval By ICC
    iccInprincipleApprovalApprovalByIcc: {
        displayedColumns: [
            {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
            {name: 'remarks', header: 'Remarks', type: 'text'},
            {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
            {name: 'edApprovalDate', header: 'ED Approval Date', type: 'date'},
            {name: 'cfoApprovalDate', header: 'CFO Approval Date', type: 'date'},
            {name: 'fileReference1', header: 'Minutes Document', type: 'file'},
            {name: 'fileReference2', header: 'Mail from CS/Internal Note Sheet', type: 'file'},
        ],
        fetchFunction: IccInprincipleApprovalService.prototype.getApprovalByIcc,
        createButton: true,
        disableCreateButtonAfterCreate: true,
        updateButton: true,
        viewButton: true,
        updateDialogWidth: '40rem',
        viewDialogWidth: '35rem',
    }
}
