import { SelectOptionLine } from '../shared/select-option.model';

/** Workflow Status Report (Reports › Miscellaneous): the SAP report "Loans: Workflow Status Report". */

export interface WorkflowStatusSelection {
    companyCode: string;
    releaseObject: string;
    /** Select-options: contract number (Ranl), business partner and date, each with any number of lines */
    contract: SelectOptionLine[];
    partner: SelectOptionLine[];
    date: SelectOptionLine[];
}

export interface WorkflowStatusRow {
    rowId: string | null;
    bukrs: string | null;
    relobj: string | null;
    partner: string | null;
    datum: string | null;
    relObject: string | null;
    ranl: string | null;
    parnter: string | null;
    boId: string | null;
    initiatorId: string | null;
    initiatorPosition: string | null;
    initiatorName: string | null;
    initiationDate: string | null;
    initiationTime: string | null;
    wfReferenceNo: string | null;
    taskText: string | null;
    approverId: string | null;
    approverPosition: string | null;
    approverName: string | null;
    approvalDate: string | null;
    approvalTime: string | null;
    releaseStatus: string | null;
    releaseStatusDesc: string | null;
    releaseIndicator: string | null;
    releaseIndicatorDesc: string | null;
}

export interface WorkflowStatusReport {
    rows: WorkflowStatusRow[];
    total: number;
    generatedOn: string;
    query: string;
}

/** A column of the result table; every column is sortable. */
export interface WorkflowStatusColumn {
    key: keyof WorkflowStatusRow;
    label: string;
    /** 'text' sorts alphabetically, 'number' numerically (leading zeros ignored), 'date' / 'time' by their ISO text */
    type: 'text' | 'number' | 'date' | 'time';
    /** Monospace (ids, numbers) */
    mono?: boolean;
    /** Minimum width in rem */
    width?: number;
}

/** The entity's properties in SEGW order, with the SAP labels. */
export const WORKFLOW_STATUS_COLUMNS: WorkflowStatusColumn[] = [
    { key: 'rowId', label: 'Row', type: 'number', mono: true, width: 4 },
    { key: 'bukrs', label: 'Company Code', type: 'text', mono: true, width: 5 },
    { key: 'relobj', label: 'Release Object', type: 'text', mono: true, width: 6 },
    { key: 'partner', label: 'Business Partner', type: 'number', mono: true, width: 7 },
    { key: 'datum', label: 'Date', type: 'date', mono: true, width: 6.5 },
    { key: 'relObject', label: 'Release Object (Text)', type: 'text', width: 8 },
    { key: 'ranl', label: 'Contract Number', type: 'number', mono: true, width: 8 },
    { key: 'parnter', label: 'Partner', type: 'number', mono: true, width: 7 },
    { key: 'boId', label: 'Bus. Operation No.', type: 'text', mono: true, width: 8 },
    { key: 'initiatorId', label: 'Initiator User Id', type: 'text', mono: true, width: 7 },
    { key: 'initiatorPosition', label: 'Initiator Position', type: 'text', width: 7 },
    { key: 'initiatorName', label: 'Initiator Name', type: 'text', width: 10 },
    { key: 'initiationDate', label: 'Initiation Date', type: 'date', mono: true, width: 6.5 },
    { key: 'initiationTime', label: 'Initiation Time', type: 'time', mono: true, width: 5.5 },
    { key: 'wfReferenceNo', label: 'WF Reference No.', type: 'text', mono: true, width: 7 },
    { key: 'taskText', label: 'Work Item', type: 'text', width: 16 },
    { key: 'approverId', label: 'Approver User Id', type: 'text', mono: true, width: 7 },
    { key: 'approverPosition', label: 'Approver Position', type: 'text', width: 7 },
    { key: 'approverName', label: 'Approver Name', type: 'text', width: 10 },
    { key: 'approvalDate', label: 'Approval Date', type: 'date', mono: true, width: 6.5 },
    { key: 'approvalTime', label: 'Approval Time', type: 'time', mono: true, width: 5.5 },
    { key: 'releaseStatus', label: 'Rel. St.', type: 'text', mono: true, width: 4 },
    { key: 'releaseStatusDesc', label: 'Release Status', type: 'text', width: 9 },
    { key: 'releaseIndicator', label: 'Rel. Ind.', type: 'text', mono: true, width: 4 },
    { key: 'releaseIndicatorDesc', label: 'Approval Status', type: 'text', width: 9 }
];

/** Release objects offered on the selection screen (SAP: Release object dropdown). */
export const RELEASE_OBJECTS: { code: string; text: string }[] = [
    { code: 'VDARL', text: 'VDARL Loan Master Data' }
];

export const DEFAULT_WORKFLOW_STATUS_SELECTION: WorkflowStatusSelection = {
    companyCode: 'PFS', releaseObject: 'VDARL', contract: [], partner: [], date: []
};

/** Compares two cell values of a column; nulls last. */
export function compareCells(a: string | null, b: string | null, type: WorkflowStatusColumn['type']): number {
    if (a === b) {
        return 0;
    }
    if (a === null || a === '') {
        return 1;
    }
    if (b === null || b === '') {
        return -1;
    }
    if (type === 'number') {
        const na = Number(a);
        const nb = Number(b);
        if (!Number.isNaN(na) && !Number.isNaN(nb)) {
            return na - nb;
        }
    }
    return a.localeCompare(b, undefined, { numeric: true, sensitivity: 'base' });
}

/** An ISO date (yyyy-MM-dd) as dd.MM.yyyy for display. */
export function displayDate(iso: string | null): string {
    if (!iso) {
        return '';
    }
    const m = /^(\d{4})-(\d{2})-(\d{2})/.exec(iso);
    return m ? `${m[3]}.${m[2]}.${m[1]}` : iso;
}
