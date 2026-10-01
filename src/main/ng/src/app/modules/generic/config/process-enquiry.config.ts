import { ProcessEnquiryService } from "../../loan-contract-search/functional-stage/process-enquiry/process-enquiry.service";
import { StageConfig } from "./generic-config.model";

// Process Enquiry - list, update dialog and update component configurations
export const processEnquiryStageConfig: StageConfig = {
    service: ProcessEnquiryService,

    list: {
        // Process Enquiry - Project Proposals
        processEnquiryProjectProposal: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'proposalFormSharingDate', header: 'Proposal Date', type: 'date'},
                {name: 'proposalStatus', header: 'Status', type: 'text'},
                {name: 'documentType', header: 'Document Type', type: 'text'},
                {name: 'documentTypeName', header: 'Document Type Description', type: 'text'},
                {name: 'documentName', header: 'Document Name', type: 'text'},
                {name: 'fileReference', header: 'File Reference', type: 'file'}
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
        }
    },

    updateDialog: {
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
        }
    },
};
