import { FormGroup } from "@angular/forms";
import { FIFTEEN_COMMA_TWO, FIVE_COMMA_TWO, NUMERIC_ONLY_REGEX, SEVEN_COMMA_TWO, SHARE_HOLDING_PERCENTAGE_REGEX, TAX_PERCENTAGE_REGEX } from "../../../common/common.regex";
import { ProjectProposalService } from "../../loan-contract-search/functional-stage/process-enquiry/project-proposal/project-proposal.service";
import { StageConfig } from "./generic-config.model";

// Project Proposal - list, update dialog and update component configurations
export const projectProposalStageConfig: StageConfig = {
    service: ProjectProposalService,

    list: {
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
                {name: 'revenue', header: 'Revenue', type: 'number'},
                {name: 'depreciation', header: 'Depreciation', type: 'number'},
                {name: 'pbt', header: 'PBT', type: 'number'},
                {name: 'netCashAccruals', header: 'Net Cash Accruals', type: 'number'},
                {name: 'ebitda', header: 'EBITDA', type: 'number'},
                {name: 'interestExpenses', header: 'Interest Expenses', type: 'number'},
                {name: 'pat', header: 'PAT', type: 'number'}
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
        }
    },

    updateDialog: {
        // Project Proposal - Credit Rating
        projectProposalCreditRating: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createDialogTitle: 'Add Credit Rating Details',
            updateDialogTitle: 'Update Credit Rating Details',
            viewDialogTitle: 'View Credit Rating Details',
            createFunction: ProjectProposalService.prototype.createProjectProposalCreditRating,
            updateFunction: ProjectProposalService.prototype.updateProjectProposalCreditRating,
            createSuccessMessage: 'Credit rating details created successfully',
            updateSuccessMessage: 'Credit rating details updated successfully',
            trackObjectAfterCreateAndUpdate: 'projectProposal',
            fieldsConfig: [
                {row: 1, span: 12, name: 'creditRating', label: 'Credit Rating', type: 'select', required: true, displayKey: 'value', valueKey: 'code',
                    viewOperationKey: 'creditRatingDescription' },
                {row: 1, span: 12, name: 'creditRatingAgency', label: 'Credit Rating Agency', type: 'select', displayKey: 'value', valueKey: 'code',
                    viewOperationKey: 'creditRatingAgencyDescription' },
                {row: 1, span: 12, name: 'creditStandingInstruction', label: 'Credit Standing Instruction', type: 'text', maxLength: 100},
                {row: 1, span: 12, name: 'creditStandingText', label: 'Credit Standing Text', type: 'text', maxLength: 100},
            ]
        },

        // Project Proposal - Share Holding
        projectProposalShareHolding: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createDialogTitle: 'Add Share Holding Details',
            updateDialogTitle: 'Update Share Holding Details',
            viewDialogTitle: 'View Share Holding Details',
            createFunction: ProjectProposalService.prototype.createProjectProposalShareHolder,
            updateFunction: ProjectProposalService.prototype.updateProjectProposalShareHolder,
            createSuccessMessage: 'Share holding details created successfully',
            updateSuccessMessage: 'Share holding details updated successfully',
            trackObjectAfterCreateAndUpdate: 'projectProposal',
            fieldsConfig: [
                {row: 1, span: 12, name: 'companyName', label: 'Company Name', type: 'text', maxLength: 200, required: true},
                {row: 2, span: 12, name: 'equityCapital', label: 'Capital (Crores)', type: 'number', maxLength: 18, pattern: FIFTEEN_COMMA_TWO},
                {row: 3, span: 12, name: 'percentageHolding', label: 'Percentage Holding', type: 'number', maxLength: 6, pattern: SHARE_HOLDING_PERCENTAGE_REGEX},
            ]
        },

        // Project Proposal - Other Loan Details Documents
        projectProposalOtherLoanDetailsDocuments: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createDialogTitle: 'Add Document',
            updateDialogTitle: 'Update Document',
            viewDialogTitle: 'View Document',
            createFunction: ProjectProposalService.prototype.createOtherLoanDetailsDocument,
            updateFunction: ProjectProposalService.prototype.updateOtherLoanDetailsDocument,
            createSuccessMessage: 'Document created successfully',
            updateSuccessMessage: 'Document updated successfully',
            trackObjectAfterCreateAndUpdate: 'projectProposal',
            fieldsConfig: [
                {row: 2, span: 12, name: 'documentType', label: 'Document Type', type: 'select', displayKey: 'description', valueKey: 'code', 
                    viewOperationKey: 'documentTypeName', required: true },
                {row: 2, span: 12, name: 'documentName', label: 'Document Name', type: 'text', maxLength: 100, required: true},
                {row: 3, span: 12, name: 'file', label: 'Select file to upload', type: 'file', required: false },
            ]
        },

        // Project Proposal - Promoter Financials
        projectProposalPromoterFinancials: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createDialogTitle: 'Add Promoter Financial Details',
            updateDialogTitle: 'Update Promoter Financial Details',
            viewDialogTitle: 'View Promoter Financial Details',
            createFunction: ProjectProposalService.prototype.createPromoterFinancials,
            updateFunction: ProjectProposalService.prototype.updatePromoterFinancials,
            createSuccessMessage: 'Promoter financial details created successfully',
            updateSuccessMessage: 'Promoter financial details updated successfully',
            trackObjectAfterCreateAndUpdate: 'projectProposal',
            fieldsConfig: [
                // Section: Financials of Previous Fiscal Periods
                { row: 1, span: 12, name: 'header1', type: 'header', label: 'Financials of Previous Fiscal Periods' },
                { row: 2, span: 3, name: 'fiscalPeriod', label: 'Fiscal Period', type: 'text', maxLength: 10, required: true },

                // Section: Profit & Loss
                { row: 3, span: 12, name: 'header2', type: 'header', label: 'Profit & Loss' },
                { row: 3, span: 3, name: 'revenue', label: 'Revenue', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 3, name: 'depreciation', label: 'Depreciation', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 3, name: 'pbt', label: 'PBT (after exceptionals)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 4, span: 3, name: 'netCashAccruals', label: 'Net Cash Accruals', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 4, span: 3, name: 'ebitda', label: 'EBITDA', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 4, span: 3, name: 'interestExpense', label: 'Interest Expense', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 4, span: 3, name: 'pat', label: 'PAT', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },

                // Section: Balance Sheet
                { row: 5, span: 12, name: 'header3', type: 'header', label: 'Balance Sheet' },
                { row: 6, span: 3, name: 'wcDebt', label: 'WC/ST Debt', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 6, span: 3, name: 'totalOutstandingLiabilities', label: 'Total Outstanding Liabilities (TOL)', type: 'text', maxLength: 18, 
                    pattern: FIFTEEN_COMMA_TWO },
                { row: 6, span: 3, name: 'adjustedTangibleNetWorth', label: 'Adjusted Tangible Net Worth (ATNW)', type: 'text', maxLength: 18, 
                    pattern: FIFTEEN_COMMA_TWO },
                { row: 6, span: 3, name: 'subAsso', label: 'Inv. in Sub/Asso.', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 7, span: 3, name: 'cpltd', label: 'CPLTD (Current Portion of Long Term Debt)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 7, span: 3, name: 'shareCapital', label: 'Share Capital', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 7, span: 3, name: 'cashAndBankBalance', label: 'Cash and Bank Balance', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 7, span: 3, name: 'netFixedAssets', label: 'Net Fixed Assets(inc cwip)', type: 'text', maxLength: 18 },
                { row: 8, span: 3, name: 'ltDebt', label: 'LT Debt (excl CPLTD)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 8, span: 3, name: 'reservesAndSurplus', label: 'Reserves and Surplus', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 8, span: 3, name: 'currentAssets', label: 'Current Assets', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 8, span: 3, name: 'quasiEquity', label: 'FCCB/Quasi Equity', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 9, span: 3, name: 'totalDebt', label: 'Total Debt', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 9, span: 3, name: 'tangibleNetWorth', label: 'Tangible Net Worth(TNW)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 9, span: 3, name: 'currentLiabilities', label: 'Current Liabilities (incl. CPLTD)', type: 'text', maxLength: 18, 
                    pattern: FIFTEEN_COMMA_TWO },

                // Section: Ratios
                { row: 10, span: 12, name: 'header4', type: 'header', label: 'Ratios' },
                { row: 11, span: 3, name: 'ebitdaMarginPercentage', label: 'EBITDA Margin %', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 11, span: 3, name: 'totalDebtEbitda', label: 'Total Debt/ EBITDA', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 11, span: 3, name: 'totalDebtTnw', label: 'Total Debt/ TNW', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 11, span: 3, name: 'currentRatio', label: 'Current Ratio', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 12, span: 3, name: 'ebitdaInterest', label: 'EBITDA/ Interest', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 12, span: 3, name: 'termDebtEbitda', label: 'Term Debt/ EBITDA', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 12, span: 3, name: 'tnw', label: 'TOL/TNW', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 13, span: 3, name: 'cashDscr', label: 'Cash DSCR', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO },
                { row: 13, span: 3, name: 'dscr', label: 'DSCR', type: 'text', maxLength: 8, pattern: FIVE_COMMA_TWO }
            ]
        },

        // Project Proposal - Collateral Details
        projectProposalCollateralDetails: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createDialogTitle: 'Add Collateral Details',
            updateDialogTitle: 'Update Collateral Details',
            viewDialogTitle: 'View Collateral Details',
            createFunction: ProjectProposalService.prototype.createCollateralDetails,
            updateFunction: ProjectProposalService.prototype.updateCollateralDetails,
            createSuccessMessage: 'Collateral details created successfully',
            updateSuccessMessage: 'Collateral details updated successfully',
            trackObjectAfterCreateAndUpdate: 'projectProposal',
            fieldsConfig: [
                { row: 1, span: 12, name: 'collateralType', label: 'Collateral Type', type: 'select', displayKey: 'value', valueKey: 'code', 
                    viewOperationKey: 'collateralTypeDescription', required: true },
                { row: 2, span: 12, name: 'details', label: 'Details', type: 'text', maxLength: 200 }
            ]
        }
    },

    update: {
        // Project Proposal
        projectProposal: {
            searchString1ForCreate: 'loanApplicationId',
            passSearchString1Via: 'Object',
            searchString2ForCreate: 'enquiryActionId',
            passSearchString2Via: 'Object',
            createFunction: ProjectProposalService.prototype.createProjectProposal,
            updateFunction: ProjectProposalService.prototype.updateProjectProposal,
            createSuccessMessage: 'Project proposal created successfully',
            updateSuccessMessage: 'Project proposal updated successfully',
            trackObjectAfterCreateAndUpdate: 'enquiryAction',
            fieldsConfig: [
                {row: 1, span: 3, name: 'loanEnquiryNumber', label: 'Loan Enquiry Id', type: 'text', readOnly: true },
                {row: 1, span: 3, name: 'proposalFormSharingDate', label: 'Date of Proposal', type: 'date' },
                {row: 1, span: 3, name: 'proposalStatus', label: 'Status', type: 'select', required: true, displayKey: 'description', valueKey: 'code' },
                {row: 1, span: 3, name: 'documentName', label: 'Document Name', type: 'text', maxLength: 100 },

                {row: 2, span: 3, name: 'documentType', label: 'Document Type', type: 'select', displayKey: 'description', valueKey: 'code', 
                    viewOperationKey: 'documentTypeName' },
                {row: 2, span: 3, name: 'documentVersion', label: 'Document Version', type: 'text', maxLength: 10 },
                {row: 2, span: 3, name: 'file', displayKey: 'fileReference', label: 'Select file to upload', type: 'file', required: false },
                {row: 2, span: 3, name: 'additionalDetails', label: 'Additional Details', type: 'text', maxLength: 100 }
            ]
        },

        // Project Proposal - Project Details
        projectProposalProjectDetails: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createFunction: ProjectProposalService.prototype.createProjectDetail,
            updateFunction: ProjectProposalService.prototype.updateProjectDetail,
            createSuccessMessage: 'Project details created successfully',
            updateSuccessMessage: 'Project details updated successfully',
            trackObjectAfterCreateAndUpdate: 'enquiryAction',
            fieldsConfig: [
                {row: 1, span: 12, name: 'header1', type: 'header', label: 'Project Details' },

                {row: 2, span: 3, name: 'projectName', label: 'Name of the Project', type: 'text', maxLength: 100 },
                {row: 2, span: 3, name: 'status', label: 'Status', type: 'select', displayKey: 'description', valueKey: 'code' },
                {row: 2, span: 3, name: 'borrowerName', label: 'Borrower Name', type: 'text', maxLength: 150 },
                {row: 2, span: 3, name: 'promoterName', label: 'Name of Sponsor/Group', type: 'text', maxLength: 150 },

                {row: 3, span: 12, name: 'header2', type: 'header', label: 'Brief Project Summary' },

                {row: 4, span: 12, name: 'loanPurpose', label: 'Purpose of Loan', type: 'text', maxLength: 250 },

                {row: 5, span: 3, name: 'projectCapacity', label: 'Project Capacity', type: 'text', maxLength: 10, pattern: SEVEN_COMMA_TWO },
                {row: 5, span: 3, name: 'projectCapacityUnit', label: 'Project Capacity Unit', type: 'select', displayKey: 'value', valueKey: 'code', 
                    nullOption: true, required: { dependsOn: 'projectCapacity' }},
                {row: 5, span: 3, name: 'state', label: 'Project Location', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 5, span: 3, name: 'district', label: 'Project District', type: 'text', maxLength: 100 },

                {row: 6, span: 12, name: 'header3', type: 'header', label: 'Loan Type, Amounts and Interest Rate' },

                {row: 7, span: 3, name: 'loanEnquiryDate', label: 'Loan Enquiry Date', type: 'date', maxValue: 'currentDate' },
                {row: 7, span: 3, name: 'loanClass', label: 'Loan Class (Sector)', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 7, span: 3, name: 'projectType', label: 'Project Type (Sub Sector)', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 7, span: 3, name: 'projectTypeCoreSector', label: 'Project Core Sector', type: 'select', displayKey: 'value', valueKey: 'code' },

                {row: 8, span: 3, name: 'financingType', label: 'Financing Type', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 8, span: 3, name: 'assistanceType', label: 'Type of Assistance', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 8, span: 3, name: 'loanType', label: 'Loan Type (Type of Loan)', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },
                {row: 8, span: 3, name: 'purposeOfLoan', label: 'Purpose of Loan', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true },

                // {row: 9, span: 3, name: 'policyExposure', label: 'Policy Exposure', type: 'select', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                {row: 9, span: 3, name: 'policyExposure', label: 'Policy Exposure', type: 'select', displayKey: 'value', valueKey: 'code', nullOption: true},
                {row: 9, span: 9, name: 'endUseOfFunds', label: 'End use of the funds to be availed from PFS', type: 'text', maxLength: 100 },

                {row: 10, span: 3, name: 'roi', label: 'Rate of Interest', type: 'text', maxLength: 5, pattern: TAX_PERCENTAGE_REGEX },
                {row: 10, span: 3, name: 'fees', label: 'Fees', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                {row: 10, span: 3, name: 'tenorYear', label: 'Tenure (Years)', type: 'text', maxLength: 2, pattern: NUMERIC_ONLY_REGEX },
                {row: 10, span: 3, name: 'tenorMonths', label: 'Tenure (Months)', type: 'text', maxLength: 2, pattern: NUMERIC_ONLY_REGEX },

                {row: 11, span: 3, name: 'moratoriumPeriod', label: 'Moratorium Period', type: 'text', maxLength: 2, pattern: NUMERIC_ONLY_REGEX },
                {row: 11, span: 3, name: 'moratoriumPeriodUnit', label: 'Moratorium Period Unit', type: 'select', displayKey: 'description', valueKey: 'code', 
                    nullOption: true, required: { dependsOn: 'moratoriumPeriod' } },
                {row: 11, span: 3, name: 'constructionPeriod', label: 'Construction Period', type: 'text', maxLength: 2, pattern: NUMERIC_ONLY_REGEX },
                {row: 11, span: 3, name: 'constructionPeriodUnit', label: 'Construction Period Unit', type: 'select', displayKey: 'description', valueKey: 'code', 
                    nullOption: true, required: { dependsOn: 'constructionPeriod' } }
            ]
        },

        // Project Proposal - Project Cost Details
        projectProposalProjectCostDetails: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createFunction: ProjectProposalService.prototype.createProjectProposalProjectCost,
            updateFunction: ProjectProposalService.prototype.updateProjectProposalProjectCost,
            createSuccessMessage: 'Project cost details created successfully',
            updateSuccessMessage: 'Project cost details updated successfully',
            trackObjectAfterCreateAndUpdate: 'enquiryAction',
            genericOnValueChangeFunction: (formGroup?: FormGroup, fieldName?: string) => {
                // Calculate debt equity ratio without grant
                var debt = isNaN(Number(formGroup?.get('debt')?.value)) ? 0 : Number(formGroup?.get('debt')?.value);
                var equity = isNaN(Number(formGroup?.get('equity')?.value)) ? 0 : Number(formGroup?.get('equity')?.value);
                if (equity > 0)
                    formGroup?.get('debtEquityRatio')?.setValue((debt/equity).toFixed(2));
                else if (equity == 0)
                    formGroup?.get('debtEquityRatio')?.setValue(0);

                // Calculate debt equity ratio with grant
                const grantAmount = isNaN(Number(formGroup?.get('grantAmount')?.value)) ? 0 : Number(formGroup?.get('grantAmount')?.value);
                if (debt && equity && grantAmount && equity > 0) {
                    const ratioWithGrant = (grantAmount + debt) / equity;
                    formGroup?.get('debtEquityRatioWithGrant')?.setValue(ratioWithGrant.toFixed(2));
                } else {
                    formGroup?.get('debtEquityRatioWithGrant')?.setValue('');
                }
            },
            fieldsConfig: [
                { row: 1, span: 12, name: 'header1', type: 'header', label: 'Project Cost Details' },

                { row: 2, span: 3, name: 'projectCost', label: 'Project Cost', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 2, span: 3, name: 'debt', label: 'Debt (Crores)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO,
                    onValueChange: true 
                },
                { row: 2, span: 3, name: 'equity', label: 'Promoter Contribution (Crores)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO,
                    onValueChange: true 
                },
                { row: 2, span: 3, name: 'pfsDebtAmount', label: 'PFS Debt Amount (Crores)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                { row: 3, span: 3, name: 'debtEquityRatio', label: 'Debt/Equity Ratio without Grant', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO,
                    readOnly: true
                },
                { row: 3, span: 3, name: 'grantAmount', label: 'Grant/Subsidy Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO,
                    onValueChange: true
                },
                { row: 3, span: 3, name: 'debtEquityRatioWithGrant', label: 'Debt/Equity Ratio with Grant', type: 'text', maxLength: 18, 
                    pattern: FIFTEEN_COMMA_TWO, readOnly: true 
                }
            ]
        },

        // Project Proposal - Other Loan Details
        projectProposalOtherLoanDetails: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createFunction: ProjectProposalService.prototype.createProjectProposalOtherLoanDetails,
            updateFunction: ProjectProposalService.prototype.updateProjectProposalOtherLoanDetails,
            createSuccessMessage: 'Other loan details created successfully',
            updateSuccessMessage: 'Other loan details updated successfully',
            trackObjectAfterCreateAndUpdate: 'enquiryAction',
            fieldsConfig: [
                { row: 1, span: 12, name: 'header1', type: 'header', label: 'Cash Flow Details' },

                {row: 2, span: 12, name: 'sourceAndCashFlow', label: 'Source and cash flow of repayment of loan and Interest (Pls also share the excel ' +
                    'calculations of both DSCR and Cash DSCR separately)', type: 'text', maxLength: 2000 },
                    
                { row: 3, span: 12, name: 'header2', type: 'header', label: 'Other Loan Details' },

                {row: 4, span: 3, name: 'optimumDateOfLoan', label: 'Optimum date by which loan is required?', type: 'date'},
                {row: 4, span: 3, name: 'consolidatedGroupLeverage', label: 'Consolidated Group Leverage', type: 'text', maxLength: 18, 
                    pattern: FIFTEEN_COMMA_TWO },
                {row: 4, span: 3, name: 'totalDebtTNW', label: 'Total Debt/TNW', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
                {row: 4, span: 3, name: 'tolTNW', label: 'TOL/TNW', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },

                {row: 5, span: 3, name: 'totalDebtTNWPercentage', label: 'Total Debt/TNW Percentage', type: 'text', maxLength: 6, 
                    pattern: SHARE_HOLDING_PERCENTAGE_REGEX },
                {row: 5, span: 3, name: 'tolTNWPercentage', label: 'TOL/TNW Percentage', type: 'text', maxLength: 6, pattern: SHARE_HOLDING_PERCENTAGE_REGEX },
                {row: 5, span: 3, name: 'delayInDebtServicing', label: 'Delays in Debt Servicing/Status of SMA etc. with various lenders', type: 'text', 
                    maxLength: 2000 }
            ]
        },

        // Project Proposal - Deal Guarantee
        projectProposalDealGuarantee: {
            searchString1ForCreate: 'projectProposalId',
            passSearchString1Via: 'Object',
            createFunction: ProjectProposalService.prototype.createDealGuaranteeTimeline,
            updateFunction: ProjectProposalService.prototype.updateDealGuaranteeTimeline,
            createSuccessMessage: 'Deal guarantee timeline details created successfully',
            updateSuccessMessage: 'Deal guarantee timeline details updated successfully',
            trackObjectAfterCreateAndUpdate: 'enquiryAction',
            fieldsConfig: [
                {row: 1, span: 12, name: 'dealTransactionStructure', label: 'Deal Transaction Structure', type: 'text', maxLength: 500 },
                {row: 2, span: 12, name: 'statusOfPBGAndMABG', label: 'Status of PBG and MABG if applicable', type: 'text', maxLength: 500},
                {row: 3, span: 12, name: 'timelinesMilestones', label: 'Timeline of the project milestones from the day of sanction to the day of proposed ' +
                    'completion', type: 'text', maxLength: 500 },
                {row: 4, span: 12, name: 'strengths', label: 'Strengths of the Project/Proposal', type: 'text', maxLength: 500 },
                {row: 5, span: 12, name: 'fundingArrangement', label: 'Details of Funding arrangements from sources other than PFS', type: 'text', maxLength: 500 },
                {row: 6, span: 12, name: 'disbursementStageSchedule', label: 'Schedule of stages of disbursement of the loan', type: 'text', maxLength: 500, },
                {row: 7, span: 12, name: 'offensesEnquiry', label: 'Any CBI/ Economic Offense Enquiry', type: 'text', maxLength: 500 },
                {row: 8, span: 12, name: 'existingRelationsPFSPTC', label: 'Existing Relations of the Borrower and Group with PFS/PTC', type: 'text', 
                    maxLength: 500 },
                {row: 9, span: 12, name: 'deviations', label: 'Deviation w.r.t operating guidelines/other policies of PFS', type: 'text', maxLength: 500 },
                {row: 10, span: 12, name: 'environmentalSystemCategory', label: 'Environmental System Category', type: 'select', displayKey: 'value', 
                    valueKey: 'code', viewOperationKey: 'environmentalSystemCategory', nullOption: true },
                {row: 11, span: 12, name: 'esmsCategorization', label: 'Environmental and Social Management System Categorization Remarks', type: 'text', 
                    maxLength: 500 },
                {row: 12, span: 12, name: 'otherProjectDetails', label: 'Other Project Details', type: 'text', maxLength: 500 }
            ]
        }
    },
};
