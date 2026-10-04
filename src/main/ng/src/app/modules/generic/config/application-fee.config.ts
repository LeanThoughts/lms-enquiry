import { FormGroup } from "@angular/forms";
import { FIFTEEN_COMMA_TWO, NUMERIC_ONLY_REGEX, SEVEN_COMMA_TWO, TAX_PERCENTAGE_REGEX } from "../../../common/common.regex";
import { ApplicationFeeService } from "../../loan-contract-search/functional-stage/application-fee/application-fee.service";
import { StageConfig } from "./generic-config.model";

// Application Fee - list, update dialog and update component configurations
export const applicationFeeStageConfig: StageConfig = {
    service: ApplicationFeeService,

    list: {
        // Application Fee - Term Sheets
        applicationFeeTermSheets: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'status', header: 'Status', type: 'text'},
                {name: 'issuanceDate', header: 'Date of Issuance', type: 'date'},
                {name: 'acceptanceDate', header: 'Acceptance Date', type: 'date'},
                {name: 'fileReference', header: 'Document', type: 'file'},
            ],
            fetchFunction: ApplicationFeeService.prototype.getTermSheets,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '35rem',
            viewDialogWidth: '35rem',
            routeResolvedData: ['preliminaryRiskAssessment', 'status'],
        },

        // Application Fee - Formal Requests
        applicationFeeFormalRequests: {
            displayedColumns: [
                {name: 'serialNumber', header: 'Serial Number', type: 'text'},
                {name: 'documentName', header: 'Document Name', type: 'text'},
                {name: 'uploadDate', header: 'Upload Date', type: 'date'},
                {name: 'documentLetterDate', header: 'Document Letter Date', type: 'date'},
                {name: 'documentReceivedDate', header: 'Document Received Date', type: 'date'},
                {name: 'fileReference', header: 'Document', type: 'file'},
            ],
            fetchFunction: ApplicationFeeService.prototype.getFormalRequests,
            createButton: true,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '40rem',
            viewDialogWidth: '40rem',
        },

        // Application Fee - Application/Inception Fee Receipts
        applicationFeeInceptionFees: {
            displayedColumns: [
                {name: 'description', header: 'Fee Type', type: 'text'},
                {name: 'statusDescription', header: 'Status', type: 'text'},
                {name: 'invoiceNumber', header: 'Invoice Number', type: 'text'},
                {name: 'invoiceDate', header: 'Invoice Date', type: 'date'},
                {name: 'amount', header: 'Amount', type: 'number'},
                {name: 'taxAmount', header: 'Tax Amount', type: 'number'},
                {name: 'totalAmount', header: 'Total Amount', type: 'number'},
                {name: 'amountReceived', header: 'Amount Received', type: 'number'},
                {name: 'rtgsNumber', header: 'RTGS Number', type: 'text'},
                {name: 'referenceNumber', header: 'Reference Number', type: 'text'},
                {name: 'remarks', header: 'Remarks', type: 'text'},
            ],
            fetchFunction: ApplicationFeeService.prototype.getInceptionFees,
            createButton: false,
            updateButton: true,
            viewButton: true,
            updateDialogWidth: '45rem',
            viewDialogWidth: '45rem',
        }
    },

    updateDialog: {
        // Application Fee - Term Sheets
        applicationFeeTermSheets: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Term Sheet',
            updateDialogTitle: 'Update Term Sheet',
            viewDialogTitle: 'View Term Sheet',
            createFunction: ApplicationFeeService.prototype.createTermSheet,
            updateFunction: ApplicationFeeService.prototype.updateTermSheet,
            createSuccessMessage: 'Term Sheet details added successfully',
            updateSuccessMessage: 'Term Sheet details updated successfully',
            trackObjectAfterCreateAndUpdate: 'applicationFee',
            fieldsConfig: [
                { row: 1, span: 12, name: 'status', label: 'Status', type: 'select', required: true, displayKey: 'description', valueKey: 'code' },
                { row: 2, span: 12, name: 'issuanceDate', label: 'Date of Issuance', type: 'date', required: true, maxValue: 'currentDate', 
                    minValue: 'preliminaryRiskAssessment.dateOfAssessment', visibleWhen: { field: 'status', value: 'Draft' } },
                { row: 3, span: 12, name: 'acceptanceDate', label: 'Acceptance Date', type: 'date', required: true, maxValue: 'currentDate', 
                    visibleWhen: { field: 'status', value: 'Final' } },
                { row: 4, span: 12, name: 'file', label: 'Upload Document (PDF)', type: 'file' },
            ]
        },

        // Application Fee - Formal Requests
        applicationFeeFormalRequests: {
            searchString2ForCreate: 'loanApplicationId',
            passSearchString2Via: 'Object',
            createDialogTitle: 'Add Formal Request',
            updateDialogTitle: 'Update Formal Request',
            viewDialogTitle: 'View Formal Request',
            createFunction: ApplicationFeeService.prototype.createFormalRequest,
            updateFunction: ApplicationFeeService.prototype.updateFormalRequest,
            createSuccessMessage: 'Formal Request details added successfully',
            updateSuccessMessage: 'Formal Request details updated successfully',
            trackObjectAfterCreateAndUpdate: 'applicationFee',
            fieldsConfig: [
                { row: 1, span: 12, name: 'documentName', label: 'Document Name', type: 'text', maxLength: 100 },
                { row: 2, span: 6, name: 'uploadDate', label: 'Upload Date', type: 'date', maxValue: 'currentDate' },
                { row: 2, span: 6, name: 'documentLetterDate', label: 'Document Letter Date', type: 'date', maxValue: 'currentDate' },
                { row: 3, span: 6, name: 'documentReceivedDate', label: 'Document Received Date', type: 'date', maxValue: 'currentDate', 
                    minValue: 'documentLetterDate' },
                { row: 3, span: 6, name: 'file', label: 'Upload Document (PDF)', type: 'file' },
            ]
        },

        // Application Fee - Application/Inception Fee Receipts. Fee records come from SAP; only the receipt details can be changed.
        applicationFeeInceptionFees: {
            updateDialogTitle: 'Update Inception Fee',
            viewDialogTitle: 'View Inception Fee',
            updateFunction: ApplicationFeeService.prototype.updateInceptionFee,
            updateSuccessMessage: 'Inception fee details updated successfully',
            trackObjectAfterCreateAndUpdate: 'applicationFee',
            fieldsConfig: [
                { row: 1, span: 6, name: 'description', label: 'Fee Type', type: 'text', readOnly: true },
                { row: 1, span: 6, name: 'statusDescription', label: 'Status', type: 'text', readOnly: true },
                { row: 2, span: 6, name: 'invoiceNumber', label: 'Invoice Number', type: 'text', readOnly: true },
                { row: 2, span: 6, name: 'invoiceDate', label: 'Invoice Date', type: 'text', readOnly: true },
                { row: 3, span: 4, name: 'amount', label: 'Amount', type: 'text', readOnly: true },
                { row: 3, span: 4, name: 'taxAmount', label: 'Tax Amount', type: 'text', readOnly: true },
                { row: 3, span: 4, name: 'totalAmount', label: 'Total Amount', type: 'text', readOnly: true },
                { row: 4, span: 6, name: 'amountReceived', label: 'Amount Received', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 4, span: 6, name: 'rtgsNumber', label: 'RTGS Number', type: 'text', maxLength: 100 },
                { row: 5, span: 6, name: 'referenceNumber', label: 'Reference Number', type: 'text', maxLength: 100 },
                { row: 6, span: 12, name: 'remarks', label: 'Remarks', type: 'text', maxLength: 200 },
            ]
        }
    },

    update: {
        // Application Fee - Project Details
        applicationFeeProjectDetails: {
            searchString1ForCreate: 'loanApplicationId',
            passSearchString1Via: 'Object',
            createFunction: ApplicationFeeService.prototype.createProjectDetails,
            updateFunction: ApplicationFeeService.prototype.updateProjectDetails,
            createSuccessMessage: 'Project details saved successfully',
            updateSuccessMessage: 'Project details updated successfully',
            trackObjectAfterCreateAndUpdate: 'applicationFee',
            genericOnValueChangeFunction: (formGroup?: FormGroup) => {
                if (!formGroup) return;
                const debt = Number(formGroup.get('debt')?.value) || 0;
                const equity = Number(formGroup.get('promoterContributionEquity')?.value) || 0;
                const grant = Number(formGroup.get('grantSubsidyAmount')?.value) || 0;
                formGroup.get('debtEquityRatio')?.setValue(equity > 0 ? (debt / equity).toFixed(2) : 0);
                formGroup.get('debtEquityRatioWithGrant')?.setValue(debt && grant && equity > 0 ? ((grant + debt) / equity).toFixed(2) : null);
            },
            fieldsConfig: [
                {row: 1, span: 12, name: 'header1', type: 'header', label: 'Project Details' },

                {row: 2, span: 3, name: 'projectName', label: 'Name of Project', type: 'text', maxLength: 60 },
                {row: 2, span: 3, name: 'promoterName', label: 'Name of Sponsor/Group', type: 'text', maxLength: 60 },
                {row: 2, span: 6, name: 'loanPurpose', label: 'Purpose of Loan', type: 'text', maxLength: 60 },

                {row: 3, span: 3, name: 'projectCapacity', label: 'Project Capacity', type: 'text', maxLength: 10, pattern: SEVEN_COMMA_TWO },
                {row: 3, span: 3, name: 'projectCapacityUnit', label: 'Project Capacity Unit', type: 'select', displayKey: 'value', valueKey: 'code', 
                    nullOption: true },
                {row: 3, span: 3, name: 'state', label: 'State', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 3, span: 3, name: 'productTypeCode', label: 'Product Type', type: 'select', displayKey: 'name', valueKey: 'code', required: true },

                {row: 4, span: 3, name: 'term', label: 'Term', type: 'select', displayKey: 'value', valueKey: 'code', required: true },
                {row: 4, span: 3, name: 'enquiryCompletionDate', label: 'Enquiry Completion Date', type: 'date', required: true, maxValue: 'currentDate' },
                {row: 4, span: 3, name: 'loanType', label: 'Loan Type', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 4, span: 3, name: 'loanClass', label: 'Loan Class', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },

                {row: 5, span: 3, name: 'assistanceType', label: 'Type of Assistance', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 5, span: 3, name: 'financingType', label: 'Financing Type', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 5, span: 3, name: 'projectType', label: 'Project Type (Sub Sector)', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 5, span: 3, name: 'projectTypeCoreSector', label: 'Project Type (Core Sector)', type: 'select', displayKey: 'value', valueKey: 'code' },

                {row: 6, span: 3, name: 'purposeOfLoan', label: 'Purpose of Loan', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },

                {row: 7, span: 12, name: 'header2', type: 'header', label: 'Project Cost and Funding (Crores)' },

                {row: 8, span: 3, name: 'projectCost', label: 'Project Cost', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                {row: 8, span: 3, name: 'debt', label: 'Debt', type: 'text', maxLength: 10, pattern: SEVEN_COMMA_TWO, onValueChange: true },
                {row: 8, span: 3, name: 'promoterContributionEquity', label: 'Promoter Contribution/Equity', type: 'text', maxLength: 10, 
                    pattern: SEVEN_COMMA_TWO, onValueChange: true },
                {row: 8, span: 3, name: 'debtEquityRatio', label: 'Debt:Equity Ratio without Grant', type: 'text', readOnly: true },

                {row: 9, span: 3, name: 'grantSubsidyAmount', label: 'Grant/Subsidy Amount', type: 'text', maxLength: 10, pattern: SEVEN_COMMA_TWO, 
                    onValueChange: true },
                {row: 9, span: 3, name: 'debtEquityRatioWithGrant', label: 'Debt:Equity Ratio with Grant', type: 'text', readOnly: true },
                {row: 9, span: 3, name: 'pfsDebtAmount', label: 'PFS Debt Amount', type: 'text', maxLength: 10, pattern: SEVEN_COMMA_TWO },
                {row: 9, span: 3, name: 'rateOfInterest', label: 'Rate of Interest', type: 'text', maxLength: 5, pattern: TAX_PERCENTAGE_REGEX },

                {row: 10, span: 12, name: 'header3', type: 'header', label: 'Tenure and Periods' },

                {row: 11, span: 3, name: 'tenorYear', label: 'Tenure (Years)', type: 'text', maxLength: 2, pattern: NUMERIC_ONLY_REGEX },
                {row: 11, span: 3, name: 'tenorMonths', label: 'Tenure (Months)', type: 'text', maxLength: 2, pattern: NUMERIC_ONLY_REGEX },

                {row: 12, span: 3, name: 'moratoriumPeriod', label: 'Moratorium Period', type: 'text', maxLength: 4, pattern: NUMERIC_ONLY_REGEX },
                {row: 12, span: 3, name: 'moratoriumPeriodUnit', label: 'Moratorium Period Unit', type: 'select', displayKey: 'description', valueKey: 'code', 
                    nullOption: true, required: { dependsOn: 'moratoriumPeriod' } },
                {row: 12, span: 3, name: 'constructionPeriod', label: 'Construction Period', type: 'text', maxLength: 4, pattern: NUMERIC_ONLY_REGEX },
                {row: 12, span: 3, name: 'constructionPeriodUnit', label: 'Construction Period Unit', type: 'select', displayKey: 'description', valueKey: 'code', 
                    nullOption: true, required: { dependsOn: 'constructionPeriod' } }
            ]
        }
    },
};
