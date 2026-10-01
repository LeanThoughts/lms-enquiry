import { FIFTEEN_COMMA_TWO, FIVE_COMMA_TWO } from "../../../common/common.regex";
import { SanctionService } from "../../loan-contract-search/functional-stage/sanction/sanction.service";
import { StageConfig } from "./generic-config.model";

// Sanction - list, update dialog and update component configurations
export const sanctionStageConfig: StageConfig = {
    service: SanctionService,

    list: {
        // Sanction - Reasons For Delay
        sanctionReasonsForDelay: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'date', header: 'Date', type: 'date'},
                {name: 'reason', header: 'Reason', type: 'text'},
            ],
            fetchFunction: SanctionService.prototype.getReasonsForDelay,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '35rem',
            viewDialogWidth: '35rem',
        },

        // Sanction - Payment Receipts - Pre Sanction
        sanctionPaymentReceiptsPreSanction: {
            displayedColumns: [
                {name: 'proformaInvoiceNumber', header: 'Proforma Invoice Number', type: 'text'},
                {name: 'proformaInvoiceDate', header: 'Proforma Invoice Date', type: 'date'},
                {name: 'feeTypeDescription', header: 'Fee Type', type: 'text'},
                {name: 'amount', header: 'Amount', type: 'number'},
                {name: 'payee', header: 'Payee', type: 'text'},
                {name: 'amountReceived', header: 'Amount Received', type: 'number'},
                {name: 'dateOfTransfer', header: 'Date of Transfer', type: 'date'},
                {name: 'rtgsNeftNumber', header: 'RTGS/NEFT Number', type: 'text'},
                {name: 'referenceNumber', header: 'Reference Number (If Any)', type: 'text'},
            ],
            fetchFunction: SanctionService.prototype.getPaymentReceiptsPreSanction,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '45rem',
            viewDialogWidth: '45rem',
            routeResolvedData: ['feeType'],
        },

        // Sanction - Original Sanction Letters
        sanctionLetters: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'typeDescription', header: 'Type', type: 'text'},
                {name: 'sanctionLetterIssueDate', header: 'Sanction Letter Issue Date', type: 'date'},
                {name: 'sanctionLetterValidToDate', header: 'Sanction Letter Valid To Date', type: 'date'},
                {name: 'originalSanctionAmount', header: 'Original Sanction Amount', type: 'number'},
                {name: 'revisedSanctionAmount', header: 'Revised Sanction Amount', type: 'number'},
                {name: 'originalInterestRate', header: 'Original Interest Rate', type: 'number'},
                {name: 'revisedInterestRate', header: 'Revised Interest Rate', type: 'number'},
                {name: 'fileReference', header: 'Document', type: 'file'},
            ],
            fetchFunction: SanctionService.prototype.getSanctionLetters,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '55rem',
            viewDialogWidth: '55rem',
            routeResolvedData: ['type', 'documentType', 'approvalByBoard'],
        },

        // Sanction - Payment Receipts - Post Sanction
        sanctionPaymentReceiptsPostSanction: {
            displayedColumns: [
                {name: 'proformaInvoiceNumber', header: 'Proforma Invoice Number', type: 'text'},
                {name: 'proformaInvoiceDate', header: 'Proforma Invoice Date', type: 'date'},
                {name: 'feeTypeDescription', header: 'Fee Type', type: 'text'},
                {name: 'amount', header: 'Amount', type: 'number'},
                {name: 'payee', header: 'Payee', type: 'text'},
                {name: 'amountReceived', header: 'Amount Received', type: 'number'},
                {name: 'dateOfTransfer', header: 'Date of Transfer', type: 'date'},
                {name: 'rtgsNeftNumber', header: 'RTGS/NEFT Number', type: 'text'},
                {name: 'referenceNumber', header: 'Reference Number (If Any)', type: 'text'},
            ],
            fetchFunction: SanctionService.prototype.getPaymentReceiptsPostSanction,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '45rem',
            viewDialogWidth: '45rem',
            routeResolvedData: ['feeType'],
        },

        // Sanction - Rejected By Customer
        sanctionRejectedByCustomer: {
            displayedColumns: [
                {name: 'approvalByBoardMeetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'rejectionCategoryDescription', header: 'Rejection Category', type: 'text'},
                {name: 'details', header: 'Reason for Rejection', type: 'text'},
            ],
            fetchFunction: SanctionService.prototype.getRejectedByCustomers,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '40rem',
            routeResolvedData: ['approvalByBoardMeetingNumber', 'rejectionCategory'],
        }
    },

    updateDialog: {
        // Sanction - Reasons For Delay
        sanctionReasonsForDelay: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Reason for Delay',
            updateDialogTitle: 'Update Reason for Delay',
            viewDialogTitle: 'View Reason for Delay',
            createFunction: SanctionService.prototype.createReasonForDelay,
            updateFunction: SanctionService.prototype.updateReasonForDelay,
            createSuccessMessage: 'Reason for delay added successfully',
            updateSuccessMessage: 'Reason for delay updated successfully',
            trackObjectAfterCreateAndUpdate: 'sanction',
            fieldsConfig: [
                { row: 1, span: 6, name: 'date', label: 'Date', type: 'date' },
                { row: 2, span: 12, name: 'reason', label: 'Reason for Delay', type: 'text', maxLength: 200, required: true },
            ]
        },

        // Sanction - Payment Receipts - Pre Sanction
        sanctionPaymentReceiptsPreSanction: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Payment Receipt - Pre Sanction',
            updateDialogTitle: 'Update Payment Receipt - Pre Sanction',
            viewDialogTitle: 'View Payment Receipt - Pre Sanction',
            createFunction: SanctionService.prototype.createPaymentReceiptPreSanction,
            updateFunction: SanctionService.prototype.updatePaymentReceiptPreSanction,
            createSuccessMessage: 'Payment receipt added successfully',
            updateSuccessMessage: 'Payment receipt updated successfully',
            trackObjectAfterCreateAndUpdate: 'sanction',
            fieldsConfig: [
                { row: 1, span: 6, name: 'proformaInvoiceNumber', label: 'Proforma Invoice Number', type: 'text', maxLength: 20, required: true },
                { row: 1, span: 6, name: 'proformaInvoiceDate', label: 'Proforma Invoice Date', type: 'date' },
                { row: 2, span: 6, name: 'feeType', label: 'Fee Type', type: 'select', required: true, displayKey: 'value', valueKey: 'code', 
                    viewOperationKey: 'feeTypeDescription' },
                { row: 2, span: 6, name: 'amount', label: 'Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 12, name: 'payee', label: 'Payee', type: 'text', maxLength: 200 },
                { row: 4, span: 6, name: 'amountReceived', label: 'Amount Received', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO, required: true },
                { row: 4, span: 6, name: 'dateOfTransfer', label: 'Date of Transfer', type: 'date' },
                { row: 5, span: 6, name: 'rtgsNeftNumber', label: 'RTGS/NEFT Number', type: 'text', maxLength: 50 },
                { row: 5, span: 6, name: 'referenceNumber', label: 'Reference Number (if any)', type: 'text', maxLength: 50 },
            ]
        },

        // Sanction - Original Sanction Letters. Amendment fields are shown only for amendment sanction types.
        sanctionLetters: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Sanction Letter',
            updateDialogTitle: 'Update Sanction Letter',
            viewDialogTitle: 'View Sanction Letter',
            createFunction: SanctionService.prototype.createSanctionLetter,
            updateFunction: SanctionService.prototype.updateSanctionLetter,
            createSuccessMessage: 'Sanction letter added successfully',
            updateSuccessMessage: 'Sanction letter updated successfully',
            trackObjectAfterCreateAndUpdate: 'sanction',
            fieldsConfig: [
                { row: 1, span: 4, name: 'type', label: 'Type', type: 'select', required: true, displayKey: 'value', valueKey: 'code', 
                    viewOperationKey: 'typeDescription' },
                { row: 1, span: 4, name: 'dateOfAmendment', label: 'Date of Amendment', type: 'date', required: true,
                    visibleWhen: { field: 'type', value: ['1', '2', '3', '4', '5', '6'] } },
                { row: 1, span: 4, name: 'borrowerRequestLetterDate', label: 'Borrower Request Letter Date', type: 'date', maxValue: 'currentDate' },
                { row: 2, span: 6, name: 'originalSanctionAmount', label: 'Original Sanction Amount', type: 'text', maxLength: 19, 
                    pattern: FIFTEEN_COMMA_TWO },
                { row: 2, span: 6, name: 'revisedSanctionAmount', label: 'Revised Sanction Amount', type: 'text', maxLength: 19, 
                    pattern: FIFTEEN_COMMA_TWO, visibleWhen: { field: 'type', value: ['1', '2', '3', '4', '5', '6'] } },
                { row: 3, span: 6, name: 'originalInterestRate', label: 'Original Interest Rate', type: 'text', maxLength: 5, pattern: FIVE_COMMA_TWO },
                { row: 3, span: 6, name: 'revisedInterestRate', label: 'Revised Interest Rate', type: 'text', maxLength: 5, pattern: FIVE_COMMA_TWO,
                    visibleWhen: { field: 'type', value: ['2', '3', '4', '5', '6'] } },
                { row: 4, span: 6, name: 'sanctionLetterIssueDate', label: 'Sanction Letter Issue Date', type: 'date', maxValue: 'currentDate', 
                    minValue: 'approvalByBoard.meetingDate' },
                { row: 4, span: 6, name: 'sanctionLetterValidToDate', label: 'Sanction Letter Valid To Date', type: 'date' },
                { row: 5, span: 4, name: 'documentTitle', label: 'Document Title', type: 'text', maxLength: 50 },
                { row: 5, span: 4, name: 'documentType', label: 'Document Type', type: 'select', required: true, displayKey: 'description', valueKey: 'code' },
                { row: 5, span: 4, name: 'file', label: 'Upload Document (PDF)', type: 'file' },
                { row: 6, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200 },
                { row: 7, span: 6, name: 'sanctionLetterAcceptanceDate', label: 'Sanction Letter Acceptance Date', type: 'date', maxValue: 'currentDate', 
                    minValue: 'sanctionLetterIssueDate' },
            ]
        },

        // Sanction - Payment Receipts - Post Sanction
        sanctionPaymentReceiptsPostSanction: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Payment Receipt - Post Sanction',
            updateDialogTitle: 'Update Payment Receipt - Post Sanction',
            viewDialogTitle: 'View Payment Receipt - Post Sanction',
            createFunction: SanctionService.prototype.createPaymentReceiptPostSanction,
            updateFunction: SanctionService.prototype.updatePaymentReceiptPostSanction,
            createSuccessMessage: 'Payment receipt added successfully',
            updateSuccessMessage: 'Payment receipt updated successfully',
            trackObjectAfterCreateAndUpdate: 'sanction',
            fieldsConfig: [
                { row: 1, span: 6, name: 'proformaInvoiceNumber', label: 'Proforma Invoice Number', type: 'text', maxLength: 20, required: true },
                { row: 1, span: 6, name: 'proformaInvoiceDate', label: 'Proforma Invoice Date', type: 'date' },
                { row: 2, span: 6, name: 'feeType', label: 'Fee Type', type: 'select', required: true, displayKey: 'value', valueKey: 'code', 
                    viewOperationKey: 'feeTypeDescription' },
                { row: 2, span: 6, name: 'amount', label: 'Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 12, name: 'payee', label: 'Payee', type: 'text', maxLength: 200 },
                { row: 4, span: 6, name: 'amountReceived', label: 'Amount Received', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO, required: true },
                { row: 4, span: 6, name: 'dateOfTransfer', label: 'Date of Transfer', type: 'date' },
                { row: 5, span: 6, name: 'rtgsNeftNumber', label: 'RTGS/NEFT Number', type: 'text', maxLength: 50 },
                { row: 5, span: 6, name: 'referenceNumber', label: 'Reference Number (if any)', type: 'text', maxLength: 50 },
            ]
        },

        // Sanction - Rejected By Customer
        sanctionRejectedByCustomer: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Rejected By Customer',
            updateDialogTitle: 'Update Rejected By Customer',
            viewDialogTitle: 'View Rejected By Customer',
            createFunction: SanctionService.prototype.createRejectedByCustomer,
            updateFunction: SanctionService.prototype.updateRejectedByCustomer,
            createSuccessMessage: 'Rejected by customer added successfully',
            updateSuccessMessage: 'Rejected by customer updated successfully',
            trackObjectAfterCreateAndUpdate: 'sanction',
            fieldsConfig: [
                { row: 1, span: 6, name: 'approvalByBoardMeetingNumber', label: 'Board Approval Meeting Number', type: 'select', required: true, 
                    displayKey: 'meetingNumber', valueKey: 'meetingNumber', viewOperationKey: 'approvalByBoardMeetingNumber' },
                { row: 1, span: 6, name: 'meetingDate', label: 'Meeting Date', type: 'date' },
                { row: 2, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'select', required: true, displayKey: 'value', 
                    valueKey: 'code', viewOperationKey: 'rejectionCategoryDescription' },
                { row: 3, span: 12, name: 'details', label: 'Reason for Rejection', type: 'text', maxLength: 200, required: true },
            ]
        }
    },
};
