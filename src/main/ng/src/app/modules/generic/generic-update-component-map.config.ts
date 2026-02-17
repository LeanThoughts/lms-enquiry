import { FIFTEEN_COMMA_TWO, FIVE_COMMA_TWO, NUMERIC_ONLY_REGEX, SEVEN_COMMA_TWO, SHARE_HOLDING_PERCENTAGE_REGEX, TAX_PERCENTAGE_REGEX } from "../../common/common.regex";
import { ProjectProposalService } from "../loan-contract-search/functional-stage/process-enquiry/project-proposal/project-proposal.service";

interface EntityUpdateComponentConfig {
    // Required for create and update components
    createFunction?: any;
    updateFunction?: any;
    createSuccessMessage?: string;
    updateSuccessMessage?: string;
    trackObjectAfterCreateAndUpdate?: string;
    searchString1ForCreate?: string;
    searchString2ForCreate?: string;
    passSearchString1Via?: string;
    passSearchString2Via?: string;
    fieldsConfig: {
        row: number;        
        span: number;
        name: string;
        label: string;
        type: string;
        nullOption?: boolean;
        required?: any;
        readOnly?: boolean;
        maxLength?: number;
        pattern?: RegExp;
        options?: any[];
        displayFunction?: any; // Function to display the value of the combobox field or to get options for the dependent select field
        itemTemplate?: string;
        displayKey?: string;
        valueKey?: string;
        defaultValue?: any;
        dependsOn?: string; // Name of the field that the dependent select field depends on
        viewOperationKey?: string; // Key to be used to display the value in the view mode
        maxValue?: any; // Maximum value for numeric or date fields
    }[];
}

// Entity-Component configuration
export const entityComponentConfigs: { [key: string]: EntityUpdateComponentConfig } = 
{
    // Business Partner - Basic Information
    businessPartnerBasicInformation: {
        fieldsConfig: [
            {row: 1, span: 12, name: 'header1', type: 'header', label: 'Identification Details' },
            {row: 2, span: 3, name: 'defaultPartnerRole', label: 'Default Partner Role', type: 'select', required: true },
            {row: 2, span: 3, name: 'partnerGroup', label: 'Partner Group', type: 'select', required: true },
            {row: 2, span: 3, name: 'partnerCategory', label: 'Partner Category', type: 'select', required: true },
            {row: 3, span: 3, name: 'externalBPNumber', label: 'External BP Id', type: 'text' },
            {row: 3, span: 3, name: 'title', label: 'Title', type: 'select' },
            {row: 3, span: 3, name: 'partyName1', label: 'Name 1', type: 'text'},
            {row: 3, span: 3, name: 'partyName2', label: 'Name 2', type: 'text'},
            {row: 4, span: 3, name: 'searchTerm1', label: 'Search Term 1', type: 'text', required: true },
            {row: 4, span: 3, name: 'searchTerm2', label: 'Search Term 2', type: 'text' },

            {row: 5, span: 12, name: 'header2', type: 'header', label: 'Address Details' },
            {row: 6, span: 3, name: 'addressLine1', label: 'Street/House No.', type: 'text', required: true },
            {row: 6, span: 3, name: 'addressLine2', label: 'Street2', type: 'text' },
            {row: 6, span: 3, name: 'addressLine3', label: 'Street3', type: 'text' },
            {row: 6, span: 3, name: 'city', label: 'City', type: 'text', required: true },
            {row: 7, span: 3, name: 'postalCode', label: 'Postal Code', type: 'text' },
            {row: 7, span: 3, name: 'state', label: 'State/Region', type: 'select' },
            {row: 7, span: 3, name: 'country', label: 'Country', type: 'select' },
            {row: 7, span: 3, name: 'addressValidFromDate', label: 'Address Valid From Date', type: 'date' },

            {row: 8, span: 12, name: 'header3', type: 'header', label: 'Communication & Other Details' },
            {row: 9, span: 3, name: 'contactNumber', label: 'Telephone', type: 'text' },
            {row: 9, span: 3, name: 'email', label: 'Email', type: 'text', required: true },
            {row: 9, span: 3, name: 'mobileNumber', label: 'Mobile', type: 'text' },
            {row: 9, span: 3, name: 'faxNumber', label: 'Fax', type: 'text' },
            {row: 10, span: 3, name: 'legalForm', label: 'Legal Form', type: 'select' },
            {row: 10, span: 3, name: 'legalEntity', label: 'Legal Entity', type: 'select' },
            {row: 10, span: 3, name: 'houseBank', label: 'House Bank', type: 'select' }
        ]
    },
    
    // Business Partner - KYC Details
    businessPartnerKYCDetails: {
        fieldsConfig: [
            {row: 1, span: 6, name: 'kycCompletionDate', label: 'KYC Completion Date', type: 'date' },
            {row: 1, span: 3, name: 'kycRiskCategory', label: 'KYC Risk Category', type: 'select' },
            {row: 2, span: 6, name: 'reKycCompletionDate', label: 'Re KYC Completion Date', type: 'date' },
            {row: 2, span: 3, name: 'reKycRiskCategory', label: 'Re KYC Risk Category', type: 'select' },
        ]
    },

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
            {row: 2, span: 3, name: 'file', label: 'Select file to upload', type: 'file', required: false },
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

            {row: 9, span: 3, name: 'policyExposure', label: 'Policy Exposure', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
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
        fieldsConfig: [
            { row: 1, span: 12, name: 'header1', type: 'header', label: 'Project Cost Details' },

            { row: 2, span: 3, name: 'projectCost', label: 'Project Cost', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 2, span: 3, name: 'debt', label: 'Debt (Crores)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 2, span: 3, name: 'equity', label: 'Promoter Contribution (Crores)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 2, span: 3, name: 'pfsDebtAmount', label: 'PFS Debt Amount (Crores)', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            
            { row: 3, span: 3, name: 'debtEquityRatio', label: 'Debt/Equity Ratio without Grant', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'grantAmount', label: 'Grant/Subsidy Amount', type: 'text', maxLength: 18, pattern: FIFTEEN_COMMA_TWO },
            { row: 3, span: 3, name: 'debtEquityRatioWithGrant', label: 'Debt/Equity Ratio with Grant', type: 'text', maxLength: 18, 
                pattern: FIFTEEN_COMMA_TWO }
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
}
