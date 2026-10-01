import { NUMERIC_ONLY_REGEX } from "../../../common/common.regex";
import { BoardApprovalService } from "../../loan-contract-search/functional-stage/board-approval/board-approval.service";
import { StageConfig } from "./generic-config.model";

// Board Approval - list, update dialog and update component configurations
export const boardApprovalStageConfig: StageConfig = {
    service: BoardApprovalService,

    list: {
        // Board Approval - Deferred By Board
        boardApprovalDeferredByBoard: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'details', header: 'Details', type: 'text'},
            ],
            fetchFunction: BoardApprovalService.prototype.getDeferredByBoards,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '40rem',
            routeResolvedData: ['projectAppraisalCompletion'],
        },

        // Board Approval - Reasons For Delay
        boardApprovalReasonsForDelay: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'date', header: 'Date', type: 'date'},
                {name: 'reason', header: 'Reason', type: 'text'},
            ],
            fetchFunction: BoardApprovalService.prototype.getReasonsForDelay,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '35rem',
            viewDialogWidth: '35rem',
            routeResolvedData: ['projectAppraisalCompletion'],
        },

        // Board Approval - Rejected By Board
        boardApprovalRejectedByBoard: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'details', header: 'Reason for Rejection', type: 'text'},
            ],
            fetchFunction: BoardApprovalService.prototype.getRejectedByBoards,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '40rem',
            routeResolvedData: ['projectAppraisalCompletion'],
        },

        // Board Approval - Approval By Board
        boardApprovalApprovalByBoard: {
            displayedColumns: [
                {name: 'meetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'details', header: 'Remarks', type: 'text'},
            ],
            fetchFunction: BoardApprovalService.prototype.getApprovalByBoards,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '40rem',
            routeResolvedData: ['projectAppraisalCompletion'],
        },

        // Board Approval - Rejected By Customer
        boardApprovalRejectedByCustomer: {
            displayedColumns: [
                {name: 'approvalByBoardMeetingNumber', header: 'Meeting Number', type: 'text'},
                {name: 'meetingDate', header: 'Meeting Date', type: 'date'},
                {name: 'rejectionCategoryDescription', header: 'Rejection Category', type: 'text'},
                {name: 'details', header: 'Reason for Rejection', type: 'text'},
            ],
            fetchFunction: BoardApprovalService.prototype.getRejectedByCustomers,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '40rem',
            routeResolvedData: ['projectAppraisalCompletion', 'approvalByBoardMeetingNumber', 'rejectionCategory'],
        }
    },

    updateDialog: {
        // Board Approval - Deferred By Board
        boardApprovalDeferredByBoard: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Deferred By Board',
            updateDialogTitle: 'Update Deferred By Board',
            viewDialogTitle: 'View Deferred By Board',
            createFunction: BoardApprovalService.prototype.createDeferredByBoard,
            updateFunction: BoardApprovalService.prototype.updateDeferredByBoard,
            createSuccessMessage: 'Deferred by board added successfully',
            updateSuccessMessage: 'Deferred by board updated successfully',
            trackObjectAfterCreateAndUpdate: 'boardApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'meetingNumber', label: 'Meeting Number', type: 'text', maxLength: 10, pattern: NUMERIC_ONLY_REGEX, 
                    required: true },
                { row: 1, span: 6, name: 'meetingDate', label: 'Meeting Date', type: 'date', 
                    minValue: 'projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO' },
                { row: 2, span: 12, name: 'details', label: 'Details', type: 'text', maxLength: 200, required: true },
            ]
        },

        // Board Approval - Reasons For Delay
        boardApprovalReasonsForDelay: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Reason for Delay',
            updateDialogTitle: 'Update Reason for Delay',
            viewDialogTitle: 'View Reason for Delay',
            createFunction: BoardApprovalService.prototype.createReasonForDelay,
            updateFunction: BoardApprovalService.prototype.updateReasonForDelay,
            createSuccessMessage: 'Reason for delay added successfully',
            updateSuccessMessage: 'Reason for delay updated successfully',
            trackObjectAfterCreateAndUpdate: 'boardApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'date', label: 'Date', type: 'date', minValue: 'projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO' },
                { row: 2, span: 12, name: 'reason', label: 'Reason for Delay (if any)', type: 'text', maxLength: 200, required: true },
            ]
        },

        // Board Approval - Rejected By Board
        boardApprovalRejectedByBoard: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Rejected By Board',
            updateDialogTitle: 'Update Rejected By Board',
            viewDialogTitle: 'View Rejected By Board',
            createFunction: BoardApprovalService.prototype.createRejectedByBoard,
            updateFunction: BoardApprovalService.prototype.updateRejectedByBoard,
            createSuccessMessage: 'Rejected by board added successfully',
            updateSuccessMessage: 'Rejected by board updated successfully',
            trackObjectAfterCreateAndUpdate: 'boardApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'meetingNumber', label: 'Meeting Number', type: 'text', maxLength: 10, pattern: NUMERIC_ONLY_REGEX, 
                    required: true },
                { row: 1, span: 6, name: 'meetingDate', label: 'Meeting Date', type: 'date', 
                    minValue: 'projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO' },
                { row: 2, span: 12, name: 'details', label: 'Reason for Rejection', type: 'text', maxLength: 200, required: true },
            ]
        },

        // Board Approval - Approval By Board
        boardApprovalApprovalByBoard: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Board Approval',
            updateDialogTitle: 'Update Board Approval',
            viewDialogTitle: 'View Board Approval',
            createFunction: BoardApprovalService.prototype.createApprovalByBoard,
            updateFunction: BoardApprovalService.prototype.updateApprovalByBoard,
            createSuccessMessage: 'Approval by board added successfully',
            updateSuccessMessage: 'Approval by board updated successfully',
            trackObjectAfterCreateAndUpdate: 'boardApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'meetingNumber', label: 'Meeting Number', type: 'text', maxLength: 10, pattern: NUMERIC_ONLY_REGEX, 
                    required: true },
                { row: 1, span: 6, name: 'meetingDate', label: 'Meeting Date', type: 'date', 
                    minValue: 'projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO' },
                { row: 2, span: 12, name: 'details', label: 'Remarks', type: 'text', maxLength: 200, required: true },
            ]
        },

        // Board Approval - Rejected By Customer
        boardApprovalRejectedByCustomer: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Rejected By Customer',
            updateDialogTitle: 'Update Rejected By Customer',
            viewDialogTitle: 'View Rejected By Customer',
            createFunction: BoardApprovalService.prototype.createRejectedByCustomer,
            updateFunction: BoardApprovalService.prototype.updateRejectedByCustomer,
            createSuccessMessage: 'Rejected by customer added successfully',
            updateSuccessMessage: 'Rejected by customer updated successfully',
            trackObjectAfterCreateAndUpdate: 'boardApproval',
            fieldsConfig: [
                { row: 1, span: 6, name: 'approvalByBoardMeetingNumber', label: 'Board Approval Meeting Number', type: 'select', required: true, 
                    displayKey: 'meetingNumber', valueKey: 'meetingNumber', viewOperationKey: 'approvalByBoardMeetingNumber' },
                { row: 1, span: 6, name: 'meetingDate', label: 'Meeting Date', type: 'date', maxValue: 'currentDate', 
                    minValue: 'projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO' },
                { row: 2, span: 12, name: 'rejectionCategory', label: 'Rejection Category', type: 'select', required: true, displayKey: 'value', 
                    valueKey: 'code', viewOperationKey: 'rejectionCategoryDescription' },
                { row: 3, span: 12, name: 'details', label: 'Reason for Rejection', type: 'text', maxLength: 200, required: true },
            ]
        }
    },
};
