import { FIFTEEN_COMMA_TWO, FIVE_COMMA_TWO } from "../../../common/common.regex";
import { IccInprincipleApprovalService } from "../../loan-contract-search/functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service";
import { StageConfig } from "./generic-config.model";

// ICC In-principle Approval - list, update dialog and update component configurations
export const iccInprincipleApprovalStageConfig: StageConfig = {
    service: IccInprincipleApprovalService,

    list: {
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
            routeResolvedData: ['enquiryCompletion'],
        },

        // ICC In-principle Approval - Approval By ICC
        iccInprincipleApprovalApprovalByIcc: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'amountApproved', header: 'Approved Amount', type: 'number'},
                {name: 'iccApprovedRoi', header: 'ICC Approved ROI', type: 'number'},
                {name: 'edApprovalDate', header: 'ED Approval Date', type: 'date'},
                {name: 'cfoApprovalDate', header: 'CFO Approval Date', type: 'date'},
                {name: 'fileReference1', header: 'Minutes Document', type: 'file'},
                {name: 'fileReference2', header: 'Mail from CS/Internal Note Sheet', type: 'file'},
                {name: 'remarks', header: 'Remarks', type: 'text'}
            ],
            fetchFunction: IccInprincipleApprovalService.prototype.getApprovalByIcc,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '35rem',
            routeResolvedData: ['enquiryCompletion', 'documentTypeMinutes', 'documentTypeMailFromCS'],
        },

        // ICC In-principle Approval - Rejected By Customer
        iccInprincipleApprovalRejectedByCustomer: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'rejectionCategory', header: 'Rejection Category', type: 'text'},
                {name: 'dateOfRejection', header: 'Date of Rejection', type: 'date'},
                {name: 'remarks', header: 'Reason For Rejection', type: 'text'},
            ],
            fetchFunction: IccInprincipleApprovalService.prototype.getRejectedByCustomer,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '35rem',
            routeResolvedData: ['enquiryCompletion'],
        },

        // ICC In-principle Approval - Loan Enhancements
        iccInprincipleApprovalLoanEnhancements: {
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
            fetchFunction: IccInprincipleApprovalService.prototype.getLoanEnhancements,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '45rem',
            viewDialogWidth: '40rem',
        },

        // ICC In-principle Approval - Risk Notifications
        iccInprincipleApprovalRiskNotifications: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Sl No.', type: 'text'},
                {name: 'notificationDate', header: 'Notification Date', type: 'date'},
                {name: 'remarks', header: 'Remarks', type: 'text'},
            ],
            fetchFunction: IccInprincipleApprovalService.prototype.getRiskNotifications,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '35rem',
            viewDialogWidth: '30rem',
        }
    },

    updateDialog: {
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
                { row: 2, span: 12, name: 'iccMeetingDate', label: 'ICC Meeting Date', type: 'date', required: true, maxValue: 'currentDate' },
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
                { row: 2, span: 12, name: 'date', label: 'Date', type: 'date', required: true, maxValue: 'currentDate' },
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
                { row: 2, span: 12, name: 'meetingDate', label: 'Meeting Date', type: 'date', required: true, maxValue: 'currentDate', minValue: 'enquiryCompletion.date' },
                { row: 3, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'text', readOnly: true, 
                    defaultValue: 'Rejected By ICC' },
                { row: 3, span: 12, name: 'reasonForRejection', label: 'Reason For Rejection', type: 'text', maxLength: 200, required: true },
            ]
        },

        // ICC In-principle Approval - Approval By ICC
        iccInprincipleApprovalApprovalByIcc: {
            searchString1ForCreate: 'iccInprincipleApprovalId',
            passSearchString1Via: 'Object',
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Approval By ICC Details',
            updateDialogTitle: 'Update Approval By ICC Details',
            viewDialogTitle: 'View Approval By ICC Details',
            createFunction: IccInprincipleApprovalService.prototype.createApprovalByIcc,
            updateFunction: IccInprincipleApprovalService.prototype.updateApprovalByIcc,
            createSuccessMessage: 'Approval by ICC details created successfully',
            updateSuccessMessage: 'Approval by ICC details updated successfully',
            trackObjectAfterCreateAndUpdate: 'iccApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'meetingNumber', label: 'Meeting Number', type: 'text', required: true, maxLength: 10 },
                { row: 1, span: 6, name: 'meetingDate', label: 'Meeting Date', type: 'date', required: true, maxValue: 'currentDate', minValue: 'enquiryCompletion.date' },
                { row: 2, span: 6, name: 'amountApproved', label: 'Approved Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO, required: true },
                { row: 2, span: 6, name: 'iccApprovedRoi', label: 'ICC Approved ROI', type: 'text', maxLength: 5, pattern: FIVE_COMMA_TWO, required: true },
                { row: 3, span: 6, name: 'cfoApprovalDate', label: 'CFO Signing Date of ICC Minutes', type: 'date', required: false, maxValue: 'currentDate', minValue: 'meetingDate', visibleWhen: 'meetingDate' },
                { row: 3, span: 6, name: 'edApprovalDate', label: 'ED Signing Date of ICC Minutes', type: 'date', required: false, maxValue: 'currentDate', minValue: 'meetingDate', visibleWhen: 'meetingDate' },
                {row: 4, span: 12, name: 'documentTypeMinutes', label: 'Document Type (Minutes)', type: 'select', displayKey: 'description', valueKey: 'code', 
                    viewOperationKey: 'documentTypeName', required: false },
                {row: 4, span: 12, name: 'file1', label: 'Upload Minutes Document', type: 'file', required: false },
                {row: 5, span: 12, name: 'documentTypeMailFromCS', label: 'Document Type (Mail from Company Secretary/Internal Note Sheet)', type: 'select', displayKey: 'description', valueKey: 'code', 
                    viewOperationKey: 'documentTypeName', required: false },
                {row: 5, span: 12, name: 'file2', label: 'Mail from Company Secretary/Internal Note Sheet', type: 'file', required: false },
                {row: 6, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200 },
            ]
        },

        // ICC In-principle Approval - Rejected By Customer
        iccInprincipleApprovalRejectedByCustomer: {
            searchString1ForCreate: 'iccInprincipleApprovalId',
            passSearchString1Via: 'Object',
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Rejected By Customer Details',
            updateDialogTitle: 'Update Rejected By Customer Details',
            viewDialogTitle: 'View Rejected By Customer Details',
            createFunction: IccInprincipleApprovalService.prototype.createRejectedByCustomer,
            updateFunction: IccInprincipleApprovalService.prototype.updateRejectedByCustomer,
            createSuccessMessage: 'Rejected by Customer details created successfully',
            updateSuccessMessage: 'Rejected by Customer details updated successfully',
            trackObjectAfterCreateAndUpdate: 'iccApproval',
            fieldsConfig: [
                { row: 1, span: 12, name: 'meetingNumber', label: 'Meeting Number', type: 'text', maxLength: 50 },
                { row: 2, span: 6, name: 'dateOfRejection', label: 'Date of Rejection', type: 'date', required: true, maxValue: 'currentDate', minValue: 'enquiryCompletion.date' },
                { row: 3, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'text', readOnly: true, 
                    defaultValue: 'Rejected By Customer' },
                { row: 4, span: 12, name: 'remarks', label: 'Reason For Rejection', type: 'text', maxLength: 200, required: true },
            ]
        },

        // ICC In-principle Approval - Loan Enhancements
        iccInprincipleApprovalLoanEnhancements: {
            searchString1ForCreate: 'iccInprincipleApprovalId',
            passSearchString1Via: 'Object',
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Loan Enhancement Details',
            updateDialogTitle: 'Update Loan Enhancement Details',
            viewDialogTitle: 'View Loan Enhancement Details',
            createFunction: IccInprincipleApprovalService.prototype.createLoanEnhancement,
            updateFunction: IccInprincipleApprovalService.prototype.updateLoanEnhancement,
            createSuccessMessage: 'Loan Enhancement details added successfully',
            updateSuccessMessage: 'Loan Enhancement details updated successfully',
            trackObjectAfterCreateAndUpdate: 'iccApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'iccMeetingNumber', label: 'ICC Meeting Number', type: 'text', maxLength: 10 },
                { row: 1, span: 6, name: 'iccClearanceDate', label: 'ICC Clearance Date', type: 'date' },
                { row: 2, span: 6, name: 'revisedProjectCost', label: 'Revised Project Cost', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 2, span: 6, name: 'revisedEquity', label: 'Revised Equity', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 6, name: 'revisedContractAmount', label: 'Revised Contract Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 6, name: 'revisedCommercialOperationsDate', label: 'Rev. COD', type: 'date' },
                { row: 4, span: 6, name: 'reviseRepaymentStartDate', label: 'Rev. Repayment Start Date', type: 'date' },
                { row: 5, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200 },
            ]
        },

        // ICC In-principle Approval - Risk Notifications
        iccInprincipleApprovalRiskNotifications: {
            searchString1ForCreate: 'iccInprincipleApprovalId',
            passSearchString1Via: 'Object',
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Risk Notification',
            updateDialogTitle: 'Update Risk Notification',
            viewDialogTitle: 'View Risk Notification',
            createFunction: IccInprincipleApprovalService.prototype.createRiskNotification,
            updateFunction: IccInprincipleApprovalService.prototype.updateRiskNotification,
            createSuccessMessage: 'Risk notification details added successfully',
            updateSuccessMessage: 'Risk notification details updated successfully',
            trackObjectAfterCreateAndUpdate: 'iccApproval',
            fieldsConfig: [
                { row: 1, span: 12, name: 'notificationDate', label: 'Notification Date', type: 'date' },
                { row: 2, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200, required: true },
            ]
        }
    },
};
