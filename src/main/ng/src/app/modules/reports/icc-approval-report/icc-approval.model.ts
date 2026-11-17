import { SelectOptionLine } from '../shared/select-option.model';

import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';

export { compareCells, displayDate };

/** ICC Approval Status Report (Reports › Loans Management): the SAP report "Loans : ICC Approval Status Report". */

export interface IccApprovalSelection {
    companyCode: string;
    fiscalYear: string;
    quarter: string;
    /** Select-option: loan contract (Ranl) with any number of lines */
    contract: SelectOptionLine[];
}

export interface IccApprovalRow {
    fyear: string | null;
    serialNo: string | null;
    quarter: string | null;
    ranl: string | null;
    status: string | null;
    bupaId: string | null;
    borrowerName: string | null;
    projectName: string | null;
    zzsanctionedAmt: string | null;
    commitCapital: string | null;
    loanType: string | null;
    sector: string | null;
    projectType: string | null;
    grpCompName: string | null;
    feePct: string | null;
    roi: string | null;
    iccStatusDesc: string | null;
    iccApprDate: string | null;
    ziccRemarks: string | null;
    bodDate: string | null;
    bodStatusDesc: string | null;
    zboardAppRemarks: string | null;
}

export interface IccApprovalReport {
    rows: IccApprovalRow[];
    total: number;
    generatedOn: string;
    query: string;
}

/** A column of the result table; every column is sortable. */
export interface IccApprovalColumn {
    key: keyof IccApprovalRow;
    label: string;
    /** 'text' sorts alphabetically, 'number' numerically, 'date' by its ISO text */
    type: 'text' | 'number' | 'date' | 'time';
    /** Monospace (ids, numbers) */
    mono?: boolean;
    /** Amounts / percentages: shown with digit grouping and two decimals */
    decimal?: boolean;
    /** Status text shown as a coloured pill */
    pill?: boolean;
    /** Minimum width in rem */
    width?: number;
}

/** The entity's properties in SEGW order, with the SAP labels. */
export const ICC_APPROVAL_COLUMNS: IccApprovalColumn[] = [
    { key: 'fyear', label: 'Fiscal Year', type: 'text', mono: true, width: 5 },
    { key: 'serialNo', label: 'Serial Number', type: 'number', mono: true, width: 5 },
    { key: 'quarter', label: 'Quarter', type: 'text', mono: true, width: 4.5 },
    { key: 'ranl', label: 'Contract Number', type: 'number', mono: true, width: 8 },
    { key: 'status', label: 'Status', type: 'text', pill: true, width: 6 },
    { key: 'bupaId', label: 'Busn. Partner', type: 'number', mono: true, width: 7 },
    { key: 'borrowerName', label: 'Borrower Name', type: 'text', width: 14 },
    { key: 'projectName', label: 'Project Name', type: 'text', width: 12 },
    { key: 'zzsanctionedAmt', label: 'Sanctioned Amount', type: 'number', mono: true, decimal: true, width: 9 },
    { key: 'commitCapital', label: 'Curr.Commitment Cap.', type: 'number', mono: true, decimal: true, width: 9 },
    { key: 'loanType', label: 'Loan Type', type: 'text', width: 7 },
    { key: 'sector', label: 'Project Sector', type: 'text', width: 9 },
    { key: 'projectType', label: 'Project Type', type: 'text', width: 9 },
    { key: 'grpCompName', label: 'Group Company Name', type: 'text', width: 12 },
    { key: 'feePct', label: 'ICC Approved Fee %', type: 'number', mono: true, decimal: true, width: 7 },
    { key: 'roi', label: 'Interest Rate %', type: 'number', mono: true, decimal: true, width: 7 },
    { key: 'iccStatusDesc', label: 'BCM (ICC) Appr Status', type: 'text', pill: true, width: 9 },
    { key: 'iccApprDate', label: 'ICC Clearance Date', type: 'date', mono: true, width: 7 },
    { key: 'ziccRemarks', label: 'ICC Remark', type: 'text', width: 16 },
    { key: 'bodDate', label: 'Board Approval', type: 'date', mono: true, width: 7 },
    { key: 'bodStatusDesc', label: 'BOD Status', type: 'text', pill: true, width: 8 },
    { key: 'zboardAppRemarks', label: 'Board App Remarks', type: 'text', width: 16 }
];

/** Quarters offered on the selection screen (SAP: Quarter dropdown). */
export const QUARTERS: { code: string; text: string }[] = [
    { code: '1', text: 'Quarter 1' },
    { code: '2', text: 'Quarter 2' },
    { code: '3', text: 'Quarter 3' },
    { code: '4', text: 'Quarter 4' }
];

export const DEFAULT_ICC_APPROVAL_SELECTION: IccApprovalSelection = {
    companyCode: 'PFS', fiscalYear: '', quarter: '', contract: []
};
