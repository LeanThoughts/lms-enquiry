import { RiskAssessmentService } from "../../loan-contract-search/functional-stage/risk-assessment/risk-assessment.service";
import { StageConfig } from "./generic-config.model";

// Prelim Risk Assessment - list, update dialog and update component configurations
export const riskAssessmentStageConfig: StageConfig = {
    service: RiskAssessmentService,

    list: {
        // Risk Assessment - Preliminary Risk Assessment
        riskAssessmentPreliminaryRiskAssessment: {
            displayedColumns: [
                {name: 'dateOfAssessment', header: 'Date of Assessment', type: 'date'},
                {name: 'remarksByRiskDepartment', header: 'Remarks By Risk Department', type: 'text'},
                {name: 'mdApprovalDate', header: 'MD Approval Date', type: 'date'},
                {name: 'documentTitle', header: 'Document Title', type: 'text'},
                {name: 'documentType', header: 'Document Type', type: 'text'},
                {name: 'fileReference', header: 'Document', type: 'file'},
                {name: 'remarks', header: 'Remarks', type: 'text'},
            ],
            fetchFunction: RiskAssessmentService.prototype.getPreliminaryRiskAssessment,
            createButton: true,
            disableCreateButtonAfterCreate: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '45rem',
            viewDialogWidth: '40rem',
            routeResolvedData: ['approvalByIcc', 'documentType'],
        }
    },

    updateDialog: {
        // Risk Assessment - Preliminary Risk Assessment
        riskAssessmentPreliminaryRiskAssessment: {
            searchString1ForCreate: 'riskAssessmentId',
            passSearchString1Via: 'Object',
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Preliminary Risk Assessment Details',
            updateDialogTitle: 'Update Preliminary Risk Assessment Details',
            viewDialogTitle: 'View Preliminary Risk Assessment Details',
            createFunction: RiskAssessmentService.prototype.createPreliminaryRiskAssessment,
            updateFunction: RiskAssessmentService.prototype.updatePreliminaryRiskAssessment,
            createSuccessMessage: 'Preliminary Risk Assessment details created successfully',
            updateSuccessMessage: 'Preliminary Risk Assessment details updated successfully',
            trackObjectAfterCreateAndUpdate: 'riskAssessment',
            fieldsConfig: [
                { row: 1, span: 6, name: 'dateOfAssessment', label: 'Date of Assessment', type: 'date', maxValue: 'currentDate', minValue: 'approvalByIcc.meetingDate' },
                { row: 2, span: 12, name: 'remarksByRiskDepartment', label: 'Remarks by Risk Department', type: 'text', maxLength: 200, required: true },
                { row: 3, span: 6, name: 'mdApprovalDate', label: 'MD Approval Date', type: 'date', maxValue: 'currentDate', minValue: 'dateOfAssessment' },
                { row: 3, span: 6, name: 'documentTitle', label: 'Document Title', type: 'text', maxLength: 20 },
                { row: 4, span: 6, name: 'documentType', label: 'Document Type', type: 'select', displayKey: 'description', valueKey: 'code' },
                { row: 4, span: 6, name: 'file', label: 'Upload Document (PDF)', type: 'file' },
                { row: 5, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200, required: true },
            ]
        }
    },
};
