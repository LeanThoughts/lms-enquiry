import { FIFTEEN_COMMA_TWO } from "../../../common/common.regex";
import { BmcApprovalService } from "../../loan-contract-search/functional-stage/bmc-approval/bmc-approval.service";
import { StageConfig } from "./generic-config.model";

// BMC Approval - list and update dialog configurations
export const bmcApprovalStageConfig: StageConfig = {
    service: BmcApprovalService,

    list: {
        // BMC Approval - Further Details
        bmcApprovalFurtherDetails: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'iccMeetingNumber', header: 'ICC Meeting Number', type: 'text'},
                {name: 'iccMeetingDate', header: 'ICC Meeting Date', type: 'date'},
                {name: 'detailsRequired', header: 'Details Required', type: 'text'},
            ],
            fetchFunction: BmcApprovalService.prototype.getFurtherDetails,
            createButton: true,
            updateButton: true,
            deleteButton: true,
            viewButton: true,
            deleteFunction: BmcApprovalService.prototype.deleteFurtherDetail,
            deleteSuccessMessage: 'Further details deleted successfully',
            updateDialogWidth: '40rem',
            viewDialogWidth: '35rem'
        },

        // BMC Approval - Reasons For Delay
        bmcApprovalReasonsForDelay: {
            displayedColumns: [
                {name: 'reasonForDelay', header: 'Reason For Delay', type: 'text'},
                {name: 'date', header: 'Date', type: 'date'},
            ],
            fetchFunction: BmcApprovalService.prototype.getReasonsForDelay,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '35rem',
            viewDialogWidth: '30rem',
        },

        // BMC Approval - Rejected By ICC
        bmcApprovalRejectedByIcc: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'reasonForRejection', header: 'Reason For Rejection', type: 'text'},
            ],
            fetchFunction: BmcApprovalService.prototype.getRejectedByIcc,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '35rem',
        },

        // BMC Approval - ICC Approval
        bmcApprovalApprovalByIcc: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Date of BMC Clearance', type: 'date'},
                {name: 'cfoApprovalDate', header: 'CFO Approval Date', type: 'date'},
                {name: 'edApprovalDate', header: 'ED Approval Date', type: 'date'},
                {name: 'fileReference1', header: 'Minutes Document', type: 'file'},
                {name: 'fileReference2', header: 'Mail from CS/Internal Note Sheet', type: 'file'},
                {name: 'remarks', header: 'Remarks', type: 'text'}
            ],
            fetchFunction: BmcApprovalService.prototype.getApprovalByIcc,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '35rem',
            routeResolvedData: ['projectAppraisalCompletion', 'documentTypeMinutes', 'documentTypeMailFromCS'],
        },

        // BMC Approval - Rejected By Customer
        bmcApprovalRejectedByCustomer: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'ICC Meeting Number', type: 'text'},
                {name: 'rejectionCategory', header: 'Rejection Category', type: 'text'},
                {name: 'dateOfRejection', header: 'Date of Rejection', type: 'date'},
                {name: 'remarks', header: 'Reason For Rejection', type: 'text'},
            ],
            fetchFunction: BmcApprovalService.prototype.getRejectedByCustomer,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '35rem',
        },

        // BMC Approval - Loan Enhancements
        bmcApprovalLoanEnhancements: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Sl No.', type: 'text'},
                {name: 'iccMeetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'iccClearanceDate', header: 'Date of ICC Clearance', type: 'date'},
                {name: 'revisedProjectCost', header: 'Rev. Project Cost', type: 'number'},
                {name: 'revisedEquity', header: 'Rev. Equity', type: 'number'},
                {name: 'revisedContractAmount', header: 'Rev. Contract Amt.', type: 'number'},
                {name: 'revisedCommercialOperationsDate', header: 'Rev. COD', type: 'date'},
                {name: 'reviseRepaymentStartDate', header: 'Rev. Repayment Start Date', type: 'date'},
                {name: 'remarks', header: 'Remarks', type: 'text'},
            ],
            fetchFunction: BmcApprovalService.prototype.getLoanEnhancements,
            createButton: true,
            updateButton: true,
            deleteButton: true,
            viewButton: true,
            deleteFunction: BmcApprovalService.prototype.deleteLoanEnhancement,
            deleteSuccessMessage: 'Loan Enhancement details deleted successfully',
            updateDialogWidth: '45rem',
            viewDialogWidth: '40rem',
        },
    },

    updateDialog: {
        // BMC Approval - Further Details
        bmcApprovalFurtherDetails: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Further Details',
            updateDialogTitle: 'Update Further Details',
            viewDialogTitle: 'View Further Details',
            createFunction: BmcApprovalService.prototype.createFurtherDetail,
            updateFunction: BmcApprovalService.prototype.updateFurtherDetail,
            createSuccessMessage: 'Further details added successfully',
            updateSuccessMessage: 'Further details updated successfully',
            trackObjectAfterCreateAndUpdate: 'bmcICCApproval',
            fieldsConfig: [
                { row: 1, span: 12, name: 'serialNumber', label: 'Serial Number', type: 'text', readOnly: true },
                { row: 2, span: 6, name: 'iccMeetingNumber', label: 'ICC Meeting Number', type: 'text', maxLength: 10 },
                { row: 2, span: 6, name: 'iccMeetingDate', label: 'ICC Meeting Date', type: 'date', maxValue: 'currentDate' },
                { row: 3, span: 12, name: 'detailsRequired', label: 'Details Required', type: 'text', maxLength: 200 },
            ]
        },

        // BMC Approval - Reasons For Delay
        bmcApprovalReasonsForDelay: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Reason For Delay',
            updateDialogTitle: 'Update Reason For Delay',
            viewDialogTitle: 'View Reason For Delay',
            createFunction: BmcApprovalService.prototype.createReasonForDelay,
            updateFunction: BmcApprovalService.prototype.updateReasonForDelay,
            createSuccessMessage: 'Reason for delay added successfully',
            updateSuccessMessage: 'Reason for delay updated successfully',
            trackObjectAfterCreateAndUpdate: 'bmcICCApproval',
            fieldsConfig: [
                { row: 1, span: 12, name: 'reasonForDelay', label: 'Reason For Delay', type: 'text', required: true, maxLength: 200 },
                { row: 2, span: 12, name: 'date', label: 'Date', type: 'date', maxValue: 'currentDate' },
            ]
        },

        // BMC Approval - Rejected By ICC
        bmcApprovalRejectedByIcc: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Rejected By ICC Details',
            updateDialogTitle: 'Update Rejected By ICC Details',
            viewDialogTitle: 'View Rejected By ICC Details',
            createFunction: BmcApprovalService.prototype.createRejectedByIcc,
            updateFunction: BmcApprovalService.prototype.updateRejectedByIcc,
            createSuccessMessage: 'Rejected by ICC details added successfully',
            updateSuccessMessage: 'Rejected by ICC details updated successfully',
            trackObjectAfterCreateAndUpdate: 'bmcICCApproval',
            fieldsConfig: [
                { row: 1, span: 12, name: 'meetingNumber', label: 'Meeting Number', type: 'text', required: true, maxLength: 10 },
                { row: 2, span: 12, name: 'meetingDate', label: 'Meeting Date', type: 'date', maxValue: 'currentDate' },
                { row: 3, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'text', readOnly: true,
                    defaultValue: 'Rejected By ICC' },
                { row: 4, span: 12, name: 'reasonForRejection', label: 'Reason For Rejection', type: 'text', maxLength: 200, required: true },
            ]
        },

        // BMC Approval - ICC Approval
        bmcApprovalApprovalByIcc: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add ICC Approval Details',
            updateDialogTitle: 'Update ICC Approval Details',
            viewDialogTitle: 'View ICC Approval Details',
            createFunction: BmcApprovalService.prototype.createApprovalByIcc,
            updateFunction: BmcApprovalService.prototype.updateApprovalByIcc,
            createSuccessMessage: 'ICC Approval details created successfully',
            updateSuccessMessage: 'ICC Approval details updated successfully',
            trackObjectAfterCreateAndUpdate: 'bmcICCApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'meetingNumber', label: 'Meeting Number', type: 'text', required: true, maxLength: 10 },
                { row: 1, span: 6, name: 'meetingDate', label: 'Date of BMC Clearance', type: 'date', maxValue: 'currentDate',
                    minValue: 'projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO' },
                { row: 2, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200, required: true },
                { row: 3, span: 6, name: 'cfoApprovalDate', label: 'CFO Signing Date of BMC Minutes', type: 'date', maxValue: 'currentDate',
                    minValue: 'meetingDate', visibleWhen: 'meetingDate' },
                { row: 3, span: 6, name: 'edApprovalDate', label: 'ED Signing Date of BMC Minutes', type: 'date', maxValue: 'currentDate',
                    minValue: 'cfoApprovalDate', visibleWhen: 'meetingDate' },
                { row: 4, span: 12, name: 'documentTypeMinutes', label: 'Document Type (Minutes)', type: 'select', displayKey: 'description', valueKey: 'code',
                    viewOperationKey: 'documentTypeName' },
                { row: 4, span: 12, name: 'file1', label: 'Upload Minutes Document', type: 'file' },
                { row: 5, span: 12, name: 'documentTypeMailFromCS', label: 'Document Type (Mail from Company Secretary/Internal Note Sheet)', type: 'select',
                    displayKey: 'description', valueKey: 'code', viewOperationKey: 'documentTypeName' },
                { row: 5, span: 12, name: 'file2', label: 'Mail from Company Secretary/Internal Note Sheet', type: 'file' },
            ]
        },

        // BMC Approval - Rejected By Customer
        bmcApprovalRejectedByCustomer: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Rejected By Customer Details',
            updateDialogTitle: 'Update Rejected By Customer Details',
            viewDialogTitle: 'View Rejected By Customer Details',
            createFunction: BmcApprovalService.prototype.createRejectedByCustomer,
            updateFunction: BmcApprovalService.prototype.updateRejectedByCustomer,
            createSuccessMessage: 'Rejected by Customer details added successfully',
            updateSuccessMessage: 'Rejected by Customer details updated successfully',
            trackObjectAfterCreateAndUpdate: 'bmcICCApproval',
            fieldsConfig: [
                { row: 1, span: 12, name: 'meetingNumber', label: 'Meeting Number', type: 'text', maxLength: 50 },
                { row: 2, span: 6, name: 'dateOfRejection', label: 'Date of Rejection', type: 'date', maxValue: 'currentDate' },
                { row: 3, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'text', readOnly: true,
                    defaultValue: 'Rejected By Customer' },
                { row: 4, span: 12, name: 'remarks', label: 'Reason For Rejection', type: 'text', maxLength: 200, required: true },
            ]
        },

        // BMC Approval - Loan Enhancements
        bmcApprovalLoanEnhancements: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Loan Enhancement Details',
            updateDialogTitle: 'Update Loan Enhancement Details',
            viewDialogTitle: 'View Loan Enhancement Details',
            createFunction: BmcApprovalService.prototype.createLoanEnhancement,
            updateFunction: BmcApprovalService.prototype.updateLoanEnhancement,
            createSuccessMessage: 'Loan Enhancement details added successfully',
            updateSuccessMessage: 'Loan Enhancement details updated successfully',
            trackObjectAfterCreateAndUpdate: 'bmcICCApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'iccMeetingNumber', label: 'ICC Meeting Number', type: 'text', maxLength: 10 },
                { row: 1, span: 6, name: 'iccClearanceDate', label: 'ICC Clearance Date', type: 'date' },
                { row: 2, span: 6, name: 'revisedProjectCost', label: 'Revised Project Cost', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 2, span: 6, name: 'revisedEquity', label: 'Revised Equity', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 6, name: 'revisedContractAmount', label: 'Revised Contract Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 6, name: 'revisedCommercialOperationsDate', label: 'Rev. COD', type: 'date' },
                { row: 4, span: 6, name: 'reviseRepaymentStartDate', label: 'Rev. Repayment Date', type: 'date' },
                { row: 5, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200 },
            ]
        },
    },
};
