import { EMAIL_REGEX } from "../../../common/common.regex";
import { WorkflowApproverService } from "../../workflow-approver/workflow-approver.service";
import { StageConfig } from "./generic-config.model";

// Each department keeps the same badge color, derived from its numeric code
const departmentColor = (approver: any): number => {
    const code = Number(approver?.departmentCode);
    return Number.isNaN(code) ? 8 : (code % 8) + 1;
};

// Workflow Approvers - list and update dialog configurations
export const workflowApproverStageConfig: StageConfig = {
    service: WorkflowApproverService,

    list: {
        workflowApprovers: {
            displayedColumns: [
                {name: 'departmentCode', header: 'Department Code', type: 'text'},
                {name: 'departmentName', header: 'Department', type: 'badge', badgeColor: departmentColor},
                {name: 'processLabel', header: 'Process', type: 'text'},
                {name: 'approverName', header: 'Approver Name', type: 'text'},
                {name: 'approverEmail', header: 'Approver Email', type: 'text'},
            ],
            fetchFunction: WorkflowApproverService.prototype.getWorkflowApprovers,
            createButton: true,
            updateButton: true,
            deleteButton: true,
            viewButton: true,
            deleteFunction: WorkflowApproverService.prototype.deleteWorkflowApprover,
            deleteConfirmationMessage: 'Are you sure you want to delete this workflow approver? Approvals for this department and process ' +
                'cannot be started until an approver is maintained again.',
            deleteSuccessMessage: 'Workflow approver deleted successfully',
            updateDialogWidth: '35rem',
            viewDialogWidth: '35rem',
            routeResolvedData: ['departmentCode', 'processName'],
        }
    },

    updateDialog: {
        workflowApprovers: {
            createDialogTitle: 'Add Workflow Approver',
            updateDialogTitle: 'Update Workflow Approver',
            viewDialogTitle: 'View Workflow Approver',
            createFunction: WorkflowApproverService.prototype.createWorkflowApprover,
            updateFunction: WorkflowApproverService.prototype.updateWorkflowApprover,
            createSuccessMessage: 'Workflow approver added successfully',
            updateSuccessMessage: 'Workflow approver updated successfully',
            fieldsConfig: [
                { row: 1, span: 12, name: 'departmentCode', label: 'Department', type: 'select', required: true,
                    displayKey: 'description', valueKey: 'code', viewOperationKey: 'departmentName' },
                { row: 2, span: 12, name: 'processName', label: 'Process', type: 'select', required: true,
                    displayKey: 'label', valueKey: 'name', viewOperationKey: 'processLabel' },
                { row: 3, span: 12, name: 'approverName', label: 'Approver Name', type: 'text', required: true, maxLength: 100 },
                { row: 4, span: 12, name: 'approverEmail', label: 'Approver Email', type: 'text', required: true, maxLength: 100,
                    pattern: EMAIL_REGEX },
            ]
        }
    },
};
